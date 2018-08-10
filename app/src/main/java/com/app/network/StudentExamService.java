package com.app.network;

import android.app.Application;
import android.os.AsyncTask;

import com.app.activities.MyApplication;
import com.app.interfaces.APIInterface;
import com.app.models.StudentExam;

import retrofit2.Call;
import retrofit2.Response;


public class StudentExamService {

    APIInterface apiInterface;
    public StudentExamService(Application app)
    {
        apiInterface = ((MyApplication) app).getApiService();
    }
    public StudentExam getStudentExamInfo(String studentId)
    {
        Response<StudentExam> response = null;
        try {
            Call<StudentExam> call = apiInterface.getStudentExamInfo(studentId);
            response = call.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response == null? null:response.body();
    }

}
