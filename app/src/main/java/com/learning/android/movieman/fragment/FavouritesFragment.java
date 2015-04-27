package com.learning.android.movieman.fragment;

import com.learning.android.movieman.activity.MovieDetailsActivity;
import com.learning.android.movieman.backend.Repository;

/**
 * Created by adrianrusu on 4/27/15.
 */
public class FavouritesFragment extends MoviesGridFromListFragment {

    public void initializeMovieIdsList() {
        movieIds = Repository.getInstance().getDbHandler().getFavoriteMovieIds();
    }

    @Override
    protected int getSourcePropertyClass() {
        return MovieDetailsActivity.SOURCE_FAVORITES_ACTIVITY;
    }
}
