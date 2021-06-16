package com.example.lazykitchen.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceManager;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.example.lazykitchen.R;
import com.example.lazykitchen.util.ActivityUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HomeActivity extends AppCompatActivity {

    String userDataUrlPrefix="http://47.100.4.109:8080/user/info";
    Intent intent;
    private String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        intent=getIntent();
        syncLocalUserData();
        this.getSupportActionBar().hide();
        ActivityUtils.add(this.getClass().getSimpleName(),this);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavController navController;
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        navController = navHostFragment.getNavController();
        AppBarConfiguration configuration = new AppBarConfiguration.Builder(R.id.searchFragment,R.id.shareFragment,R.id.personFragment).build();
        NavigationUI.setupActionBarWithNavController(this,navController,configuration);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }

    public void syncLocalUserData(){
        OkHttpClient client = new OkHttpClient();
        //String phone=intent.getStringExtra("phone");
        String phone="17321050252";
        String userDataUrl=userDataUrlPrefix+"?phone="+phone;
        Request request = new Request.Builder()
                .url(userDataUrl)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Toast toast = Toast.makeText(HomeActivity.this, "获取用户信息失败", Toast.LENGTH_SHORT);
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
                    String name=((Map)map.get("userInfo")).get("name").toString();
                    String sex=((Map)map.get("userInfo")).get("gender").toString();
                    //处理UI需要切换到UI线程处理
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            // 更新逻辑写在这里
                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
                            prefs.edit().putString("name",name).apply();
                            String sexCode="2";
                            if(sex.equals("男")) {
                                sexCode = "0";
                            }
                            else if (sex.equals("女")){
                                sexCode= "1";
                            }
                            prefs.edit().putString("sex",sexCode).apply();
                        }
                    });
                }
            }
        });
    }
}