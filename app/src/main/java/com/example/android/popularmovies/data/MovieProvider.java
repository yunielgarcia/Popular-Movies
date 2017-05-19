package com.example.android.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by ygarcia on 5/18/2017.
 */

public class MovieProvider extends ContentProvider{

    /** Tag for the log messages */
    public static final String LOG_TAG = MovieProvider.class.getSimpleName();

    /** URI matcher code for the content URI for the movies table */
    private static final int MOVIES = 100;

    /** URI matcher code for the content URI for a single movie in the movies table */
    private static final int MOVIE_ID = 101;

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDbHelper mOpenHelper;

    public static UriMatcher buildUriMatcher() {

        /*
         * All paths added to the UriMatcher have a corresponding code to return when a match is
         * found. The code passed into the constructor of UriMatcher here represents the code to
         * return for the root URI. It's common to use NO_MATCH as the code for this case.
         */
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        /*
         * For each type of URI you want to add, create a corresponding code. Preferably, these are
         * constant fields in your class so that you can use them throughout the class and you no
         * they aren't going to change. In Sunshine, we use CODE_WEATHER or CODE_WEATHER_WITH_DATE.
         */

        /* This URI is content://com.example.android.popularmovies/movies/ */
        matcher.addURI(authority, MovieContract.PATH_MOVIES, MOVIES);

        /*
         * This URI would look something like content://com.example.android.popularmovies/movies/14
         * The "/#" signifies to the UriMatcher that if PATH_MOVIES is followed by ANY number,
         * that it should return the MOVIE_ID code
         */
        matcher.addURI(authority, MovieContract.PATH_MOVIES + "/#", MOVIE_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIE_ID:
                return insertMovie(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    /**
     * Insert a movie into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */
    private Uri insertMovie(Uri uri, ContentValues values) {

        validateValues(values);

        // Get writable database
        SQLiteDatabase database = mOpenHelper.getWritableDatabase();

        // Insert the new pet with the given values
        long id = database.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);

        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        //Notify all listeners that the data has changed for the movie content URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Once we know the ID of the new row in the table,
        // return the new URI with the ID appended to the end of it
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    private void validateValues(ContentValues values){
        // Check that the values are not null
        String title = values.getAsString(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE);
        // Check that the title is not null
        if (title == null) {
            throw new IllegalArgumentException("Movie requires a name");
        }
    }
}
