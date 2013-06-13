package fr.tkeunebr.ic07;

import android.app.Activity;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.MenuItem;

public class PreferencesActivity extends Activity {
    public static final String KEY_PREF_RINGTONE_STYLE = "pref_key_ringtone_style";

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

            final ListPreference musicStyle = (ListPreference) findPreference(KEY_PREF_RINGTONE_STYLE);
            musicStyle.setSummary(getPreferenceManager().getSharedPreferences().getString(KEY_PREF_RINGTONE_STYLE,
                    getString(R.string.pref_music_random)));
            musicStyle.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    musicStyle.setSummary(musicStyle.getValue());
                    return true;
                }

            });
        }
    }
}
