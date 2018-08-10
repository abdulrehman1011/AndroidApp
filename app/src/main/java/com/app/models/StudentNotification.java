package com.app.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StudentNotification {

    @SerializedName("Notify")
    @Expose
    private List<Notify> notify = null;

    public List<Notify> getNotify() {
        return notify;
    }

    public void setNotify(List<Notify> notify) {
        this.notify = notify;
    }

}