package com.example.android.popularmovies;

import android.app.LoaderManager;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.async.AsyncReviewsTaskLoader;
import com.example.android.popularmovies.async.AsyncTrailerTaskLoader;
import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.model.Film;
import com.example.android.popularmovies.model.Review;
import com.example.android.popularmovies.model.Trailer;
import com.example.android.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieDetail extends AppCompatActivity {

    private static final int EXISTING_MOVIE_LOADER = 1;
    private static final int TRAILER_LOADER = 2;
    private static final int REVIEWS_LOADER = 3;
    private boolean isFavorite;
    private long movieSelectedId;
    // Defines a string to contain the selection clause
    String mSelectionClause = null;
    // Initializes an array to contain selection arguments
    String[] mSelectionArgs = {""};
    //Projection
    private static final String[] PROJECTION = {
            MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_TITLE
    };
    private int mSourceId;


    TextView title_tv;
    ImageView poster_iv;
    TextView date_tv;
    TextView vote_tv;
    TextView plot_tv;
    MenuItem favMenuItem;
    ImageView play_img_view;

    AsyncTrailerTaskLoader asyncTrailerTaskLoader;

    Film movieSelected;
    Trailer trailer;
    ArrayList<Review> reviews;

    ReviewAdapter reviewAdapter;
    ListView reviewsListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        //retrieving data passed to the activity
        movieSelected = getIntent().getParcelableExtra("movieSelected");
        mSourceId = movieSelected.getmSourceId();

        String full_img_url = NetworkUtils.BASE_POST_URL + NetworkUtils.POST_FILE_SIZE_URL + movieSelected.getmPosterPath();

        //finding views
        title_tv = (TextView) findViewById(R.id.tv_detail_movie_title);
        poster_iv = (ImageView) findViewById(R.id.iv_detail_movie_poster);
        date_tv = (TextView) findViewById(R.id.tv_detail_date);
        vote_tv = (TextView) findViewById(R.id.tv_detail_votes);
        plot_tv = (TextView) findViewById(R.id.tv_plot);
        play_img_view = (ImageView) findViewById(R.id.play_trailer);
        reviewsListView = (ListView) findViewById(R.id.reviews_list_view);

        //initializing views
        title_tv.setText(movieSelected.getmTitle());
        date_tv.setText(movieSelected.getmReleaseDate());
        plot_tv.setText(movieSelected.getmOverview());
        vote_tv.setText(String.valueOf(movieSelected.getmVoteAverage()));
        Picasso.with(this).load(full_img_url).into(poster_iv);

        play_img_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String movie_key = trailer.getKey();

                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + movie_key));
                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.youtube.com/watch?v=" + movie_key));
                try {
                    startActivity(appIntent);
                } catch (ActivityNotFoundException ex) {
                    startActivity(webIntent);
                }
            }
        });

        //get cursor for single movie
        LoaderManager.LoaderCallbacks<Cursor> mMovieDetailLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
                // Constructs a selection clause that matches the Id (sourceId)
                mSelectionClause = MovieContract.MovieEntry.COLUMN_SOURCE_ID + " = ?";

                // Moves the sourceId of the selected movie to the selection arguments.
                mSelectionArgs[0] = String.valueOf(mSourceId);


                return new CursorLoader(getBaseContext(), MovieContract.MovieEntry.CONTENT_URI,
                        PROJECTION, mSelectionClause, mSelectionArgs, null);
            }

            @Override
            public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
                // Some providers return null if an error occurs, others throw an exception
                if (null == data) {
                    Toast.makeText(getBaseContext(), "Sorry, an error happened.", Toast.LENGTH_SHORT).show();
        /*
         * Insert code here to handle the error. Be sure not to use the cursor! You may want to
         * call android.util.Log.e() to log this error.
         *
         */
                    isFavorite = false;
                    // If the Cursor is empty, the provider found no matches
                } else if (data.getCount() < 1) {

        /*
         * Insert code here to notify the user that the search was unsuccessful. This isn't necessarily
         * an error. You may want to offer the user the option to insert a new row, or re-type the
         * search term.
         */
                    isFavorite = false;
                } else {
                    // Insert code here to do something with the results
                    isFavorite = true;
                    int idIndex = data.getColumnIndex("_id");
                    data.moveToFirst();
                    movieSelectedId = data.getInt(idIndex);
                }
            }

            @Override
            public void onLoaderReset(android.content.Loader<Cursor> loader) {

            }
        };
        //get trailers
        LoaderManager.LoaderCallbacks<Trailer> trailersLoaderCallbacks = new LoaderManager.LoaderCallbacks<Trailer>() {
            @Override
            public Loader<Trailer> onCreateLoader(int id, Bundle args) {
                asyncTrailerTaskLoader = new AsyncTrailerTaskLoader(getBaseContext(), String.valueOf(movieSelected.getmSourceId()));
                return asyncTrailerTaskLoader;
            }

            @Override
            public void onLoadFinished(Loader<Trailer> loader, Trailer data) {
                trailer = data;
            }

            @Override
            public void onLoaderReset(Loader<Trailer> loader) {

            }
        };
        //get reviews
        final LoaderManager.LoaderCallbacks<ArrayList<Review>> reviewsLoaderCallbacks = new LoaderManager.LoaderCallbacks<ArrayList<Review>>() {
            @Override
            public Loader<ArrayList<Review>> onCreateLoader(int id, Bundle args) {
                return new AsyncReviewsTaskLoader(getBaseContext(), String.valueOf(movieSelected.getmSourceId()));
            }

            @Override
            public void onLoadFinished(Loader<ArrayList<Review>> loader, ArrayList<Review> data) {
                reviews = data;
                reviewAdapter = new ReviewAdapter(getBaseContext(), reviews);
                reviewsListView.setAdapter(reviewAdapter);
            }

            @Override
            public void onLoaderReset(Loader<ArrayList<Review>> loader) {

            }
        };



        //checking if the current movie is among favorites
        getLoaderManager().initLoader(EXISTING_MOVIE_LOADER, null, mMovieDetailLoaderCallback);
        getLoaderManager().initLoader(TRAILER_LOADER, null, trailersLoaderCallbacks);
        getLoaderManager().initLoader(REVIEWS_LOADER, null, reviewsLoaderCallbacks);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/detail.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.detail, menu);
        favMenuItem = menu.findItem(R.id.action_add_favorite);

        if (isFavorite) {
            favMenuItem.setIcon(R.drawable.ic_star_white_18dp);
        } else {
            favMenuItem.setIcon(R.drawable.ic_star_border_white_18dp);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_add_favorite:
                if (isFavorite) {
                    removeFromFavorite();
                } else {
                    insertToFavorite();
                }

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void removeFromFavorite() {
        //build uri
        Uri itemUri = ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI, movieSelectedId);

        int rowsDeleted = getContentResolver().delete(itemUri, null, null);
        if (rowsDeleted > 0) {
            isFavorite = false;
            favMenuItem.setIcon(R.drawable.ic_star_border_white_18dp);
        } else {
            // Otherwise, the deletion failed and we can display a toast.
            Toast.makeText(this, getString(R.string.remove_fav_failed),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void insertToFavorite() {
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();

        if (TextUtils.isEmpty(movieSelected.getmTitle()) || movieSelected.getmSourceId() == 0 || TextUtils.isEmpty(movieSelected.getmPosterPath())) {
            Toast.makeText(this, getString(R.string.error_add_fav),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, movieSelected.getmTitle());
        values.put(MovieContract.MovieEntry.COLUMN_SOURCE_ID, movieSelected.getmSourceId());
        values.put(MovieContract.MovieEntry.COLUMN_IMG_PATH, movieSelected.getmPosterPath());
        values.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movieSelected.getmReleaseDate());
        values.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movieSelected.getmOverview());
        values.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, movieSelected.getmVoteAverage());


        Uri newUri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, values);

        // Show a toast message depending on whether or not the insertion was successful
        if (newUri == null) {
            // If the new content URI is null, then there was an error with insertion.
            Toast.makeText(this, getString(R.string.insert_movie_failed),
                    Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast.
            isFavorite = true;
            favMenuItem.setIcon(R.drawable.ic_star_white_18dp);
            Toast.makeText(this, getString(R.string.insert_movie_success),
                    Toast.LENGTH_SHORT).show();
        }

    }

}
