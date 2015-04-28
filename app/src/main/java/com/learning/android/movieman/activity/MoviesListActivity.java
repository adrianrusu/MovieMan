package com.learning.android.movieman.activity;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.learning.android.movieman.R;
import com.learning.android.movieman.fragment.NavigationDrawerFragment;

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

        final NavigationDrawerFragment navDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        navDrawerFragment.setUp((DrawerLayout) findViewById(R.id.drawer_layout), toolbar, R.id.fragment_navigation_drawer);

        Bundle extras = getIntent().getExtras();

        if (extras.containsKey(LIST_TYPE)) {
            listType = extras.getString(LIST_TYPE);
            setTitle(listType);
        }
    }

}
