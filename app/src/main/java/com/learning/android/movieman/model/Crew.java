package com.learning.android.movieman.model;

import com.google.gson.annotations.SerializedName;

public class Crew {

    private Long id;
    @SerializedName("credit_id")
    private String creditId;
    private String name;
    private String job;
    private String department;
    @SerializedName("profile_path")
    private String profilePath;

    public Crew(Long id, String creditId, String name, String job, String department, String profilePath) {
        this.id = id;
        this.creditId = creditId;
        this.name = name;
        this.job = job;
        this.department = department;
        this.profilePath = profilePath;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreditId() {
        return creditId;
    }

    public void setCreditId(String creditId) {
        this.creditId = creditId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }
}
