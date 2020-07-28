package com.example.youtubesearchclient;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.prefs);
        EditTextPreference userSearch = findPreference(getString(R.string.pref_search_key));
        EditTextPreference userpref = findPreference(getString(R.string.pref_user_key));
        userpref.setSummary(userpref.getText());
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        // save user account information
        if (key.equals(getString(R.string.pref_user_key))) {
            EditTextPreference preference = findPreference(key);
            preference.setSummary(preference.getText());
        }

        if(key.equals(R.string.pref_search_key)) {
            EditTextPreference userSearch = findPreference(key);
            userSearch.setSummary(userSearch.getText());
        }
        if (key.equals(getString(R.string.pref_theme_key))){

            if (Preferences.getApp_Theme().equals("false")){
                Preferences.setApp_Theme("true");
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            } else if (Preferences.getApp_Theme().equals("true")) {
                Preferences.setApp_Theme("false");
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }



            int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
            switch (currentNightMode) {
                case Configuration.UI_MODE_NIGHT_NO:
                    // Night mode is not active, we're using the light theme
                    Log.d(TAG, "light");
                    break;
                case Configuration.UI_MODE_NIGHT_YES:
                    // Night mode is active, we're using dark theme
                    break;
            }

            Log.d(TAG, "Clicked " + key + " " + Preferences.getApp_Theme());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
