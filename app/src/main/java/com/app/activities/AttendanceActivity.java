package com.app.activities;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.interfaces.IServices;
import com.app.oigs.R;
import com.app.models.AttendenceDetail;
import com.app.models.StudentArrangedAttendance;
import com.app.models.StudentList;
import com.app.models.StudentLogout;
import com.app.models._StudentAttendance;
import com.app.network.Services;
import com.app.network.StudentAttendanceService;
import com.app.sessions.SessionManager;
import com.app.utils.ImageHolder;
import com.google.gson.Gson;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class AttendanceActivity extends BaseActivity {

    MaterialCalendarView widget;
    private String mStudentId;
    private TextView mHeaderTitle;
    FrameLayout overlay;
    private PresentDecorator presentDecorator;
    private AbsentDecorator absentDecorator;
    List<AttendenceDetail> data = new ArrayList<AttendenceDetail>();
    ArrayList<CalendarDay> presentDates = new ArrayList<>();
    ArrayList<CalendarDay> absentDates = new ArrayList<>();
    private static String POPUP_CONSTANT = "mPopup";
    private static String POPUP_FORCE_SHOW_ICON = "setForceShowIcon";
    private Button mBtnPresent, mBtnAbsent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        mHeaderTitle = (TextView) findViewById(R.id.tvHeaderTitle);
        mHeaderTitle.setText(getString(R.string.header_attendance));
        mHeaderTitle.setTextColor(Color.parseColor(ImageHolder.getHeaderTextColor()));
        try {
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
        overlay = (FrameLayout) findViewById(R.id.progressBarHolder);
        overlay.setVisibility(View.VISIBLE);
        mBtnPresent = (Button) findViewById(R.id.btn_present);
        mBtnAbsent = (Button) findViewById(R.id.btn_absent);
        AsyncTaskRunner task = new AsyncTaskRunner(this);
        task.execute();
        widget = (MaterialCalendarView) findViewById(R.id.calendarView);
        widget.setShowOtherDates(MaterialCalendarView.SHOW_ALL);
        Date c = Calendar.getInstance().getTime();
        widget.setCurrentDate(c);
        widget.setDateSelected(c,true);
        widget.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView materialCalendarView, @NonNull CalendarDay calendarDay, boolean b) {
                try
                {
                    DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                    DateFormat inputFormat2 = new SimpleDateFormat("MM/dd/yyyy");
                    String calenderDate =inputFormat.format(calendarDay.getDate());
                    Date selectedDate =  inputFormat.parse(calenderDate);
                    boolean checkData =  false;
                    for(int i = 0; i < data.size(); i++)
                    {
                        Date attendanceDate = inputFormat.parse(data.get(i).getDate().substring(0,10).trim());
                        if(attendanceDate == selectedDate)
                        {
                            Toast.makeText(AttendanceActivity.this, ""+data.get(i).getAttandence(), Toast.LENGTH_SHORT).show();
                            checkData = true;
                            break;
                        }
                    }
                    if(!checkData)
                    {
                        Toast.makeText(AttendanceActivity.this, "No attendance record!", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception ex)
                {

                }

            }
        });
        Calendar instance1 = Calendar.getInstance();
        instance1.set(instance1.get(Calendar.YEAR), Calendar.JANUARY, 1);




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
        PopupMenu popup = new PopupMenu(AttendanceActivity.this, view);
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
                        AsyncLogoutTaskRunner runner = new AsyncLogoutTaskRunner(AttendanceActivity.this);
                        runner.execute();
                        break;
                }
                return false;
            }
        });
        popup.show();
    }
    public class AbsentDecorator implements DayViewDecorator {

        private CalendarDay date;
        private int color;
        private HashSet<CalendarDay> dates;

        public AbsentDecorator(int color, Collection<CalendarDay> dates) {
            this.color = color;
            this.dates = new HashSet<>(dates);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return dates.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(color));
        }

        /**
         * We're changing the internals, so make sure to call {@linkplain MaterialCalendarView#invalidateDecorators()}
         */
        public void setDate(Date date) {
            this.date = CalendarDay.from(date);
        }
    }
    public class PresentDecorator implements DayViewDecorator {

        private CalendarDay date;
        private int color;
        private HashSet<CalendarDay> dates;

       public PresentDecorator(int color, Collection<CalendarDay> dates) {
            this.color = color;
            this.dates = new HashSet<>(dates);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return dates.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(color));
        }

        /**
         * We're changing the internals, so make sure to call {@linkplain MaterialCalendarView#invalidateDecorators()}
         */
        public void setDate(Date date) {
            this.date = CalendarDay.from(date);
        }
    }
    private class AsyncTaskRunner extends AsyncTask<String, String, _StudentAttendance> {
        private Context mContext;

        public AsyncTaskRunner (Context context){
            mContext = context;
        }

        @Override
        protected _StudentAttendance doInBackground(String... params) {
            _StudentAttendance stdObj;
            StudentAttendanceService service = new StudentAttendanceService(getApplication());
            stdObj = service.getStudentAttendance(mStudentId);
            return stdObj;
        }
        @Override
        protected void onPostExecute(_StudentAttendance result) {
            if(result != null)
            {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.MONTH, 0);
                int presentCount = 0;
                int absentCount = 0;
                for(int i = 0; i< result.getAttandenceDetails().size(); i++)
                {
                    AttendenceDetail studentAttendanceObj = new AttendenceDetail();
                    studentAttendanceObj = result.getAttandenceDetails().get(i);
                    Date date;
                    try {
                        DateFormat inputFormat = new SimpleDateFormat("MM/dd/yyyy");
                        date = inputFormat.parse(studentAttendanceObj.getDate().substring(0,10).trim());
                    }
                    catch (Exception ex)
                    {

                        date = Calendar.getInstance().getTime();

                    }
                    if(studentAttendanceObj.getAttandence().equals("Present"))
                    {
                        presentCount++;
                        CalendarDay day = CalendarDay.from(date);
                        presentDates.add(day);
                    }
                    else
                    {
                        absentCount++;
                        CalendarDay day = CalendarDay.from(date);
                        absentDates.add(day);
                    }

                    data.add(studentAttendanceObj);
                }
                mBtnPresent.setText(presentCount+" | Present");
                mBtnAbsent.setText(absentCount+" | Absent");
                presentDecorator = new PresentDecorator(Color.parseColor("#00e500"),presentDates);
                absentDecorator = new AbsentDecorator(Color.RED,absentDates);
                widget.addDecorators(
                        presentDecorator,absentDecorator
                );

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
                Intent i = new Intent(AttendanceActivity.this, LoginActivity.class);
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
