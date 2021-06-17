package com.example.lazykitchen.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lazykitchen.R;
import com.example.lazykitchen.util.ActivityUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class VerifyActivity extends AppCompatActivity {

    private TextView verifyView;

    private final String resend_url="http://10.0.2.2:10243/common/user/send_verify_code";
    private final String url="http://10.0.2.2:10243/common/user/verify";

    CountDownTimer timer = new CountDownTimer(10000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            verifyView.setText((millisUntilFinished / 1000) + "秒后可重发");
        }

        @Override
        public void onFinish() {
            verifyView.setEnabled(true);
            verifyView.setTextColor(Color.rgb(0,0,0));
            verifyView.setText("获取验证码");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        ActivityUtils.add(this.getClass().getSimpleName(),this);
        TextView ID = findViewById(R.id.ID);
        EditText code = findViewById(R.id.code);
        Intent intent = getIntent();
        String phone = intent.getStringExtra("phone");
        verifyView = findViewById(R.id.timer);
        ID.setText("已经给手机"+phone+"发送了验证信息");
        timer.start();
        verifyView.setTextColor(Color.rgb(115,115, 115));
        verifyView.setEnabled(false);
        Button login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(code.getText().toString().isEmpty()){
                    Toast toast = Toast.makeText(VerifyActivity.this, "请输入验证码！", Toast.LENGTH_SHORT);
                    toast.show();
                }else{
                    String phoneStr=getIntent().getStringExtra("phone");
                    String codeStr=code.getText().toString();
                    post(url,phoneStr,codeStr);
                }
            }
        });
        verifyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyView.setTextColor(Color.rgb(115,115, 115));
                resendPost(resend_url,"phone",phone);
                timer.start();
            }
        });
    }

    public void post(String url,String phone,String code){
        OkHttpClient client = new OkHttpClient();
        FormBody body = new FormBody.Builder()
                .add("phone",phone)
                .add("code",code)
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
                        Toast toast = Toast.makeText(VerifyActivity.this, "验证失败！", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    String result = response.body().string();
                    //处理UI需要切换到UI线程处理
                    Gson g = new Gson();
                    JsonObject obj = g.fromJson(result, JsonObject.class);
                    if(obj.get("code").toString().equals("0")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast toast = Toast.makeText(VerifyActivity.this, "验证成功！", Toast.LENGTH_SHORT);
                                toast.show();
                                Intent intent = new Intent(VerifyActivity.this,HomeActivity.class);
                                intent.putExtra("phone",phone);
                                startActivity(intent);
                                ActivityUtils.destory("LoginActivity");
                                ActivityUtils.destory("VerifyActivity");
                            }
                        });
                    }
                    else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast toast = Toast.makeText(VerifyActivity.this, "验证失败！", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });
                    }
                }
            }
        });
    }

    public void resendPost(String url,String key,String value){
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
                        Toast toast = Toast.makeText(VerifyActivity.this, "验证码发送失败！", Toast.LENGTH_SHORT);
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
                            Toast toast = Toast.makeText(VerifyActivity.this, "验证码发送成功！", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });
                }
            }
        });
    }
}