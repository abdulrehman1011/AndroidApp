package com.app.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.adapters.StudentFeeAdapter;
import com.app.adapters.StudentNotificationAdapter;
import com.app.interfaces.IServices;
import com.app.lyceum.american.americanlyceumapp.R;
import com.app.models.Notify;
import com.app.models.StudentFee;
import com.app.models.StudentList;
import com.app.models.StudentLogout;
import com.app.models.StudentNotification;
import com.app.network.Services;
import com.app.network.StudentFeeService;
import com.app.network.StudentNotificationService;
import com.app.sessions.SessionManager;
import com.google.gson.Gson;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends BaseActivity {

    ListView mNotificationList;
    private String mStudentId;
    private TextView mHeaderTitle;
    FrameLayout overlay;
    private static String POPUP_CONSTANT = "mPopup";
    private static String POPUP_FORCE_SHOW_ICON = "setForceShowIcon";
    List<Notify> notificationList = new ArrayList<Notify>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        try {
           /* if(getIntent().getExtras().getString("Message") != null)
            {
                Toast.makeText(NotificationActivity.this, "Note : "+getIntent().getExtras().getString("Message"), Toast.LENGTH_LONG).show();
            }*/
            if(getIntent().getExtras().getString("student_id") != null)
            {
                mStudentId = getIntent().getExtras().getString("student_id");
            }
        }
        catch (Exception ex)
        {

        }
        mHeaderTitle = (TextView) findViewById(R.id.tvHeaderTitle);
        mHeaderTitle.setText(getString(R.string.header_notification));
        overlay = (FrameLayout) findViewById(R.id.progressBarHolder);
        ((ImageButton)findViewById(R.id.btn_notification)).setVisibility(View.GONE);
        mNotificationList =(ListView)findViewById(R.id.list_notifications);
        overlay.setVisibility(View.VISIBLE);
        AsyncTaskRunner runner = new AsyncTaskRunner(this);
        runner.execute();

    }
    public void onHeaderBack(View view) {
        finish();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }
    public void onContextMenuClick(View view) {
        showPopup(view);
    }
    public void showPopup(View view) {
        PopupMenu popup = new PopupMenu(NotificationActivity.this, view);
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
                            StudentList studentList = gson.fromJson(session.getValues("RECORDS"), StudentList.class);
                            Intent i =  new Intent();
                            i.setClass(getApplicationContext(), Home2Activity.class);
                            i.putExtra("LIST", studentList);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);startActivity(i);
                            finish();
                            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                        }
                        break;
                    case R.id.cmLogout:

                        AsyncLogoutTaskRunner runner = new AsyncLogoutTaskRunner(NotificationActivity.this);
                        runner.execute();
                        break;
                }
                return false;
            }
        });
        popup.show();
    }
    private class AsyncTaskRunner extends AsyncTask<String, String, StudentNotification> {
        private Context mContext;

        public AsyncTaskRunner (Context context){
            mContext = context;
        }

        @Override
        protected StudentNotification doInBackground(String... params) {
            StudentNotification stdObj;
            StudentNotificationService service = new StudentNotificationService(getApplication());
            stdObj = service.getStudentNotification(mStudentId);
            return stdObj;
        }
        @Override
        protected void onPostExecute(StudentNotification result) {
            if(result != null)
            {
                notificationList= result.getNotify();
                StudentNotificationAdapter adapter = new StudentNotificationAdapter(result,NotificationActivity.this);
                mNotificationList.setAdapter(adapter);

            }
            else
            {
                Toast.makeText(mContext,"No Data Found",Toast.LENGTH_SHORT).show();
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
    private class AsyncLogoutTaskRunner extends AsyncTask<String, String, StudentLogout> {
        private Context mContext;
        IServices service;
        public AsyncLogoutTaskRunner (Context context){
            mContext = context;
        }

        @Override
        protected StudentLogout doInBackground(String... params) {

            StudentLogout stdObj;
            service = new Services(mContext,getApplication());
            OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();
            String mPlayerId = status.getSubscriptionStatus().getUserId();
            stdObj = service.LogoutStudent(mPlayerId);
            return stdObj;
        }
        @Override
        protected void onPostExecute(StudentLogout result) {
            if(result != null && result.getLogout().equalsIgnoreCase("true"))
            {
                SessionManager session = new SessionManager(getApplicationContext());
                session.logoutUser();
                Intent i = new Intent(NotificationActivity.this, LoginActivity.class);
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
