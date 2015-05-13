package com.learning.android.movieman.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
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
import com.android.volley.toolbox.ImageLoader;
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

public abstract class MoviesGridFragment extends Fragment implements RecyclerViewSelectionListener {

    private static final String MOVIES_LIST = "movies_list";

    protected MovieListAdapter movieListAdapter;
    protected TextView textViewError;

    protected List<MovieSmall> movies = new ArrayList<>();

    protected abstract String getRequestUrl();

    protected abstract int getSourcePropertyClass();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grid_card, container, false);

        textViewError = (TextView) view.findViewById(R.id.text_error);
        RecyclerView moviesRecyclerView = (RecyclerView) view.findViewById(R.id.home_movies_list);

        initMoviesGridRecyclerView(moviesRecyclerView);

        if (savedInstanceState != null) {
            movies = (List<MovieSmall>) savedInstanceState.getSerializable(MOVIES_LIST);
            movieListAdapter.setElements(movies);
        } else {
            sendApiRequest();
        }

        // Inflate the layout for this fragment
        return view;
    }

    protected void initMoviesGridRecyclerView(RecyclerView moviesRecyclerView) {
        moviesRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        movieListAdapter = new MovieListAdapter(getActivity());
        movieListAdapter.setSelectionListener(this);
        moviesRecyclerView.setAdapter(movieListAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(MOVIES_LIST, (java.io.Serializable) movies);
    }

    protected void sendApiRequest() {
        RequestQueue requestQueue = Repository.getInstance().getRequestQueue();

        if (getRequestUrl() != null) {
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
    }

    protected void handleResponse(JSONObject response) {
        textViewError.setVisibility(View.GONE);
        movies = JsonUtils.parseMovieSmallListJsonResponse(response);
        if (movies.isEmpty()) {
            textViewError.setVisibility(View.VISIBLE);
            textViewError.setText(R.string.error_no_result);
        } else {
            movieListAdapter.setElements(movies);
        }
    }

    protected void handleError(VolleyError error) {
        textViewError.setVisibility(View.VISIBLE);
        if (error instanceof TimeoutError || error instanceof NoConnectionError || error instanceof NetworkError) {
            textViewError.setText(R.string.error_timeout);
        } else if (error instanceof AuthFailureError || error instanceof ServerError) {
            textViewError.setText(R.string.error_auth_failure);
        } else if (error instanceof ParseError) {
            textViewError.setText(R.string.error_parser);
        }
    }

    public void refresh() {
        sendApiRequest();
    }

    @Override
    public void itemClicked(final View view, final int i) {
        int position = i;
        if (movieListAdapter.hasHeader()) {
            if (position == MovieListAdapter.VIEW_TYPE_HEADER) {
                return;
            }
            position -= 1;
        }
        Intent movieDetailsIntent = new Intent(getActivity(), MovieDetailsActivity.class);
        movieDetailsIntent.putExtra("id", movies.get(position).getId());
        movieDetailsIntent.putExtra("source", getSourcePropertyClass());

        if (movies.get(position).getBackdropPath() != null) {
            getMovieBackropAndPassToMovieActivity(movieDetailsIntent, position);
        } else {
            startActivity(movieDetailsIntent);
        }
    }

    private void getMovieBackropAndPassToMovieActivity(final Intent intent, final int position) {
        ImageLoader imageLoader = Repository.getInstance().getImageLoader();
        imageLoader.get(UrlEndpoints.URL_API_IMAGES + UrlEndpoints.URL_BACKDROP_SMALL + movies.get(position).getBackdropPath(), new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                Bitmap bitmap = response.getBitmap();
                if (!isImmediate || bitmap != null) {
                    if (bitmap != null) {
                        intent.putExtra("backdrop", bitmap);
                        getVibrantColorsAndPassToMovieActivity(intent, bitmap);
                    } else {
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                startActivity(intent);
            }
        });
    }

    private void getVibrantColorsAndPassToMovieActivity(final Intent intent, final Bitmap bitmap) {
        Palette.generateAsync(bitmap, new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                Palette.Swatch vibrant = palette.getVibrantSwatch();
                if (vibrant != null) {
                    intent.putExtra("vibrantRgbColor", vibrant.getRgb());
                    intent.putExtra("vibrantTitleTextColor", vibrant.getTitleTextColor());
                }
                startActivity(intent);
            }
        });
    }
}
