package com.example.android.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

public class MovieDetail extends AppCompatActivity {

    TextView title_tv;
    ImageView poster_iv;
    TextView date_tv;
    TextView vote_tv;
    TextView plot_tv;

    Film movieSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        //retrieving data passed to the activity
        movieSelected = getIntent().getParcelableExtra("movieSelected");

        String full_img_url = NetworkUtils.BASE_POST_URL + NetworkUtils.POST_FILE_SIZE_URL + movieSelected.getmPosterPath();

        //finding views
        title_tv = (TextView) findViewById(R.id.tv_detail_movie_title);
        poster_iv = (ImageView) findViewById(R.id.iv_detail_movie_poster);
        date_tv = (TextView) findViewById(R.id.tv_detail_date);
        vote_tv = (TextView)findViewById(R.id.tv_detail_votes);
        plot_tv = (TextView)findViewById(R.id.tv_plot);

        //initializing views
        title_tv.setText(movieSelected.getmTitle());
        date_tv.setText(movieSelected.getmReleaseDate());
        plot_tv.setText(movieSelected.getmOverview());
        vote_tv.setText( String.valueOf(movieSelected.getmVoteAverage()));
        Picasso.with(this).load(full_img_url).into(poster_iv);
    }
}
