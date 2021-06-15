package com.example.lazykitchen.util;

import android.graphics.Color;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lazykitchen.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AdapterDay extends BaseAdapter {

    private List<String> day;
    private ArrayList<Boolean> flag;
    private Boolean[] newFlags;

    public AdapterDay(List<String> day, ArrayList<Boolean> flag) {
        this.day = day;
        this.flag = flag;
        newFlags = new Boolean[day.size()];
        for(int i = 0,j=0;i<day.size();i++){
            if(day.get(i)=="")
                newFlags[i] = false;
            else{
                if(flag == null|| flag.size()==0)
                    newFlags[i] = false;
                else{
                    newFlags[i] = flag.get(j);
                    j++;
                }
            }
        }
    }

    @Override
    public int getCount() {
        return day.size();
    }

    @Override
    public Object getItem(int position) {
        return day.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewholder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_item, parent,false);
            viewholder= new AdapterDay.ViewHolder();
            viewholder.textView = (TextView) convertView.findViewById(R.id.dayItem);
            viewholder.imageView = (ImageView) convertView.findViewById(R.id.star);
            convertView.setTag(viewholder);// 如果convertView为空就 把holder赋值进去
        } else {
            viewholder = (AdapterDay.ViewHolder) convertView.getTag();// 如果convertView不为空，那么就在convertView中getTag()拿出来
        }
        String num = day.get(position);
        viewholder.textView.setText(num);
        viewholder.imageView.setVisibility(View.INVISIBLE);
        if (newFlags[position])
            viewholder.imageView.setVisibility(View.VISIBLE);
        return convertView;
    }

    static class ViewHolder {
        TextView textView;
        ImageView imageView;
    }
}
