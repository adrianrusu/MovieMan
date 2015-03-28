package com.learning.android.movieman.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.learning.android.movieman.R;
import com.learning.android.movieman.activity.MovieDetailsActivity;
import com.learning.android.movieman.adapter.MovieListAdapter;
import com.learning.android.movieman.adapter.RecyclerViewSelectionListener;
import com.learning.android.movieman.backend.Repository;
import com.learning.android.movieman.model.MovieSmall;
import com.learning.android.movieman.util.JsonUtils;
import com.learning.android.movieman.util.UrlEndpoints;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements RecyclerViewSelectionListener {
    private static final String MOVIES_LIST = "movies_list";

    private RecyclerView moviesRecyclerView;
    private MovieListAdapter movieListAdapter;
    private TextView textViewError;

    private List<MovieSmall> movies = new ArrayList<>();

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        textViewError = (TextView) view.findViewById(R.id.text_error);
        moviesRecyclerView = (RecyclerView) view.findViewById(R.id.home_movies_list);

        moviesRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        movieListAdapter = new MovieListAdapter(getActivity());
        movieListAdapter.setSelectionListener(this);
        moviesRecyclerView.setAdapter(movieListAdapter);

        if (savedInstanceState != null) {
            movies = (List<MovieSmall>) savedInstanceState.getSerializable(MOVIES_LIST);
            movieListAdapter.setElements(movies);
        } else {
            sendApiRequest();
        }

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(MOVIES_LIST, (java.io.Serializable) movies);
    }

    private void sendApiRequest() {
        RequestQueue requestQueue = Repository.getInstance().getRequestQueue();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getRequestUrl(), (String) null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                handleResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handleError(error);
            }
        });
        requestQueue.add(request);
    }

    private void handleResponse(JSONObject response) {
        textViewError.setVisibility(View.GONE);
        movies = JsonUtils.parseMovieSmallListJsonResponse(response);
        movieListAdapter.setElements(movies);
    }

    private void handleError(VolleyError error) {
        textViewError.setVisibility(View.VISIBLE);
        if (error instanceof TimeoutError || error instanceof NoConnectionError || error instanceof NetworkError) {
            textViewError.setText(R.string.error_timeout);
        } else if (error instanceof AuthFailureError || error instanceof ServerError) {
            textViewError.setText(R.string.error_auth_failure);
        } else if (error instanceof ParseError) {
            textViewError.setText(R.string.error_parser);
        }
    }

    private String getRequestUrl() {
        return UrlEndpoints.NOW_PLAYING_URL + UrlEndpoints.URL_PARAM_API_KEY;
    }

    public void refresh() {
        sendApiRequest();
    }

    @Override
    public void itemClicked(View view, int position) {
        Intent movieDetailsIntent = new Intent(getActivity(), MovieDetailsActivity.class);
        movieDetailsIntent.putExtra("id", movies.get(position).getId());
        startActivity(movieDetailsIntent);
    }
}