package com.learning.android.movieman.backend;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by adrianrusu on 3/21/15.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "MovieManDB";

    private static final String TABLE_WATCHLIST = "WATCHLIST";
    private static final String KEY_WATCHLIST_ID = "ID";

    private static final String TABLE_FAVORITES = "FAVORITES";
    private static final String KEY_FAVORITE_ID = "ID";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
