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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.learning.android.movieman.R;
import com.learning.android.movieman.activity.MovieActivity;
import com.learning.android.movieman.adapter.MovieListAdapter;
import com.learning.android.movieman.adapter.RecyclerViewSelectionListener;
import com.learning.android.movieman.backend.Repository;
import com.learning.android.movieman.model.MovieSmall;
import com.learning.android.movieman.util.JsonUtils;
import com.learning.android.movieman.util.UrlEndpoints;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements RecyclerViewSelectionListener {
    private static final String MOVIES_LIST = "movies_list";

    private RecyclerView moviesRecyclerView;
    private MovieListAdapter movieListAdapter;
    private TextView textViewError;

    private RequestQueue requestQueue;
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
        requestQueue = Repository.getInstance().getRequestQueue();
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
        movies = parseJsonResponse(response);
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
        return new StringBuilder(UrlEndpoints.NOW_PLAYING_URL).append(UrlEndpoints.URL_PARAM_API_KEY).toString();
    }

    private List<MovieSmall> parseJsonResponse(JSONObject jsonObject) {
        List<MovieSmall> result = new ArrayList<>();
        if (jsonObject == null || jsonObject.length() == 1) {
            return result;
        }

        try {
            Gson gson = JsonUtils.getGsonForApi();
            result = gson.fromJson(jsonObject.get(JsonUtils.KEY_RESULTS).toString(), new TypeToken<List<MovieSmall>>() {
            }.getType());
        } catch (JSONException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    public void refresh() {
        sendApiRequest();
    }

    @Override
    public void itemClicked(View view, int position) {
        Intent movieDetailsIntent = new Intent(getActivity(), MovieActivity.class);
        movieDetailsIntent.putExtra("id", movies.get(position).getId());
        startActivity(movieDetailsIntent);
    }
}