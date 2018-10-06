package com.app.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EmployeeAttendance {

    @SerializedName("student_name")
    @Expose
    private String studentName;
    @SerializedName("student_attendance")
    @Expose
    private List<String> studentAttendance = null;
    @SerializedName("student_class")
    @Expose
    private List<String> studentClass = null;
    @SerializedName("attendance_date")
    @Expose
    private List<String> attendanceDate = null;

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public List<String> getStudentAttendance() {
        return studentAttendance;
    }

    public void setStudentAttendance(List<String> studentAttendance) {
        this.studentAttendance = studentAttendance;
    }

    public List<String> getStudentClass() {
        return studentClass;
    }

    public void setStudentClass(List<String> studentClass) {
        this.studentClass = studentClass;
    }

    public List<String> getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(List<String> attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

}