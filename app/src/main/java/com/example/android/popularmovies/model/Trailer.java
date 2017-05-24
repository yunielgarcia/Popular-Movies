package com.example.android.popularmovies.model;

/**
 * Created by ygarcia on 5/24/2017.
 */

public class Trailer {
    private String id,
            key,
            name,
            site,
            type;
    private int size;

    public Trailer(String id, String key, String name, String site, String type, int size) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.site = site;
        this.type = type;
        this.size = size;
    }

    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getSite() {
        return site;
    }

    public String getType() {
        return type;
    }

    public int getSize() {
        return size;
    }
}
