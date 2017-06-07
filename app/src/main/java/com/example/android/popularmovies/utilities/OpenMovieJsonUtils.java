package com.example.android.popularmovies.utilities;

import com.example.android.popularmovies.model.Film;
import com.example.android.popularmovies.model.Review;
import com.example.android.popularmovies.model.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ygarcia on 4/5/2017.
 */

public final class OpenMovieJsonUtils {

    /**
     * This method parses JSON from a web response and returns an array of Strings
     *
     * @param moviesJsonResult JSON response from server
     * @return Array of Strings describing movies data
     * @throws JSONException If JSON data cannot be properly parsed
     */
    public static ArrayList<Film> getArrayListFromJson(String moviesJsonResult)
            throws JSONException {

        final String LIST = "results";

        final String TITLE = "title";
        final String OVERVIEW = "overview";
        final String RELEASE_DATE = "release_date";
        final String POSTER_PATH = "poster_path";
        final String VOTE_AVG = "vote_average";
        final String SOURCE_ID = "id";

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
            int sourceId;

            /* Get the JSON object representing the movie */
            JSONObject movie = movieArray.getJSONObject(i);

            title = movie.getString(TITLE);
            overview = movie.getString(OVERVIEW);
            release_date = movie.getString(RELEASE_DATE);
            poster_path = movie.getString(POSTER_PATH);
            vote_average = (float) movie.getDouble(VOTE_AVG);
            sourceId = movie.getInt(SOURCE_ID);

            Film film = new Film(title, release_date, poster_path, vote_average, overview, sourceId);

            moviesArrayList.add(film);
//            parsedMoviesData[i] = title + vote_average;
        }

        return moviesArrayList;
    }

    public static Trailer getTrailer(String trailerJsonResult)
            throws JSONException {

        final String LIST = "results";

        final String ID = "id";
        final String KEY = "key";
        final String NAME = "name";
        final String SITE = "site";
        final String TYPE = "type";
        final String SIZE = "size";

        JSONObject trailerJson = new JSONObject(trailerJsonResult);

        JSONArray trailerArray = trailerJson.getJSONArray(LIST);

            /* These are the values that will be collected */
        String id,
                key,
                name,
                site,
                type;
        int size;

            /* Get the JSON object representing the trailer */
        JSONObject trailer = trailerArray.getJSONObject(0);


        id = trailer.getString(ID);
        key = trailer.getString(KEY);
        name = trailer.getString(NAME);
        site = trailer.getString(SITE);
        type = trailer.getString(TYPE);
        size = trailer.getInt(SIZE);

        Trailer trailerObj = new Trailer(id, key, name, site, type, size);

        return trailerObj;
    }

    public static ArrayList<Review> getReviewsList(String reviewsJsonResult)
            throws JSONException {

        final String LIST = "results";

        final String ID = "id";
        final String AUTHOR = "author";
        final String CONTENT = "content";
        final String URL = "url";

        /* ArrayList to hold Reviews objects */
        ArrayList<Review> reviewsArray = new ArrayList<>();

        JSONObject reviewJson = new JSONObject(reviewsJsonResult);

        JSONArray reviewArray = reviewJson.getJSONArray(LIST);

//        parsedMoviesData = new String[reviewArray.length()];

        for (int i = 0; i < reviewArray.length(); i++) {

            /* These are the values that will be collected */
            String id;
            String author;
            String content;
            String url;

            /* Get the JSON object representing the review */
            JSONObject reviewJsonObj = reviewArray.getJSONObject(i);

            id = reviewJsonObj.getString(ID);
            author = reviewJsonObj.getString(AUTHOR);
            content = reviewJsonObj.getString(CONTENT);
            url = reviewJsonObj.getString(URL);

            Review review = new Review(id, author, content, url);

            reviewsArray.add(review);
        }

        return reviewsArray;
    }
}
