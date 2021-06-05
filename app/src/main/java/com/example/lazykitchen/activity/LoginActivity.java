package com.example.lazykitchen.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lazykitchen.R;
import com.example.lazykitchen.util.ActivityUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class LoginActivity extends AppCompatActivity {

    private EditText phone;

    private final String url="http://10.0.2.2:10243/common/user/send_verify_code";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActivityUtils.add(this.getClass().getSimpleName(),this);
        Button getCode = findViewById(R.id.getCode);
        CheckBox checkBox = findViewById(R.id.checkBox);
        phone = findViewById(R.id.phone);
        getCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneStr = phone.getText().toString();
                if (phoneStr.isEmpty()) {
                    Toast toast = Toast.makeText(LoginActivity.this, "请输入手机号码！", Toast.LENGTH_SHORT);
                    toast.show();
                } else if (phoneStr.length() != 11) {
                    Toast toast = Toast.makeText(LoginActivity.this, "请输入正确手机号码！", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    if (checkBox.isChecked()){
                        post(url,"phone",phoneStr);
                        Intent intent = new Intent(LoginActivity.this, VerifyActivity.class);
                        intent.putExtra("phone", phoneStr);
                        startActivity(intent);
                    }else{
                        Toast toast = Toast.makeText(LoginActivity.this, "请阅读用户协议！", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            }
        });
    }

    public void post(String url,String key,String value){
        OkHttpClient client = new OkHttpClient();
        FormBody body = new FormBody.Builder()
                .add(key,value)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //...
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Toast toast = Toast.makeText(LoginActivity.this, "验证码发送失败！", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    String result = response.body().string();
                    //处理UI需要切换到UI线程处理
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Toast toast = Toast.makeText(LoginActivity.this, "验证码发送成功！", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });
                }
            }
        });
    }
}

