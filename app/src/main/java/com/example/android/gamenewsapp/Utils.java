package com.example.android.gamenewsapp;

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
import java.util.List;

/**
 * Helper methods related to requesting and receiving Article data from The Guardian.
 */
public final class Utils {

    public static final String LOG_TAG = Utils.class.getName();

    /**
     * A private constructor method. No Utils objects are created, the method
     * is used to call the other static variables and methods associated with it.
     */
    private Utils() {
    }

    /**
     * Query the Guardian dataset and return a list of {@link Article} objects.
     */
    public static List<Article> fetchArticles(String requestUrl) {

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error making the HTTP request.", e);
        }

        // Extract and return relevant fields from the
        // JSON response and create a list of {@link Article}s.
        return extractArticles(jsonResponse);
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String urlString) {
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error creating URL.", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = null;

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000); // Milliseconds.
            urlConnection.setConnectTimeout(15000); // Milliseconds.
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
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
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder outputString = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader =
                    new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                outputString.append(line);
                line = bufferedReader.readLine();
            }
        }
        return outputString.toString();
    }

    /**
     * Return a list of {@link Article} objects that has been built up from
     * parsing a JSON response.
     */
    private static List<Article> extractArticles(String jsonResponse) {

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding Articles to.
        List<Article> articles = new ArrayList<>();

        // Try to parse the String jsonResponse. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject jsonObject = new JSONObject(jsonResponse);

            // Extract the JSONObject associated with the key "response"
            // from the parent JSONObject.
            JSONObject jsonObjectResponse = jsonObject.getJSONObject("response");

            // Extract the JSONArray associated with the key "results"
            // which represents a list of results(Articles).
            JSONArray jsonArrayResults = jsonObjectResponse.getJSONArray("results");

            // For each Article in jsonArrayResults, create a {@link Article} object.
            for (int i = 0; i < jsonArrayResults.length(); i++) {

                // Get a single Article at position i in the list of articles.
                JSONObject currentResult = jsonArrayResults.getJSONObject(i);

                // Extract the value for the key called "webTitle"
                String currentTitle = currentResult.getString("webTitle");

                // Extract the value for the key called "sectionName"
                String currentSection = currentResult.getString("sectionName");

                // Extract the value for the key called "webPublicationDate"
                String currentDate = currentResult.getString("webPublicationDate");

                // Extract the value for the key called "webUrl"
                String currentUrl = currentResult.getString("webUrl");

                // Extract the JSONArray associated with the key "tags"
                // which represents a list of tags(contributors for the article).
                JSONArray currentTags = currentResult.getJSONArray("tags");

                // A string to hold the Authors name.
                String currentAuthor;

                // Check if there is a value stored in the currentTags Array.
                if (currentTags.length() > 0) {
                    currentAuthor = currentTags.getJSONObject(0).getString("webTitle");
                } else {
                    currentAuthor = "No Author Listed";
                }

                // Check if there is a date of publication.
                if (currentDate == null || currentDate.isEmpty()) {
                    currentDate = "No Date Found.";
                }

                // Create a new {@link Article} object.
                Article currentArticle = new Article(
                        currentTitle, currentAuthor, currentSection, currentDate, currentUrl);

                // Add the {@link Article} object to the articles List.
                articles.add(currentArticle);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "problem parsing the JSON result.");
        }
        return articles;
    }
}
