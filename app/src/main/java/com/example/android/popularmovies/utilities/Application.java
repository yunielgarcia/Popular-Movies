package com.example.android.popularmovies.utilities;

import com.facebook.stetho.Stetho;

/**
 * Created by ygarcia on 6/6/2017.
 */

public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Stetho.initializeWithDefaults(this);
    }
}
