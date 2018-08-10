package com.app.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.app.adapters.StudentExamDetailAdapter;
import com.app.lyceum.american.americanlyceumapp.R;
import com.app.models.ExamResult;
import com.app.models.ExamType;
import com.app.models.StudentExam;
import com.app.models.StudentExamDetail;
import com.app.models.StudentList;
import com.app.network.StudentExamMarksDetailService;
import com.app.sessions.SessionManager;
import com.google.gson.Gson;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ExamDetailActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private String mStudentId;
    private String mStudentName;
    private int mExamId;
    private String mExamName;
    private TextView mHeaderTitle;
    private StudentExamDetail examObj;
    private String mExamJsonData;
    ListView examDetailList;
    FrameLayout overlay;
    Button btnNextExamResult;
    List<ExamType> mExamData;
    private int mNextExamId = 0;
    private static String POPUP_CONSTANT = "mPopup";
    private static String POPUP_FORCE_SHOW_ICON = "setForceShowIcon";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_detail);
        if(getIntent().getExtras().getString("student_name") != null)
        {
            mStudentName = getIntent().getExtras().getString("student_name");
        }
        if(getIntent().getExtras().getString("student_id") != null)
        {
            mStudentId = getIntent().getExtras().getString("student_id");
        }
        if(getIntent().getExtras().getInt("exam_id") > 0)
        {
            mExamId = getIntent().getExtras().getInt("exam_id");
        }
        if(getIntent().getExtras().getString("exam_name") != null)
        {
            mExamName = getIntent().getExtras().getString("exam_name");
        }
        if(getIntent().getExtras().getString("exam_list") != null)
        {
            mExamJsonData = getIntent().getExtras().getString("exam_list");
        }
        Gson gson = new Gson();
        mExamData = (gson.fromJson(mExamJsonData, StudentExam.class)).getExamType();
        mNextExamId = mExamId;
        //btnNextExamResult.setText(mExamName);
        mHeaderTitle = (TextView) findViewById(R.id.tvHeaderTitle);
        mHeaderTitle.setText(mExamName);
        examDetailList =(ListView)findViewById(R.id.list_exam_detail);
        overlay = (FrameLayout) findViewById(R.id.progressBarHolder);
        btnNextExamResult = (Button)findViewById(R.id.btn_exam_detail_next);
        btnNextExamResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mExamData.size() > 1)
                {
                    overlay.setVisibility(View.VISIBLE);
                    new AsyncTaskRunner(ExamDetailActivity.this).execute();
                }

            }
        });
        overlay.setVisibility(View.VISIBLE);
        new AsyncTaskRunner(this).execute();
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
        PopupMenu popup = new PopupMenu(ExamDetailActivity.this, view);
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
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }


    private void setNextExamId()
    {
        for(int i=0; i< mExamData.size();i++)
        {
            if(mExamData.get(i).getTestId() == mNextExamId)
            {
                if(i+1 == mExamData.size())
                {
                    mNextExamId = mExamData.get(0).getTestId();
                    mExamId = mNextExamId;
                    mExamName = mExamData.get(0).getTestName();
                    btnNextExamResult.setText(mExamName);
                }
                else
                {
                    mNextExamId = mExamData.get(i+1).getTestId();
                    mExamId = mNextExamId;
                    mExamName = mExamData.get(i+1).getTestName();
                    btnNextExamResult.setText(mExamName);
                    break;
                }

            }
        }
    }


    private class AsyncTaskRunner extends AsyncTask<String, String, StudentExamDetail> {
        private Context mContext;

        public AsyncTaskRunner (Context context){
            mContext = context;
        }

        @Override
        protected StudentExamDetail doInBackground(String... params) {

            StudentExamMarksDetailService service = new StudentExamMarksDetailService(getApplication());
            examObj = service.getStudentExamDetail(mStudentId, mNextExamId);
            return examObj;
        }
        @Override
        protected void onPostExecute(StudentExamDetail result) {
            if(result != null && result.getExamResults() != null)
            {
                examObj = result;
                StudentExamDetailAdapter adapter = new StudentExamDetailAdapter(result,ExamDetailActivity.this);

                examDetailList.setAdapter(adapter);
                examDetailList.setOnItemClickListener(ExamDetailActivity.this);
                adapter.notifyDataSetChanged();
            }
            else
            {
                StudentExamDetail temp = new StudentExamDetail();
                temp.setExamResults(new ArrayList<ExamResult>());
                StudentExamDetailAdapter adapter = new StudentExamDetailAdapter(temp,ExamDetailActivity.this);
                examDetailList.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                examDetailList.setOnItemClickListener(ExamDetailActivity.this);
                Toast.makeText(mContext,"No Data Found for \n "+mExamName,Toast.LENGTH_SHORT).show();
            }
            mHeaderTitle.setText(mExamName);
            if(mExamData.size() > 1)
            {
                setNextExamId();
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
