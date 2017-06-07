package com.example.android.popularmovies.async;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.android.popularmovies.model.Trailer;
import com.example.android.popularmovies.utilities.NetworkUtils;
import com.example.android.popularmovies.utilities.OpenMovieJsonUtils;

import java.net.URL;

/**
 * Created by ygarcia on 6/6/2017.
 */

public class AsyncTrailerTaskLoader extends AsyncTaskLoader<Trailer>{

    private Trailer mData;
    private String mMovieId;


    public AsyncTrailerTaskLoader(Context context, String movieId) {
        super(context);
        mMovieId = movieId;
    }

    @Override
    protected void onStartLoading() {
        if (mData != null) {
            // Use cached data
            deliverResult(mData);
        } else {
            // We have no data, so kick off loading it
            forceLoad();
        }
    }

    @Override
    public Trailer loadInBackground() {
        String trailerJsonResult = null;
        Trailer trailer;
        URL trailersUrl = NetworkUtils.buildTrailerUrl(mMovieId);
        try {
            trailerJsonResult = NetworkUtils.getResponseFromHttpUrl(trailersUrl);
            trailer = OpenMovieJsonUtils.getTrailer(trailerJsonResult);

            return trailer;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deliverResult(Trailer data) {
        mData = data;
        super.deliverResult(mData);
    }
}
