package com.example.android.popularmovies;

/**
 * Created by Yggarcia on 4/8/2017.
 */

public class Film {

    private String mTitle;
    private String mReleaseDate;
    private String mPosterPath;
    private float mVoteAverage;
    private String mOverview;

    public Film(String mTitle, String mReleaseDate, String mPosterPath, float mVoteAverage, String overview) {
        this.mTitle = mTitle;
        this.mReleaseDate = mReleaseDate;
        this.mPosterPath = mPosterPath;
        this.mVoteAverage = mVoteAverage;
        this.mOverview = overview;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmReleaseDate() {
        return mReleaseDate;
    }

    public String getmPosterPath() {
        return mPosterPath;
    }

    public float getmVoteAverage() {
        return mVoteAverage;
    }

    public String getmOverview() {
        return mOverview;
    }
}
