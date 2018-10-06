package com.app.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.adapters.StudentExamAdapter;
import com.app.adapters.StudentFeeAdapter;
import com.app.interfaces.IServices;
import com.app.oigs.R;
import com.app.models.ExamType;
import com.app.models.Student;
import com.app.models.StudentDetail;
import com.app.models.StudentExam;
import com.app.models.StudentFee;
import com.app.models.StudentList;
import com.app.models.StudentLogout;
import com.app.network.Services;
import com.app.network.StudentDataService;
import com.app.network.StudentExamService;
import com.app.sessions.SessionManager;
import com.app.utils.ImageHolder;
import com.google.gson.Gson;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ExamActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private String mStudentId;
    private String stdName;
    private TextView mHeaderTitle;
    FrameLayout overlay;
    ListView examList;
    StudentExam studentExamObj;
    private static String POPUP_CONSTANT = "mPopup";
    private static String POPUP_FORCE_SHOW_ICON = "setForceShowIcon";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);
        if(getIntent().getExtras().getString("student_id") != null)
        {
            mStudentId = getIntent().getExtras().getString("student_id");
        }

        Gson gson = new Gson();
        SessionManager session = new SessionManager(getApplicationContext());
        StudentList studentList = gson.fromJson(session.getValues("RECORDS"), StudentList.class);
        examList =(ListView)findViewById(R.id.listview_exam);
        for (Student std:studentList.getStudents())
        {
            if(std.getStudentId().equals(mStudentId))
            {
                stdName = std.getStudentName();
                break;
            }
        }
        mHeaderTitle = (TextView) findViewById(R.id.tvHeaderTitle);
        mHeaderTitle.setText(stdName+""+getString(R.string.header_exam));
        try {
            mHeaderTitle.setTextColor(Color.parseColor(ImageHolder.getHeaderTextColor()));
            RelativeLayout rl = (RelativeLayout)findViewById(R.id.include);
            rl.setBackgroundColor(Color.parseColor(ImageHolder.getHeaderColor()));
            Picasso.get().load(ImageHolder.getLogoUrl()).into((ImageView)findViewById(R.id.imageView3));
        }
        catch (Exception ex)
        {

        }
        mHeaderTitle.setTextSize(16.0f);
        overlay = (FrameLayout) findViewById(R.id.progressBarHolder);
        overlay.setVisibility(View.VISIBLE);
        AsyncTaskRunner task = new AsyncTaskRunner(this);
        task.execute();

    }
    @Override
    public void onBackPressed() {
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
        PopupMenu popup = new PopupMenu(ExamActivity.this, view);
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
                        AsyncLogoutTaskRunner runner = new AsyncLogoutTaskRunner(ExamActivity.this);
                        runner.execute();
                        break;
                }
                return false;
            }
        });
        popup.show();
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ExamType exam = studentExamObj.getExamType().get(position);
        Intent i =  new Intent();
        i.setClass(ExamActivity.this, ExamDetailActivity.class);
        i.putExtra("exam_id", exam.getTestId());
        i.putExtra("exam_name", exam.getTestName());
        i.putExtra("student_id", mStudentId);
        i.putExtra("student_name", mStudentId);
        Gson gson = new Gson();
        String data = gson.toJson(studentExamObj);
        i.putExtra("exam_list", data);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    private class AsyncTaskRunner extends AsyncTask<String, String, StudentExam> {
        private Context mContext;

        public AsyncTaskRunner (Context context){
            mContext = context;
        }

        @Override
        protected StudentExam doInBackground(String... params) {
            StudentExam stdObj;
            StudentExamService service = new StudentExamService(getApplication());
            stdObj = service.getStudentExamInfo(mStudentId);
            return stdObj;
        }
        @Override
        protected void onPostExecute(StudentExam result) {
            if(result != null)
            {
                studentExamObj = result;
                StudentExamAdapter adapter = new StudentExamAdapter(result,ExamActivity.this);
                examList.setAdapter(adapter);
                examList.setOnItemClickListener(ExamActivity.this);
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
    public void onClickNotification(View view) {
        Intent i =  new Intent();
        i.setClass(this, NotificationActivity.class);
        i.putExtra("student_id", mStudentId);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
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
                Intent i = new Intent(ExamActivity.this, LoginActivity.class);
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
