package com.learning.android.movieman.fragment;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.learning.android.movieman.activity.MovieDetailsActivity;
import com.learning.android.movieman.backend.Repository;
import com.learning.android.movieman.model.MovieUpdate;
import com.learning.android.movieman.util.JsonUtils;
import com.learning.android.movieman.util.UrlEndpoints;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LatestMoviesFragment extends MoviesGridFromListFragment {

    private List<Long> latestChangedMovies;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        latestChangedMovies = new ArrayList<>();
        sendApiRequestLatestMovieChanges();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initializeMovieIdsList() {
        movieIds = latestChangedMovies;
    }

    @Override
    protected int getSourcePropertyClass() {
        return MovieDetailsActivity.SOURCE_MOVIES_LISTS;
    }

    private void sendApiRequestLatestMovieChanges() {
        RequestQueue requestQueue = Repository.getInstance().getRequestQueue();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, UrlEndpoints.URL_API_LATEST, (String) null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                handleLatestChangesResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handleError(error);
            }
        });
        requestQueue.add(request);
    }

    private void handleLatestChangesResponse(JSONObject response) {
        latestChangedMovies.clear();
        List<MovieUpdate> movieUpdates = JsonUtils.parseMovieUpdatesJsonResponse(response);

        int i = 0;
        while (latestChangedMovies.size() <= 20 && i < movieUpdates.size()) {
            if (movieUpdates.get(i).getAdult() != null && !movieUpdates.get(i).getAdult()) {
                latestChangedMovies.add(movieUpdates.get(i).getId());
            }
            i++;
        }

        refresh();
    }
}
