package com.example.arnal.booklistingapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by arnal on 1/30/17.
 */

public class QueryUtils {
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

   public static List<Books> fetchEarthquakeData(String requestUrl) {

        //create url object
        URL url = createUrl(requestUrl);


        //Perform HTTP request to the Url and receive JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHTTPRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making httprequest.", e);
        }
        //Extract relevant fields from the JSON response and create a list {@link Earthquake}s
        List<Books> bookses = extractBookDataJson(jsonResponse);
       Log.v("Loader State","Loader fetched data from fetchBookData() method. ");
        return bookses;
    }

    private static URL createUrl(String requestUrl) {
        URL url = null;
        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHTTPRequest(URL url) throws IOException {
        String jsonResponse = "";

        //If the url null return early
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 );
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //If the request was successful response(200)
            // then read the input stream and parse the response
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {

                //Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                //could be thrown.
                inputStream.close();
            }
        }

        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();

        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link Books} objects that has been built up from
     * parsing a JSON response.
     */
    public static List<Books> extractBookDataJson(String bookJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(bookJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding earthquakes to
        List<Books> bookData = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.
            JSONObject jsonObj = new JSONObject(bookJSON);
            // Getting JSON Array node
            JSONArray bArray = jsonObj.getJSONArray("items");


            // looping through All Books
            for (int i = 0; i < bArray.length(); i++) {
                JSONObject bObject = bArray.getJSONObject(i);


                JSONObject volumeInfo = bObject.getJSONObject("volumeInfo");

                //jsObject = jsObject.getJSONObject("properties");

                String title;
                // Extract the value for the key called "title"
                if(volumeInfo.has("title")){
                    title = volumeInfo.getString("title");
                    Log.v("title value",title);
                }else{
                    title = "N/A";
                }
                // Extract the value for the key called "subtitle"
                String subTitle;
                if(volumeInfo.has("publisher")){
                    subTitle = volumeInfo.getString("publisher");
                    Log.v("subtitle value",subTitle);
                }else{
                    subTitle = "N/A";
                }
                //Extract authors
                StringBuilder sb = new StringBuilder();
                if(volumeInfo.has("authors")){
                    JSONArray authors = volumeInfo.getJSONArray("authors");
                    if(volumeInfo.has("authors")){
                        for(int j = 0; j < authors.length() ; j++){
                            Log.v("j=0","for loop entered");
                            String author = authors.getString(j);
                            Log.v("author",author);
                            sb.append(author+ " ");
                            Log.v("Sb value :",sb.toString());
                            sb.append(" ");
                        }
                    }else{
                        sb.append("N/A");
                    }
                }
                String authorsOfBook = sb.toString();

              /*  String title = properties.getString("title");
                String subTitle = properties.getString("subtitle");
                String authors = properties.getString("authors");
                String date = properties.getString("publishedDate");*/

                //Extract the value for the key called "pages"
                String date;
                if(volumeInfo.has("publishedDate")){
                    date = volumeInfo.getString("publishedDate");
                    Log.v("publishedDate",date);
                }else{
                    date = "N/A";
                }

                bookData.add(new Books(title, subTitle, authorsOfBook, date));


            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return bookData;
    }

}
