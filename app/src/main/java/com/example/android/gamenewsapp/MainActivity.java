package com.example.android.gamenewsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ArticleAdapter mAdapter;
    private List<Article> articles;
    private static final String NEWS_BASE_URL = "http://content.guardianapis.com/search";
// full url = https://content.guardianapis.com/search?section=games&show-tags=contributor&show-references=author&order-by=newest&api-key=test
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find a reference to the {@link ListView} in the layout
        ListView newsListView = (ListView) findViewById(R.id.list);

        mAdapter = new ArticleAdapter(this, new ArrayList<Article>());

        // TODO get article objects from json. use a loader
        articles = new ArrayList<Article>();
        for (int i = 0; i <= 15; i++) {
            articles.add(new Article("Article title", "Author Name", "Section",
                    "Apr 24, 2021", "url"));
        }

        if (articles != null && !articles.isEmpty()) {
            mAdapter.addAll(articles);
        }



        newsListView.setAdapter(mAdapter);
    }
}