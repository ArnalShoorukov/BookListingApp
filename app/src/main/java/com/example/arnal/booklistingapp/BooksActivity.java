package com.example.arnal.booklistingapp;

import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class BooksActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Books>>, View.OnClickListener{

    private static final String LOG_TAG = BooksActivity.class.getName();

    /*String query global variable*/
    private  String urlQuery;
    /*EditText to collect the query*/
    private EditText searchBook;
    /* Button to search for a book*/
    private Button searchButton;
    /*ListView global variable*/
    private ListView bookListView;
    //Adapter for list of books
    private BookAdapter mAdapter;
    /**
     * +     * Constant value for the book loader ID. We can choose any integer.
     * +     * This really only comes into play if you're using multiple loaders.
     */
    private static final int BOOK_LOADER_ID = 1;

    /** TextView that is displayed when the list is empty */
    private TextView mEmptyStateTextView;
    /**
     * URL for Book data from the Google database
     */
    private String GOOGLE_BOOKS_URL;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_activity);



        // Find a reference to the {@link ListView} in the layout
        bookListView = (ListView) findViewById(R.id.list);

        // Create a new {@link ArrayAdapter} of books
        mAdapter = new BookAdapter(this, new ArrayList<Books>());
        bookListView.setAdapter(mAdapter);
        // Find a reference to the {@link Button} in the layout
        searchButton = (Button)findViewById(R.id.search_button);
        //Set listener for the click event
        searchButton.setOnClickListener(this);
    }

    @Override
    public Loader<List<Books>> onCreateLoader(int id, Bundle args) {
        Log.v("Loader State","on Create Loader");
        // Create a new loader for the given URL
        return new BookLoader(this, GOOGLE_BOOKS_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Books>> loader, List<Books> bookses) {

        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No books found."
        mEmptyStateTextView.setText(R.string.no_books);

        // Clear the adapter of previous book data
        mAdapter.clear();
        // If there is a valid list of {@link Books}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (bookses != null && !bookses.isEmpty()) {
            mAdapter.addAll(bookses);
            mAdapter.notifyDataSetChanged();
        }
        Log.v("Loader State","on Load Finished");
    }

    @Override
    public void onLoaderReset(Loader<List<Books>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }

    @Override
    public void onClick(View v) {
        searchBook = (EditText)findViewById(R.id.searchBook);
        urlQuery = searchBook.getText().toString();

        GOOGLE_BOOKS_URL  = "https://www.googleapis.com/books/v1/volumes?q="+urlQuery+"&maxResults=10";
        Log.v("url",GOOGLE_BOOKS_URL);
        mEmptyStateTextView = (TextView) findViewById(R.id.text);

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(BOOK_LOADER_ID, null, this);
            //check if there's already a loader
            if(getLoaderManager().getLoader(BOOK_LOADER_ID).isStarted()){
                //restart it if there's one
                getLoaderManager().restartLoader(BOOK_LOADER_ID,null,this);
            }
        }else{
            //Otherwise,display error
            //First hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);

        }
        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        mAdapter.clear();
        //bookListView.invalidateViews();
        bookListView.setAdapter(mAdapter);
        //((BookAdapter)bookListView.getAdapter()).notifyDataSetChanged();
        bookListView.setEmptyView(mEmptyStateTextView);
    }

    //Closing tag for main class
}

