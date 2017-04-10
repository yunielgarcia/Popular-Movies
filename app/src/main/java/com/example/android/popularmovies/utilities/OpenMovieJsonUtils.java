package com.example.android.popularmovies.utilities;

import android.content.Context;
import android.graphics.Movie;

import com.example.android.popularmovies.Film;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import static android.R.attr.description;
import static android.R.attr.fingerprintAuthDrawable;

/**
 * Created by ygarcia on 4/5/2017.
 */

public final class OpenMovieJsonUtils {

    /**
     * This method parses JSON from a web response and returns an array of Strings
     *
     * @param moviesJsonResult JSON response from server
     *
     * @return Array of Strings describing movies data
     *
     * @throws JSONException If JSON data cannot be properly parsed
     */
    public static ArrayList<Film> getArrayListFromJson(String moviesJsonResult)
            throws JSONException {

        /* Weather information. Each day's forecast info is an element of the "list" array */
        final String LIST = "results";


        final String TITLE = "title";
        final String OVERVIEW = "overview";
        final String RELEASE_DATE = "release_date";
        final String POSTER_PATH = "poster_path";
        final String VOTE_AVG = "vote_average";

        /* ArrayList to hold movie objects */
        ArrayList<Film> moviesArrayList = new ArrayList<>();

        JSONObject movieJson = new JSONObject(moviesJsonResult);

        JSONArray movieArray = movieJson.getJSONArray(LIST);

//        parsedMoviesData = new String[movieArray.length()];

        for (int i = 0; i < movieArray.length(); i++) {

            /* These are the values that will be collected */
            String title;
            String release_date;
            String poster_path;
            float vote_average;
            String overview;

            /* Get the JSON object representing the movie */
            JSONObject movie = movieArray.getJSONObject(i);

            title = movie.getString(TITLE);
            overview = movie.getString(OVERVIEW);
            release_date = movie.getString(RELEASE_DATE);
            poster_path = movie.getString(POSTER_PATH);
            vote_average = (float) movie.getDouble(VOTE_AVG);

            Film film = new Film(title, release_date, poster_path, vote_average, overview);

            moviesArrayList.add(film);
//            parsedMoviesData[i] = title + vote_average;
        }

        return moviesArrayList;
    }
}
