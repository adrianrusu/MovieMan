package com.learning.android.movieman.model;

import java.io.Serializable;

public class ProductionCompany implements Serializable {
    private Long id;
    private String name;

    public ProductionCompany() {
    }

    public ProductionCompany(Long id, String name) {
        this.id = id;
        this.name = name;
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

}
