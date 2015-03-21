package com.learning.android.movieman.model;

import java.util.Date;

/**
 * Created by adrianrusu on 3/21/15.
 */
public class Person {
    private Long id;
    private String name;
    private String homepage;
    private String placeOfBirth;
    private Date birthday;
    private Date deathday;
    private String biography;

    public Person() {
    }

    public Person(Long id, String name, String homepage, String placeOfBirth, Date birthday, Date deathday, String biography) {
        this.id = id;
        this.name = name;
        this.homepage = homepage;
        this.placeOfBirth = placeOfBirth;
        this.birthday = birthday;
        this.deathday = deathday;
        this.biography = biography;
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

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Date getDeathday() {
        return deathday;
    }

    public void setDeathday(Date deathday) {
        this.deathday = deathday;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }
}
