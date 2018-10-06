package com.app.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.interfaces.IServices;
import com.app.emp.R;
import com.app.models.EmployeeList;
import com.app.models.SchoolProfile;
import com.app.models.EmployeeLogout;
import com.app.network.Services;
import com.app.network.StudentSchoolProfileService;
import com.app.sessions.SessionManager;
import com.app.utils.ImageHolder;
import com.google.gson.Gson;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class StudentDetailsActivity extends BaseActivity {

    FrameLayout overlay;
    private String mStudentId;
    private TextView mHeaderTitle;
    private TextView mContact;
    private TextView mEmail;
    private TextView mAddress;
    private static String POPUP_CONSTANT = "mPopup";
    private static String POPUP_FORCE_SHOW_ICON = "setForceShowIcon";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details);
        mHeaderTitle = (TextView) findViewById(R.id.tvHeaderTitle);
        mHeaderTitle.setText(getString(R.string.header_school_detail));
        mContact = (TextView) findViewById(R.id.tv_call_numbers);
        mEmail = (TextView) findViewById(R.id.tv_message);
        mAddress = (TextView) findViewById(R.id.tv_location);
        try {
            mHeaderTitle.setTextColor(Color.parseColor(ImageHolder.getHeaderTextColor()));
            RelativeLayout rl = (RelativeLayout)findViewById(R.id.include);
            rl.setBackgroundColor(Color.parseColor(ImageHolder.getHeaderColor()));
            Picasso.get().load(ImageHolder.getLogoUrl()).into((ImageView)findViewById(R.id.imageView3));
        }
        catch (Exception ex)
        {

        }
        if(getIntent().getExtras().getString("student_id") != null)
        {

            mStudentId = getIntent().getExtras().getString("student_id");
        }
        overlay = (FrameLayout) findViewById(R.id.progressBarHolder);
        overlay.setVisibility(View.VISIBLE);
        AsyncTaskRunner task = new AsyncTaskRunner(this);
        task.execute();
    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);

    }
    public void onHeaderBack(View view) {
        finish();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }
    public void onContextMenuClick(View view) {
        showPopup(view);
    }
    public void showPopup(View view) {
        PopupMenu popup = new PopupMenu(StudentDetailsActivity.this, view);
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
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                SessionManager session = new SessionManager(getApplicationContext());

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
                    case R.id.cmHome:
                        if(!session.getValues("RECORDS").equals(""))
                        {
                            Gson gson = new Gson();
                            EmployeeList studentList = gson.fromJson(session.getValues("RECORDS"), EmployeeList.class);
                            Intent i =  new Intent();
                            i.setClass(getApplicationContext(), Home2Activity.class);
                            i.putExtra("LIST", studentList);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            finish();
                            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                        }
                        break;
                    case R.id.cmLogout:
                        if(!session.getValues("RECORDS").equals(""))
                        {
                            Gson gson = new Gson();
                            EmployeeList studentList = gson.fromJson(session.getValues("RECORDS"), EmployeeList.class);
                            Intent i =  new Intent();
                            i.setClass(getApplicationContext(), Home2Activity.class);
                            i.putExtra("LIST", studentList);
                            startActivity(i);
                            finish();
                            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                        }
                        AsyncLogoutTaskRunner runner = new AsyncLogoutTaskRunner(StudentDetailsActivity.this);
                        runner.execute();
                        break;
                }
                return false;
            }
        });
        popup.show();
    }
    private class AsyncTaskRunner extends AsyncTask<String, String, SchoolProfile> {
        private Context mContext;

        public AsyncTaskRunner (Context context){
            mContext = context;
        }

        @Override
        protected SchoolProfile doInBackground(String... params) {
            SchoolProfile stdObj;
            StudentSchoolProfileService service = new StudentSchoolProfileService(getApplication());
            stdObj = service.getSchoolProfile(mStudentId);
            return stdObj;
        }
        @Override
        protected void onPostExecute(SchoolProfile result) {
            if(result != null)
            {
                mContact.setText(result.getSchoolPhone());
                mEmail.setText(result.getSchoolEmail());
                mAddress.setText(result.getBranchName()+"— \n"+result.getSchoolAdress());
            }
            else
            {
                Toast.makeText(mContext,"Unable to fetch school profile",Toast.LENGTH_SHORT).show();
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
    public void onClickNotification(View view) {
        Intent i =  new Intent();
        i.setClass(this, NotificationActivity.class);
        i.putExtra("student_id", mStudentId);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
    private class AsyncLogoutTaskRunner extends AsyncTask<String, String, EmployeeLogout> {
        private Context mContext;
        IServices service;
        public AsyncLogoutTaskRunner (Context context){
            mContext = context;
        }

        @Override
        protected EmployeeLogout doInBackground(String... params) {

            EmployeeLogout stdObj;
            service = new Services(mContext,getApplication());
            OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();
            String mPlayerId = status.getSubscriptionStatus().getUserId();
            stdObj = service.LogoutStudent(mPlayerId);
            return stdObj;
        }
        @Override
        protected void onPostExecute(EmployeeLogout result) {
            if(result != null && result.getLogout().equalsIgnoreCase("true"))
            {
                SessionManager session = new SessionManager(getApplicationContext());
                session.logoutUser();
                Intent i = new Intent(StudentDetailsActivity.this, LoginActivity.class);
                // set the new task and clear flags
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                finish();
                startActivity(i);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
            else
            {
                Toast.makeText(mContext,"Logout Failed",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPreExecute() {

        }


        @Override
        protected void onProgressUpdate(String... text) {

        }
    }
}
