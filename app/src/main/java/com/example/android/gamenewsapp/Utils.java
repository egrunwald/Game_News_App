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


public final class Utils {

    public static final String LOG_TAG = Utils.class.getName();

    private Utils() {
    }

    public static List<Article> fetchArticles(String requestUrl) {
        URL url = createUrl(requestUrl);

        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error making the HTTP request.", e);
        }

        return extractArticles(jsonResponse);
    }

    private static URL createUrl(String urlString) {
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error creating URL.", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = null;

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
                inputStream.close();
            }
        }
        return jsonResponse;
    }

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

    private static List<Article> extractArticles(String jsonResponse) {
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }
        Log.e(LOG_TAG, "json =" + jsonResponse);
        List<Article> articles = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);

            JSONObject jsonObjectResponse = jsonObject.getJSONObject("response");

            JSONArray jsonArrayResults = jsonObjectResponse.getJSONArray("results");

            for (int i = 0; i < jsonArrayResults.length(); i++) {
                JSONObject currentResult = jsonArrayResults.getJSONObject(i);
                Log.e(LOG_TAG, "i =" + i);

                String currentTitle = currentResult.getString("webTitle");
                Log.e(LOG_TAG, "Title =" + currentTitle);

                String currentSection = currentResult.getString("sectionName");
                Log.e(LOG_TAG, "Section =" + currentSection);

                String currentDate = currentResult.getString("webPublicationDate");
                Log.e(LOG_TAG, "Date =" + currentDate);

                String currentUrl = currentResult.getString("webUrl");
                Log.e(LOG_TAG, "url =" + currentUrl);

                JSONArray currentTags = currentResult.getJSONArray("tags");

                String currentAuthor = "";
                Log.e(LOG_TAG, "Author =" + currentAuthor);


                if (currentTags.length() > 0) {
                    currentAuthor = currentTags.getJSONObject(0).getString("webTitle");
                } else {
                    currentAuthor = "No Author Listed";
                }

                if (currentDate == null || currentDate.isEmpty()) {
                    currentDate = "No Date Found.";
                }

                Article currentArticle = new Article(
                        currentTitle, currentAuthor, currentSection, currentDate, currentUrl);
                articles.add(currentArticle);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "problem parsing the JSON result.");
        }
        return articles;
    }
}
