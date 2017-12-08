package com.tapura.moviestar.data.sql;


import android.net.Uri;
import android.provider.BaseColumns;

public final class FavouriteMoviesContract {

    public static final String AUTHORITY = "com.tapura.moviestar";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_FAVOURITES = "favourites";

    private FavouriteMoviesContract() {
    }

    public static class FavouriteEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVOURITES).build();

        public static final String TABLE_NAME = "favourites";
        public static final String COLUMN_ID_MOVIE = "movie_id";
    }
}
