package com.learning.android.movieman.model;

import java.io.Serializable;

/**
 * Created by adrianrusu on 3/21/15.
 */
public class Genre implements Serializable {
    private Long id;
    private String name;

    public Genre() {
    }

    public Genre(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
