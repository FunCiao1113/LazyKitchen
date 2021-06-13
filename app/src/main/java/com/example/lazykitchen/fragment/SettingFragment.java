package com.example.lazykitchen.fragment;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.example.lazykitchen.R;


public class SettingFragment extends PreferenceFragmentCompat {


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey);
    }

}
