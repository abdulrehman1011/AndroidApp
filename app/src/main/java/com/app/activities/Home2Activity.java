package com.app.activities;

import android.app.ActionBar;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.app.adapters.StudentListAdapter;
import com.app.lyceum.american.americanlyceumapp.R;
import com.app.models.Student;
import com.app.models.StudentList;
import com.app.sessions.SessionManager;
import com.app.utils.ImageHolder;
import com.onesignal.OneSignal;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class Home2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ListView studentList;
    StudentList list;
    SessionManager session = null;
    ImageView mLogoImage, mSideMenuLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_home2);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_home);

            //Drawable drawable = ContextCompat.getDrawable(getApplicationContext(),R.drawable.arrow_icon);
            toolbar.setOverflowIcon(null);
            toolbar.setContentInsetsAbsolute(0,0);
            toolbar.setContentInsetStartWithNavigation(0);
            String co = ImageHolder.getHeaderColor();
            toolbar.setBackgroundColor(Color.parseColor(ImageHolder.getHeaderColor()));
            toolbar.setTitleTextColor(Color.parseColor(ImageHolder.getHeaderTextColor()));
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
            //Text color

            try {

                ColorStateList colorStateList = ColorStateList.valueOf(Color.parseColor(ImageHolder.getMenuTextColor()));

                navigationView.setItemTextColor(colorStateList);
                // end
                //Set Item back ground
                ColorDrawable cd = new ColorDrawable(Color.parseColor(ImageHolder.getMenuItemBgColor()));

                navigationView.setItemBackground(cd);
                //end
                //Set background
                navigationView.setBackgroundColor(Color.parseColor(ImageHolder.getMenuMainBackgroundColor()));
                //end
                //header nav
                View headerLayout = navigationView.getHeaderView(0);
                headerLayout.setBackgroundColor(Color.parseColor(ImageHolder.getSideMennuLogoBackgroundColor()));
                mSideMenuLogo = (ImageView)headerLayout.findViewById(R.id.side_menu_logo) ;
                //end
                mLogoImage = (ImageView)findViewById(R.id.home_logo_image) ;
                if(!ImageHolder.getLogoUrl().equalsIgnoreCase(""))
                {
                    String pic = ImageHolder.getLogoUrl();
                    Picasso.get().load(ImageHolder.getLogoUrl()).into(mLogoImage);
                   // Picasso.get().load(ImageHolder.getLogoUrl()).into(mSideMenuLogo);
                    //mLogoImage.setImageBitmap(ImageHolder.getBitmap("logo"));
                    //mSideMenuLogo.setImageBitmap(ImageHolder.getBitmap("logo"));
                }
                else
                {
                    mLogoImage.setImageResource(R.drawable.logo);
                    mSideMenuLogo.setImageResource(R.drawable.logo);
                }

            }catch (Exception ex)
            {

            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.parseColor(ImageHolder.getStatusbarColor()));
            }
            OneSignal.clearOneSignalNotifications();
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
                            i.putExtra("student_id", std.getStudentId());
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
        catch ( Exception ex)
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
            i.putExtra("student_id", list.getStudents().get(0).getStudentId());
            startActivity(i);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        } else if (id == R.id.nav_notifications) {
            Intent i =  new Intent();
            i.setClass(this, NotificationActivity.class);
            i.putExtra("student_id", list.getStudents().get(0).getStudentId());
            startActivity(i);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        } else if (id == R.id.nav_student_gallery) {

        } else if (id == R.id.nav_rateus) {

            //Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
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
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse(rateUrl)));
                }
            }


        } else if (id == R.id.nav_logout) {
            session.logoutUser();
            Intent i = new Intent(this, LoginActivity.class);
            // set the new task and clear flags
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            finish();
            startActivity(i);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void onClickRightMenu(View item) {
        Intent i =  new Intent();
        i.setClass(this, NotificationActivity.class);
        i.putExtra("student_id", list.getStudents().get(0).getStudentId());
        startActivity(i);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
}
