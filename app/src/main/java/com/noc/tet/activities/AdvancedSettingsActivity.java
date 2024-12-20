package com.noc.tet.activities;

import com.noc.tet.R;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.media.AudioManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.MenuItem;

public class AdvancedSettingsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {

    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.advanced_preferences);
        ActionBar actionBar = getActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        Preference pref = findPreference("pref_rng");
        pref.setSummary(PreferenceManager.getDefaultSharedPreferences(this).getString("pref_rng", "").equals("sevenbag") ? getResources().getStringArray(R.array.randomizer_preference_array)[0] : getResources().getStringArray(R.array.randomizer_preference_array)[1]);//"7-Bag-Randomization");
        pref = findPreference("pref_fpslimittext");
        pref.setSummary(PreferenceManager.getDefaultSharedPreferences(this).getString("pref_fpslimittext", ""));
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        assert key != null;
        if (key.equals("pref_rng")) {
            Preference connectionPref = findPreference(key);
            // Set summary to be the user-description for the selected value
            connectionPref.setSummary(sharedPreferences.getString(key, "").equals("sevenbag") ? getResources().getStringArray(R.array.randomizer_preference_array)[0] : getResources().getStringArray(R.array.randomizer_preference_array)[1]);//"7-Bag-Randomization");
        }
        if (key.equals("pref_fpslimittext")) {
            Preference connectionPref = findPreference(key);
            // Set summary to be the user-description for the selected value
            connectionPref.setSummary(sharedPreferences.getString(key, ""));
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
}
