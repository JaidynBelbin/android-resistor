package com.example.yardenbourg.resistor;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by Yardenbourg on 16/06/16.
 */
public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }
}
