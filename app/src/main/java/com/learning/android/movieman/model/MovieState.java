package com.learning.android.movieman.model;

public class MovieState {
    private Long movieId;
    private boolean isFavourite;
    private boolean isWatchlist;

    public MovieState(Long movieId, boolean isFavourite, boolean isWatchlist) {
        this.movieId = movieId;
        this.isFavourite = isFavourite;
        this.isWatchlist = isWatchlist;
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean isFavourite) {
        this.isFavourite = isFavourite;
    }

    public boolean isWatchlist() {
        return isWatchlist;
    }

    public void setWatchlist(boolean isWatchlist) {
        this.isWatchlist = isWatchlist;
    }
}
