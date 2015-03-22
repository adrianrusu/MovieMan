package com.learning.android.movieman.util;

/**
 * Created by adrianrusu on 3/22/15.
 */
public interface JsonKeys {
    public static final String KEY_RESULTS = "results";

    public interface Movie {
        public static final String KEY_POSTER_PATH = "poster_path";
        public static final String KEY_ID = "id";
        public static final String KEY_TITLE = "title";
        public static final String KEY_RELEASE_DATE = "release_date";
        public static final String KEY_VOTE_AVERAGE = "vote_average";
        public static final String KEY_VOTE_COUNT = "vote_count";
        public static final String KEY_POPULARITY = "popularity";
    }
}
