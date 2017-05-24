package com.example.android.popularmovies.model;

/**
 * Created by ygarcia on 5/24/2017.
 */

public class Review {
    private String id,
            author,
            content,
            url;

    public Review(String id, String author, String content, String url) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }
}
