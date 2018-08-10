package com.app.network;


import android.app.Application;

import com.app.activities.MyApplication;
import com.app.interfaces.APIInterface;
import com.app.models.StudentDetail;

import retrofit2.Call;
import retrofit2.Response;


public class StudentDataService {
    APIInterface apiInterface;
    public StudentDataService(Application app)
    {
        apiInterface = ((MyApplication) app).getApiService();
    }

    public StudentDetail getStudentDetail(String studentId)
    {
        Response<StudentDetail> response = null;
        try {
            Call<StudentDetail> call = apiInterface.getStudentDetail(studentId);
            response = call.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response == null? null:response.body();
    }
}
