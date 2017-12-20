package com.tapura.moviestar.data;


import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import com.tapura.moviestar.model.Movie;

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
        public static final String COLUMN_MOVIE_TITLE = "title";
        public static final String COLUMN_MOVIE_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_MOVIE_OVERVIEW = "overview";
        public static final String COLUMN_MOVIE_RATING = "rating";
        public static final String COLUMN_MOVIE_RELEASE_DATE = "date";
        public static final String COLUMN_MOVIE_POSTER = "poster";
        public static final String COLUMN_MOVIE_BACKDROP = "backdrop";

        public static ContentValues getContentValues(Movie movie) {
            ContentValues cv = new ContentValues();

            cv.put(FavouriteMoviesContract.FavouriteEntry.COLUMN_ID_MOVIE, movie.getId());
            cv.put(FavouriteMoviesContract.FavouriteEntry.COLUMN_MOVIE_TITLE, movie.getTitle());
            cv.put(FavouriteMoviesContract.FavouriteEntry.COLUMN_MOVIE_ORIGINAL_TITLE, movie.getOriginalTitle());
            cv.put(FavouriteMoviesContract.FavouriteEntry.COLUMN_MOVIE_OVERVIEW, movie.getOverview());
            cv.put(FavouriteMoviesContract.FavouriteEntry.COLUMN_MOVIE_RATING, movie.getVoteAverage());
            cv.put(FavouriteMoviesContract.FavouriteEntry.COLUMN_MOVIE_RELEASE_DATE, movie.getReleaseDate());
            cv.put(FavouriteMoviesContract.FavouriteEntry.COLUMN_MOVIE_POSTER, movie.getPosterPath());
            cv.put(FavouriteMoviesContract.FavouriteEntry.COLUMN_MOVIE_BACKDROP, movie.getBackdropPath());

            return cv;
        }

        public static Movie getMovie(Cursor c) {
            Movie m = new Movie();

            m.setId(c.getInt(c.getColumnIndexOrThrow(FavouriteMoviesContract.FavouriteEntry.COLUMN_ID_MOVIE)));
            m.setTitle(c.getString(c.getColumnIndexOrThrow(FavouriteMoviesContract.FavouriteEntry.COLUMN_MOVIE_TITLE)));
            m.setOriginalTitle(c.getString(c.getColumnIndexOrThrow(FavouriteMoviesContract.FavouriteEntry.COLUMN_MOVIE_ORIGINAL_TITLE)));
            m.setOverview(c.getString(c.getColumnIndexOrThrow(FavouriteMoviesContract.FavouriteEntry.COLUMN_MOVIE_OVERVIEW)));
            m.setPosterPath(c.getString(c.getColumnIndexOrThrow(FavouriteMoviesContract.FavouriteEntry.COLUMN_MOVIE_POSTER)));
            m.setBackdropPath(c.getString(c.getColumnIndexOrThrow(FavouriteMoviesContract.FavouriteEntry.COLUMN_MOVIE_BACKDROP)));
            m.setVoteAverage(c.getDouble(c.getColumnIndexOrThrow(FavouriteMoviesContract.FavouriteEntry.COLUMN_MOVIE_RATING)));
            m.setReleaseDate(c.getString(c.getColumnIndexOrThrow(FavouriteMoviesContract.FavouriteEntry.COLUMN_MOVIE_RELEASE_DATE)));
            return m;
        }
    }
}
