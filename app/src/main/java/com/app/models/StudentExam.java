package com.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StudentExam {

    @SerializedName("examType")
    @Expose
    private List<ExamType> examType = null;

    public List<ExamType> getExamType() {
        return examType;
    }

    public void setExamType(List<ExamType> examType) {
        this.examType = examType;
    }

}