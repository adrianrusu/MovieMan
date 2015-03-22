package com.learning.android.movieman.util;

import com.learning.android.movieman.backend.MovieManApplication;

/**
 * Created by adrianrusu on 3/22/15.
 */
public class UrlEndpoints {
    public static final String URL_API_HOME = "http://api.themoviedb.org/3/";
    public static final String NOW_PLAYING_URL = URL_API_HOME + "movie/now_playing";
    public static final String URL_CHAR_QUESTION = "?";
    public static final String URL_PARAM_API_KEY = URL_CHAR_QUESTION + "api_key=" + MovieManApplication.API_KEY;
    public static final String URL_CHAR_AMPERSAND = "&";
}
