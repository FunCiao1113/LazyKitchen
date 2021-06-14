package com.example.lazykitchen.fragment;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lazykitchen.R;
import com.example.lazykitchen.activity.HeadActivity;
import com.example.lazykitchen.activity.HomeActivity;
import com.example.lazykitchen.activity.LoginActivity;
import com.example.lazykitchen.activity.SettingsActivity;
import com.example.lazykitchen.activity.VerifyActivity;
import com.example.lazykitchen.util.AdapterBadge;
import com.example.lazykitchen.util.AdapterDay;
import com.example.lazykitchen.util.AdapterWeek;
import com.example.lazykitchen.util.BadgeItem;
import com.example.lazykitchen.util.DateUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PersonFragment extends Fragment {

    private PersonViewModel mViewModel;
    private TextView yearAndMonth;
    final String[] weekItem={"日","一","二","三","四","五","六"};
    private List<String> dayItem = new ArrayList<String>();
    private List<BadgeItem> badgeItems = new ArrayList<>();
    AdapterDay adapterDay;
    Calendar calendar = Calendar.getInstance(Locale.CHINA);

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.person_fragment, container, false);
        yearAndMonth = view.findViewById(R.id.yearAndMonth);
        initial(calendar);
        GridView week = view.findViewById(R.id.week);
        AdapterWeek adapterWeek = new AdapterWeek(weekItem);
        week.setAdapter(adapterWeek);
        GridView day = view.findViewById(R.id.day);
        adapterDay = new AdapterDay(dayItem);
        day.setAdapter(adapterDay);
        ImageButton settings = view.findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
            }
        });
        ImageView imageView = view.findViewById(R.id.head_image);
        imageView.setImageResource(R.drawable.ic_baseline_face_24);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HeadActivity.class);
                startActivity(intent);
            }
        });
        ImageButton left = view.findViewById(R.id.left);
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH,-1);
                dayItem.clear();
                initial(calendar);
                adapterDay.notifyDataSetChanged();
            }
        });
        ImageButton right = view.findViewById(R.id.right);
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH,1);
                dayItem.clear();
                initial(calendar);
                adapterDay.notifyDataSetChanged();
            }
        });
        RecyclerView recyclerView = view.findViewById(R.id.achievement);
        initialBadge();
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        AdapterBadge adapterBadge = new AdapterBadge(badgeItems);
        recyclerView.setAdapter(adapterBadge);
        return view;
    }

    public void initialBadge(){
        for(int i=0;i<8;i++){
            BadgeItem badgeItem = new BadgeItem(R.drawable.ic_baseline_spa_24,""+i);
            badgeItems.add(badgeItem);
        }
    }

    public void initial(Calendar calendar){
        DateUtil dateUtil = new DateUtil(calendar);
        yearAndMonth.setText(dateUtil.getDate());
        int days = dateUtil.getHowManyDays();
        int blank = dateUtil.getWhichDay();
        for(int i=1;i<blank;i++)
            dayItem.add("");
        for(int i=1;i<=days;i++)
            if(i<10)
                dayItem.add("0"+i);
            else
                dayItem.add(""+i);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PersonViewModel.class);
        // TODO: Use the ViewModel
    }

}