package com.example.android.popularmovies.async;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.android.popularmovies.model.Review;
import com.example.android.popularmovies.utilities.NetworkUtils;
import com.example.android.popularmovies.utilities.OpenMovieJsonUtils;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by ygarcia on 6/7/2017.
 */

public class AsyncReviewsTaskLoader extends AsyncTaskLoader<ArrayList<Review>>{

    private ArrayList<Review> mData;
    private String mMovieId;

    public AsyncReviewsTaskLoader(Context context, String movieId) {
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
    public ArrayList<Review> loadInBackground() {
        String reviewsResults = null;
        ArrayList<Review> reviewsList;

        URL reviewRequestUrl = NetworkUtils.buildReviewUrl(mMovieId);

        try {
            reviewsResults = NetworkUtils.getResponseFromHttpUrl(reviewRequestUrl);
            reviewsList = OpenMovieJsonUtils.getReviewsList(reviewsResults);
            return  reviewsList;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deliverResult(ArrayList<Review> data) {
        mData = data;
        super.deliverResult(mData);
    }
}
