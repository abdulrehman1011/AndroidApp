package com.app.activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.interfaces.IServices;
import com.app.oigs.R;
import com.app.models.FeeByMonth;
import com.app.models.Student;
import com.app.models.StudentFeeDetail;
import com.app.models.StudentList;
import com.app.models.StudentLogout;
import com.app.network.Services;
import com.app.network.StudentFeeMonthService;
import com.app.network.StudentFeeVoucherService;
import com.app.sessions.SessionManager;
import com.app.utils.ImageHolder;
import com.google.gson.Gson;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static android.support.constraint.Constraints.TAG;

public class FeeDetailActivity extends BaseActivity implements View.OnClickListener {

    private String monthId;
    private String monthName;
    private String mStudentId;
    private TextView stdName;
    private TextView mHeaderTitle;
    private TextView feeMonth;
    private TextView feeTotalAmount;
    private TextView feeAmountPaid;
    private TextView feeOutstandingAmountPaid;
    private TextView feeDepositDate;
    private FrameLayout btnGetVoucher;
    private static String POPUP_CONSTANT = "mPopup";
    private static String POPUP_FORCE_SHOW_ICON = "setForceShowIcon";
    FrameLayout overlay;

    private DownloadManager downloadManager;
    private long refid;
    private Uri Download_Uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee_detail);
        if(getIntent().getExtras().getString("fee_id") != null)
        {
            monthId = getIntent().getExtras().getString("fee_id");
            monthName = getIntent().getExtras().getString("fee_month");
        }
        if(getIntent().getExtras().getString("student_id") != null)
        {
            mStudentId = getIntent().getExtras().getString("student_id");
        }
        mHeaderTitle = (TextView) findViewById(R.id.tvHeaderTitle);
        mHeaderTitle.setText(getString(R.string.header_fee_detail));
        stdName = (TextView) findViewById(R.id.fee_std_name);
        feeMonth = (TextView) findViewById(R.id.fee_month_name);
        feeTotalAmount = (TextView) findViewById(R.id.fee_amount);
        feeAmountPaid = (TextView) findViewById(R.id.fee_amount_paid);
        feeOutstandingAmountPaid = (TextView) findViewById(R.id.fee_outstanding_amount);
        feeDepositDate = (TextView) findViewById(R.id.fee_deposit_date);
        btnGetVoucher = (FrameLayout) findViewById(R.id.btn_get_voucher);
        try {
            mHeaderTitle.setTextColor(Color.parseColor(ImageHolder.getHeaderTextColor()));
            RelativeLayout rl = (RelativeLayout)findViewById(R.id.include);
            rl.setBackgroundColor(Color.parseColor(ImageHolder.getHeaderColor()));
            Picasso.get().load(ImageHolder.getLogoUrl()).into((ImageView)findViewById(R.id.imageView4));
        }
        catch (Exception ex)
        {

        }
        try {
            Picasso.get().load(ImageHolder.getRedBtnUrl()).into((ImageView) findViewById(R.id.redbtn_image));
            //BitmapDrawable bdrawable = new BitmapDrawable(getApplicationContext().getResources(), ImageHolder.getBitmap("redbtn"));
            //btnGetVoucher.setBackground(bdrawable);
        }
        catch (Exception ex)
        {

        }

        btnGetVoucher.setOnClickListener(this);
        overlay = (FrameLayout) findViewById(R.id.progressBarHolder);
        overlay.setVisibility(View.VISIBLE);
        Gson gson = new Gson();
        SessionManager session = new SessionManager(getApplicationContext());
        StudentList studentList = gson.fromJson(session.getValues("RECORDS"), StudentList.class);
        for (Student std:studentList.getStudents())
        {
            if(std.getStudentId().equals(mStudentId))
            {
                stdName.setText(std.getStudentName());
                break;
            }
        }
       // ActivityCompat.requestPermissions(this, new String[]{
          //      Manifest.permission.WRITE_EXTERNAL_STORAGE
        //}, 1);
        registerReceiver(onComplete,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        AsyncTaskRunner taskRunner = new AsyncTaskRunner(this);
        taskRunner.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();

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
        PopupMenu popup = new PopupMenu(FeeDetailActivity.this, view);
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
                        AsyncLogoutTaskRunner runner = new AsyncLogoutTaskRunner(FeeDetailActivity.this);
                        runner.execute();
                        break;
                }
                return false;
            }
        });
        popup.show();
    }
    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }
    @Override
    public void onClick(View v) {

        try
        {
            // custom dialog
            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.download_dialog);
            dialog.setTitle("Download PDF");

            // set the custom dialog components - text, image and button


            RelativeLayout dialogButton = (RelativeLayout) dialog.findViewById(R.id.dialogButtonOK);
            // if button is clicked, close the custom dialog
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(),"Downloading start",Toast.LENGTH_SHORT).show();

                    downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                    Download_Uri = Uri.parse("http://api.master.org.pk/api/Fee?std_id="+mStudentId+"&fee_month="+monthId);
                    DownloadManager.Request request = new DownloadManager.Request(Download_Uri);
                    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                    request.setAllowedOverRoaming(false);
                    request.setTitle("Downloading " + mStudentId+".pdf");
                    request.setDescription("Downloading " + mStudentId+".pdf");
                    request.setVisibleInDownloadsUi(true);
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,  "/" + mStudentId +".pdf");

                    refid = downloadManager.enqueue(request);
                    dialog.dismiss();
                }
            });

            dialog.show();

        }
        catch (Exception ex )
        {
            Toast.makeText(FeeDetailActivity.this, "Failed to download file", Toast.LENGTH_SHORT).show();
        }
    }
    private class AsyncTaskRunner extends AsyncTask<String, String, StudentFeeDetail> {
        private Context mContext;

        public AsyncTaskRunner (Context context){
            mContext = context;
        }

        @Override
        protected StudentFeeDetail doInBackground(String... params) {
            StudentFeeDetail stdObj;
            StudentFeeMonthService service = new StudentFeeMonthService(getApplication());
            stdObj = service.getStudentFeeDetail(mStudentId, monthId);
            return stdObj;
        }
        @Override
        protected void onPostExecute(StudentFeeDetail result) {
            if(result != null)
            {
                FeeByMonth data = result.getFeeByMonth().get(0);
                feeMonth.setText(monthName);
                feeTotalAmount.setText( data.getTotal().toString()==""?"0":data.getTotal().toString());
                feeAmountPaid.setText(data.getAmountReceived().toString());
                feeOutstandingAmountPaid.setText(data.getOutstanding().toString());
                feeDepositDate.setText(data.getSubmissionDate());

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
    public static Intent getOpenFileIntent(File file) {
        Uri uri = Uri.fromFile(file);
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String extension = "pdf";
        String type = mime.getMimeTypeFromExtension(extension);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, type);
        return intent;
    }
    BroadcastReceiver onComplete = new BroadcastReceiver() {

        public void onReceive(Context ctxt, Intent intent) {
            long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

            String CHANNEL_ID = "my_channel_01";
            if (referenceId > -1)
            {

                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(getApplicationContext(), "notify_001");
                if(Build.VERSION.SDK_INT>=24){
                    try{
                        Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                        m.invoke(null);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }

                int requestID = (int) System.currentTimeMillis();
                File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File file = new File(dir.getAbsolutePath()+"/" + mStudentId +".pdf");
                MimeTypeMap mime = MimeTypeMap.getSingleton();
                String ext=file.getName().substring(file.getName().indexOf(".")+1);
                String type = mime.getMimeTypeFromExtension(ext);
                Intent openFile = new Intent(Intent.ACTION_VIEW, Uri.fromFile(file));
                openFile.setDataAndType(Uri.fromFile(file), type);
                openFile.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                PendingIntent p = PendingIntent.getActivity(FeeDetailActivity.this, 0, openFile, 0);
                NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
                bigText.bigText("Download");
                bigText.setBigContentTitle("Download Voucher");
                bigText.setSummaryText("Reg No : "+mStudentId+" Voucher for month" + monthName);

                mBuilder.setContentIntent(p);
                mBuilder.setSmallIcon(R.drawable.logo);
                mBuilder.setContentTitle("Download Voucher Complete");
                mBuilder.setContentText("Reg No : "+mStudentId+" Voucher for month" + monthName);
                mBuilder.setPriority(Notification.PRIORITY_MAX);
                mBuilder.setStyle(bigText);

                NotificationManager mNotificationManager =
                        (NotificationManager) getApplication().getSystemService(Context.NOTIFICATION_SERVICE);


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel("notify_001",
                            "Channel human readable title",
                            NotificationManager.IMPORTANCE_DEFAULT);
                    mNotificationManager.createNotificationChannel(channel);
                }

                mNotificationManager.notify(0, mBuilder.build());
            }
        }
    };




    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            // permission granted
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        try
        {
            unregisterReceiver(onComplete);
        }
        catch (Exception ex)
        {
            //Toast.makeText(getApplicationContext(), ""+ex.getMessage(),Toast.LENGTH_SHORT).show();
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
                Intent i = new Intent(FeeDetailActivity.this, LoginActivity.class);
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
