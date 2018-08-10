package com.app.network;

import android.app.Application;

import com.app.activities.MyApplication;
import com.app.interfaces.APIInterface;
import com.app.models.SchoolProfile;
import com.app.models.StudentNotification;

import retrofit2.Call;
import retrofit2.Response;


public class StudentSchoolProfileService {
    APIInterface apiInterface;
    public StudentSchoolProfileService(Application app)
    {
        apiInterface = ((MyApplication) app).getApiService();
    }

    public SchoolProfile getSchoolProfile(String studentId)
    {
        Response<SchoolProfile> response = null;
        try {
            Call<SchoolProfile> call = apiInterface.getSchoolProfile(studentId);
            response = call.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response == null? null:response.body();
    }
}