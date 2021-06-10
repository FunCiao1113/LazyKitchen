package com.example.lazykitchen.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lazykitchen.R;

public class AdapterWeek extends BaseAdapter {

    private String[] week;
    private Context context;

    public AdapterWeek(String[] week){
        this.week = week;
    }

    @Override
    public int getCount() {
        return week.length;
    }

    @Override
    public Object getItem(int position) {
        return week[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewholder = null;
        if (convertView == null) {
            convertView =LayoutInflater.from(parent.getContext()).inflate(R.layout.week_item, parent,false);
            viewholder= new ViewHolder();
            viewholder.textView = (TextView) convertView.findViewById(R.id.weekItem);
            convertView.setTag(viewholder);// 如果convertView为空就 把holder赋值进去
        } else {
            viewholder = (ViewHolder) convertView.getTag();// 如果convertView不为空，那么就在convertView中getTag()拿出来
        }
        String num = week[position];
        DateUtil dateUtil = new DateUtil();
        viewholder.textView.setText(num);
        return convertView;
    }

    static class ViewHolder {
        TextView textView;
    }

}
