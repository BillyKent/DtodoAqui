package com.miedo.dtodoaqui.data;

import java.io.Serializable;

public class ProfileTO implements Serializable {
    private Integer id;
    private String firstName;
    private String lastName;
    private String description;
    private String phone;
    private String country;
    private String address;
    private String facebookUrl;

    public ProfileTO() {
    }

    public ProfileTO(Integer id, String firstName, String lastName, String phone, String country, String address, String description, String facebookUrl) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.country = country;
        this.address = address;
        this.description = description;
        this.facebookUrl = facebookUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFacebookUrl() {
        return facebookUrl;
    }

    public void setFacebookUrl(String facebookUrl) {
        this.facebookUrl = facebookUrl;
    }

    @Override
    public String toString() {
        return "ProfileTO{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", country='" + country + '\'' +
                ", address='" + address + '\'' +
                ", description='" + description + '\'' +
                ", facebookUrl='" + facebookUrl + '\'' +
                '}';
    }
}
