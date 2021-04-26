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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<Article>> {

    public static final String LOG_TAG = MainActivity.class.getName();

    private ArticleAdapter mAdapter;

    private static final String NEWS_BASE_URL = "http://content.guardianapis.com/search";

    private static final int ARTICLE_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find a reference to the {@link ListView} in the layout
        ListView newsListView = (ListView) findViewById(R.id.list);

        mAdapter = new ArticleAdapter(this, new ArrayList<>());

        newsListView.setAdapter(mAdapter);

        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            android.app.LoaderManager loaderManager = getLoaderManager();

            loaderManager.initLoader(ARTICLE_LOADER_ID, null, this);

        }

        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Article currentArticle = mAdapter.getItem(position);

                Uri articleUri = Uri.parse(currentArticle.getArticleData("url"));

                Intent webIntent = new Intent(Intent.ACTION_VIEW, articleUri);
                if (webIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(webIntent);
                }
            }
        });
    }

    @Override
    public Loader<List<Article>> onCreateLoader(int id, Bundle args) {

        Uri baseUri = Uri.parse(NEWS_BASE_URL);

        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("section", "games");
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("show-references", "author");
        uriBuilder.appendQueryParameter("order-by", "newest");
        uriBuilder.appendQueryParameter("api-key", "test");

        // full url = https://content.guardianapis.com/search?section=games&show-tags=contributor&show-references=author&order-by=newest&api-key=test
        Log.i(LOG_TAG, "TEST: compleated uri: " + uriBuilder.toString());

        return new ArticleLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(android.content.Loader<List<Article>> loader, List<Article> data) {
        mAdapter.clear();

        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(android.content.Loader<List<Article>> loader) {
        mAdapter.clear();
    }
}