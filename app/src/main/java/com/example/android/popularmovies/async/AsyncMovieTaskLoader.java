package com.example.android.popularmovies.async;

import android.content.AsyncTaskLoader;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import com.example.android.popularmovies.model.Film;
import com.example.android.popularmovies.utilities.NetworkUtils;
import com.example.android.popularmovies.utilities.OpenMovieJsonUtils;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by ygarcia on 5/19/2017.
 */

public class AsyncMovieTaskLoader extends AsyncTaskLoader<ArrayList<Film>>{

    private String mSortOption;
    private ArrayList<Film> mData;
    private Context mContext;
    private BroadcastReceiver broadcastReceiver;
    private IntentFilter intentFilter;
    boolean dataSourceChange = false;
    private static final String SELECTED_OPTION = "selectedOption";

    public AsyncMovieTaskLoader(Context context, String sortOption) {
        super(context);
        mContext = context;
        mSortOption = sortOption;
    }

    @Override
    protected void onStartLoading() {
        if (mData != null) {
            // Use cached data
            deliverResult(mData);
        } else {
            // We have no data, so kick off loading it
            forceLoad();
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    dataSourceChange = true;
                    if (intent.hasExtra(SELECTED_OPTION)){
                        String option = intent.getStringExtra(SELECTED_OPTION);
                        mSortOption = option;
                    }
                    forceLoad();
                }
            };

            intentFilter = new IntentFilter();
            intentFilter.addAction("DataSourceChangeNotification");
            LocalBroadcastManager.getInstance(mContext).registerReceiver(broadcastReceiver, intentFilter);
        }
    }

    @Override
    public ArrayList<Film> loadInBackground() {
        String movieResults = null;
        ArrayList<Film> movieListResults;
        URL movieRequestUrl = NetworkUtils.buildUrl(mSortOption);
        try {
            movieResults = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);
            movieListResults = OpenMovieJsonUtils.getArrayListFromJson(movieResults);
            dataSourceChange = false;
            return movieListResults;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deliverResult(ArrayList<Film> data) {
        mData = data;
        super.deliverResult(mData);

    }

    @Override
    protected void onReset() {
        super.onReset();
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(broadcastReceiver);
    }


}
