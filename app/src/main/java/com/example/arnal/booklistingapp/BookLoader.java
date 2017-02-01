package com.example.arnal.booklistingapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

/**
 * Created by arnal on 1/31/17.
 */

public class BookLoader extends AsyncTaskLoader<List<Books>> {

    /** Tag for log messages */
    private static final String LOG_TAG = BookLoader.class.getName();

    /** Query URL */
    private String mUrl;


    public BookLoader(Context context, String url){
        super(context);
        mUrl = url;
    }
    @Override
    protected void onStartLoading() {
        forceLoad();
        Log.v("Loader State","Loader starts loading");
        super.onStartLoading();
    }

    @Override
    public List<Books> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        List<Books> bookses = QueryUtils.fetchEarthquakeData(mUrl);
        return bookses;
    }
}
