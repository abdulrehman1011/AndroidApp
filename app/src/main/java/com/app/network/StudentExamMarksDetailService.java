package com.app.network;


import android.app.Application;

import com.app.activities.MyApplication;
import com.app.interfaces.APIInterface;
import com.app.models.StudentExamDetail;

import retrofit2.Call;
import retrofit2.Response;

public class StudentExamMarksDetailService  {

    APIInterface apiInterface;
    public StudentExamMarksDetailService(Application app)
    {
        apiInterface = ((MyApplication) app).getApiService();
    }
    public StudentExamDetail getStudentExamDetail(String studentId,int mark_role)
    {
        Response<StudentExamDetail> response = null;
        try {
            Call<StudentExamDetail> call = apiInterface.getStudentExamDetail(studentId,mark_role);
            response = call.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response == null? null:response.body();
    }
}
