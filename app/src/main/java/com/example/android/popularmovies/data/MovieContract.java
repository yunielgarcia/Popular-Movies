package com.example.android.popularmovies.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by ygarcia on 5/18/2017.
 */

public final class MovieContract {

    /**
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website.  A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * device.
     */
    public static final String CONTENT_AUTHORITY = "com.example.android.popularmovies";

    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Possible path (appended to base content URI for possible URI's)
     * For instance, content://com.example.android.pets/pets/ is a valid path for
     * looking at pet data. content://com.example.android.pets/staff/ will fail,
     * as the ContentProvider hasn't been given any information on what to do with "staff".
     */
    public static final String PATH_MOVIES = "movies";

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private MovieContract() {
    }


    /**
     * Inner class that defines constant values for the movies database table.
     * Each entry in the table represents a single movie.
     */
    public static final class MovieEntry implements BaseColumns{

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MOVIES);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of pets.
         */
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;

        public final static String TABLE_NAME = "movies";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_MOVIE_TITLE = "title";
        public final static String COLUMN_MOVIE_ID = "movieId";
    }

}
