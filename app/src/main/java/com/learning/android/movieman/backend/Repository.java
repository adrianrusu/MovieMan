package com.learning.android.movieman.backend;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by adrianrusu on 3/21/15.
 */
public class Repository {

    private static Repository instance;

    private RequestQueue requestQueue;
    private ImageLoader imageLoader;

    public Repository() {
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
        if (instance == null) {
            instance = new Repository();
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }
}
