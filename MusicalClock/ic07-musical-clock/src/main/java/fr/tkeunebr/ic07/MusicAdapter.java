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
		if (resources.getString(R.string.pref_music_classical).equals(musicStyle))  	{
			return R.raw.classic_short;
		}

		return R.raw.pop_short;
	}
}
