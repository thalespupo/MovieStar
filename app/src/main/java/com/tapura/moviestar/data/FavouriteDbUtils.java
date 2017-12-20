package com.tapura.moviestar.data;


import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.tapura.moviestar.model.Movie;

import java.util.ArrayList;
import java.util.List;

import static com.tapura.moviestar.Constants.APP_TAG;

public class FavouriteDbUtils {

    private static final String CLASS_TAG = FavouriteDbUtils.class.getSimpleName() + ":: ";

    public static void insert(Context context, Movie movie) {
        context.getContentResolver().insert(FavouriteMoviesContract.FavouriteEntry.CONTENT_URI,
                FavouriteMoviesContract.FavouriteEntry.getContentValues(movie));
    }

    public static void delete(Context context, int id) {
        Uri uri = FavouriteMoviesContract.FavouriteEntry.CONTENT_URI.buildUpon()
                .appendPath(String.valueOf(id))
                .build();
        context.getContentResolver().delete(uri, null, null);
    }

    public static boolean isFavouriteMovie(Context context, int id) {

        Cursor cursor = context.getContentResolver().query(ContentUris.withAppendedId(FavouriteMoviesContract.FavouriteEntry.CONTENT_URI,
                id),
                null,
                null,
                null,
                null);
        if (cursor == null) return false;
        cursor.close();
        return cursor.getCount() > 0;
    }

    public static List<Movie> getFavouriteMovies(Context context) {
        Cursor c = null;
        try {
            c = context.getContentResolver().query(FavouriteMoviesContract.FavouriteEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    FavouriteMoviesContract.FavouriteEntry.COLUMN_ID_MOVIE);
        } catch (Exception e) {
            Log.e(APP_TAG, CLASS_TAG + "Failed to asynchronously load data.");
            e.printStackTrace();
            return null;
        }

        List<Movie> list = new ArrayList<>();
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                list.add(FavouriteMoviesContract.FavouriteEntry.getMovie(c));
                c.moveToNext();
            }
        }
        if (c != null) {
            c.close();
        }
        return list;
    }
}
