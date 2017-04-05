package com.example.android.popularmovies.utilities;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.example.android.popularmovies.MainActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Scanner;

/**
 * Created by ygarcia on 4/5/2017.
 */

public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String MOVIE_BASE_URL = "https://api.themoviedb.org/3/";

    private static final String POPULAR_MOVIE = "movie/popular";
    public static final String SORT_BY_POPULAR_MOVIE = "0";

    private static final String TOP_RATED_MOVIE = "movie/top_rated";
    public static final String SORT_BY_TOP_RATED_MOVIE = "1";

    private static final String KEY_PARAM = "api_key";
    private static final String API_KEY_VALUE = "137e317c7f6b4260303bbee302a5af98";

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

//    /**
//     * Make an HTTP request to the given URL and return a String as the response.
//     */
//    public static String makeHttpRequest(URL url) throws IOException {
//        String jsonResponse = "";
//
//        // If the URL is null, then return early.
//        if (url == null) {
//            return jsonResponse;
//        }
//
//        HttpURLConnection urlConnection = null;
//        InputStream inputStream = null;
//        try {
//            urlConnection = (HttpURLConnection) url.openConnection();
//            urlConnection.setReadTimeout(10000 /* milliseconds */);
//            urlConnection.setConnectTimeout(15000 /* milliseconds */);
//            urlConnection.setRequestMethod("GET");
//            urlConnection.connect();
//
//            // If the request was successful (response code 200),
//            // then read the input stream and parse the response.
//            if (urlConnection.getResponseCode() == 200) {
//                inputStream = urlConnection.getInputStream();
//                jsonResponse = readFromStream(inputStream);
//            } else {
//                Log.e(TAG, "Error response code: " + urlConnection.getResponseCode());
//            }
//        } catch (IOException e) {
//            Log.e(TAG, "Problem retrieving the movie JSON results.", e);
//        } finally {
//            if (urlConnection != null) {
//                urlConnection.disconnect();
//            }
//            if (inputStream != null) {
//                inputStream.close();
//            }
//        }
//        return jsonResponse;
//    }
//
//
//    /**
//     * Convert the {@link InputStream} into a String which contains the
//     * whole JSON response from the server.
//     */
//    private static String readFromStream(InputStream inputStream) throws IOException {
//        StringBuilder output = new StringBuilder();
//        if (inputStream != null) {
//            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
//            BufferedReader reader = new BufferedReader(inputStreamReader);
//            String line = reader.readLine();
//            while (line != null) {
//                output.append(line);
//                line = reader.readLine();
//            }
//        }
//        return output.toString();
//    }

}
