package com.learning.android.movieman.fragment;


import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.learning.android.movieman.R;
import com.learning.android.movieman.activity.MovieDetailsActivity;
import com.learning.android.movieman.activity.MoviesListActivity;
import com.learning.android.movieman.adapter.MovieListAdapter;
import com.learning.android.movieman.util.UrlEndpoints;

public class HomeFragment extends MoviesGridFragment {

    @Override
    protected void initMoviesGridRecyclerView(RecyclerView moviesRecyclerView) {
        final GridLayoutManager manager = new GridLayoutManager(getActivity(), 3);
        moviesRecyclerView.setLayoutManager(manager);

        View header = LayoutInflater.from(getActivity()).inflate(R.layout.home_buttons_header, moviesRecyclerView, false);
        CardView btnUpcoming = (CardView) header.findViewById(R.id.button_upcoming);
        btnUpcoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleIntentTo(MoviesListActivity.UPCOMING);
            }
        });
        CardView btnPopular = (CardView) header.findViewById(R.id.button_popular);
        btnPopular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleIntentTo(MoviesListActivity.POPULAR);
            }
        });
        CardView btnTopRated = (CardView) header.findViewById(R.id.button_top_rated);
        btnTopRated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleIntentTo(MoviesListActivity.TOP_RATED);
            }
        });
        CardView btnLatest = (CardView) header.findViewById(R.id.button_latest);
        btnLatest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleIntentTo(MoviesListActivity.LATEST);
            }
        });

        movieListAdapter = new MovieListAdapter(getActivity(), header);
        moviesRecyclerView.setAdapter(movieListAdapter);
        movieListAdapter.setSelectionListener(this);

        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return movieListAdapter.getItemViewType(position) == MovieListAdapter.VIEW_TYPE_HEADER ? manager.getSpanCount() : 1;
            }
        });
    }

    private void handleIntentTo(String destination) {
        Intent intent = new Intent(getActivity(), MoviesListActivity.class);
        intent.putExtra(MoviesListActivity.LIST_TYPE, destination);
        startActivity(intent);
    }

    @Override
    protected String getRequestUrl() {
        return UrlEndpoints.URL_API_NOW_PLAYING;
    }

    @Override
    protected int getSourcePropertyClass() {
        return MovieDetailsActivity.SOURCE_MAIN_ACTIVITY;
    }
}