package com.miedo.dtodoaqui.data;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class EstablishmentTO implements Serializable {

    private int id;
    private String name;
    private String address;
    private String category;
    private String description;
    private LatLng latLng;
    private boolean isVerified;
    private String openingHours;
    private String price;
    private String slug;
    private float rating;

    public EstablishmentTO(int id, String name, String address, String category, String description, LatLng latLng, boolean isVerified, String openingHours, String price, String slug, float rating) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.category = category;
        this.description = description;
        this.latLng = latLng;
        this.isVerified = isVerified;
        this.openingHours = openingHours;
        this.price = price;
        this.slug = slug;
        this.rating = rating;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }
}
