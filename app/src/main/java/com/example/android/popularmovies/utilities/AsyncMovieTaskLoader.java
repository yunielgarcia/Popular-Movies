package com.example.android.popularmovies.utilities;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.android.popularmovies.Film;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by ygarcia on 5/19/2017.
 */

public class AsyncMovieTaskLoader extends AsyncTaskLoader<ArrayList<Film>>{
    private String mSortOption;
    private ArrayList<Film> mCached;
    private Throwable mError;
    private LoaderListener<ArrayList<Film>> mListener;

    public AsyncMovieTaskLoader(Context context, String sortOption) {
        super(context);
        mSortOption = sortOption;
        this.init();
    }

    private void init() {
        this.mError = null;
    }

    public void setListener(LoaderListener<ArrayList<Film>> listener) {
        this.mListener = listener;
    }

    /**
     * Called when there is new data to deliver to the client.  The
     * super class will take care of delivering it; the implementation
     * here just adds a little more logic.
     */
    @Override
    public void deliverResult(ArrayList<Film> data) {
        // If the loader is reset and it is going to finish, purge the cached data
        if (this.isReset()) {
            if (mCached != null) {
                mCached = null;
            }
            return;
        }

        mCached = data;

        if (this.isStarted()) {
            super.deliverResult(data);
        }
    }

    @Override
    protected void onStartLoading() {
        // Return the cached data if exists
        if (mCached != null) {
            deliverResult(mCached);
            return;
        }

        // If data source is changed or cached data is null, try to get data
        if (takeContentChanged() || mCached == null) {
            this.forceLoad();
        }
    }

    /**
     * Handles a request to stop the Loader.
     */
    @Override
    protected void onStopLoading() {
        this.cancelLoad();
    }

    /**
     * Handles a request to completely reset the Loader.
     */
    @Override
    protected void onReset() {
        super.onReset();

        // Ensure the loader is stopped
        onStopLoading();

        // Initialize status and delete the cached data so that, in the next time, new data is fetched
        init();
        mCached = null;
    }


    @Override
    public ArrayList<Film> loadInBackground() {

        if (mListener != null) {
            mListener.onLoadStarted(this);
        }

        String movieResults = null;
        ArrayList<Film> movieListResults;
        URL movieRequestUrl = NetworkUtils.buildUrl(mSortOption);
        try {
            movieResults = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);
            movieListResults = OpenMovieJsonUtils.getArrayListFromJson(movieResults);
            return movieListResults;

        } catch (Exception e) {
            mError = e;
            e.printStackTrace();
            return null;
        }
    }

    public void refresh(String sortOption) {
        mSortOption = sortOption;
        reset();
        startLoading();
    }

    public Throwable getError() {
        return this.mError;
    }

    public boolean hasError() {
        return this.mError != null;
    }

    public static interface LoaderListener<T> {
        public void onLoadStarted(final AsyncTaskLoader<ArrayList<Film>> loader);
        public void onLoadFinished(final AsyncTaskLoader<ArrayList<Film>> loader, ArrayList<Film> data);
    }
}
