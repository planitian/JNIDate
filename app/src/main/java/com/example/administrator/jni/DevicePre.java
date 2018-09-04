package com.example.administrator.jni;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android_serialport_api.SerialPort;
import android_serialport_api.SerialPortFinder;

public class DevicePre extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
    private SerialPortFinder mSerialPortFinder;
    private Context context;
    private String[] entries;
    private String[] entryValues;

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
        ListPreference baudrates = (ListPreference) findPreference("electronic_baudrates");
        ListPreference lightBaudrates = (ListPreference) findPreference("light_baudrates");
        ListPreference lightDevices = (ListPreference) findPreference("light_port");
        entries = mSerialPortFinder.getAllDevices();
        entryValues = mSerialPortFinder.getAllDevicesPath();
        System.out.println(this.getClass().getSimpleName() + "  " + entries.toString());
        eleDevices.setEntries(entries);
        eleDevices.setEntryValues(entryValues);
        eleDevices.setSummary(eleDevices.getValue());
        eleDevices.setOnPreferenceChangeListener(this);


        baudrates.setSummary(baudrates.getValue());
        baudrates.setOnPreferenceChangeListener(this);

        lightDevices.setEntries(entries);
        lightDevices.setEntryValues(entryValues);
        lightDevices.setSummary(lightDevices.getValue());
        lightDevices.setOnPreferenceChangeListener(this);
        lightBaudrates.setSummary(lightBaudrates.getValue());
        lightBaudrates.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        preference.setSummary(newValue.toString());
        return true;
    }

    public String[] notRepetition(String[] content, String pp) {
        int len = content.length;
        String[] temp = new String[content.length - 1];
        for (int i = 0; i < content.length; i++) {
            if (content[i].equals(pp)) {
                for (int n = i; n < len; n++) {
                    content[n] = content[n + 1];
                }
                break;
            }
        }
        temp = Arrays.copyOf(content, len - 1);
        return temp;
    }
}

