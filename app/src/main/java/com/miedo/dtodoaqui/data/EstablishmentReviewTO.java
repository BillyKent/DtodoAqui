package com.miedo.dtodoaqui.data;

import java.util.List;

public class EstablishmentReviewTO {
    int id;
    String username;
    String name;
    String decription;
    List<String> images;
    float rating;

    public EstablishmentReviewTO(int id, String username, String name, String decription, float rating, List<String> images) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.decription = decription;
        this.images = images;
        this.rating = rating;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDecription() {
        return decription;
    }

    public void setDecription(String decription) {
        this.decription = decription;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
