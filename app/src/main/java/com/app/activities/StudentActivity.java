package com.app.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.interfaces.IServices;
import com.app.emp.R;
import com.app.models.EmployeeList;
import com.app.models.EmployeeLogout;
import com.app.models.EmployeeNotification;
import com.app.network.Services;
import com.app.network.EmployeeNotificationService;
import com.app.sessions.SessionManager;
import com.app.utils.ImageHolder;
import com.app.utils.Util;
import com.google.gson.Gson;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StudentActivity extends BaseActivity {

    private TextView mHeaderTitle;
    private String mStudentId;
    private static String POPUP_CONSTANT = "mPopup";
    private static String POPUP_FORCE_SHOW_ICON = "setForceShowIcon";
    TextView titleText;
    TextView contentText;
    RelativeLayout notifyLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        mHeaderTitle = (TextView) findViewById(R.id.tvHeaderTitle);

        try {
            mHeaderTitle.setText(ImageHolder.getHeaderTitle());
            mHeaderTitle.setTextColor(Color.parseColor(ImageHolder.getHeaderTextColor()));
            RelativeLayout rl = (RelativeLayout)findViewById(R.id.include);
            rl.setBackgroundColor(Color.parseColor(ImageHolder.getHeaderColor()));
        }
        catch (Exception ex)
        {

        }
        if(getIntent().getExtras().getString("student_id") != null)
        {
            mStudentId = getIntent().getExtras().getString("student_id");
        }
        notifyLayout = (RelativeLayout)findViewById(R.id.notification_box);
        titleText = (TextView) findViewById(R.id.tv_notification_title);
        contentText = (TextView) findViewById(R.id.tv_notification_content);
        AsyncTaskRunner runner = new AsyncTaskRunner(this);
        runner.execute();
    }
    public void onClickProfile(View view){

        //something TODO
        Intent i =  new Intent();
        i.setClass(StudentActivity.this, ProfileActivity.class);
        i.putExtra("student_id", mStudentId);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

    }
    public void onClickAtt(View view){

        //something TODO
        Intent i =  new Intent();
        i.setClass(StudentActivity.this, AttendanceActivity.class);
        i.putExtra("student_id", mStudentId);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

    }
    public void onClickExam(View view){

        //something TODO
       /* Intent i =  new Intent();
        i.setClass(StudentActivity.this, ExamActivity.class);
        i.putExtra("student_id", mStudentId);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);*/
    }
    public void onClickFee(View view){

        //something TODO
       /* Intent i =  new Intent();
        i.setClass(StudentActivity.this, FeeActivity.class);
        i.putExtra("student_id", mStudentId);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);*/
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
        PopupMenu popup = new PopupMenu(StudentActivity.this, view);
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
                            EmployeeList employeesList = gson.fromJson(session.getValues("RECORDS"), EmployeeList.class);
                            Intent i =  new Intent();
                            i.setClass(getApplicationContext(), Home2Activity.class);
                            i.putExtra("LIST", employeesList);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            finish();
                            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                        }
                        break;
                    case R.id.cmLogout:
                        AsyncLogoutTaskRunner runner = new AsyncLogoutTaskRunner(StudentActivity.this);
                        runner.execute();
                        break;
                }
                return false;
            }
        });
        popup.show();
    }
    private class AsyncTaskRunner extends AsyncTask<String, String, EmployeeNotification> {
        private Context mContext;

        public AsyncTaskRunner (Context context){
            mContext = context;
        }

        @Override
        protected EmployeeNotification doInBackground(String... params) {
            EmployeeNotification stdObj;
            EmployeeNotificationService service = new EmployeeNotificationService(getApplication());
            stdObj = service.getEmployeeNotification(mStudentId);
            return stdObj;
        }
        @Override
        protected void onPostExecute(EmployeeNotification result) {
            if(result != null)
            {
                try {
                    SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                    Date date = format.parse(result.getNotify().get(0).getNotifyDate().toString().toLowerCase());
                    String monthString  = (String) DateFormat.format("MMM",  date);
                    String day          = (String) DateFormat.format("dd",   date); // 20
                    titleText.setText(result.getNotify().get(0).getSubject() +" | "+ Util.getDayFormat(Integer.parseInt(day), monthString));
                    contentText.setText(result.getNotify().get(0).getText());
                    notifyLayout.setVisibility(View.VISIBLE);
                }
                catch (Exception ex)
                {

                }


            }
            else
            {
                Toast.makeText(mContext,"No Data Found",Toast.LENGTH_SHORT).show();
            }
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
                Intent i = new Intent(StudentActivity.this, LoginActivity.class);
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
