package com.learning.android.movieman.backend;

import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by adrianrusu on 3/21/15.
 */
public class Repository {

    private static Repository instance;

    private DatabaseHandler dbHandler;

    private Repository(Context context) {
        dbHandler = new DatabaseHandler(context);
    }

    public static Repository getInstance() {
        return instance;
    }

//    private static class RecievePopularMovies extends AsyncTask

}
