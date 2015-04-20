package com.learning.android.movieman.backend;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.learning.android.movieman.model.MovieState;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adrianrusu on 3/21/15.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "MovieManDB";

    private static final String TABLE_MOVIES = "movies";

    private static final String KEY_MOVIE_ID = "id";
    private static final String KEY_MOVIE_WATCHLIST = "watchlist";
    private static final String KEY_MOVIE_FAVORITE = "favorite";

    private static final String CREATE_TABLE_MOVIES = "CREATE TABLE " + TABLE_MOVIES + " (" +
            KEY_MOVIE_ID + " INTEGER PRIMARY KEY, " +
            KEY_MOVIE_WATCHLIST + " TEXT NOT NULL, " +
            KEY_MOVIE_FAVORITE + " TEXT NOT NULL);";

    private static final String[] SELECT_ALL = new String[]{KEY_MOVIE_ID, KEY_MOVIE_WATCHLIST, KEY_MOVIE_FAVORITE};

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_MOVIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(this.getClass().getCanonicalName(), "Upgrading database. Existing contents will be lost. [" + oldVersion + "] -> [" + newVersion + "]");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIES);

        // Recreate after dropping
        onCreate(db);
    }

    public void persistMovieState(MovieState movieState) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MOVIE_ID, movieState.getMovieId());
        values.put(KEY_MOVIE_WATCHLIST, String.valueOf(movieState.isWatchlist()));
        values.put(KEY_MOVIE_FAVORITE, String.valueOf(movieState.isFavourite()));

        db.insert(TABLE_MOVIES, null, values);
        db.close();
    }

    public MovieState getMovieState(Long movieId) {
        SQLiteDatabase db = getReadableDatabase();
        MovieState result = null;

        Cursor cursor = db.query(TABLE_MOVIES, SELECT_ALL, KEY_MOVIE_ID + "=?", new String[] { String.valueOf(movieId) }, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int watchlistIndex = cursor.getColumnIndex(KEY_MOVIE_WATCHLIST);
                int favouriteIndex = cursor.getColumnIndex(KEY_MOVIE_FAVORITE);

                result = new MovieState(movieId, Boolean.parseBoolean(cursor.getString(favouriteIndex)), Boolean.parseBoolean(cursor.getString(watchlistIndex)));
            }
            cursor.close();
        }
        db.close();
        return result;
    }

    public List<MovieState> getMovieStates() {
        SQLiteDatabase db = getReadableDatabase();
        List<MovieState> resultList = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_MOVIES, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int movieIdIndex = cursor.getColumnIndex(KEY_MOVIE_ID);
                int watchlistIndex = cursor.getColumnIndex(KEY_MOVIE_WATCHLIST);
                int favouriteIndex = cursor.getColumnIndex(KEY_MOVIE_FAVORITE);

                resultList.add(new MovieState(cursor.getLong(movieIdIndex), Boolean.parseBoolean(cursor.getString(favouriteIndex)), Boolean.parseBoolean(cursor.getString(watchlistIndex))));
            }
            cursor.close();
        }
        db.close();
        return resultList;
    }

    public List<Long> getWatchlistMovieIds() {
        SQLiteDatabase db = getReadableDatabase();
        List<Long> resultList = new ArrayList<>();

        Cursor cursor = db.query(TABLE_MOVIES, new String[] { KEY_MOVIE_ID }, KEY_MOVIE_WATCHLIST + "=?", new String[] { String.valueOf(true) }, null, null, null );
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int movieIdIndex = cursor.getColumnIndex(KEY_MOVIE_ID);

                resultList.add(cursor.getLong(movieIdIndex));
            }
            cursor.close();
        }
        db.close();
        return resultList;
    }

    public List<Long> getFavoriteMovieIds() {
        SQLiteDatabase db = getReadableDatabase();
        List<Long> resultList = new ArrayList<>();

        Cursor cursor = db.query(TABLE_MOVIES, new String[] { KEY_MOVIE_ID }, KEY_MOVIE_FAVORITE + "=?", new String[] { String.valueOf(true) }, null, null, null );
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int movieIdIndex = cursor.getColumnIndex(KEY_MOVIE_ID);

                resultList.add(cursor.getLong(movieIdIndex));
            }
            cursor.close();
        }
        db.close();
        return resultList;
    }

    public void updateMovieState(MovieState movieState) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MOVIE_WATCHLIST, String.valueOf(movieState.isWatchlist()));
        values.put(KEY_MOVIE_FAVORITE, String.valueOf(movieState.isFavourite()));

        db.update(TABLE_MOVIES, values, KEY_MOVIE_ID + "=?", new String[] { String.valueOf(movieState.getMovieId()) });
        db.close();
    }

    private void showCurrentMovieState(Long movieId) {
        MovieState persistedMovieState = getMovieState(movieId);
        System.out.println("Persisted movie [id : " + persistedMovieState.getMovieId() + ", isWatchlist : " + persistedMovieState.isWatchlist() + ", isFavorite : " + persistedMovieState.isFavourite() + "]");
    }
}
