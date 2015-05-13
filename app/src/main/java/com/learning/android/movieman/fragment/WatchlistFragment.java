package com.learning.android.movieman.fragment;

import com.learning.android.movieman.activity.MovieDetailsActivity;
import com.learning.android.movieman.backend.Repository;

public class WatchlistFragment extends MoviesGridFromListFragment {

    @Override
    public void initializeMovieIdsList() {
        movieIds = Repository.getInstance().getDbHandler().getWatchlistMovieIds();
    }

    @Override
    protected int getSourcePropertyClass() {
        return MovieDetailsActivity.SOURCE_WATCHLIST_ACTIVITY;
    }
}
