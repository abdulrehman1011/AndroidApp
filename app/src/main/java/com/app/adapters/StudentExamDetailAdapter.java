package com.app.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.app.master.R;
import com.app.models.ExamResult;
import com.app.models.StudentExamDetail;


public class StudentExamDetailAdapter extends ArrayAdapter<ExamResult> {

    private StudentExamDetail dataSet;
    private final Activity mContext;

    public StudentExamDetailAdapter(StudentExamDetail data, Activity context) {
        super(context, R.layout.row_item_exam_detail, data.getExamResults());
        this.dataSet = data;
        this.mContext=context;

    }
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater= mContext.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.row_item_exam_detail, null,true);
        TextView marksObt = (TextView) rowView.findViewById(R.id.tv_marks_obt);
        TextView subjectName = (TextView) rowView.findViewById(R.id.tv_subject_name);
        TextView totalMarks = (TextView) rowView.findViewById(R.id.tv_total_marks);
        marksObt.setText(dataSet.getExamResults().get(position).getStdObtainMark().toString());
        subjectName.setText(dataSet.getExamResults().get(position).getSubjectName());
        totalMarks.setText(dataSet.getExamResults().get(position).getStdTotalMark().toString());
        return rowView;

    }

}