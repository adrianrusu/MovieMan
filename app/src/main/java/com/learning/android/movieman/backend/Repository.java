package com.learning.android.movieman.backend;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.learning.android.movieman.model.MovieState;

public class Repository {

    private static Repository instance;

    private DatabaseHandler dbHandler;

    private RequestQueue requestQueue;
    private ImageLoader imageLoader;

    public Repository(Context context) {
        dbHandler = new DatabaseHandler(context);

        requestQueue = Volley.newRequestQueue(MovieManApplication.getAppContext());
        imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {

            private LruCache<String, Bitmap> cache = new LruCache<>((int) ((Runtime.getRuntime().maxMemory() / 1024) / 8));

            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });
    }

    public static Repository getInstance() {
        return instance;
    }

    public static void setInstance(Repository instance) {
        Repository.instance = instance;
    }

    public DatabaseHandler getDbHandler() {
        return dbHandler;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    public String toggleWatchlist(Long movieId) {
        MovieState movieState = dbHandler.getMovieState(movieId);
        if (movieState == null) {
            movieState = new MovieState(movieId, false, true, null);
            dbHandler.persistMovieState(movieState);
            return "Added to watchlist";
        } else if (movieState.isWatchlist()) {
            movieState.setWatchlist(false);
            dbHandler.updateMovieState(movieState);
            return "Removed from watchlist";
        } else {
            movieState.setWatchlist(true);
            dbHandler.updateMovieState(movieState);
            return "Added to watchlist";
        }
    }

    public String toggleFavorite(Long movieId) {
        MovieState movieState = dbHandler.getMovieState(movieId);
        if (movieState == null) {
            movieState = new MovieState(movieId, true, false, null);
            dbHandler.persistMovieState(movieState);
            return "Added to favorites";
        } else if (movieState.isFavourite()) {
            movieState.setFavourite(false);
            dbHandler.updateMovieState(movieState);
            return "Removed from favorites";
        } else {
            movieState.setFavourite(true);
            dbHandler.updateMovieState(movieState);
            return "Added to favorites";
        }
    }
}