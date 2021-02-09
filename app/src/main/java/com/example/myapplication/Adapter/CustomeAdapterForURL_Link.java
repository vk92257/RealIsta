package com.example.myapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
public class CustomeAdapterForURL_Link extends BaseAdapter {
    private Context context;

    @Override
    public int getCount() {
        return 0;
    }
    @Override
    public Object getItem(int position) {
        return position;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        convertView = LayoutInflater.from(context).inflate(R.layout.row, parent, false);
//        serialNum = convertView.findViewById(R.id.serailNumber);
//        name = convertView.findViewById(R.id.studentName);
//        contactNum = convertView.findViewById(R.id.mobileNum);
//        serialNum.setText(" " + arrayList.get(position).getNum());
//        name.setText(arrayList.get(position).getName());
//        contactNum.setText(arrayList.get(position).getMobileNumber());
        return convertView;
    }
}