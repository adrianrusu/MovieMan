package com.learning.android.movieman.fragment;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.learning.android.movieman.backend.Repository;
import com.learning.android.movieman.model.MovieSmall;
import com.learning.android.movieman.util.JsonUtils;
import com.learning.android.movieman.util.UrlEndpoints;

import org.json.JSONObject;

import java.util.List;

public abstract class MoviesGridFromListFragment extends MoviesGridFragment {

    protected List<Long> movieIds;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        initializeMovieIdsList();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void sendApiRequest() {
        for (Long movieId : movieIds) {
            sendApiRequest(movieId);
        }
    }

    private void sendApiRequest(Long movieId) {
        RequestQueue requestQueue = Repository.getInstance().getRequestQueue();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, UrlEndpoints.URL_API_MOVIE + movieId.toString() + UrlEndpoints.URL_PARAM_API_KEY, (String) null, new Response.Listener<JSONObject>() {
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

    @Override
    protected void handleResponse(JSONObject response) {
        movies.add(JsonUtils.parseMovieJsonResponse(response));
        if (movieIds.size() == movies.size()) {
            movieListAdapter.setElements(movies);
        }
    }

    @Override
    protected String getRequestUrl() {
        return null;
    }

    public abstract void initializeMovieIdsList();

    public void refresh() {
        initializeMovieIdsList();
        boolean needsRefresh = false;
        if (movieIds.size() != movies.size()) {
            needsRefresh = true;
        } else {
            for (MovieSmall movie : movies) {
                if (!movieIds.contains(movie.getId())) {
                    needsRefresh = true;
                }
            }
        }
        if (needsRefresh) {
            movies.clear();
            movieListAdapter.setElements(movies);
            sendApiRequest();
        }
    }
}