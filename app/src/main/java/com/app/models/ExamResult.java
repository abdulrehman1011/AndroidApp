package com.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExamResult {

    @SerializedName("subject_name")
    @Expose
    private String subjectName;
    @SerializedName("std_obtain_mark")
    @Expose
    private String stdObtainMark;
    @SerializedName("std_total_mark")
    @Expose
    private String stdTotalMark;
    @SerializedName("remarks")
    @Expose
    private String remarks;

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getStdObtainMark() {
        return stdObtainMark;
    }

    public void setStdObtainMark(String stdObtainMark) {
        this.stdObtainMark = stdObtainMark;
    }

    public String getStdTotalMark() {
        return stdTotalMark;
    }

    public void setStdTotalMark(String stdTotalMark) {
        this.stdTotalMark = stdTotalMark;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}