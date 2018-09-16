package com.app.network;

import android.app.Application;

import com.app.activities.MyApplication;
import com.app.interfaces.APIInterface;
import com.app.models.AppRateUrlModel;
import com.app.models.StudentList;

import retrofit2.Call;
import retrofit2.Response;

public class AppRateUrlService {
    APIInterface apiInterface;
    StudentList result;
    public AppRateUrlService(Application app)
    {
        apiInterface = ((MyApplication) app).getApiService();
    }

    public AppRateUrlModel getRateUrl(String id)
    {
        Response<AppRateUrlModel> response = null;
        try {
            Call<AppRateUrlModel> call = apiInterface.getRateURL(id);
            response = call.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response == null? null:response.body();
    }
}
