package com.learning.android.movieman.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by adrianrusu on 3/21/15.
 */
public class Movie {
    private Long id;
    private String imdbId;
    private String title;
    private String originalTitle;
    private String language;
    private String originalLanguage;
    private String homepage;
    private List<Genre> genres;
    private List<Company> productionCompanies;
    private String overview;
    private String posterPath;
    private Double popularity;
    private Date releaseDate;
    private BigDecimal budget;
    private BigDecimal revenue;
    private Integer runtime;
    private String status;
    private String tagLine;
    private Double voteAverage;
    private Integer voteCount;

    public Movie() {
    }

    public Movie(Long id, String imdbId, String title, String originalTitle, String language, String originalLanguage, String homepage,
                 List<Genre> genres, List<Company> productionCompanies, String overview, String posterPath, Double popularity, Date releaseDate,
                 BigDecimal budget, BigDecimal revenue, Integer runtime, String status, String tagLine, Double voteAverage, Integer voteCount) {
        this.id = id;
        this.imdbId = imdbId;
        this.title = title;
        this.originalTitle = originalTitle;
        this.language = language;
        this.originalLanguage = originalLanguage;
        this.homepage = homepage;
        this.genres = genres;
        this.productionCompanies = productionCompanies;
        this.overview = overview;
        this.posterPath = posterPath;
        this.popularity = popularity;
        this.releaseDate = releaseDate;
        this.budget = budget;
        this.revenue = revenue;
        this.runtime = runtime;
        this.status = status;
        this.tagLine = tagLine;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
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

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }
}
