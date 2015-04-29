package com.learning.android.movieman.util;

import com.learning.android.movieman.backend.MovieManApplication;

/**
 * Created by adrianrusu on 3/22/15.
 */
public class UrlEndpoints {
    public static final String URL_PARAM_API_KEY = "?api_key=" + MovieManApplication.API_KEY;
    public static final String URL_PARAM_QUERY = "&query=";
    public static final String URL_PARAM_SEARCH_TYPE_AUTOCOMPLETE = "&search_type=ngram";
    public static final String URL_PARAM_APPEND = "&append_to_response=credits,trailers";

    public static final String URL_API_HOME = "http://api.themoviedb.org/3/";
    public static final String URL_API_MOVIE = URL_API_HOME + "movie/";
    public static final String URL_API_NOW_PLAYING = URL_API_MOVIE + "now_playing" + URL_PARAM_API_KEY;
    public static final String URL_API_UPCOMING = URL_API_MOVIE + "upcoming" + URL_PARAM_API_KEY;
    public static final String URL_API_POPULAR = URL_API_MOVIE + "popular" + URL_PARAM_API_KEY;
    public static final String URL_API_TOP_RATED = URL_API_MOVIE + "top_rated" + URL_PARAM_API_KEY;
    public static final String URL_API_LATEST = URL_API_MOVIE + "changes" + URL_PARAM_API_KEY;
    public static final String URL_API_SEARCH = URL_API_HOME + "search/movie" + URL_PARAM_API_KEY + URL_PARAM_QUERY;
    public static final String URL_API_IMAGES = "http://image.tmdb.org/t/p/";
    public static final String URL_BACKDROP_SMALL = "w300";
    public static final String URL_BACKDROP_MEDIUM = "w780";
    public static final String URL_BACKDROP_LARGE = "w1280";
    public static final String URL_POSTER_SMALL = "w154";
    public static final String URL_POSTER_MEDIUM = "w342";
    public static final String URL_POSTER_LARGE = "w780";
    public static final String URL_PROFILE_SMALL = "w45";
    public static final String URL_PROFILE_MEDIUM = "w185";
    public static final String URL_PROFILE_LARGE = "w632";
}
