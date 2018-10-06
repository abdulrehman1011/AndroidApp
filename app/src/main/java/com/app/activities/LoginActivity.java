package com.app.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.camera2.TotalCaptureResult;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.interfaces.IServices;
import com.app.oigs.R;
import com.app.models.AppRateUrlModel;
import com.app.models.StudentList;
import com.app.network.Services;
import com.app.sessions.SessionManager;
import com.app.utils.ImageHolder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.onesignal.OSPermissionObserver;
import com.onesignal.OSPermissionStateChanges;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static com.app.activities.MyApplication.getContext;


public class LoginActivity extends BaseActivity  {

    private IServices service;
    private TextView mHeaderTitle;
    private EditText mUserMobileNo;
    private StudentList stdObj;
    private String mPlayerId;
    SessionManager session = null;
    FrameLayout overlay;
    private static String POPUP_CONSTANT = "mPopup";
    private static String POPUP_FORCE_SHOW_ICON = "setForceShowIcon";
    double density;
    String screenDensity= "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mUserMobileNo = (EditText) findViewById(R.id.et_username);
        mHeaderTitle = (TextView) findViewById(R.id.tvHeaderTitle);
        mHeaderTitle.setText(getString(R.string.header_login));
        overlay = (FrameLayout) findViewById(R.id.progressBarHolder);

        density = getResources().getDisplayMetrics().density;
        int aa= getResources().getDisplayMetrics().densityDpi;

