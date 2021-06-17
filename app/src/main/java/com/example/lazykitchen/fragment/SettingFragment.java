package com.example.lazykitchen.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.example.lazykitchen.R;
import com.example.lazykitchen.activity.HomeActivity;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class SettingFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener {

    private EditTextPreference namePreference = null;
    private ListPreference sexPreference = null;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    String updateInfoUrl="http://47.100.4.109:8080/user/update_info";
    String[] sex=new String[]{"男","女","未知"};

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        namePreference = findPreference("name");
        sexPreference = findPreference("sex");
        namePreference.setOnPreferenceChangeListener(this);
        sexPreference.setOnPreferenceChangeListener(this);

        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(getActivity());
        namePreference.setSummary(prefs.getString("name","厨房新人"));
        sexPreference.setSummary(sex[Integer.parseInt(prefs.getString("sex","2"))]);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String newName = null;
        String newSex = null;
        if(preference==namePreference){
            newName=(String) newValue;
            //namePreference.setSummary(newName);
        }
        else if(preference==sexPreference){
            newSex=sex[Integer.parseInt((String) newValue)];
            //sexPreference.setSummary(newSex);
        }
        updateUserInfo(newName,newSex);
        return true;
    }

    public void updateUserInfo(String name,String sex){
        OkHttpClient client = new OkHttpClient();
        long id=1;
        JsonObject json=new JsonObject();
        json.addProperty("id",id);
        if(name!=null) {
            json.addProperty("name", name);
        }
        else if(sex!=null) {
            json.addProperty("gender", sex);
        }
        RequestBody body = RequestBody.create(JSON, json.toString());
        System.out.println(json.toString());
        Request request = new Request.Builder()
                .url(updateInfoUrl)
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
                        Toast toast = Toast.makeText(getActivity(), "修改用户信息失败", Toast.LENGTH_SHORT);
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
                            Toast toast = Toast.makeText(getActivity(), "修改用户信息成功", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });
                }
            }
        });
    }
}
