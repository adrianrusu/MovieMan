package com.learning.android.movieman.fragment;

import com.learning.android.movieman.activity.MovieDetailsActivity;
import com.learning.android.movieman.util.UrlEndpoints;

/**
 * Created by adrianrusu on 4/29/15.
 */
public class PopularMoviesFragment extends MoviesGridFragment {
    @Override
    protected String getRequestUrl() {
        return UrlEndpoints.URL_API_POPULAR;
    }

    @Override
    protected int getSourcePropertyClass() {
        return MovieDetailsActivity.SOURCE_MOVIES_LISTS;
    }
}