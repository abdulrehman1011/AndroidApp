package com.app.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StudentFee {

    @SerializedName("feeMonth")
    @Expose
    private List<FeeMonth> feeMonth = null;

    public List<FeeMonth> getFeeMonth() {
        return feeMonth;
    }

    public void setFeeMonth(List<FeeMonth> feeMonth) {
        this.feeMonth = feeMonth;
    }

}