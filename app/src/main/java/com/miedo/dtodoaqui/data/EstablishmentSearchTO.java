package com.miedo.dtodoaqui.data;

public class EstablishmentSearchTO {

    private Integer id;
    private String name;
    private String address;
    private String urlImage;
    private float rating;

    public EstablishmentSearchTO(Integer id, String name, String address, String urlImage, float rating) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.urlImage = urlImage;
        this.rating = rating;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "EstablishmentSearchTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", urlImage='" + urlImage + '\'' +
                ", rating=" + rating +
                '}';
    }
}
