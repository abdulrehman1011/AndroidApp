package com.app.activities;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.app.adapters.StudentListAdapter;
import com.app.interfaces.IServices;
import com.app.lyceum.american.americanlyceumapp.R;
import com.app.models.Student;
import com.app.models.StudentList;
import com.app.models.StudentLogout;
import com.app.network.Services;
import com.app.sessions.SessionManager;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;

public class Home2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private IServices service;
    ListView studentList;
    StudentList list;
    SessionManager session = null;
    private String mPlayerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_home);
        //Drawable drawable = ContextCompat.getDrawable(getApplicationContext(),R.drawable.arrow_icon);
        toolbar.setOverflowIcon(null);
        toolbar.setContentInsetsAbsolute(0,0);
        toolbar.setContentInsetStartWithNavigation(0);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        toolbar.setNavigationIcon(R.drawable.menu_icon);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_home);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();
        mPlayerId = status.getSubscriptionStatus().getUserId();
        session = new SessionManager(getApplicationContext());
        try {
            list = (StudentList)getIntent().getExtras().getParcelable("LIST");
            if(list != null)
            {
                StudentListAdapter adapter = new StudentListAdapter(list,this);
                studentList =(ListView)findViewById(R.id.listview_students);
                studentList.setAdapter(adapter);
                studentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Student std = list.getStudents().get(position);
                        Intent i =  new Intent();
                        i.setClass(Home2Activity.this, StudentActivity.class);
                        i.putExtra("student_id", std.student_id);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                    }
                });
            }
        }
        catch (Exception ex)
        {

        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finish();
            overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home2, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_school_details) {

            Intent i =  new Intent();
            i.setClass(this, StudentDetailsActivity.class);
            i.putExtra("student_id", list.getStudents().get(0).student_id);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        } else if (id == R.id.nav_notifications) {
            Intent i =  new Intent();
            i.setClass(this, NotificationActivity.class);
            i.putExtra("student_id", list.getStudents().get(0).student_id);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        } else if (id == R.id.nav_student_gallery) {

        } else if (id == R.id.nav_rateus) {
            try {
            String rateUrl = session.getValues("rate_url");
            if(!rateUrl.equalsIgnoreCase(""))
            {


                    Uri uri = Uri.parse(rateUrl);
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    // To count with Play market backstack, After pressing back button,
                    // to taken back to our application, we need to add following flags to intent.
                    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                            Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    startActivity(goToMarket);

            }
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
            }
            /*Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
            }*/

        } else if (id == R.id.nav_logout) {
            AsyncTaskRunner runner = new AsyncTaskRunner(this);
            runner.execute();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void onClickRightMenu(View item) {
        Intent i =  new Intent();
        i.setClass(this, NotificationActivity.class);
        i.putExtra("student_id", list.getStudents().get(0).student_id);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
    private class AsyncTaskRunner extends AsyncTask<String, String, StudentLogout> {
        private Context mContext;

        public AsyncTaskRunner (Context context){
            mContext = context;
        }

        @Override
        protected StudentLogout doInBackground(String... params) {

            StudentLogout stdObj;
            service = new Services(mContext,getApplication());
            stdObj = service.LogoutStudent(mPlayerId);
            return stdObj;
        }
        @Override
        protected void onPostExecute(StudentLogout result) {
            if(result != null && result.getLogout().equalsIgnoreCase("true"))
            {
                session.logoutUser();
                Intent i = new Intent(Home2Activity.this, LoginActivity.class);
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
