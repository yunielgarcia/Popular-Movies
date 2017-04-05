package com.example.android.popularmovies;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovies.utilities.NetworkUtils;
import com.example.android.popularmovies.utilities.OpenMovieJsonUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.sql.Array;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    TextView mSearchResultsTextView;

    String selectedOption = NetworkUtils.SORT_BY_POPULAR_MOVIE;

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;

    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
         * The ProgressBar that will indicate to the user that we are loading data. It will be
         * hidden when no data is loading.
         *
         * Please note: This so called "ProgressBar" isn't a bar by default. It is more of a
         * circle. We didn't make the rules (or the names of Views), we just follow them.
         */
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

//        mSearchResultsTextView = (TextView) findViewById(R.id.tv_search_results_json);
        /*
         * Using findViewById, we get a reference to our RecyclerView from xml. This allows us to
         * do things like set the adapter of the RecyclerView and toggle the visibility.
         */
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movie);

        /* This TextView is used to display errors and will be hidden if there are no errors */
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        /*
         * LinearLayoutManager can support HORIZONTAL or VERTICAL orientations. The reverse layout
         * parameter is useful mostly for HORIZONTAL layouts that should reverse for right to left
         * languages.
         */
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);

        /*
         * Use this setting to improve performance if you know that changes in content do not
         * change the child layout size in the RecyclerView
         */
        mRecyclerView.setHasFixedSize(true);

        /*
         * The ForecastAdapter is responsible for linking our weather data with the Views that
         * will end up displaying our weather data.
         */
        mMovieAdapter = new MovieAdapter();


        /* Setting the adapter attaches it to the RecyclerView in our layout. */
        mRecyclerView.setAdapter(mMovieAdapter);


        loadMovieData();

    }

    private void loadMovieData(){
        new MovieAsyncTask().execute(selectedOption);
    }

    public class MovieAsyncTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... params) {

            String sortOption = params[0];
            String movieResults = null;
            String[] jsonMovieResults;
            URL movieRequestUrl = NetworkUtils.buildUrl(sortOption);
            try {
                movieResults = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);
                jsonMovieResults = OpenMovieJsonUtils.getSimpleMovieStringsFromJson(movieResults);
                return jsonMovieResults;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(String[] jsonMovieResults) {
//            if (jsonMovieResults != null && !jsonMovieResults.equals("")){
//                mSearchResultsTextView.setText(jsonMovieResults);
//            }

//            mSearchResultsTextView.setText(Arrays.toString(jsonMovieResults).replaceAll("\\[|\\]", ""));


            if (jsonMovieResults != null) {
//                showWeatherDataView();
                mMovieAdapter.setWeatherData(jsonMovieResults);
            } else {
//                showErrorMessage();
            }
        }
    }
}
