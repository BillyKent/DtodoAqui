package com.miedo.dtodoaqui.data;

import java.io.Serializable;

public class ProfileTO implements Serializable {
    private Integer id;
    private String firstName;
    private String avatarName;
    private String lastName;
    private String description;
    private String userId;
    private String phone;
    private String country;
    private String address;

    private String facebookUrl;

    public ProfileTO() {
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

    public String getAvatarName() {
        return avatarName;
    }

    public void setAvatarName(String avatarName) {
        this.avatarName = avatarName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "ProfileTO{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", avatarName='" + avatarName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", description='" + description + '\'' +
                ", userId='" + userId + '\'' +
                ", phone='" + phone + '\'' +
                ", country='" + country + '\'' +
                ", address='" + address + '\'' +
                ", facebookUrl='" + facebookUrl + '\'' +
                '}';
    }
}
