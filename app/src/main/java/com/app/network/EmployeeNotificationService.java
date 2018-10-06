package com.app.network;

import android.app.Application;

import com.app.activities.MyApplication;
import com.app.interfaces.APIInterface;
import com.app.models.EmployeeNotification;

import retrofit2.Call;
import retrofit2.Response;

public class EmployeeNotificationService {
    APIInterface apiInterface;
    public EmployeeNotificationService(Application app)
    {
        apiInterface = ((MyApplication) app).getApiService();
    }

    public EmployeeNotification getEmployeeNotification(String studentId)
    {
        Response<EmployeeNotification> response = null;
        try {
            Call<EmployeeNotification> call = apiInterface.getStudentNotifications(studentId);
            response = call.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response == null? null:response.body();
    }
}
