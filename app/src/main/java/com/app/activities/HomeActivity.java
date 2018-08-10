package com.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.app.adapters.StudentListAdapter;
import com.app.lyceum.american.americanlyceumapp.R;
import com.app.models.Student;
import com.app.models.StudentList;
import com.app.sessions.SessionManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class HomeActivity extends BaseActivity {
    private TextView mHeaderTitle;
    ListView studentList;
    StudentList list;
    private static String POPUP_CONSTANT = "mPopup";
    private static String POPUP_FORCE_SHOW_ICON = "setForceShowIcon";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        list = (StudentList)getIntent().getExtras().getParcelable("LIST");
        mHeaderTitle = (TextView) findViewById(R.id.tvHeaderTitle);
        mHeaderTitle.setText(getString(R.string.header_home));
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
                    i.setClass(HomeActivity.this, StudentActivity.class);
                    i.putExtra("student_id", std.student_id);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                }
            });
        }
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
        PopupMenu popup = new PopupMenu(HomeActivity.this, view);
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
                        startActivity(intent);
                        break;
                    case R.id.cmEmail:
                        Toast.makeText(getApplicationContext(), "Email" , Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.cmHome:
                        Toast.makeText(getApplicationContext(), "Home" , Toast.LENGTH_SHORT).show();
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
}
