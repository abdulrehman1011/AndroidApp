package com.app.network;

import android.app.Application;

import com.app.activities.MyApplication;
import com.app.interfaces.APIInterface;
import com.app.models.StudentFeeDetail;

import retrofit2.Call;
import retrofit2.Response;


public class StudentFeeMonthService {

    APIInterface apiInterface;
    public StudentFeeMonthService(Application app)
    {
        apiInterface = ((MyApplication) app).getApiService();
    }
    public StudentFeeDetail getStudentFeeDetail(String studentId, String fee_month)
    {
        Response<StudentFeeDetail> response = null;
        try {
            Call<StudentFeeDetail> call = apiInterface.getStudentFeeDetail(studentId,fee_month);
            response = call.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response == null? null:response.body();
    }
}
