package com.learning.android.movieman.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by adrianrusu on 3/22/15.
 */
public class JsonUtils {
    public static final String KEY_RESULTS = "results";

    public static final Gson getGsonForApi() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("yyyy-MM-dd");
        return gsonBuilder.create();
    }
}
