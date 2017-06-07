package com.example.android.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.popularmovies.model.Review;

import java.util.ArrayList;

/**
 * Created by ygarcia on 6/7/2017.
 */

public class ReviewAdapter extends ArrayAdapter<Review>{


    public ReviewAdapter(@NonNull Context context, @NonNull ArrayList<Review> reviews) {
        super(context, 0, reviews);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.review_list_item, parent, false);
        }

        // Get the review object located at this position in the list
        Review currentReview = getItem(position);

        // Find the TextView in the list_item.xml layout with the ID version_name
        TextView authorTextView = (TextView) listItemView.findViewById(R.id.author_review_list_item);
        // set this text on the name TextView
        authorTextView.setText(currentReview.getAuthor());

        // Find the TextView in the list_item.xml layout with the ID version_number
        TextView contentTextView = (TextView) listItemView.findViewById(R.id.content_review_list_item);
        // Get the miwok word from the current Word object and
        // set this text on the miwok TextView
        contentTextView.setText(currentReview.getContent());

        return listItemView;
    }
}
