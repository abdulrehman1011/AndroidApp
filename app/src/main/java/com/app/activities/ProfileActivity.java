package com.app.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.interfaces.IServices;
import com.app.master.R;
import com.app.models.StudentDetail;
import com.app.models.StudentList;
import com.app.models.StudentLogout;
import com.app.network.Services;
import com.app.network.StudentDataService;
import com.app.sessions.SessionManager;
import com.app.utils.ImageHolder;
import com.app.utils.Util;
import com.google.gson.Gson;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;


import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileActivity extends BaseActivity {

    private String mStudentId;
    private TextView stdName;
    private TextView fatherName;
    private TextView motherName;
    private TextView className;
    private TextView contactNo;
    private TextView address;
    private TextView mHeaderTitle;
    CircleImageView imageView;
    FrameLayout overlay;
    private static String POPUP_CONSTANT = "mPopup";
    private static String POPUP_FORCE_SHOW_ICON = "setForceShowIcon";
    private static int RESULT_LOAD_IMAGE = 1;

    SessionManager session = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        if(getIntent().getExtras().getString("student_id") != null)
        {

            mStudentId = getIntent().getExtras().getString("student_id");
        }
        mHeaderTitle = (TextView) findViewById(R.id.tvHeaderTitle);
        mHeaderTitle.setText(getString(R.string.header_profile));
        try {
            mHeaderTitle.setTextColor(Color.parseColor(ImageHolder.getHeaderTextColor()));
            RelativeLayout rl = (RelativeLayout)findViewById(R.id.include);
            rl.setBackgroundColor(Color.parseColor(ImageHolder.getHeaderColor()));
        }
        catch (Exception ex)
        {

        }

        stdName = (TextView) findViewById(R.id.std_name);
        fatherName = (TextView) findViewById(R.id.father_name);
        motherName = (TextView) findViewById(R.id.mother_name);
        className = (TextView) findViewById(R.id.present_class);
        contactNo = (TextView) findViewById(R.id.contact);
        address = (TextView) findViewById(R.id.address);
        overlay = (FrameLayout) findViewById(R.id.progressBarHolder);
        imageView = (CircleImageView) findViewById(R.id.iv_logo_profile);
        session = new SessionManager(getApplicationContext());
        try {
            if(session.getValues("image_stdid") != null )
            {

                String str = session.getValues("image_stdid");
                String[] std = str.split("\\|");
                if(std[0].equalsIgnoreCase(mStudentId))
                {
                    Bitmap bmp = Util.decodeBase64(std[1]);
                    imageView.setImageResource(android.R.color.transparent);
                    imageView.setImageDrawable(null);
                    imageView.setImageBitmap(null);
                    imageView.destroyDrawingCache();
                    imageView.invalidate();
                    imageView.setImageBitmap(bmp);
                    imageView.invalidate();
                }
                else
                {
                    imageView.setImageResource(R.drawable.default_avatar);
                }

            }
            else
            {
                imageView.setBackgroundResource(R.drawable.default_avatar);
            }
        }
        catch (Exception ex)
        {

        }

        overlay.setVisibility(View.VISIBLE);
        AsyncTaskRunner task = new AsyncTaskRunner(this);
        task.execute();
    }

    @Override
    public void onBackPressed() {
        //Intent intent = new Intent();
        //intent.putExtra("student_id", mStudentId);
        //setResult(RESULT_OK, intent);
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
        PopupMenu popup = new PopupMenu(ProfileActivity.this, view);
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

                switch (item.getItemId()) {
                    case R.id.cmCall:
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        //intent.setData(Uri.parse("tel:" + "+880XXXXXXXXXXXX"));
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
                        AsyncLogoutTaskRunner runner = new AsyncLogoutTaskRunner(ProfileActivity.this);
                        runner.execute();
                        break;
                }
                return false;
            }
        });
        popup.show();
    }
    private class AsyncTaskRunner extends AsyncTask<String, String, StudentDetail> {
        private Context mContext;

        public AsyncTaskRunner (Context context){
            mContext = context;
        }

        @Override
        protected StudentDetail doInBackground(String... params) {
            StudentDetail stdObj;
            StudentDataService service = new StudentDataService(getApplication());
            stdObj = service.getStudentDetail(mStudentId);
            return stdObj;
        }
        @Override
        protected void onPostExecute(StudentDetail result) {
            if(result != null)
            {
                stdName.setText(result.getStudentName());
                fatherName.setText(result.getFatherName());
                motherName.setText(result.getMotherName());
                className.setText(result.getClass_());
                contactNo.setText(result.getEmergencyContact());
                address.setText(result.getAddress());

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
    public void onClickGetImage(View view) {
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            try {


                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                String ext = picturePath.substring(picturePath.lastIndexOf(".") + 1);
                session.addValues("image_stdid", mStudentId+"|"+Util.encodeTobase64(bitmap,ext));
                Bitmap bmp = Util.decodeBase64(Util.encodeTobase64(bitmap,ext));
                imageView.setImageResource(android.R.color.transparent);
                imageView.setImageDrawable(null);
                imageView.setImageBitmap(null);
                imageView.destroyDrawingCache();
                imageView.invalidate();
                imageView.setImageBitmap(bmp);
                imageView.invalidate();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Bitmap bmp = BitmapFactory.decodeFile(picturePath);
            //session.addValues("image",selectedImage);


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
                Intent i = new Intent(ProfileActivity.this, LoginActivity.class);
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
