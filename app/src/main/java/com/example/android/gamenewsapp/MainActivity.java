package com.example.android.gamenewsapp;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.Loader;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.net.ConnectivityManager;

import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<Article>> {

    // Identifies the current activity for a LOG statement.
    public static final String LOG_TAG = MainActivity.class.getName();

    // ID number for the current Loader
    private static final int ARTICLE_LOADER_ID = 1;

    // The base url for the guardian json query
    private final String NEWS_BASE_URL = "http://content.guardianapis.com/search";

    // Adapter variable for the list of news articles.
    private ArticleAdapter mAdapter;

    // TextView variable to use when there are no articles to display.
    private TextView emptyStateTextView;

    /**
     * This method creates the main activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the app to display the activity_main xml layout.
        setContentView(R.layout.activity_main);

        // Find a reference to the {@link ListView} in the layout
        ListView newsListView = (ListView) findViewById(R.id.list);

        // Create a new {@link ArrayAdapter} of Articles.
        mAdapter = new ArticleAdapter(this, new ArrayList<>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface.
        newsListView.setAdapter(mAdapter);

        // Find a reference to the {@link TextView} in the layout.
        emptyStateTextView = findViewById(R.id.no_data);

        // Set the adapter on the {@link ListView}
        // so the emptyStateTextView can be displayed.
        newsListView.setEmptyView(emptyStateTextView);

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {

            // Get a reference to the LoaderManager, in order to interact with loaders.
            android.app.LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader.
            loaderManager.initLoader(ARTICLE_LOADER_ID, null, this);

        } else {
            // Otherwise, stop displaying the progressBar view and display error.

            // Find the {@link ProgressBar} from the layout.
            View lodingIndicator = findViewById(R.id.progress_bar);

            // Hide the progressBar.
            lodingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message.
            emptyStateTextView.setText(getText(R.string.no_internet));
        }

        // Set an item click listener on the {@link ListView}, which sends an intent to a web
        // browser to open a website with more information about the selected Article.
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Find the current Article that was clicked.
                Article currentArticle = mAdapter.getItem(position);

                // Convert the String URL into a URI object to pass to the Intent.
                Uri articleUri = Uri.parse(currentArticle.getArticleData("url"));

                // Create a new Intent to view the news Article web page.
                Intent webIntent = new Intent(Intent.ACTION_VIEW, articleUri);
                if (webIntent.resolveActivity(getPackageManager()) != null) {

                    // Send the intent to launch a new activity.
                    startActivity(webIntent);
                }
            }
        });
    }

    /**
     * onCreateLoader creates and returns a new Loader for the given ID.
     */
    @Override
    public Loader<List<Article>> onCreateLoader(int id, Bundle args) {

        // Breaks apart the URI string that's passed into its parameter
        Uri baseUri = Uri.parse(NEWS_BASE_URL);

        // Prepares the baseUri that we just parsed so we can add query parameters to it.
        Uri.Builder uriBuilder = baseUri.buildUpon();

        // Append query parameter and its value. For example, the `section=games`.
        uriBuilder.appendQueryParameter("section", "games");
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("show-references", "author");
        uriBuilder.appendQueryParameter("order-by", "newest");
        uriBuilder.appendQueryParameter("api-key", "test");

        // Log the full url = https://content.guardianapis.com/search?section=games&show-tags=contributor&show-references=author&order-by=newest&api-key=test
        // to test uriBuilder is working.
        Log.i(LOG_TAG, "TEST: compleated uri: " + uriBuilder.toString());

        // Return the completed uri.
        return new ArticleLoader(this, uriBuilder.toString());
    }

    /**
     * Run when the Loader has finished.
     */
    @Override
    public void onLoadFinished(android.content.Loader<List<Article>> loader, List<Article> data) {

        // Find the {@link ProgressBar} from the layout.
        View progressBar = findViewById(R.id.progress_bar);

        // Hide the ProgressBar.
        progressBar.setVisibility(View.GONE);

        // Set the EmptyState to display No Articles Found text.
        emptyStateTextView.setText(getText(R.string.no_data));

        // Clear the Adapter of any Article Data.
        mAdapter.clear();

        // If there is a valid list of {@link Article}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(android.content.Loader<List<Article>> loader) {

        // Loader reset, clear the Adapter of any Article Data.
        mAdapter.clear();
    }
}