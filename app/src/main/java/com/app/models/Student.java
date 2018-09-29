package com.app.models;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Student implements Serializable
{
    @SerializedName("student_id")
    @Expose
    private String studentId;
    @SerializedName("student_name")
    @Expose
    private String studentName;
    @SerializedName("LogoFolder")
    @Expose
    private String logoFolder;
    @SerializedName("Headercolor")
    @Expose
    private String headercolor;
    @SerializedName("Homebuttoncolor")
    @Expose
    private String homebuttoncolor;
    @SerializedName("menuItemBackgroundcolor")
    @Expose
    private String menuItemBackgroundcolor;
    @SerializedName("menuTextColor")
    @Expose
    private String menuTextColor;
    @SerializedName("MainBackgroundColor")
    @Expose
    private String mainBackgroundColor;
    @SerializedName("statusbarcolor")
    @Expose
    private String statusbarcolor;
    @SerializedName("headerTextColor")
    @Expose
    private String headerTextColor;
    @SerializedName("logobackgroundcolor")
    @Expose
    private String logobackgroundcolor;
    @SerializedName("School")
    @Expose
    private String school;

    public String getHeaderTextColor() {
        return headerTextColor;
    }

    public void setHeaderTextColor(String headerTextColor) {
        this.headerTextColor = headerTextColor;
    }
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

    public String getLogoFolder() {
        return logoFolder;
    }

    public void setLogoFolder(String logoFolder) {
        this.logoFolder = logoFolder;
    }

    public String getHeadercolor() {
        return headercolor;
    }

    public void setHeadercolor(String headercolor) {
        this.headercolor = headercolor;
    }

    public String getHomebuttoncolor() {
        return homebuttoncolor;
    }

    public void setHomebuttoncolor(String homebuttoncolor) {
        this.homebuttoncolor = homebuttoncolor;
    }
    public String getMenuItemBackgroundcolor() {
        return menuItemBackgroundcolor;
    }

    public void setMenuItemBackgroundcolor(String menuItemBackgroundcolor) {
        this.menuItemBackgroundcolor = menuItemBackgroundcolor;
    }

    public String getMenuTextColor() {
        return menuTextColor;
    }

    public void setMenuTextColor(String menuTextColor) {
        this.menuTextColor = menuTextColor;
    }

    public String getMainBackgroundColor() {
        return mainBackgroundColor;
    }

    public void setMainBackgroundColor(String mainBackgroundColor) {
        this.mainBackgroundColor = mainBackgroundColor;
    }

    public String getStatusbarcolor() {
        return statusbarcolor;
    }

    public void setStatusbarcolor(String statusbarcolor) {
        this.statusbarcolor = statusbarcolor;
    }

    public String getLogobackgroundcolor() {
        return logobackgroundcolor;
    }

    public void setLogobackgroundcolor(String logobackgroundcolor) {
        this.logobackgroundcolor = logobackgroundcolor;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }
}
