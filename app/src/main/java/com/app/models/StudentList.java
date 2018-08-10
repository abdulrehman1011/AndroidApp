package com.app.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class StudentList implements Parcelable {
    public static final Creator<StudentList> CREATOR = new Creator<StudentList>() {
        @Override
        public StudentList createFromParcel(Parcel in) {
            return new StudentList(in);
        }

        @Override
        public StudentList[] newArray(int size) {
            return new StudentList[size];
        }
    };

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
    private List<Student> students = new ArrayList<Student>();

    public StudentList(Parcel in ) {
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
