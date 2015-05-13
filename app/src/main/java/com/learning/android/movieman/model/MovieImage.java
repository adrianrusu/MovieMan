package com.learning.android.movieman.model;

import java.util.List;

public class MovieImage {
    private Long id;
    private List<Backdrop> backdrops;

    public MovieImage() {
    }

    public MovieImage(Long id, List<Backdrop> backdrops) {
        this.id = id;
        this.backdrops = backdrops;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Backdrop> getBackdrops() {
        return backdrops;
    }

    public void setBackdrops(List<Backdrop> backdrops) {
        this.backdrops = backdrops;
    }
}
