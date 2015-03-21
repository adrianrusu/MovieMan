package com.learning.android.movieman.model;

/**
 * Created by adrianrusu on 3/21/15.
 */
public class Company {
    private Long id;
    private String name;
    private String description;
    private String homepage;
    private String headquarters;
    private String logoPath;
    private Long parentCompany;

    public Company() {
    }

    public Company(Long id, String name, String description, String homepage, String headquarters, String logoPath, Long parentCompany) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.homepage = homepage;
        this.headquarters = headquarters;
        this.logoPath = logoPath;
        this.parentCompany = parentCompany;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getHeadquarters() {
        return headquarters;
    }

    public void setHeadquarters(String headquarters) {
        this.headquarters = headquarters;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public Long getParentCompany() {
        return parentCompany;
    }

    public void setParentCompany(Long parentCompany) {
        this.parentCompany = parentCompany;
    }
}
