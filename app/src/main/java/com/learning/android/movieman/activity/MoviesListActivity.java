package com.learning.android.movieman.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.learning.android.movieman.R;

public class MoviesListActivity extends ActionBarActivity {

    public static final String LIST_TYPE = "moviesListType";

    public static final String UPCOMING = "Upcoming Movies";
    public static final String POPULAR = "Popular Movies";
    public static final String LATEST = "Latest Movies";
    public static final String TOP_RATED = "Top Rated Movies";

    private String listType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();

        if (extras.containsKey(LIST_TYPE)) {
            listType = extras.getString(LIST_TYPE);
            setTitle(listType);
        }
    }

}
