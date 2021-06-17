package com.example.lazykitchen.fragment;

import androidx.lifecycle.ViewModelProvider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.vod.upload.VODUploadCallback;
import com.alibaba.sdk.android.vod.upload.VODUploadClient;
import com.alibaba.sdk.android.vod.upload.VODUploadClientImpl;
import com.alibaba.sdk.android.vod.upload.model.UploadFileInfo;
import com.alibaba.sdk.android.vod.upload.model.VodInfo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.lazykitchen.R;
import com.example.lazykitchen.activity.HeadActivity;
import com.example.lazykitchen.activity.SettingsActivity;
import com.example.lazykitchen.util.AdapterBadge;
import com.example.lazykitchen.util.AdapterDay;
import com.example.lazykitchen.util.AdapterWeek;
import com.example.lazykitchen.util.BadgeItem;
import com.example.lazykitchen.util.DateUtil;
import com.example.lazykitchen.util.MyGridView;
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
    private TextView record;
    private TextView idView;
    private ImageView imageView;
    private Boolean[] badges = {true, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false};
    private Button signIn;
    private int cnt = 0;
    private MyGridView day;
    final String[] weekItem = {"日", "一", "二", "三", "四", "五", "六"};
    private List<String> dayItem = new ArrayList<String>();
    private ArrayList<Boolean> flag = new ArrayList<>();
    private List<BadgeItem> badgeItems = new ArrayList<>();
    AdapterDay adapterDay;
    Calendar calendar = Calendar.getInstance(Locale.CHINA);
    String signInInfoUrlPrefix = "http://47.100.4.109:8080/sign_in_info";
    String signInUrl = "http://47.100.4.109:8080/sign_in";
    LocalBroadcastManager broadcastManager;
    LocalReceiver localReceiver;

    private static final String HEAD_CHANGE = "head_change";//头像更新

    class LocalReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Toast.makeText(context, "收到了本地发出的广播", Toast.LENGTH_SHORT).show();
            Uri imageUrl = Uri.parse(intent.getStringExtra("imageUrl"));
            RequestOptions options = new RequestOptions()
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.baseline_edit_off_red_300_24dp)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .override(720, 720);
            Glide.with(imageView.getContext())
                    .load(imageUrl)
                    .apply(options)
                    .fitCenter()
                    .into(imageView);
        }
    }

    private void receiveHeadChange() {
        broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(HEAD_CHANGE);
        localReceiver = new LocalReceiver();
        broadcastManager.registerReceiver(localReceiver, intentFilter);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.person_fragment, container, false);
        yearAndMonth = view.findViewById(R.id.yearAndMonth);
        System.out.println(calendar);
        initial(calendar);
        record = view.findViewById(R.id.record);
        idView = view.findViewById(R.id.id);
        loadIdAndUserName();
        loadUserHead();
        record.setText("该月已签到" + cnt + "天");
        GridView week = view.findViewById(R.id.week);
        AdapterWeek adapterWeek = new AdapterWeek(weekItem);
        week.setAdapter(adapterWeek);
        day = view.findViewById(R.id.day);
        ImageButton settings = view.findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
            }
        });
        imageView = view.findViewById(R.id.head_image);
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
                calendar.add(Calendar.MONTH, -1);
                dayItem.clear();
                flag.clear();
                initial(calendar);
                //adapterDay.notifyDataSetChanged();
            }
        });
        ImageButton right = view.findViewById(R.id.right);
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, 1);
                dayItem.clear();
                flag.clear();
                initial(calendar);
                //adapterDay.notifyDataSetChanged();
            }
        });
        signIn = view.findViewById(R.id.signIn);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        RecyclerView recyclerView = view.findViewById(R.id.achievement);
        initialBadge();
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        AdapterBadge adapterBadge = new AdapterBadge(badgeItems);
        recyclerView.setAdapter(adapterBadge);
        receiveHeadChange();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadIdAndUserName();
        loadUserHead();
    }

    public void loadIdAndUserName() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        idView.setText("ID:" + prefs.getString("ID", "0001") + "\n\n" +
                "用户名:" + prefs.getString("name", "厨房新人"));
    }

    public void loadUserHead() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String id = prefs.getString("ID", "1");
    }

    public void initialBadge() {
        for (int i = 0; i < 19; i++) {
            int j = i*2;
            if (badges[i]) {
                BadgeItem badgeItem = new BadgeItem(getBadge(j), "" + i);
                badgeItems.add(badgeItem);
            }
            else{
                BadgeItem badgeItem = new BadgeItem(getBadge(j+1), "" + i);
                badgeItems.add(badgeItem);
            }
        }
    }

    public int getBadge(int i) {
        switch (i) {
            case 0:
                return R.drawable.badge0;
            case 1:
                return R.drawable.badge1;
            case 2:
                return R.drawable.badge2;
            case 3:
                return R.drawable.badge3;
            case 4:
                return R.drawable.badge4;
            case 5:
                return R.drawable.badge5;
            case 6:
                return R.drawable.badge6;
            case 7:
                return R.drawable.badge7;
            case 8:
                return R.drawable.badge8;
            case 9:
                return R.drawable.badge9;
            case 10:
                return R.drawable.badge10;
            case 11:
                return R.drawable.badge11;
            case 12:
                return R.drawable.badge12;
            case 13:
                return R.drawable.badge13;
            case 14:
                return R.drawable.badge14;
            case 15:
                return R.drawable.badge15;
            case 16:
                return R.drawable.badge16;
            case 17:
                return R.drawable.badge17;
            case 18:
                return R.drawable.badge18;
            case 19:
                return R.drawable.badge19;
            case 20:
                return R.drawable.badge20;
            case 21:
                return R.drawable.badge21;
            case 22:
                return R.drawable.badge22;
            case 23:
                return R.drawable.badge23;
            case 24:
                return R.drawable.badge24;
            case 25:
                return R.drawable.badge25;
            case 26:
                return R.drawable.badge26;
            case 27:
                return R.drawable.badge27;
            case 28:
                return R.drawable.badge28;
            case 29:
                return R.drawable.badge29;
            case 30:
                return R.drawable.badge30;
            case 31:
                return R.drawable.badge31;
            case 32:
                return R.drawable.badge32;
            case 33:
                return R.drawable.badge33;
            case 34:
                return R.drawable.badge34;
            case 35:
                return R.drawable.badge35;
            case 36:
                return R.drawable.badge36;
            case 37:
                return R.drawable.badge37;
            default:
                return R.drawable.app_icon_background;
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
        loadSignInInfo(calendar);
    }

    private void loadSignInInfo(Calendar calendar) {
        DateUtil dateUtil = new DateUtil(calendar);
        OkHttpClient client = new OkHttpClient();
        long id=1;
        int month=dateUtil.getMonth();
        System.out.println(month);
        String signInInfoUrl=signInInfoUrlPrefix+"?id="+id+"&month="+month;
        System.out.println(signInInfoUrl);
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
                    flag = (ArrayList<Boolean>) map.get("table");
                    cnt = (int)((double) map.get("cnt"));
                    //处理UI需要切换到UI线程处理
                    getActivity().runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Calendar tmp = Calendar.getInstance(Locale.CHINA);
                            if(flag.get(tmp.get(Calendar.DAY_OF_MONTH)-1)){
                                signIn.setText("已签到");
                                signIn.setEnabled(false);
                            }
                            adapterDay = new AdapterDay(dayItem,flag);
                            day.setAdapter(adapterDay);
                            record.setText("该月已签到"+cnt+"天");
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
                        Toast toast = Toast.makeText(getActivity(), "签到失败！", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    Gson gson=new Gson();
                    Map map=gson.fromJson(response.body().string(),Map.class);
                    System.out.println(map.get("code").toString());
                    //处理UI需要切换到UI线程处理
                    getActivity().runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            signIn.setText("已签到");
                            signIn.setEnabled(false);
                            Toast.makeText(getActivity(),"签到成功", Toast.LENGTH_SHORT).show();
                            dayItem.clear();
                            initial(calendar);
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