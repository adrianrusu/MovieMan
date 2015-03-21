package com.learning.android.movieman.model;

/**
 * Created by adrianrusu on 3/21/15.
 */
public class Backdrop {
    private String filePath;
    private int width;
    private int height;
    private double aspectRatio;
    private Double voteAverage;
    private int voteCount;

    public Backdrop() {
    }

    public Backdrop(String filePath, int width, int height, double aspectRatio, Double voteAverage, int voteCount) {
        this.filePath = filePath;
        this.width = width;
        this.height = height;
        this.aspectRatio = aspectRatio;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public double getAspectRatio() {
        return aspectRatio;
    }

    public void setAspectRatio(double aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }
}
