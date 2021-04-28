package com.example.android.gamenewsapp;

public class Article {

    private String mTitle;

    private String mAuthor;

    private String mSection;

    private String mDate;

    private String mUrl;

    /**
     * Create a new Article object.
     * @param title    is the Title of the Article.
     * @param author   is the Author or main Contributor of the Article
     * @param section  is the News category of the Article.
     * @param date     is the Date of publication of the Article.
     * @param url      is the URL for the full Article.
     */
    public Article(String title, String author, String section, String date, String url) {
        mTitle = title;
        mAuthor = author;
        mSection = section;
        mDate = date;
        mUrl = url;
    }

    /**
     * A method for returning data from the article object.
     * @param category Selects the data to be returned.
     * @return the selected data String.
     */
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
