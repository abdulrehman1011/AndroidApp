package com.app.network;


import android.app.Application;

import com.app.activities.MyApplication;
import com.app.interfaces.APIInterface;
import com.app.models.EmployeeList;


import retrofit2.Call;
import retrofit2.Response;

public class EmployeeServices {


    APIInterface apiInterface;
    EmployeeList result;
    public EmployeeServices(Application app)
    {
        apiInterface = ((MyApplication) app).getApiService();
    }

    public EmployeeList getEmployees(String mobileno, String playerId)
    {
        Response<EmployeeList> response = null;
        try {
            Call<EmployeeList> call = apiInterface.getStudentList(mobileno,playerId);
            response = call.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response == null? null:response.body();
    }

}
