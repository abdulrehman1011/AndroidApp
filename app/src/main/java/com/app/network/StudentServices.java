package com.app.network;


import android.app.Application;

import com.app.activities.MyApplication;
import com.app.interfaces.APIInterface;
import com.app.models.StudentList;


import retrofit2.Call;
import retrofit2.Response;

public class StudentServices {

    APIInterface apiInterface;
    StudentList result;
    public StudentServices(Application app)
    {
        apiInterface = ((MyApplication) app).getApiService();
    }

    public StudentList getStudents(String mobileno)
    {
        Response<StudentList> response = null;
        try {
            Call<StudentList> call = apiInterface.getStudentList(mobileno,"test");
            response = call.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response == null? null:response.body();
    }

}
