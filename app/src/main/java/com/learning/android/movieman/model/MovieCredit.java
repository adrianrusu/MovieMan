package com.learning.android.movieman.model;

import java.util.List;

/**
 * Created by adrianrusu on 3/21/15.
 */
public class MovieCredit {
    private Long id;
    private List<Cast> cast;

    public MovieCredit() {
    }

    public MovieCredit(Long id, List<Cast> cast) {
        this.id = id;
        this.cast = cast;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Cast> getCast() {
        return cast;
    }

    public void setCast(List<Cast> cast) {
        this.cast = cast;
    }
}
