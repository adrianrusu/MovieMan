package com.learning.android.movieman.model;

import java.util.List;

/**
 * Created by adrianrusu on 4/14/15.
 */
public class Trailer {
    private List<YoutubeTrailer> youtube;

    public Trailer() {
    }

    public Trailer(List<YoutubeTrailer> youtube) {
        this.youtube = youtube;
    }

    public List<YoutubeTrailer> getYoutube() {
        return youtube;
    }

    public void setYoutube(List<YoutubeTrailer> youtube) {
        this.youtube = youtube;
    }
}
