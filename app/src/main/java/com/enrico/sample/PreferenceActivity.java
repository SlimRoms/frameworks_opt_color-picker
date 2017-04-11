package com.enrico.sample;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v14.preference.PreferenceFragment;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.Preference;
import android.support.v7.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.widget.Toast;

import com.enrico.colorpicker.ColorPickerPreference;

@SuppressLint("NewApi")
public class PreferenceActivity extends AppCompatActivity {

    //ContextThemeWrapper
    ContextThemeWrapper themeWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //apply activity's theme if dark theme is enabled
        themeWrapper = new ContextThemeWrapper(getBaseContext(), this.getTheme());

        Preferences.applyTheme(themeWrapper, getBaseContext());

        setContentView(R.layout.preference_activity);

        //provide back navigation
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (getFragmentManager().findFragmentById(R.id.content_frame) == null) {

            SettingsFragment settingsFragment = new SettingsFragment();
            getFragmentManager().beginTransaction().replace(R.id.content_frame, settingsFragment).commit();
        }

    }

    //close app
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {

            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onColorSelection(DialogFragment dialogFragment, int color) {

        int tag;

        tag = Integer.valueOf(dialogFragment.getTag());

        switch (tag) {

            //do something on color selection
            case 3:
                // colorDialog.setColorPreferenceSummary(firstPreference, color, PreferenceActivity.this, getResources());
                //colorDialog.setPickerColor(PreferenceActivity.this, 3, color);

                //do your shit here
                Toast.makeText(getBaseContext(), getResources().getString(R.string.selection) + tag + getResources().getString(R.string.is) + Integer.toHexString(color).toUpperCase(), Toast.LENGTH_SHORT)
                        .show();
                break;

            case 4:
                //colorDialog.setColorPreferenceSummary(secondPreference, color, PreferenceActivity.this, getResources());
                //colorDialog.setPickerColor(PreferenceActivity.this, 4, color);

                //do your shit here
                Toast.makeText(getBaseContext(), getResources().getString(R.string.selection) + tag + getResources().getString(R.string.is) + Integer.toHexString(color).toUpperCase(), Toast.LENGTH_SHORT)
                        .show();
                break;
        }

    }

    public static class SettingsFragment extends PreferenceFragment {

        //preferences
        static ColorPickerPreference firstPreference;
        static ColorPickerPreference secondPreference;

        private SharedPreferences.OnSharedPreferenceChangeListener mListenerOptions;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.info_pref);

            //get the preferences
            firstPreference = (ColorPickerPreference) findPreference("preferenceColor");
            secondPreference = (ColorPickerPreference) findPreference("preferenceColor2");

            //get preferences colors
            // int color = colorDialog.getPickerColor(getActivity(), 3);
            // int color2 = colorDialog.getPickerColor(getActivity(), 4);

            //set preferences colors
            // colorDialog.setColorPreferenceSummary(firstPreference, color, getActivity(), getResources());
            // colorDialog.setColorPreferenceSummary(secondPreference, color2, getActivity(), getResources());

            firstPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();

                    //associate dialog with selected preference
                    //       colorDialog.showColorPicker(appCompatActivity, 3);
                    return false;
                }
            });

            secondPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();

                    //associate dialog with selected preference
                    //   colorDialog.showColorPicker(appCompatActivity, 4);
                    return false;
                }
            });

            //initialize shared preference change listener
            //some preferences when enabled requires an app reboot
            mListenerOptions = new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences preftheme, String key) {

                    //on theme selection restart the app
                    if (key.equals(getResources().getString(R.string.pref_theme))) {
                        restartApp();
                    }
                }
            };
        }

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        }

        //register preferences changes
        @Override
        public void onResume() {
            super.onResume();

            //register preferences changes
            getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(mListenerOptions);
        }

        //unregister preferences changes
        @Override
        public void onPause() {
            getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(mListenerOptions);
            super.onPause();
        }

        //method to restart the app and apply the changes
        private void restartApp() {
            Intent newIntent = new Intent(getActivity(), MainActivity.class);
            newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(newIntent);
            getActivity().overridePendingTransition(0, 0);
            getActivity().finish();
        }
    }
}
