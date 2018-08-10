package com.app.network;

import android.app.Application;

import com.app.activities.MyApplication;
import com.app.interfaces.APIInterface;
import com.app.models.StudentDetail;
import com.app.models.StudentNotification;

import retrofit2.Call;
import retrofit2.Response;

public class StudentNotificationService {
    APIInterface apiInterface;
    public StudentNotificationService(Application app)
    {
        apiInterface = ((MyApplication) app).getApiService();
    }

    public StudentNotification getStudentNotification(String studentId)
    {
        Response<StudentNotification> response = null;
        try {
            Call<StudentNotification> call = apiInterface.getStudentNotifications(studentId);
            response = call.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response == null? null:response.body();
    }
}
