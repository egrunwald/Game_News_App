package com.example.android.gamenewsapp;

import android.content.Context;


import android.content.AsyncTaskLoader;

import java.util.List;

/**
 * Loads a list of Articles by using an AsyncTask to perform the
 * network request to the given URL.
 */
public class ArticleLoader extends AsyncTaskLoader<List<Article>> {

    // Holds the Query URL.
    private final String mUrl;

    /**
     * Creates a new {@link ArticleLoader}
     * @param context of the activity.
     * @param url     to load the date from.
     */
    public ArticleLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<Article> loadInBackground() {

        // Check for a URL to query.
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of Articles
        return Utils.fetchArticles(mUrl);
    }
}
