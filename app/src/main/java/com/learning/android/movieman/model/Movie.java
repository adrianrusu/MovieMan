package com.learning.android.movieman.model;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by adrianrusu on 3/21/15.
 */
public class Movie extends MovieSmall {
    @SerializedName("imdb_id")
    private String imdbId;
    @SerializedName("original_title")
    private String originalTitle;
    private String language;
    @SerializedName("original_name")
    private String originalLanguage;
    @SerializedName("homepage")
    private String homepage;
    private List<Genre> genres;
    @SerializedName("production_companies")
    private List<ProductionCompany> productionCompanies;
    private String overview;
    private BigDecimal budget;
    private BigDecimal revenue;
    private Integer runtime;
    private String status;
    private String tagline;
    private MovieCredit credits;

    public Movie(Long id, String title, Date releaseDate, Double voteAverage, Integer voteCount, String posterPath, String backdropPath, Double popularity, String imdbId, String originalTitle, String language, String originalLanguage, String homepage, List<Genre> genres, List<ProductionCompany> productionCompanies, String overview, BigDecimal budget, BigDecimal revenue, Integer runtime, String status, String tagline, MovieCredit credits) {
        super(id, title, releaseDate, voteAverage, voteCount, posterPath, backdropPath, popularity);
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
        this.tagline = tagline;
        this.credits = credits;
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

    public List<ProductionCompany> getProductionCompanies() {
        return productionCompanies;
    }

    public void setProductionCompanies(List<ProductionCompany> productionCompanies) {
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

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public MovieCredit getCredits() {
        return credits;
    }

    public void setCredits(MovieCredit credits) {
        this.credits = credits;
    }
}
