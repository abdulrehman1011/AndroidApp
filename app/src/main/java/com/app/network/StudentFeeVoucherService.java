package com.app.network;

import android.app.Application;

import com.app.activities.MyApplication;
import com.app.interfaces.APIInterface;

import java.io.InputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;


public class StudentFeeVoucherService {
    APIInterface apiInterface;
    private int totalFileSize;

    public StudentFeeVoucherService(Application app)
    {
        apiInterface = ((MyApplication) app).getApiService();
    }

    public InputStream getStudentNotification(String studentId,String feeMonth)
    {
        Response<ResponseBody> response = null;
        try {
            Call<ResponseBody> call = apiInterface.getStudentFeeVoucher(studentId, feeMonth);
            response = call.execute();
            if(response.isSuccessful()) {

                return response.body().byteStream();

            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



}
