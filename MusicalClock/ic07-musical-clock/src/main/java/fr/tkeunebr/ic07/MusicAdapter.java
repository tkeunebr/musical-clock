package fr.tkeunebr.ic07;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import java.util.Random;

public class MusicAdapter {
	private static final int[] sMusicFiles = new int[]{
			R.raw.classic_short,
			R.raw.pop_short,
			R.raw.rock_short
	};
	private final Random mRandom = new Random();
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
			return sMusicFiles[0];
		}
		if (resources.getString(R.string.pref_music_pop).equals(musicStyle)) {
			return sMusicFiles[1];
		}
		if (resources.getString(R.string.pref_music_rock).equals(musicStyle)) {
			return sMusicFiles[2];
		}
		if (resources.getString(R.string.pref_music_random).equals(musicStyle)) {
			return sMusicFiles[mRandom.nextInt(sMusicFiles.length)];
		}

		throw new IllegalStateException("Music preference is not set or contains wrong value");
	}
}
