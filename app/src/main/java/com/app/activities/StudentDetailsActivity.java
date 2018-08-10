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
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.app.lyceum.american.americanlyceumapp.R;
import com.app.models.SchoolProfile;
import com.app.models.StudentDetail;
import com.app.models.StudentList;
import com.app.network.StudentDataService;
import com.app.network.StudentSchoolProfileService;
import com.app.sessions.SessionManager;
import com.google.gson.Gson;

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
                            StudentList studentList = gson.fromJson(session.getValues("RECORDS"), StudentList.class);
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
                            StudentList studentList = gson.fromJson(session.getValues("RECORDS"), StudentList.class);
                            Intent i =  new Intent();
                            i.setClass(getApplicationContext(), Home2Activity.class);
                            i.putExtra("LIST", studentList);
                            startActivity(i);
                            finish();
                            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                        }
                        session.logoutUser();
                        Intent i = new Intent(getApplicationContext(), IntroActivity.class);
                        // set the new task and clear flags
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
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
}
