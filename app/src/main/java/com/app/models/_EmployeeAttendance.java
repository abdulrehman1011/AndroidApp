package com.app.models;

import java.util.List;
        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class _EmployeeAttendance {

    @SerializedName("AttandenceDetails")
    @Expose
    private List<AttendenceDetail> attandenceDetails = null;

    public List<AttendenceDetail> getAttandenceDetails() {
        return attandenceDetails;
    }

    public void setAttandenceDetails(List<AttendenceDetail> attandenceDetails) {
        this.attandenceDetails = attandenceDetails;
    }

}