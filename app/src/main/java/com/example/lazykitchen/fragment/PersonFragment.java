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
import android.widget.Toast;

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
import com.example.lazykitchen.util.GsonUtils;
import com.example.lazykitchen.util.VideoItem;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PersonFragment extends Fragment {

    private PersonViewModel mViewModel;
    private TextView yearAndMonth;
    final String[] weekItem={"日","一","二","三","四","五","六"};
    private List<String> dayItem = new ArrayList<String>();
    private List<BadgeItem> badgeItems = new ArrayList<>();
    AdapterDay adapterDay;
    Calendar calendar = Calendar.getInstance(Locale.CHINA);
    String signInInfoUrl="http://47.100.4.109:8080/sign_in_info";
    String signInUrl="http://47.100.4.109:8080/sign_in";

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
        for(int i=1;i<=days;i++) {
            if (i < 10)
                dayItem.add("0" + i);
            else
                dayItem.add("" + i);
        }
        loadSignInInfo();
    }

    private void loadSignInInfo() {
        OkHttpClient client = new OkHttpClient();
        long id=1;
        int month=6;
        signInInfoUrl=signInInfoUrl+"?id="+id+"&month="+month;
        Request request = new Request.Builder()
                .url(signInInfoUrl)
                .get()
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //...
                getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Toast toast = Toast.makeText(getActivity().getApplicationContext(), "获取签到信息失败！", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    Gson gson=new Gson();
                    Map map=gson.fromJson(response.body().string(),Map.class);
                    System.out.println(map.get("table").toString());
                    //处理UI需要切换到UI线程处理
                    getActivity().runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            // 更新逻辑写在这里
                        }
                    });
                }
            }
        });
    }

    // 点击签到按钮后调用
    private void signIn() {
        OkHttpClient client = new OkHttpClient();
        long id=1;
        FormBody.Builder builder=new FormBody.Builder();
        builder.add("id", String.valueOf(id));
        FormBody body=builder.build();
        Request request = new Request.Builder()
                .url(signInUrl)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //...
                getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Toast toast = Toast.makeText(getActivity().getApplicationContext(), "签到失败！", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    Gson gson=new Gson();
                    Map map=gson.fromJson(response.body().string(),Map.class);
                    // code=0->成功 code=-1->失败
                    System.out.println(map.get("code").toString());
                    //处理UI需要切换到UI线程处理
                    getActivity().runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            // 更新逻辑写在这里
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PersonViewModel.class);
        // TODO: Use the ViewModel
    }

}