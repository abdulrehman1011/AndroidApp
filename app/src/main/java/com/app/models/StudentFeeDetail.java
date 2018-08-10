package com.app.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StudentFeeDetail {

    @SerializedName("feeByMonth")
    @Expose
    private List<FeeByMonth> feeByMonth = null;

    public List<FeeByMonth> getFeeByMonth() {
        return feeByMonth;
    }

    public void setFeeByMonth(List<FeeByMonth> feeByMonth) {
        this.feeByMonth = feeByMonth;
    }

}