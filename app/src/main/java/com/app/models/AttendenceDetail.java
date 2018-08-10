package com.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AttendenceDetail {

    @SerializedName("attandence")
    @Expose
    private String attandence;
    @SerializedName("date")
    @Expose
    private String date;

    public String getAttandence() {
        return attandence;
    }

    public void setAttandence(String attandence) {
        this.attandence = attandence;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}