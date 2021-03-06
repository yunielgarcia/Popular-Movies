package com.example.android.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovies.model.Film;
import com.example.android.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ygarcia on 4/5/2017.
 */

/**
 * {@link MovieAdapter} exposes a list of movies to a
 * {@link android.support.v7.widget.RecyclerView}
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private ArrayList<Film> mMovieData;

    /*
     * An on-click handler that we've defined to make it easy for an Activity to interface with
     * our RecyclerView
     */
    final private ListItemClickListener mOnClickListener;

    public MovieAdapter(ListItemClickListener listener) {
        this.mOnClickListener = listener;
    }

    /**
     * The interface that receives onClick messages.
     */
    public interface ListItemClickListener {
        void onListItemClick(Film filmSelected);
    }

    /**
     * This method is used to set the weather forecast on a ForecastAdapter if we've already
     * created one. This is handy when we get new data from the web but don't want to create a
     * new ForecastAdapter to display it.
     *
     * @param movieData The new weather data to be displayed.
     */
    public void setMovieData(ArrayList<Film> movieData) {
        mMovieData = movieData;
        notifyDataSetChanged();
    }


    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param parent The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item (which ours doesn't) you
     *                  can use this viewType integer to provide a different layout. See
     *                  {@link android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}
     *                  for more details.
     * @return A new MovieAdapterViewHolder that holds the View for each list item
     */
    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new MovieAdapterViewHolder(view);
    }


    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the weather
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param holder The ViewHolder which should be updated to represent the
     *                                  contents of the item at the given position in the data set.
     * @param position                  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
        Film singleMovieObj = mMovieData.get(position);
        Context context = holder.mImageView.getContext();
        String movie_poster_path = singleMovieObj.getmPosterPath();
        String full_img_url = NetworkUtils.BASE_POST_URL + NetworkUtils.POST_FILE_SIZE_URL + movie_poster_path;

        Picasso.with(context).load(full_img_url).into(holder.mImageView);
    }


    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our forecast
     */
    @Override
    public int getItemCount() {
        if (null == mMovieData) return 0;
        return mMovieData.size();
    }


    /**
     * Cache of the children views for a forecast list item.
     */
    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final ImageView mImageView;

        public MovieAdapterViewHolder(View view) {
            super(view);
            mImageView = (ImageView)view.findViewById(R.id.iv_poster);
            view.setOnClickListener(this);
        }


        /**
         * Called whenever a user clicks on an item in the list.
         * @param view The View that was clicked
         */
        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            Film filmSelected = mMovieData.get(clickedPosition);
            mOnClickListener.onListItemClick(filmSelected);
        }


        /**
         * This gets called by the child views during a click.
         *
         * @param v The View that was clicked
         */
//        @Override
//        public void onClick(View v) {
//            int adapterPosition = getAdapterPosition();
//            String weatherForDay = mWeatherData[adapterPosition];
//            mClickHandler.onClick(weatherForDay);
//        }
    }
}
