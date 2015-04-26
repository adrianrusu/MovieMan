package com.learning.android.movieman.fragment;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.learning.android.movieman.activity.MovieDetailsActivity;
import com.learning.android.movieman.backend.Repository;
import com.learning.android.movieman.util.JsonUtils;
import com.learning.android.movieman.util.UrlEndpoints;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by adrianrusu on 4/26/15.
 */
public class WatchlistFragment extends MoviesGridFragment {

    List<Long> watchlistMovieIds;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        watchlistMovieIds = Repository.getInstance().getDbHandler().getWatchlistMovieIds();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void sendApiRequest() {
        for (Long movieId : watchlistMovieIds) {
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
        if (watchlistMovieIds.size() == movies.size()) {
            movieListAdapter.setElements(movies);
        }
    }

    @Override
    protected String getRequestUrl() {
        return null;
    }

    @Override
    protected int getSourcePropertyClass() {
        return MovieDetailsActivity.SOURCE_WATCHLIST_ACTIVITY;
    }
}
