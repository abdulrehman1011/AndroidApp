package com.app.adapters;

import android.app.Activity;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;

import com.app.oigs.R;
import com.app.models.FeeMonth;
import com.app.models.Student;
import com.app.models.StudentFee;

public class StudentFeeAdapter extends ArrayAdapter<FeeMonth> {
    private StudentFee dataSet;
    private final Activity mContext;



    public StudentFeeAdapter(StudentFee data, Activity context) {
        super(context, R.layout.row_fee_item, data.getFeeMonth());
        this.dataSet = data;
        this.mContext=context;

    }


    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater= mContext.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.row_fee_item, null,true);
        FeeMonth obj = dataSet.getFeeMonth().get(position);
        Button titleText = (Button) rowView.findViewById(R.id.fee_list_row_btn);
        titleText.setText(obj.getMonthName());
        return rowView;

    }


}
