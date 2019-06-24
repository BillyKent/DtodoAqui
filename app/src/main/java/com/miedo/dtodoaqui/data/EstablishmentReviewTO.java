package com.miedo.dtodoaqui.data;

public class EstablishmentReviewTO {
    int id;
    String username;
    String name;
    String decription;

    public EstablishmentReviewTO(int id, String username, String name, String decription) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.decription = decription;
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
}
