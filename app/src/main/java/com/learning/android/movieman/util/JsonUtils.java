package com.learning.android.movieman.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.learning.android.movieman.model.Movie;
import com.learning.android.movieman.model.MovieSmall;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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

    public static List<MovieSmall> parseMovieSmallListJsonResponse(JSONObject jsonObject) {
        List<MovieSmall> result = new ArrayList<>();
        if (jsonObject == null || jsonObject.length() == 1) {
            return result;
        }

        try {
            Gson gson = getGsonForApi();
            result = gson.fromJson(jsonObject.get(JsonUtils.KEY_RESULTS).toString(), new TypeToken<List<MovieSmall>>() {
            }.getType());
        } catch (JSONException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    public static Movie parseMovieJsonResponse(JSONObject jsonObject) {
        if (jsonObject == null || jsonObject.length() == 1) {
            return null;
        }

        return getGsonForApi().fromJson(jsonObject.toString(), Movie.class);
    }
}
