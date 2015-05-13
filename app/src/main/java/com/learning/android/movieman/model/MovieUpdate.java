package com.learning.android.movieman.model;

public class MovieUpdate {

    private Long id;
    private Boolean adult;

    public MovieUpdate() {
    }

    public MovieUpdate(Long id, Boolean adult) {
        this.id = id;
        this.adult = adult;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getAdult() {
        return adult;
    }

    public void setAdult(Boolean adult) {
        this.adult = adult;
    }
}
