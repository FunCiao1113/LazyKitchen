package com.example.lazykitchen.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.example.lazykitchen.R;

public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.addPreferencesFromResource(R.xml.settings);
    }
}