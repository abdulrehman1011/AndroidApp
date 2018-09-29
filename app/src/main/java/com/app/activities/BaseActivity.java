package com.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.interfaces.InternetConnectionListener;
import com.app.master.R;
import com.app.utils.ImageHolder;

public class BaseActivity extends Activity implements InternetConnectionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApplication) getApplication()).setInternetConnectionListener(this);
        ConnectivityManager cm = (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        android.net.NetworkInfo wifi = cm
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        android.net.NetworkInfo datac = cm
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((wifi != null & datac != null)
                && (wifi.isConnected() | datac.isConnected())) {
            //connection is avlilable
        }else{
            //no connection
            Toast toast = Toast.makeText(getApplicationContext(), "No Internet Connection",
                    Toast.LENGTH_LONG);
            toast.show();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(ImageHolder.getStatusbarColor()));
        }


    }

    @Override
    public void onPause() {
        super.onPause();
        ((MyApplication) getApplication()).removeInternetConnectionListener();
    }
    @Override
    public void onInternetUnavailable() {
        // hide content UI
        // show No Internet Connection UI
        final String toast = "No Internet available";
        runOnUiThread(() -> Toast.makeText(BaseActivity.this,toast , Toast.LENGTH_SHORT).show());

    }
}
