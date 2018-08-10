package com.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FeeByMonth {

    @SerializedName("fee_id")
    @Expose
    private Integer feeId;
    @SerializedName("MonthlyFee")
    @Expose
    private Integer monthlyFee;
    @SerializedName("TrasportFee")
    @Expose
    private Integer trasportFee;
    @SerializedName("Outstanding")
    @Expose
    private Integer outstanding;
    @SerializedName("Total")
    @Expose
    private String total;
    @SerializedName("amount_received")
    @Expose
    private Integer amountReceived;
    @SerializedName("new_balance")
    @Expose
    private Integer newBalance;
    @SerializedName("submission_date")
    @Expose
    private String submissionDate;

    public Integer getFeeId() {
        return feeId;
    }

    public void setFeeId(Integer feeId) {
        this.feeId = feeId;
    }

    public Integer getMonthlyFee() {
        return monthlyFee;
    }

    public void setMonthlyFee(Integer monthlyFee) {
        this.monthlyFee = monthlyFee;
    }

    public Integer getTrasportFee() {
        return trasportFee;
    }

    public void setTrasportFee(Integer trasportFee) {
        this.trasportFee = trasportFee;
    }

    public Integer getOutstanding() {
        return outstanding;
    }

    public void setOutstanding(Integer outstanding) {
        this.outstanding = outstanding;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public Integer getAmountReceived() {
        return amountReceived;
    }

    public void setAmountReceived(Integer amountReceived) {
        this.amountReceived = amountReceived;
    }

    public Integer getNewBalance() {
        return newBalance;
    }

    public void setNewBalance(Integer newBalance) {
        this.newBalance = newBalance;
    }

    public String getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(String submissionDate) {
        this.submissionDate = submissionDate;
    }

}