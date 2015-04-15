package com.learning.android.movieman.backend;

import android.app.Application;
import android.content.Context;

/**
 * Created by adrianrusu on 3/22/15.
 */
public class MovieManApplication extends Application {

    public static final String API_KEY = "b59556d48c2a757635e500ba15896257";
    private static MovieManApplication instance;

    public static MovieManApplication getInstance() {
        return instance;
    }

    public static Context getAppContext() {
        return instance.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Repository.setInstance(new Repository(getApplicationContext()));
    }
}
