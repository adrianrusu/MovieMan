package com.learning.android.movieman.backend;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.learning.android.movieman.model.MovieState;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adrianrusu on 3/21/15.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "MovieManDB";

    private static final String TABLE_MOVIES = "MOVIES";

    private static final String KEY_MOVIE_ID = "ID";
    private static final String KEY_MOVIE_WATCHLIST = "WATCHLIST";
    private static final String KEY_MOVIE_FAVORITE = "FAVORITE";

    private static final String[] SELECT_ALL = new String[]{KEY_MOVIE_ID, KEY_MOVIE_WATCHLIST, KEY_MOVIE_FAVORITE};

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_MOVIES + " (" +
                KEY_MOVIE_ID + " INTEGER PRIMARY KEY, " +
                KEY_MOVIE_WATCHLIST + " VARCHAR(5), " +
                KEY_MOVIE_FAVORITE + " VARCHAR(5))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIES);
        onCreate(db);
    }

    public void addMovie(Long id) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MOVIE_ID, id);

        db.insert(TABLE_MOVIES, null, values);
    }

    public MovieState getMovieState(Long id) {
        SQLiteDatabase db = getReadableDatabase();
        MovieState result = null;

        Cursor cursor = db.query(TABLE_MOVIES, SELECT_ALL, KEY_MOVIE_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor.moveToFirst()) {
            result = new MovieState(cursor.getLong(0), Boolean.valueOf(cursor.getString(1)), Boolean.valueOf(cursor.getString(2)));
        }

        return result;
    }

    private void updateMovieWatchlist(Long id, boolean state) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MOVIE_WATCHLIST, String.valueOf(state));

        int result = db.update(TABLE_MOVIES, values, KEY_MOVIE_ID + "=?", new String[]{String.valueOf(id)});
        db.close();

        if (result == 0) {
            addMovie(id);
            updateMovieFavorite(id, state);
        }
    }

    private void updateMovieFavorite(Long id, boolean state) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MOVIE_FAVORITE, String.valueOf(state));

        int result = db.update(TABLE_MOVIES, values, KEY_MOVIE_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        if (result == 0) {
            addMovie(id);
            updateMovieFavorite(id, state);
        }
    }

    public List<Long> getWatchlistIds() {
        SQLiteDatabase db = getReadableDatabase();
        List<Long> resultList = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_MOVIES, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                if (Boolean.parseBoolean(cursor.getString(1))) {
                    resultList.add(cursor.getLong(0));
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();

        return resultList;
    }

    public List<Long> getFavouritesIds() {
        SQLiteDatabase db = getReadableDatabase();
        List<Long> resultList = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_MOVIES, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                if (Boolean.parseBoolean(cursor.getString(2))) {
                    resultList.add(cursor.getLong(0));
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();

        return resultList;
    }

    public boolean isInWatchlist(Long id) {
        MovieState state = getMovieState(id);
        if (state != null) {
            return state.isWatchlist();
        } else {
            return false;
        }
    }

    public boolean isInFavourites(Long id) {
        MovieState state = getMovieState(id);
        if (state != null) {
            return state.isFavourite();
        } else {
            return false;
        }
    }

    public void addToWatchlist(Long id) {
        updateMovieWatchlist(id, true);
    }

    public void addToFavorites(Long id) {
        updateMovieFavorite(id, true);
    }

    public void removeFromWatchList(Long id) {
        updateMovieWatchlist(id, false);
    }

    public void removeFromFavorites(Long id) {
        updateMovieFavorite(id, false);
    }
}
