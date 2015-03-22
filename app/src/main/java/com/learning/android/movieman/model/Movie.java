package com.learning.android.movieman.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by adrianrusu on 3/21/15.
 */
public class Movie extends MovieSmall {
    private String imdbId;
    private String originalTitle;
    private String language;
    private String originalLanguage;
    private String homepage;
    private List<Genre> genres;
    private List<Company> productionCompanies;
    private String overview;
    private BigDecimal budget;
    private BigDecimal revenue;
    private Integer runtime;
    private String status;
    private String tagLine;

    public Movie() {
    }

    public Movie(Long id, String title, Date releaseDate, Double voteAverage, Integer voteCount, String posterPath,
                 Double popularity, String imdbId, String originalTitle, String language, String originalLanguage,
                 String homepage, List<Genre> genres, List<Company> productionCompanies, String overview, BigDecimal budget,
                 BigDecimal revenue, Integer runtime, String status, String tagLine) {
        super(id, title, releaseDate, voteAverage, voteCount, posterPath, popularity);
        this.imdbId = imdbId;
        this.originalTitle = originalTitle;
        this.language = language;
        this.originalLanguage = originalLanguage;
        this.homepage = homepage;
        this.genres = genres;
        this.productionCompanies = productionCompanies;
        this.overview = overview;
        this.budget = budget;
        this.revenue = revenue;
        this.runtime = runtime;
        this.status = status;
        this.tagLine = tagLine;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public List<Company> getProductionCompanies() {
        return productionCompanies;
    }

    public void setProductionCompanies(List<Company> productionCompanies) {
        this.productionCompanies = productionCompanies;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    public BigDecimal getRevenue() {
        return revenue;
    }

    public void setRevenue(BigDecimal revenue) {
        this.revenue = revenue;
    }

    public Integer getRuntime() {
        return runtime;
    }

    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTagLine() {
        return tagLine;
    }

    public void setTagLine(String tagLine) {
        this.tagLine = tagLine;
    }
}
