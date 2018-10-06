package com.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EmployeeDetail {

    @SerializedName("Student_Name")
    @Expose
    private String studentName;
    @SerializedName("Father_Name")
    @Expose
    private String fatherName;
    @SerializedName("Mother_Name")
    @Expose
    private String motherName;
    @SerializedName("Address")
    @Expose
    private String address;
    @SerializedName("Emergency_Contact")
    @Expose
    private String emergencyContact;
    @SerializedName("Class")
    @Expose
    private String _class;

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public String getClass_() {
        return _class;
    }

    public void setClass_(String _class) {
        this._class = _class;
    }


}