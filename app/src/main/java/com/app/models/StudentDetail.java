package com.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StudentDetail {

    @SerializedName("Student_Id")
    @Expose
    private String studentId;
    @SerializedName("Student_Name")
    @Expose
    private String studentName;
    @SerializedName("Father_Name")
    @Expose
    private String fatherName;
    @SerializedName("Mother_Name")
    @Expose
    private String motherName;
    @SerializedName("B_Form")
    @Expose
    private String bForm;
    @SerializedName("DOB")
    @Expose
    private String dOB;
    @SerializedName("Gender")
    @Expose
    private String gender;
    @SerializedName("Address")
    @Expose
    private String address;
    @SerializedName("Father_Mobile")
    @Expose
    private String fatherMobile;
    @SerializedName("Mother_Mobile")
    @Expose
    private String motherMobile;
    @SerializedName("Emergency_Contact")
    @Expose
    private String emergencyContact;
    @SerializedName("Telephone")
    @Expose
    private String telephone;
    @SerializedName("Class")
    @Expose
    private String _class;
    @SerializedName("Class_Fees")
    @Expose
    private Integer classFees;
    @SerializedName("Monthly_Fees")
    @Expose
    private Integer monthlyFees;
    @SerializedName("Transport")
    @Expose
    private Integer transport;
    @SerializedName("Security")
    @Expose
    private Integer security;
    @SerializedName("Admission_amount")
    @Expose
    private Integer admissionAmount;
    @SerializedName("Admission_date")
    @Expose
    private String admissionDate;
    @SerializedName("Discount_amount")
    @Expose
    private Integer discountAmount;
    @SerializedName("Discount_comment")
    @Expose
    private String discountComment;
    @SerializedName("Blood_Group")
    @Expose
    private String bloodGroup;
    @SerializedName("Allergy")
    @Expose
    private String allergy;
    @SerializedName("student_id_pk")
    @Expose
    private Integer studentIdPk;
    @SerializedName("Remarks")
    @Expose
    private String remarks;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

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

    public String getBForm() {
        return bForm;
    }

    public void setBForm(String bForm) {
        this.bForm = bForm;
    }

    public String getDOB() {
        return dOB;
    }

    public void setDOB(String dOB) {
        this.dOB = dOB;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFatherMobile() {
        return fatherMobile;
    }

    public void setFatherMobile(String fatherMobile) {
        this.fatherMobile = fatherMobile;
    }

    public String getMotherMobile() {
        return motherMobile;
    }

    public void setMotherMobile(String motherMobile) {
        this.motherMobile = motherMobile;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getClass_() {
        return _class;
    }

    public void setClass_(String _class) {
        this._class = _class;
    }

    public Integer getClassFees() {
        return classFees;
    }

    public void setClassFees(Integer classFees) {
        this.classFees = classFees;
    }

    public Integer getMonthlyFees() {
        return monthlyFees;
    }

    public void setMonthlyFees(Integer monthlyFees) {
        this.monthlyFees = monthlyFees;
    }

    public Integer getTransport() {
        return transport;
    }

    public void setTransport(Integer transport) {
        this.transport = transport;
    }

    public Integer getSecurity() {
        return security;
    }

    public void setSecurity(Integer security) {
        this.security = security;
    }

    public Integer getAdmissionAmount() {
        return admissionAmount;
    }

    public void setAdmissionAmount(Integer admissionAmount) {
        this.admissionAmount = admissionAmount;
    }

    public String getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(String admissionDate) {
        this.admissionDate = admissionDate;
    }

    public Integer getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Integer discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getDiscountComment() {
        return discountComment;
    }

    public void setDiscountComment(String discountComment) {
        this.discountComment = discountComment;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getAllergy() {
        return allergy;
    }

    public void setAllergy(String allergy) {
        this.allergy = allergy;
    }

    public Integer getStudentIdPk() {
        return studentIdPk;
    }

    public void setStudentIdPk(Integer studentIdPk) {
        this.studentIdPk = studentIdPk;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}