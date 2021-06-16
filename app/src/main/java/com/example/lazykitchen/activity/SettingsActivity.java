package com.example.lazykitchen.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.example.lazykitchen.R;
import com.example.lazykitchen.fragment.SettingFragment;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.container, SettingFragment.class, null)
                    .commit();
        }

    }
}