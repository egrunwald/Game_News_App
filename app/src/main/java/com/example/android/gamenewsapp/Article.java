package com.example.android.gamenewsapp;

public class Article {

    private String mTitle;

    private String mAuthor;

    private String mSection;

    private String mDate;

    private String mUrl;

    public Article(String title, String author, String section, String date, String url) {
        mTitle = title;
        mAuthor = author;
        mSection = section;
        mDate = date;
        mUrl = url;
    }

    public String getArticleData(String category) {
        String data = null;
        switch (category) {
            case "title":
                data = mTitle;
                break;
            case "author":
                data = mAuthor;
                break;
            case "section":
                data = mSection;
                break;
            case "date":
                data = mDate;
                break;
            case "url":
                data = mUrl;
                break;
        }
        return data;
    }
}
