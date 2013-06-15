package fr.tkeunebr.ic07;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

public class MusicAdapter {
	private final SharedPreferences mPrefs;
	private final Context mContext;

	public MusicAdapter(Context context) {
		mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		mContext = context;
	}

	public int getAlarmAsResource() {
		final Resources resources = mContext.getResources();
		final String musicStyle = mPrefs.getString(PreferencesActivity.KEY_PREF_RINGTONE_STYLE,
				resources.getString(R.string.pref_music_random));
		if (resources.getString(R.string.pref_music_classical).equals(musicStyle)) {
			return R.raw.classic_short;
		}
		if (resources.getString(R.string.pref_music_pop).equals(musicStyle)) {
			return R.raw.pop_short;
		}
		if (resources.getString(R.string.pref_music_rock).equals(musicStyle)) {
			return R.raw.rock_short;
		}

		final int timeToSleep = mPrefs.getInt(PreferencesActivity.KEY_PREF_TIME_TO_SLEEP,
				Integer.parseInt(resources.getString(R.string.pref_time_to_sleep_default)));

		return R.raw.pop_short;
	}
}
