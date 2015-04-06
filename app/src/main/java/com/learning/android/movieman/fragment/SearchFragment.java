package com.learning.android.movieman.fragment;


import android.os.Bundle;

import com.learning.android.movieman.activity.MovieDetailsActivity;
import com.learning.android.movieman.util.UrlEndpoints;

public class SearchFragment extends MoviesGridFragment {
    private static final String ARG_QUERY = "query";

    private String query;
    private boolean matchModePhrase = true;

    public static SearchFragment newInstance(String param1) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_QUERY, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            query = getArguments().getString(ARG_QUERY);
        }
    }

    @Override
    protected String getRequestUrl() {
        if (query != null) {
            return UrlEndpoints.URL_API_SEARCH + query + UrlEndpoints.URL_PARAM_SEARCH_TYPE_AUTOCOMPLETE;
        }
        return null;
    }

    @Override
    protected int getSourcePropertyClass() {
        return MovieDetailsActivity.SOURCE_SEARCH_ACTIVITY;
    }
}
