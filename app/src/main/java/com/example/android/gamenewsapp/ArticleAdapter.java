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

    public ArticleAdapter(Activity context, List<Article> articles) {
        super(context, 0, articles);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        Article currentArticle = getItem(position);

        String articleTitle = currentArticle.getArticleData("title");
        String articleAuthor = currentArticle.getArticleData("author");
        String articleSection = currentArticle.getArticleData("section");
        String articleDate = formatDate(currentArticle.getArticleData("date"));

        TextView titleTextView = listItemView.findViewById(R.id.tv_title);
        titleTextView.setText(articleTitle);

        TextView authorTextView = listItemView.findViewById(R.id.tv_author);
        authorTextView.setText(articleAuthor);

        TextView sectionTextView = listItemView.findViewById(R.id.tv_section);
        sectionTextView.setText(articleSection);

        TextView dateTextView = listItemView.findViewById(R.id.tv_date);
        dateTextView.setText(articleDate);

        return listItemView;
    }

    private String formatDate(String jsonDate) {

        SimpleDateFormat jsonDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "LLL dd, yyyy", Locale.US);
        try {
            Date jsonDateParsed = jsonDateFormat.parse(jsonDate);
            return dateFormat.format(jsonDateParsed);
        } catch (ParseException e) {
            Log.e(LOG_TAG, "problem formatting date.", e);
            return "Bad Date Format.";
        }
    }
}
