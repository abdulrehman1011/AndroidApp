package com.app.activities;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.app.interfaces.APIInterface;
import com.app.interfaces.InternetConnectionListener;
import com.app.network.NetworkConnectionInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;

import com.app.notifications.MyNotificationOpenedHandler;
import com.app.notifications.MyNotificationReceivedHandler;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyApplication extends Application {
    private static Context context;
    private InternetConnectionListener mInternetConnectionListener;
    private APIInterface apiService;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        //MyNotificationOpenedHandler : This will be called when a notification is tapped on.
        //MyNotificationReceivedHandler : This will be called when a notification is received while your app is running.
        OneSignal.startInit(this)
                .setNotificationOpenedHandler(new MyNotificationOpenedHandler())
                .setNotificationReceivedHandler( new MyNotificationReceivedHandler() )
                .init();
        OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();
        status.getSubscriptionStatus().getUserId();
    }

    public void setInternetConnectionListener(InternetConnectionListener listener) {
        mInternetConnectionListener = listener;
    }

    public void removeInternetConnectionListener() {
        mInternetConnectionListener = null;
    }

    public APIInterface getApiService() {
        if (apiService == null) {
            apiService = provideRetrofit("http://api.master.org.pk").create(APIInterface.class);
        }
        return apiService;
    }
    private Retrofit provideRetrofit(String url) {
        return new Retrofit.Builder()
                .baseUrl(url)
                .client(provideOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().serializeNulls().create()))
                .build();
    }
    private OkHttpClient provideOkHttpClient() {
        OkHttpClient.Builder okhttpClientBuilder = new OkHttpClient.Builder();
        okhttpClientBuilder.connectTimeout(30, TimeUnit.SECONDS);
        okhttpClientBuilder.readTimeout(30, TimeUnit.SECONDS);
        okhttpClientBuilder.writeTimeout(30, TimeUnit.SECONDS);
        okhttpClientBuilder.addInterceptor(new NetworkConnectionInterceptor() {
            @Override
            public boolean isInternetAvailable() {
                return MyApplication.this.isNetworkAvailable();
            }

            @Override
            public void onInternetUnavailable() {
                // we can broadcast this event to activity/fragment/service
                // through LocalBroadcastReceiver or
                // RxBus/EventBus
                // also we can call our own interface method
                // like this.

                mInternetConnectionListener.onInternetUnavailable();
            }
        });
        return okhttpClientBuilder.build();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        android.net.NetworkInfo wifi = cm
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        android.net.NetworkInfo datac = cm
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((wifi != null & datac != null)
                && (wifi.isConnected() | datac.isConnected())) {
            //connection is avlilable
            return true;
        }else{
            //no connection
            return false;
        }
        /*ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();*/
    }
}
