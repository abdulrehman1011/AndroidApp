package com.app.network;


import android.app.Application;

import com.app.activities.MyApplication;
import com.app.interfaces.APIInterface;
import com.app.models.StudentFee;

import retrofit2.Call;
import retrofit2.Response;

public class StudentFeeService {

    APIInterface apiInterface;
    public StudentFeeService(Application app)
    {
        apiInterface = ((MyApplication) app).getApiService();
    }
    public StudentFee getStudentFeeInfo(String studentId)
    {
        Response<StudentFee> response = null;
        try {
            Call<StudentFee> call = apiInterface.getStudentFeeInfo(studentId);
            response = call.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response == null? null:response.body();
    }
}
