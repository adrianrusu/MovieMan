package com.learning.android.movieman.model;

import java.util.List;

public class MovieCredit {
    private List<Cast> cast;
    private List<Crew> crew;

    public MovieCredit() {
    }

    public MovieCredit(List<Cast> cast, List<Crew> crew) {
        this.cast = cast;
        this.crew = crew;
    }

    public List<Cast> getCast() {
        return cast;
    }

    public void setCast(List<Cast> cast) {
        this.cast = cast;
    }

    public List<Crew> getCrew() {
        return crew;
    }

    public void setCrew(List<Crew> crew) {
        this.crew = crew;
    }
}
