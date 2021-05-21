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

public class VerifyActivity extends AppCompatActivity {

    private TextView verifyView;
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
        TextView ID = findViewById(R.id.ID);
        EditText code = findViewById(R.id.code);
        Intent intent = getIntent();
        String str = intent.getStringExtra("data");
        verifyView = findViewById(R.id.timer);
        ID.setText("已经给手机"+str+"发送了验证信息");
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
                    Intent intent = new Intent(VerifyActivity.this,HomeActivity.class);
                    startActivity(intent);
                }
            }
        });
        verifyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyView.setTextColor(Color.rgb(115,115, 115));
                timer.start();
            }
        });
    }
}