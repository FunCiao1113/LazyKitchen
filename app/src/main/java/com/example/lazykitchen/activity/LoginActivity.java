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

public class LoginActivity extends AppCompatActivity {

    private EditText phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button getCode = findViewById(R.id.getCode);
        CheckBox checkBox = findViewById(R.id.checkBox);
        phone = findViewById(R.id.phone);
        getCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = phone.getText().toString();
                if (str.isEmpty()) {
                    Toast toast = Toast.makeText(LoginActivity.this, "请输入手机号码！", Toast.LENGTH_SHORT);
                    toast.show();

                } else if (str.length() != 11) {
                    Toast toast = Toast.makeText(LoginActivity.this, "请输入正确手机号码！", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    if (checkBox.isChecked()){
                        Intent intent = new Intent(LoginActivity.this, VerifyActivity.class);
                        intent.putExtra("data", str);
                        startActivity(intent);
                    }else{
                        Toast toast = Toast.makeText(LoginActivity.this, "请阅读用户协议！", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            }
        });
    }
}

