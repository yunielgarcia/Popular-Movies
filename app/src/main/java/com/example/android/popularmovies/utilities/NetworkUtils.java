package com.example.android.popularmovies.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by ygarcia on 4/5/2017.
 */

public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String MOVIE_BASE_URL = "https://api.themoviedb.org/3/";

    private static final String MOVIES = "movie/";

    private static final String TRAILERS = "/videos";
    private static final String REVIEWS = "/reviews";

    private static final String POPULAR_MOVIE = MOVIES + "popular";
    public static final String SORT_BY_POPULAR_MOVIE = "0";

    private static final String TOP_RATED_MOVIE = MOVIES + "top_rated";
    public static final String SORT_BY_TOP_RATED_MOVIE = "1";

    private static final String KEY_PARAM = "api_key";
    private static final String API_KEY_VALUE = "137e317c7f6b4260303bbee302a5af98";

    public static final String BASE_POST_URL =  "https://image.tmdb.org/t/p";
    public static final String POST_FILE_SIZE_URL =  "/w500";





    /**
     * Builds the URL used to talk to the movie server using a sorting param.
     *
     * @return The URL to use to query the movie server.
     * */
    public static URL buildUrl(String sortParam) {

        String sortingChoice;

        switch (sortParam){
            case SORT_BY_POPULAR_MOVIE:
                sortingChoice = POPULAR_MOVIE;
                break;
            case SORT_BY_TOP_RATED_MOVIE:
                sortingChoice = TOP_RATED_MOVIE;
                break;
            default:
                sortingChoice = POPULAR_MOVIE;
                break;
        }

        Uri builtUri = Uri.parse(MOVIE_BASE_URL + sortingChoice).buildUpon()
                .appendQueryParameter(KEY_PARAM, API_KEY_VALUE)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static URL buildTrailerUrl(String movieId){
        Uri builtUri = Uri.parse(MOVIE_BASE_URL + MOVIES + movieId + TRAILERS).buildUpon()
                .appendQueryParameter(KEY_PARAM, API_KEY_VALUE)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static URL buildReviewUrl(String movieId){
        Uri builtUri = Uri.parse(MOVIE_BASE_URL + MOVIES + movieId + REVIEWS).buildUpon()
                .appendQueryParameter(KEY_PARAM, API_KEY_VALUE)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }


    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