        switch (getResources().getDisplayMetrics().densityDpi) {
            case DisplayMetrics.DENSITY_LOW:
                screenDensity = "drawable-ldpi";
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                screenDensity = "drawable-mdpi";
                break;
            case DisplayMetrics.DENSITY_HIGH:
                screenDensity = "drawable-hdpi";
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                screenDensity = "drawable-xhdpi";
                break;
            case DisplayMetrics.DENSITY_420:
                screenDensity = "drawable-xxhdpi";
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
                screenDensity = "drawable-xxhdpi";
                break;
            case DisplayMetrics.DENSITY_XXXHIGH:
                screenDensity = "drawable-xxxhdpi";
                break;
        }
        double d = getResources().getDisplayMetrics().density;
        String app = getResources().getString(R.string.app_name);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.parseColor("#000000"));
                //ColorStateList colorStateList = ColorStateList.valueOf(Color.parseColor("#a05211"));
                //mUserMobileNo.setBackgroundTintList(colorStateList);
            }
        if(app.equalsIgnoreCase("Master"))
        {
            ((ImageView)findViewById(R.id.imageView3)).setImageResource(R.drawable.logo_2);
            ((Button)findViewById(R.id.button2)).setBackground(ContextCompat.getDrawable(this, R.drawable.button_2));
            ((RelativeLayout)findViewById(R.id.include)).setBackgroundColor(Color.parseColor("#72d0ea"));
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.parseColor("#72d0ea"));
                //ColorStateList colorStateList = ColorStateList.valueOf(Color.parseColor("#a05211"));
                //mUserMobileNo.setBackgroundTintList(colorStateList);
            }*/

            //mUserMobileNo.setCursorVisible(true);
        }
        else
        {
            ((ImageView)findViewById(R.id.imageView3)).setImageResource(R.drawable.logo);
            ((Button)findViewById(R.id.button2)).setBackground(ContextCompat.getDrawable(this, R.drawable.button));
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.RED);
            }*/
        }
        ((ImageButton)findViewById(R.id.btn_notification)).setVisibility(View.GONE);
        OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();

        mPlayerId = status.getSubscriptionStatus().getUserId();
        session = new SessionManager(getApplicationContext());
        if(!session.getValues("RECORDS").equals(""))
        {

            Gson gson = new Gson();
            StudentList studentList = gson.fromJson(session.getValues("RECORDS"), StudentList.class);
            Intent i =  new Intent();
            i.setClass(this, Home2Activity.class);
            i.putExtra("LIST", studentList);
            startActivity(i);
            finish();
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    public void onClickLogin(View view) {


        OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();

        mPlayerId = status.getSubscriptionStatus().getUserId();

        if(mUserMobileNo.getText().toString().trim().equals(""))
        {
            //Toast.makeText(this, "Plese enter your number !", Toast.LENGTH_LONG).show();
            return;
        }
        if(mPlayerId.trim().equals(""))
        {
            Toast.makeText(this, "Please wait device is registering !", Toast.LENGTH_LONG).show();
            return;
        }
        overlay.setVisibility(View.VISIBLE);
        AsyncTaskRunner runner = new AsyncTaskRunner(this);
        runner.execute();
    }
    public void onHeaderBack(View view) {
        finish();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }
    public void onContextMenuClick(View view) {
        showPopup(view);
    }
    public void showPopup(View view) {
        PopupMenu popup = new PopupMenu(LoginActivity.this, view);
        try {
            // Reflection apis to enforce show icon
            Field[] fields = popup.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.getName().equals(POPUP_CONSTANT)) {
                    field.setAccessible(true);
                    Object menuPopupHelper = field.get(popup);
                    Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                    Method setForceIcons = classPopupHelper.getMethod(POPUP_FORCE_SHOW_ICON, boolean.class);
                    setForceIcons.invoke(menuPopupHelper, true);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        popup.getMenuInflater().inflate(R.menu.login_popup_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.cmCall:
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        startActivity(intent);
                        break;
                    case R.id.cmEmail:
                        Intent intentEmail = new Intent(Intent.ACTION_VIEW);
                        Uri data = Uri.parse("mailto:?subject= &body= ");
                        intentEmail.setData(data);
                        startActivity(intentEmail);
                        break;
                    case R.id.cmLogout:
                        Toast.makeText(getApplicationContext(), "Logout" , Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });
        popup.show();
    }



    private class AsyncTaskRunner extends AsyncTask<String, String, StudentList> {
        private Context mContext;

        public AsyncTaskRunner (Context context){
            mContext = context;
        }

        @Override
        protected StudentList doInBackground(String... params) {

            service = new Services(mContext,getApplication());
            stdObj = service.GetStudentList(mUserMobileNo.getText().toString().trim(), mPlayerId);
            AppRateUrlModel rateUrl = service.GetAppRateURL("2160");
            if(rateUrl != null && !rateUrl.getRateUrl().equalsIgnoreCase(""))
            {
                session.addValues("rate_url",rateUrl.getRateUrl());
            }
            //ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                 //   getApplicationContext())
                //    .build();
          //  ImageLoader imageLoader = ImageLoader.getInstance();
           // imageLoader.init(config);
           // Bitmap logo = imageLoader.loadImageSync(stdObj.getStudents().get(0).getLogoFolder()+"/"+screenDensity+"/logo.png");
           // Bitmap btnImage = imageLoader.loadImageSync(stdObj.getStudents().get(0).getHomebuttoncolor()+"/"+screenDensity+"/button.png");
           // ImageHolder.addBitmap(logo,"logo");
            //ImageHolder.addBitmap(btnImage,"redbtn");
            if(stdObj != null && stdObj.getStudents().size() > 0)
            {

                //String headerError = stdObj.getStudents().get(0).getHeadercolor();
                //headerError = headerError.replace("\r\n","");
                //stdObj.getStudents().get(0).setHeadercolor(headerError);
                ImageHolder.setRedBtnUrl(stdObj.getStudents().get(0).getHomebuttoncolor()+"/"+screenDensity+"/button.png");
                ImageHolder.setLogoUrl(stdObj.getStudents().get(0).getLogoFolder()+"/"+screenDensity+"/logo.png");
                ImageHolder.setHeaderColor(stdObj.getStudents().get(0).getHeadercolor());
                ImageHolder.setMenuItemBgColor(stdObj.getStudents().get(0).getMenuItemBackgroundcolor());
                ImageHolder.setMenuMainBackgroundColor(stdObj.getStudents().get(0).getMainBackgroundColor());
                ImageHolder.setMenuTextColor(stdObj.getStudents().get(0).getMenuTextColor());
                ImageHolder.setStatusbarColor(stdObj.getStudents().get(0).getStatusbarcolor());
                ImageHolder.setHeaderTextColor(stdObj.getStudents().get(0).getHeaderTextColor() == null?"#FF00000":stdObj.getStudents().get(0).getHeaderTextColor());
                ImageHolder.setSideMennuLogoBackgroundColor(stdObj.getStudents().get(0).getLogobackgroundcolor());
            }

            return stdObj;
        }
        @Override
        protected void onPostExecute(StudentList result) {
            if(result != null && result.getStudents().size() > 0)
            {
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.serializeNulls();
                Gson gson = gsonBuilder.create();
                String jsonInString = gson.toJson(result);
                session.addValues("menuItemBackgroundColor",result.getStudents().get(0).getMenuItemBackgroundcolor());
                session.addValues("menuTextColor",result.getStudents().get(0).getMenuTextColor());
                session.addValues("MainBackgroundColor",result.getStudents().get(0).getMainBackgroundColor());
                session.addValues("statusBarColor",result.getStudents().get(0).getStatusbarcolor());
                session.addValues("logo",result.getStudents().get(0).getLogoFolder()+"/"+screenDensity+"/logo.png");
                session.addValues("redbtn",result.getStudents().get(0).getHomebuttoncolor()+"/"+screenDensity+"/button.png");
                session.addValues("headerColor",result.getStudents().get(0).getHeadercolor());
                session.addValues("logobgcolor",result.getStudents().get(0).getLogobackgroundcolor());
                session.addValues("headerTextColor",result.getStudents().get(0).getHeaderTextColor()==null?"#FF00000":result.getStudents().get(0).getHeaderTextColor());
                session.addValues("RECORDS",jsonInString);
                session.addValues("USERID",mUserMobileNo.getText().toString().trim());

                Intent i =  new Intent();
                i.setClass(mContext, Home2Activity.class);
                i.putExtra("LIST", stdObj);
                finish();
                startActivity(i);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
            else
            {
                Toast.makeText(mContext,"Login Failed",Toast.LENGTH_SHORT).show();
            }
            overlay.setVisibility(View.GONE);
        }


        @Override
        protected void onPreExecute() {

        }


        @Override
        protected void onProgressUpdate(String... text) {

        }
    }
}
