package com.example.android.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Yggarcia on 4/8/2017.
 */

public class Film implements Parcelable{

    private String mTitle;
    private String mReleaseDate;
    private String mPosterPath;
    private float mVoteAverage;
    private String mOverview;
    private int mSourceId;

    public Film(String mTitle, String mReleaseDate, String mPosterPath, float mVoteAverage, String overview, int sourceId) {
        this.mTitle = mTitle;
        this.mReleaseDate = mReleaseDate;
        this.mPosterPath = mPosterPath;
        this.mVoteAverage = mVoteAverage;
        this.mOverview = overview;
        this.mSourceId = sourceId;
    }

    protected Film(Parcel in) {
        mTitle = in.readString();
        mReleaseDate = in.readString();
        mPosterPath = in.readString();
        mVoteAverage = in.readFloat();
        mOverview = in.readString();
        mSourceId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mReleaseDate);
        dest.writeString(mPosterPath);
        dest.writeFloat(mVoteAverage);
        dest.writeString(mOverview);
        dest.writeInt(mSourceId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Film> CREATOR = new Creator<Film>() {
        @Override
        public Film createFromParcel(Parcel in) {
            return new Film(in);
        }

        @Override
        public Film[] newArray(int size) {
            return new Film[size];
        }
    };

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

    public int getmSourceId() {
        return mSourceId;
    }
}
