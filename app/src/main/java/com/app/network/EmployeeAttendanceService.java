package com.app.network;


import android.app.Application;

import com.app.activities.MyApplication;
import com.app.interfaces.APIInterface;
import com.app.models._EmployeeAttendance;

import retrofit2.Call;
import retrofit2.Response;


public class EmployeeAttendanceService {
    APIInterface apiInterface;
    public EmployeeAttendanceService(Application app)
    {
        apiInterface = ((MyApplication) app).getApiService();
    }

    public _EmployeeAttendance getEmployeeAttendance(String studentId)
    {
        Response<_EmployeeAttendance> response = null;
        try {
            Call<_EmployeeAttendance> call = apiInterface.getStudentAttendance(studentId);
            response = call.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response == null? null:response.body();
    }
}
