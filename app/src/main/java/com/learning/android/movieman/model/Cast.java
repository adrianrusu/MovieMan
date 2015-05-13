package com.learning.android.movieman.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Cast implements Serializable {

    private Long id;
    @SerializedName("cast_id")
    private Long castId;
    @SerializedName("credit_id")
    private String creditId;
    private String name;
    private String character;
    private int order;
    @SerializedName("profile_path")
    private String profilePath;

    public Cast() {
    }

    public Cast(Long id, Long castId, String creditId, String name, String character, int order, String profilePath) {
        this.id = id;
        this.castId = castId;
        this.creditId = creditId;
        this.name = name;
        this.character = character;
        this.order = order;
        this.profilePath = profilePath;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCastId() {
        return castId;
    }

    public void setCastId(Long castId) {
        this.castId = castId;
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

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }
}
