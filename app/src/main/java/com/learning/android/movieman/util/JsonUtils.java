package com.learning.android.movieman.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.learning.android.movieman.model.Movie;
import com.learning.android.movieman.model.MovieSmall;
import com.learning.android.movieman.model.MovieUpdate;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {
    public static final String KEY_RESULTS = "results";

    public static Gson getGsonForApi() {
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

            String jsonString = jsonObject.get(JsonUtils.KEY_RESULTS).toString().replaceAll("\\\"\\\"|\\\"null\"", "null");
            result = gson.fromJson(jsonString, new TypeToken<List<MovieSmall>>() {
            }.getType());
        } catch (JSONException e) {
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

    public static List<MovieUpdate> parseMovieUpdatesJsonResponse(JSONObject jsonObject) {
        List<MovieUpdate> result = new ArrayList<>();
        if (jsonObject == null || jsonObject.length() == 1) {
            return result;
        }

        try {
            Gson gson = getGsonForApi();

            String jsonString = jsonObject.get(JsonUtils.KEY_RESULTS).toString().replaceAll("\\\"\\\"|\\\"null\"", "null");
            result = gson.fromJson(jsonString, new TypeToken<List<MovieUpdate>>() {
            }.getType());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }
}
