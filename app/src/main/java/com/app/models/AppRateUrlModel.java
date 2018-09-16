package com.app.models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class AppRateUrlModel {
    @SerializedName("rate_url")
    @Expose
    private String rateUrl;

    public String getRateUrl() {
        return rateUrl;
    }

    public void setRateUrl(String rateUrl) {
        this.rateUrl = rateUrl;
    }
}
