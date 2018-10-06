package com.app.adapters;

import android.app.Activity;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.app.oigs.R;
import com.app.models.Notify;
import com.app.models.StudentNotification;
import com.app.utils.Util;

import java.text.SimpleDateFormat;
import java.util.Date;


public class StudentNotificationAdapter extends ArrayAdapter<Notify> {
    private StudentNotification dataSet;
    private final Activity mContext;



    public StudentNotificationAdapter(StudentNotification data, Activity context) {
        super(context, R.layout.notification_layout, data.getNotify());
        this.dataSet = data;
        this.mContext=context;

    }


    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater= mContext.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.notification_layout, null,true);
        Notify obj = dataSet.getNotify().get(position);
        TextView titleText = (TextView) rowView.findViewById(R.id.tv_notification_title);
        TextView contentText = (TextView) rowView.findViewById(R.id.tv_notification_content);
        try {
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            Date date = format.parse(obj.getNotifyDate().toString().toLowerCase());
            String monthString  = (String) DateFormat.format("MMM",  date);
            String day          = (String) DateFormat.format("dd",   date); // 20
            titleText.setText(obj.getSubject().toUpperCase() +" | "+ Util.getDayFormat(Integer.parseInt(day), monthString));
            contentText.setText(obj.getText());
        }
        catch (Exception ex)
        {

        }

        return rowView;

    }


}