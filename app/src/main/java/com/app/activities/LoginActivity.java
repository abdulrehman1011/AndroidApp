package com.app.activities;

import android.content.Context;
import android.content.Intent;
import android.hardware.camera2.TotalCaptureResult;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.app.interfaces.IServices;
import com.app.lyceum.american.americanlyceumapp.R;
import com.app.models.StudentList;
import com.app.network.Services;
import com.app.sessions.SessionManager;
import com.google.gson.Gson;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


public class LoginActivity extends BaseActivity {

    private IServices service;
    private TextView mHeaderTitle;
    private EditText mUserMobileNo;
    private StudentList stdObj;
    SessionManager session = null;
    FrameLayout overlay;
    private static String POPUP_CONSTANT = "mPopup";
    private static String POPUP_FORCE_SHOW_ICON = "setForceShowIcon";
    private String mPlayerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mUserMobileNo = (EditText) findViewById(R.id.et_username);
        mHeaderTitle = (TextView) findViewById(R.id.tvHeaderTitle);
        mHeaderTitle.setText(getString(R.string.header_login));
        overlay = (FrameLayout) findViewById(R.id.progressBarHolder);
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
        if(mUserMobileNo.getText().toString().trim().equals(""))
        {
            Toast.makeText(this, "Plese enter your number !", Toast.LENGTH_LONG).show();
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
            stdObj = service.GetStudentList(mUserMobileNo.getText().toString().trim(),mPlayerId);
            return stdObj;
        }
        @Override
        protected void onPostExecute(StudentList result) {
            if(result != null && result.getStudents().size() > 0)
            {
                Gson gson = new Gson();
                String jsonInString = gson.toJson(result);

                session.addValues("RECORDS",jsonInString);
                session.addValues("USERID",mUserMobileNo.getText().toString().trim());

                Intent i =  new Intent();
                i.setClass(mContext, AttendanceActivity.class);
                i.putExtra("LIST", stdObj);
                //i.putExtra("student_id", result.getStudents().get(0).student_id);
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
