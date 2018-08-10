package com.app.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.app.lyceum.american.americanlyceumapp.R;
import com.app.models.Student;
import com.app.models.StudentList;

public class StudentListAdapter extends ArrayAdapter<Student>{

    private StudentList dataSet;
    private final Activity mContext;
    private static class ViewHolder {
        TextView studentName;

    }
    public StudentListAdapter(StudentList data, Activity context) {
        super(context, R.layout.row_item, data.getStudents());
        this.dataSet = data;
        this.mContext=context;

    }
    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater= mContext.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.row_item, null,true);

        TextView titleText = (TextView) rowView.findViewById(R.id.list_item_text);
        titleText.setText(dataSet.getStudents().get(position).getStudent_name());
        return rowView;

    }

}
