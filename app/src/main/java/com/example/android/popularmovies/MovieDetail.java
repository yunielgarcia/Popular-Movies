package com.example.android.popularmovies;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.data.MovieDbHelper;
import com.example.android.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

public class MovieDetail extends AppCompatActivity {

    TextView title_tv;
    ImageView poster_iv;
    TextView date_tv;
    TextView vote_tv;
    TextView plot_tv;

    Film movieSelected;

    // To access our database
    private MovieDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        //retrieving data passed to the activity
        movieSelected = getIntent().getParcelableExtra("movieSelected");

        //we instantiate our subclass of SQLiteOpenHelper and pass the context, which is the current activity.
        mDbHelper = new MovieDbHelper(this);

        String full_img_url = NetworkUtils.BASE_POST_URL + NetworkUtils.POST_FILE_SIZE_URL + movieSelected.getmPosterPath();

        //finding views
        title_tv = (TextView) findViewById(R.id.tv_detail_movie_title);
        poster_iv = (ImageView) findViewById(R.id.iv_detail_movie_poster);
        date_tv = (TextView) findViewById(R.id.tv_detail_date);
        vote_tv = (TextView) findViewById(R.id.tv_detail_votes);
        plot_tv = (TextView) findViewById(R.id.tv_plot);

        //initializing views
        title_tv.setText(movieSelected.getmTitle());
        date_tv.setText(movieSelected.getmReleaseDate());
        plot_tv.setText(movieSelected.getmOverview());
        vote_tv.setText(String.valueOf(movieSelected.getmVoteAverage()));
        Picasso.with(this).load(full_img_url).into(poster_iv);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/detail.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_share:
                //validar if is currentrly favorite or not then insert or delect upon db
                insertToFavorite();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void insertToFavorite() {
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();

        if (TextUtils.isEmpty(movieSelected.getmTitle()) || movieSelected.getmSourceId() == 0 || TextUtils.isEmpty(movieSelected.getmPosterPath())) {
            Toast.makeText(this, getString(R.string.error_add_fav),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, movieSelected.getmTitle());
        values.put(MovieContract.MovieEntry.COLUMN_SOURCE_ID, movieSelected.getmSourceId());
        values.put(MovieContract.MovieEntry.COLUMN_IMG_PATH, movieSelected.getmPosterPath());
        values.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movieSelected.getmReleaseDate());
        values.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movieSelected.getmOverview());
        values.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, movieSelected.getmVoteAverage());


        Uri newUri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, values);

        // Show a toast message depending on whether or not the insertion was successful
        if (newUri == null) {
            // If the new content URI is null, then there was an error with insertion.
            Toast.makeText(this, getString(R.string.insert_movie_failed),
                    Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast.
            Toast.makeText(this, getString(R.string.insert_movie_success),
                    Toast.LENGTH_SHORT).show();
        }

    }
}
