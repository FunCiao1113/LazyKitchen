package com.example.lazykitchen.fragment;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.example.lazykitchen.R;
import com.example.lazykitchen.util.AdapterDay;
import com.example.lazykitchen.util.AdapterWeek;
import com.example.lazykitchen.util.DateUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PersonFragment extends Fragment {

    private PersonViewModel mViewModel;
    private TextView yearAndMonth;
    private String[] weekItem={"日","一","二","三","四","五","六"};
    private List<String> dayItem = new ArrayList<String>();
    public static PersonFragment newInstance() {
        return new PersonFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.person_fragment, container, false);
        yearAndMonth = view.findViewById(R.id.yearAndMonth);
        initial();
        GridView week = view.findViewById(R.id.week);
        AdapterWeek adapterWeek = new AdapterWeek(weekItem);
        week.setAdapter(adapterWeek);
        GridView day = view.findViewById(R.id.day);
        AdapterDay adapterDay = new AdapterDay(dayItem);
        day.setAdapter(adapterDay);
        return view;
    }

    public void initial(){
        DateUtil dateUtil = new DateUtil();
        yearAndMonth.setText(dateUtil.getDate());
        int days = dateUtil.getHowManyDays();
        int blank = dateUtil.getWhichDay();
        for(int i=1;i<blank;i++)
            dayItem.add("");
        for(int i=1;i<=days;i++)
            dayItem.add(i+"");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PersonViewModel.class);
        // TODO: Use the ViewModel
    }

}