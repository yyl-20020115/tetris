package com.noc.tet.activities;

import com.noc.tet.R;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.media.AudioManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.view.MenuItem;

public class SettingsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener, OnPreferenceClickListener {

    private String add_ms(String timeString) {
        return timeString + this.getString(R.string.ms);// " ms";
    }

    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.simple_preferences);

        ActionBar actionBar = getActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        Preference pref = findPreference("pref_advanced");
        pref.setOnPreferenceClickListener(this);

        pref = findPreference("pref_vibDurOffset");
        String timeString = PreferenceManager.getDefaultSharedPreferences(this).getString("pref_vibDurOffset", "");
        if (timeString.isEmpty())
            timeString = "0";
        timeString = add_ms(timeString);
        pref.setSummary(timeString);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        assert key != null;
        if (key.equals("pref_vibDurOffset")) {
            Preference connectionPref = findPreference(key);
            // Set summary to be the user-description for the selected value
            String timeString = sharedPreferences.getString(key, "");
            if (timeString.isEmpty())
                timeString = "0";
            timeString = add_ms(timeString);
            connectionPref.setSummary(timeString);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        Intent intent = new Intent(this, AdvancedSettingsActivity.class);
        startActivity(intent);
        return true;
    }
}
