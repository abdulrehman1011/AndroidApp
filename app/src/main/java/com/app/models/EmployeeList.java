package com.app.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class EmployeeList implements Parcelable {
    public static final Creator<EmployeeList> CREATOR = new Creator<EmployeeList>() {
        @Override
        public EmployeeList createFromParcel(Parcel in) {
            return new EmployeeList(in);
        }

        @Override
        public EmployeeList[] newArray(int size) {
            return new EmployeeList[size];
        }
    };

    public List<Employee> getEmployees() {
        return students;
    }

    public void setEmployees(List<Employee> students) {
        this.students = students;
    }
    private List<Employee> students = new ArrayList<Employee>();



    public EmployeeList(Parcel in ) {
        readFromParcel( in );
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(students);
    }
    private void readFromParcel(Parcel in ) {
        in.readList(students,null);
    }
}
