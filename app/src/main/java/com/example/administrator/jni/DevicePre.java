package com.example.administrator.jni;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.annotation.Nullable;

import android_serialport_api.SerialPortFinder;

public class DevicePre extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
    private SerialPortFinder mSerialPortFinder;
    private Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.serial_port);
        mSerialPortFinder = ((MyApplication) getActivity().getApplication()).serialPortFinder;

        // Devices
        final ListPreference eleDevices = (ListPreference) findPreference("electronic_port");


        String[] entries = mSerialPortFinder.getAllDevices();
        String[] entryValues = mSerialPortFinder.getAllDevicesPath();
        System.out.println(this.getClass().getSimpleName() + "  " + entries.toString());
        eleDevices.setEntries(entries);
        eleDevices.setEntryValues(entryValues);
        eleDevices.setSummary(eleDevices.getValue());
        eleDevices.setOnPreferenceChangeListener(this);

        ListPreference baudrates=(ListPreference)findPreference("electronic_baudrates");
        baudrates.setSummary(baudrates.getValue());
        baudrates.setOnPreferenceChangeListener(this);

        ListPreference lightDevices=(ListPreference)findPreference("light_port");
        lightDevices.setEntries(entries);
        lightDevices.setSummary(lightDevices.getValue());
        lightDevices.setOnPreferenceChangeListener(this);

        ListPreference lightBaudrates=(ListPreference)findPreference("light_baudrates");
        lightBaudrates.setSummary(lightBaudrates.getValue());
        // Baud rates
//        final ListPreference baudrates = (ListPreference)findPreference("BAUDRATE");
//        baudrates.setSummary(baudrates.getValue());
//        baudrates.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
//            public boolean onPreferenceChange(Preference preference, Object newValue) {
//                preference.setSummary((String)newValue);
//                return true;
//            }
//        });
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
                preference.setSummary(newValue.toString());
        return true;
    }
}
