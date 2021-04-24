package com.example.android.gamenewsapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ArticleAdapter extends ArrayAdapter<Article> {

    public ArticleAdapter(Activity context, List<Article>articles) {
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
        String articleDate = currentArticle.getArticleData("date");

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
}
