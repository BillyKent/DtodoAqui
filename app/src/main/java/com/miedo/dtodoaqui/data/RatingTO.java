package com.miedo.dtodoaqui.data;

public class RatingTO {
    int id;
    int value;
    int userId;

    public RatingTO(int id, int value, int userId) {
        this.id = id;
        this.value = value;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
