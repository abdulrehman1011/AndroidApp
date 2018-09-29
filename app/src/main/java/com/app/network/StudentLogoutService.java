package com.app.network;

import android.app.Application;

import com.app.activities.MyApplication;
import com.app.interfaces.APIInterface;
import com.app.models.StudentLogout;

import retrofit2.Call;
import retrofit2.Response;

public class StudentLogoutService {
    APIInterface apiInterface;
    public StudentLogoutService(Application app)
    {
        apiInterface = ((MyApplication) app).getApiService();
    }

    public StudentLogout logoutStudent(String id)
    {
        Response<StudentLogout> response = null;
        try {
            Call<StudentLogout> call = apiInterface.logoutStudent(id);
            response = call.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response == null? null:response.body();
    }
}
