package com.example.android.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ygarcia on 5/18/2017.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = MovieDbHelper.class.getSimpleName();

    /**
     * Name of the database file
     */
    private static final String DATABASE_NAME = "popularMovies.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 2;

    /**
     * Constructs a new instance of {@link MovieDbHelper}.
     *
     * @param context of the app
     */
    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Create a String that contains the SQL statement to create the pets table
    String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " ("
            + MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + MovieContract.MovieEntry.COLUMN_SOURCE_ID + " INTEGER NOT NULL, "
            + MovieContract.MovieEntry.COLUMN_IMG_PATH + " TEXT NOT NULL ,"
            + MovieContract.MovieEntry.COLUMN_RELEASE_DATE + " TEXT ,"
            + MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE + " REAL ,"
            + MovieContract.MovieEntry.COLUMN_OVERVIEW + " TEXT ,"
            + MovieContract.MovieEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL );";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME;

    /**
     * +     * This is called when the database is created for the first time.
     * +
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Execute the SQL statement
        db.execSQL(SQL_CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
