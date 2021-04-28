package com.example.android.gamenewsapp;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ArticleAdapter extends ArrayAdapter<Article> {
    public static final String LOG_TAG = ArrayAdapter.class.getName();

    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the list is the data we want
     * to populate into the lists.
     *
     * @param context     The current context. Used to inflate the layout file.
     * @param articles    A List of Article objects to display in a list.
     */
    public ArticleAdapter(Activity context, List<Article> articles) {
        super(context, 0, articles);
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position    the position in the list of data that should be displayed
     *                    in the listItemView.
     * @param convertView the recycled view to populate.
     * @param parent      the parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // Check if the existing view is being reused, otherwise inflate the view.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        // Get the {@link Article} object located at this position in the list.
        Article currentArticle = getItem(position);

        // Create Variables for each piece of the Article object data.
        String articleTitle = currentArticle.getArticleData("title");
        String articleAuthor = currentArticle.getArticleData("author");
        String articleSection = currentArticle.getArticleData("section");
        String articleDate = formatDate(currentArticle.getArticleData("date"));

        // Find the {@link TextView} for the article title from the layout.
        TextView titleTextView = listItemView.findViewById(R.id.tv_title);

        // Set the views text from the articleTitle variable.
        titleTextView.setText(articleTitle);

        // Find the {@link TextView} for the article author from the layout.
        TextView authorTextView = listItemView.findViewById(R.id.tv_author);

        // Set the views text from the articleAuthor variable.
        authorTextView.setText(articleAuthor);

        // Find the {@link TextView} for the article section from the layout.
        TextView sectionTextView = listItemView.findViewById(R.id.tv_section);

        // Set the views text from the articleSection variable.
        sectionTextView.setText(articleSection);

        // Find the {@link TextView} for the article date from the layout.
        TextView dateTextView = listItemView.findViewById(R.id.tv_date);

        // Set the views text from the articleDate variable.
        dateTextView.setText(articleDate);

        // Return the whole list item layout
        // So that it can be shown in the ListView
        return listItemView;
    }

    /**
     * A method to take the date String returned from the json request
     * and format it for display in the app.
     * @param jsonDate A date String returned by the JSON request.
     * @return A reformatted date String.
     */
    private String formatDate(String jsonDate) {

        // The date format the JSON request returns.
        SimpleDateFormat jsonDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);

        // The date format to be displayed in the app.
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "LLL dd, yyyy", Locale.US);

        // Try to format the date String.
        try {

            // Breaks apart the date String passed into its parameter.
            Date jsonDateParsed = jsonDateFormat.parse(jsonDate);

            // Rebuild the date String with the new format and then return it.
            return dateFormat.format(jsonDateParsed);
        } catch (ParseException e) {

            // Log if an exception is thrown and the date cannot be formatted.
            Log.e(LOG_TAG, "problem formatting date.", e);

            // Return a Bad Date Format message to be displayed.
            return "Bad Date Format.";
        }
    }
}
