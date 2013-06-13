package fr.tkeunebr.ic07;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.MenuItem;

import fr.tkeunebr.ic07.ui.custom.NumberPickerDialogPreference;

public class PreferencesActivity extends Activity {
    public static final String KEY_PREF_RINGTONE_STYLE = "pref_key_ringtone_style";
    public static final String KEY_PREF_TIME_TO_SLEEP = "pref_key_time_to_sleep";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        // Display the fragment as the main content.
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .replace(android.R.id.content, new SettingsFragment())
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public static class SettingsFragment extends PreferenceFragment {
        public SettingsFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preferences);
            final SharedPreferences sharedPreferences = getPreferenceManager().getSharedPreferences();
            if (sharedPreferences == null) {
                return;
            }
            final ListPreference musicStyle = (ListPreference) findPreference(KEY_PREF_RINGTONE_STYLE);
            if (musicStyle != null) {
                musicStyle.setSummary(sharedPreferences.getString(KEY_PREF_RINGTONE_STYLE,
                        getString(R.string.pref_music_random)));
                musicStyle.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                        preference.setSummary(String.valueOf(newValue));
                        return true;
                    }
                });
            }
            final NumberPickerDialogPreference timeToSleep = (NumberPickerDialogPreference)
                    findPreference(KEY_PREF_TIME_TO_SLEEP);
            if (timeToSleep != null) {
                final int value = sharedPreferences.getInt(KEY_PREF_TIME_TO_SLEEP,
                        Integer.parseInt(getString(R.string.pref_time_to_sleep_default)));
                timeToSleep.setSummary(String.valueOf(value));
                timeToSleep.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                        preference.setSummary(String.valueOf(newValue));
                        return true;
                    }
                });
            }
        }
    }
}
