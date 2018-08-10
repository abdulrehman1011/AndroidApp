package com.app.network;


import android.app.Application;

import com.app.activities.MyApplication;
import com.app.interfaces.APIInterface;
import com.app.models.StudentAttendance;
import com.app.models._StudentAttendance;

import retrofit2.Call;
import retrofit2.Response;


public class StudentAttendanceService  {
    APIInterface apiInterface;
    public StudentAttendanceService(Application app)
    {
        apiInterface = ((MyApplication) app).getApiService();
    }

    public _StudentAttendance getStudentAttendance(String studentId)
    {
        Response<_StudentAttendance> response = null;
        try {
            Call<_StudentAttendance> call = apiInterface.getStudentAttendance(studentId);
            response = call.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response == null? null:response.body();
    }
}
