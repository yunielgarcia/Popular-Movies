package com.example.android.popularmovies;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.utilities.AsyncMovieTaskLoader;
import com.example.android.popularmovies.utilities.NetworkUtils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ListItemClickListener {

    private String selectedOption = NetworkUtils.SORT_BY_POPULAR_MOVIE;
    private static final String SELECTED_OPTION = "selectedOption";

    private static final int MOVIES_LOADER_ID = 0;
    private static final int FAVORITE_LOADER_ID = 2;

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;

    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;

    AsyncMovieTaskLoader asyncMovieTaskLoader;

    //The correct answer is as per @dymmeh's comment, i.e. not for the Activity to implement
    // two LoaderCallbacks interfaces but for the activity to contain two LoaderCallbacks
    // implementations. By way of example: initialise your LoaderCallbacks fields in your activity...

    private LoaderManager.LoaderCallbacks<ArrayList<Film>> dataNetworkSourceLoaderListener;
    private LoaderManager.LoaderCallbacks<Cursor> dataBaseSourceLoaderListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
         * The ProgressBar that will indicate to the user that we are loading data. It will be
         * hidden when no data is loading.
         *
         * Please note: This so called "ProgressBar" isn't a bar by default. It is more of a
         * circle. We didn't make the rules (or the names of Views), we just follow them.
         */
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        /*
         * Using findViewById, we get a reference to our RecyclerView from xml. This allows us to
         * do things like set the adapter of the RecyclerView and toggle the visibility.
         */
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movie);

        /* This TextView is used to display errors and will be hidden if there are no errors */
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        /*
         * LinearLayoutManager can support HORIZONTAL or VERTICAL orientations. The reverse layout
         * parameter is useful mostly for HORIZONTAL layouts that should reverse for right to left
         * languages.
         */
        GridLayoutManager layoutManager
                = new GridLayoutManager(this, 2);

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        /*
         * Use this setting to improve performance if you know that changes in content do not
         * change the child layout size in the RecyclerView
         */
        mRecyclerView.setHasFixedSize(true);

        /*
         * The MovieAdapter is responsible for linking our weather data with the Views that
         * will end up displaying our movie data.
         */
        mMovieAdapter = new MovieAdapter(this);


        /* Setting the adapter attaches it to the RecyclerView in our layout. */
        mRecyclerView.setAdapter(mMovieAdapter);


        dataNetworkSourceLoaderListener = new LoaderManager.LoaderCallbacks<ArrayList<Film>>() {

            @Override
            public Loader<ArrayList<Film>> onCreateLoader(int id, Bundle args) {
                asyncMovieTaskLoader = new AsyncMovieTaskLoader(getBaseContext(), selectedOption);
                mLoadingIndicator.setVisibility(View.VISIBLE);
                return asyncMovieTaskLoader;
            }

            @Override
            public void onLoadFinished(Loader<ArrayList<Film>> loader, ArrayList<Film> movieListResults) {
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                if (movieListResults != null) {
                    showWeatherDataView();
                    mMovieAdapter.setMovieData(movieListResults);
                } else {
                    showErrorMessage();
                }
            }

            @Override
            public void onLoaderReset(Loader<ArrayList<Film>> loader) {

            }
        };

        dataBaseSourceLoaderListener = new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                return new CursorLoader(getBaseContext(), MovieContract.MovieEntry.CONTENT_URI, null, null, null, null);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
//                ArrayList<WhateverTypeYouWant> mArrayList = new ArrayList<WhateverTypeYouWant>();
//                for(mCursor.moveToFirst(); !mCursor.isAfterLast(); mCursor.moveToNext()) {
//                    // The Cursor is now set to the right position
//                    mArrayList.add(mCursor.getWhateverTypeYouWant(WHATEVER_COLUMN_INDEX_YOU_WANT));
//                }
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {

            }
        };


        loadMovieData();

    }

    private void loadMovieData() {
        getLoaderManager().initLoader(MOVIES_LOADER_ID, null, dataNetworkSourceLoaderListener);
    }

    @Override
    public void onListItemClick(Film filmSelected) {
        Intent startMovieDetailIntent = new Intent(this, MovieDetail.class);
        startMovieDetailIntent.putExtra("movieSelected", filmSelected);
        startActivity(startMovieDetailIntent);
    }


    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.sort_popular:
                selectedOption = NetworkUtils.SORT_BY_POPULAR_MOVIE;
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(
                        new Intent("DataSourceChangeNotification").putExtra(SELECTED_OPTION, selectedOption));
                return true;
            case R.id.sort_highest_rated:
                selectedOption = NetworkUtils.SORT_BY_TOP_RATED_MOVIE;
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(
                        new Intent("DataSourceChangeNotification").putExtra(SELECTED_OPTION, selectedOption));
                return true;
            case R.id.sort_favorite:
                getLoaderManager().initLoader(FAVORITE_LOADER_ID, null, dataBaseSourceLoaderListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method will make the View for the movies data visible and
     * hide the error message.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showWeatherDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the weather data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the error message visible and hide the movies
     * View.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

}
