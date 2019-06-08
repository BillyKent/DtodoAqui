package com.miedo.dtodoaqui.data;

public class UserTO {

    private String id;
    private String username;
    private String password;
    private String email;
    private String jwt;
    private ProfileTO profile;

    public UserTO() {
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ProfileTO getProfile() {
        return profile;
    }

    public void setProfile(ProfileTO profile) {
        this.profile = profile;
    }

    @Override
    public String toString() {
        return "UserTO{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", profile=" + profile +
                '}';
    }
}
