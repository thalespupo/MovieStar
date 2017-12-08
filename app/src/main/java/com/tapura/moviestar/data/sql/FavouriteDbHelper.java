package com.tapura.moviestar.data.sql;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavouriteDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favourite.db";
    private static final int DATABASE_VERSION = 1;

    public FavouriteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_FAVOURITE_TABLE ="CREATE TABLE " +
                FavouriteMoviesContract.FavouriteEntry.TABLE_NAME + " (" +
                FavouriteMoviesContract.FavouriteEntry.COLUMN_ID_MOVIE + " INTEGER PRIMARY KEY " +
                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_FAVOURITE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXIST " + FavouriteMoviesContract.FavouriteEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
