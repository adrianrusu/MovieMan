package com.learning.android.movieman.activity;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.learning.android.movieman.R;
import com.learning.android.movieman.fragment.LatestMoviesFragment;
import com.learning.android.movieman.fragment.NavigationDrawerFragment;
import com.learning.android.movieman.fragment.PopularMoviesFragment;
import com.learning.android.movieman.fragment.TopRatedMoviesFragment;
import com.learning.android.movieman.fragment.UpcomingMoviesFragment;

public class MoviesListActivity extends ActionBarActivity {

    public static final String LIST_TYPE = "moviesListType";

    public static final String UPCOMING = "Upcoming Movies";
    public static final String POPULAR = "Popular Movies";
    public static final String LATEST = "Latest Movies";
    public static final String TOP_RATED = "Top Rated Movies";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final NavigationDrawerFragment navDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        navDrawerFragment.setUp((DrawerLayout) findViewById(R.id.drawer_layout), null, R.id.fragment_navigation_drawer);
        handleExtras(getIntent().getExtras());
    }

    private void handleExtras(Bundle extras) {
        if (extras.containsKey(LIST_TYPE)) {
            String listType = extras.getString(LIST_TYPE);
            setTitle(listType);

            switch (listType) {
                case UPCOMING:
                    UpcomingMoviesFragment upcomingFragment = new UpcomingMoviesFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, upcomingFragment).commit();
                    break;
                case POPULAR:
                    PopularMoviesFragment popularFragment = new PopularMoviesFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, popularFragment).commit();
                    break;
                case LATEST:
                    LatestMoviesFragment latestFragment = new LatestMoviesFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, latestFragment).commit();
                    break;
                case TOP_RATED:
                    TopRatedMoviesFragment topRatedFragment = new TopRatedMoviesFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, topRatedFragment).commit();
            }
        }
    }

}
