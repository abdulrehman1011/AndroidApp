package com.app.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.interfaces.InternetConnectionListener;
import com.app.master.R;
import com.app.models.StudentList;
import com.app.sessions.SessionManager;
import com.app.utils.ImageHolder;
import com.google.gson.Gson;


import static android.support.constraint.Constraints.TAG;

public class IntroActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, InternetConnectionListener {
    SessionManager session = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((MyApplication) getApplication()).setInternetConnectionListener(IntroActivity.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(),R.drawable.heart_unfilled);
        toolbar.setOverflowIcon(drawable);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        toggle.syncState();

        toolbar.setNavigationIcon(null);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.BLACK);
            }

        String app = getResources().getString(R.string.app_name);
        if(app.equalsIgnoreCase("Master"))
        {
            ((ImageView)findViewById(R.id.intro_logo)).setImageResource(R.drawable.logo_2);
            ((Button)findViewById(R.id.backgroundImage)).setBackground(ContextCompat.getDrawable(this, R.drawable.button_2));
            toolbar.setBackgroundColor(Color.parseColor("#72d0ea"));
           /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.parseColor("#a05211"));
            }*/

        }
        else
        {
            ((ImageView)findViewById(R.id.intro_logo)).setImageResource(R.drawable.logo_2);
            ((Button)findViewById(R.id.backgroundImage)).setBackground(ContextCompat.getDrawable(this, R.drawable.button));

        }
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
        boolean flag = isStoragePermissionGranted();
        try {
            session = new SessionManager(getApplicationContext());
            if(!session.getValues("RECORDS").equals("")) {

                AsyncTaskRunner runner = new AsyncTaskRunner(this);
                runner.execute();
            }
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }


    }
    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
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
        runOnUiThread(() -> Toast.makeText(IntroActivity.this,toast , Toast.LENGTH_SHORT).show());
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_school_details) {

            Intent i =  new Intent();
            i.setClass(this, StudentDetailsActivity.class);
            i.putExtra("student_id", "186");
            startActivity(i);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        } else if (id == R.id.nav_notifications) {
            Intent i =  new Intent();
            i.setClass(this, NotificationActivity.class);
            i.putExtra("student_id", "186");
            startActivity(i);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        } else if (id == R.id.nav_student_gallery) {

        } else if (id == R.id.nav_rateus) {

        } else if (id == R.id.nav_logout) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void onClickGotoLogin(View view) {
        Intent i =  new Intent();
        i.setClass(this, LoginActivity.class);
        finish();
        startActivity(i);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    public void onClickRightMenu(View item) {
        Toast.makeText(IntroActivity.this,"Liked",Toast.LENGTH_SHORT).show();
    }
    private class AsyncTaskRunner extends AsyncTask<String, Void, String> {
        private Context mContext;

        public AsyncTaskRunner (Context context){
            mContext = context;
        }

        @Override
        protected String doInBackground(String... params) {
            try
            {
                String logoUrl = session.getValues("logo");
                String btnImageUrl = session.getValues("redbtn");
                //ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                     //   getApplicationContext())
                        //.build();
                //ImageLoader imageLoader = ImageLoader.getInstance();
                //imageLoader.init(config);
               // Bitmap logo = imageLoader.loadImageSync(logoUrl);
                //Bitmap btnImage = imageLoader.loadImageSync(btnImageUrl);
                //ImageHolder.addBitmap(logo,"logo");
                //ImageHolder.addBitmap(btnImage,"redbtn");
                ImageHolder.setRedBtnUrl(session.getValues("redbtn"));
                ImageHolder.setLogoUrl(session.getValues("logo"));
                ImageHolder.setHeaderColor(session.getValues("headerColor"));
                ImageHolder.setMenuItemBgColor(session.getValues("menuItemBackgroundColor"));
                ImageHolder.setMenuMainBackgroundColor(session.getValues("MainBackgroundColor"));
                ImageHolder.setMenuTextColor(session.getValues("menuTextColor"));
                ImageHolder.setStatusbarColor(session.getValues("statusBarColor"));
                ImageHolder.setHeaderTextColor(session.getValues("headerTextColor"));
                ImageHolder.setSideMennuLogoBackgroundColor(session.getValues("logobgcolor"));
                ImageHolder.setHeaderTitle(session.getValues("headertitle"));

            }
            catch (Exception ex)
            {

            }

            Gson gson = new Gson();
            StudentList studentList = gson.fromJson(session.getValues("RECORDS"), StudentList.class);
            Intent i =  new Intent();
            i.setClass(getApplicationContext(), Home2Activity.class);
            i.putExtra("LIST", studentList);
            startActivity(i);
            finish();
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                return "";
        }



        @Override
        protected void onPreExecute() {

        }



    }

}
