package com.miedo.dtodoaqui.data;

public class ReportTO {
    private int id;
    private boolean isApproved;
    private int establishmentId;
    private String message;
    private int userId;

    public ReportTO(int id, boolean isApproved, int establishmentId, String message, int userId) {
        this.id = id;
        this.isApproved = isApproved;
        this.establishmentId = establishmentId;
        this.message = message;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    public int getEstablishmentId() {
        return establishmentId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getUserId() {
        return userId;
    }

}
