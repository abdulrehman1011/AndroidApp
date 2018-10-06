package com.app.network;


import android.app.Application;

import com.app.activities.MyApplication;
import com.app.interfaces.APIInterface;
import com.app.models.EmployeeDetail;

import retrofit2.Call;
import retrofit2.Response;


public class EmployeeDataService {
    APIInterface apiInterface;
    public EmployeeDataService(Application app)
    {
        apiInterface = ((MyApplication) app).getApiService();
    }

    public EmployeeDetail getEmployeeDetail(String studentId)
    {
        Response<EmployeeDetail> response = null;
        try {
            Call<EmployeeDetail> call = apiInterface.getStudentDetail(studentId);
            response = call.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response == null? null:response.body();
    }
}
