package com.app.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.app.lyceum.american.americanlyceumapp.R;
import com.app.models.ExamType;
import com.app.models.StudentExam;



public class StudentExamAdapter extends ArrayAdapter<ExamType> {

    private StudentExam dataSet;
    private final Activity mContext;

    public StudentExamAdapter(StudentExam data, Activity context) {
        super(context, R.layout.row_item_exam, data.getExamType());
        this.dataSet = data;
        this.mContext=context;

    }
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater= mContext.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.row_item_exam, null,true);
        Button titleText = (Button) rowView.findViewById(R.id.exam_list_row_btn);
        titleText.setText(dataSet.getExamType().get(position).getTestName());
        return rowView;

    }

}