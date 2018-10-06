package com.app.network;

import android.app.Application;

import com.app.activities.MyApplication;
import com.app.interfaces.APIInterface;
import com.app.models.EmployeeLogout;

import retrofit2.Call;
import retrofit2.Response;

public class EmployeeLogoutService {
    APIInterface apiInterface;
    public EmployeeLogoutService(Application app)
    {
        apiInterface = ((MyApplication) app).getApiService();
    }

    public EmployeeLogout logoutStudent(String id)
    {
        Response<EmployeeLogout> response = null;
        try {
            Call<EmployeeLogout> call = apiInterface.logoutStudent(id);
            response = call.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response == null? null:response.body();
    }
}
