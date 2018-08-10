package com.app.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.app.adapters.StudentFeeAdapter;
import com.app.adapters.StudentListAdapter;
import com.app.lyceum.american.americanlyceumapp.R;
import com.app.models.FeeMonth;
import com.app.models.Student;
import com.app.models.StudentDetail;
import com.app.models.StudentFee;
import com.app.models.StudentList;
import com.app.network.StudentDataService;
import com.app.network.StudentFeeMonthService;
import com.app.network.StudentFeeService;
import com.app.sessions.SessionManager;
import com.google.gson.Gson;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class FeeActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private String mStudentId;
    private TextView mHeaderTitle;
    ListView monthList;
    StudentFee studentFeeObj;
    FrameLayout overlay;
    private static String POPUP_CONSTANT = "mPopup";
    private static String POPUP_FORCE_SHOW_ICON = "setForceShowIcon";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee);
        try {
            if(getIntent().getExtras().getString("student_id") != null)
            {
                mStudentId = getIntent().getExtras().getString("student_id");
            }
        }
        catch (Exception ex)
        {

        }
        mHeaderTitle = (TextView) findViewById(R.id.tvHeaderTitle);
        mHeaderTitle.setText(getString(R.string.header_fee));
        overlay = (FrameLayout) findViewById(R.id.progressBarHolder);
        monthList =(ListView)findViewById(R.id.fee_list);
        overlay.setVisibility(View.VISIBLE);
        AsyncTaskRunner runner = new AsyncTaskRunner(this);
        runner.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        FeeMonth std = studentFeeObj.getFeeMonth().get(position);
        Intent i =  new Intent();
        i.setClass(FeeActivity.this, FeeDetailActivity.class);
        i.putExtra("fee_id", std.getMonthId());
        i.putExtra("fee_month", std.getMonthName());
        i.putExtra("student_id", mStudentId);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
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
        PopupMenu popup = new PopupMenu(FeeActivity.this, view);
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
    private class AsyncTaskRunner extends AsyncTask<String, String, StudentFee> {
        private Context mContext;

        public AsyncTaskRunner (Context context){
            mContext = context;
        }

        @Override
        protected StudentFee doInBackground(String... params) {
            StudentFee stdObj;
            StudentFeeService service = new StudentFeeService(getApplication());
            stdObj = service.getStudentFeeInfo(mStudentId);
            return stdObj;
        }
        @Override
        protected void onPostExecute(StudentFee result) {
            if(result != null)
            {
                studentFeeObj= result;
                StudentFeeAdapter adapter = new StudentFeeAdapter(result,FeeActivity.this);
                monthList.setAdapter(adapter);
                monthList.setOnItemClickListener(FeeActivity.this);

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
}
