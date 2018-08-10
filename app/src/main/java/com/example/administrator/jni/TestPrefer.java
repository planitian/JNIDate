package com.example.administrator.jni;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.annotation.Nullable;

public class TestPrefer extends PreferenceFragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.serial_port);
        getPreferenceManager().setSharedPreferencesName("SerialPortConfiguration");
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        switch (preference.getKey()){

        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }
}
