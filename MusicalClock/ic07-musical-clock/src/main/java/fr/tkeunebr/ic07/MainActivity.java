package fr.tkeunebr.ic07;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;

import de.keyboardsurfer.android.widget.crouton.Configuration;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import fr.tkeunebr.ic07.util.DateUtils;

public class MainActivity extends Activity {
	private final Configuration mCroutonConfiguration = new Configuration.Builder()
			.setDuration(Configuration.DURATION_LONG)
			.build();
	private DatePicker mDatePicker;
	private TimePicker mTimePicker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

		mDatePicker = (DatePicker) findViewById(R.id.date_picker);
		mTimePicker = (TimePicker) findViewById(R.id.time_picker);
		mTimePicker.setIs24HourView(DateFormat.is24HourFormat(this));
		mTimePicker.setCurrentHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_settings:
				startActivity(new Intent(this, PreferencesActivity.class));
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void setAlarm(View v) {
		final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		final Calendar calSet = Calendar.getInstance();

		calSet.set(Calendar.YEAR, mDatePicker.getYear());
		calSet.set(Calendar.MONTH, mDatePicker.getMonth());
		calSet.set(Calendar.DAY_OF_MONTH, mDatePicker.getDayOfMonth());
		calSet.set(Calendar.HOUR_OF_DAY, mTimePicker.getCurrentHour());
		calSet.set(Calendar.MINUTE, mTimePicker.getCurrentMinute());
		calSet.set(Calendar.SECOND, 0);
		calSet.set(Calendar.MILLISECOND, 0);

		final int timeToFallAsleep = prefs.getInt(
				PreferencesActivity.KEY_PREF_TIME_TO_SLEEP,
				Integer.parseInt(getResources().getString(R.string.pref_time_to_sleep_default))
		);
		final SleepCalculator sleepCalculator = new SleepCalculator(calSet, timeToFallAsleep);
		sleepCalculator.computeWakeUpTime();

		final String msg;
		if (calSet.getTimeInMillis() < System.currentTimeMillis()) {
			msg = "L'alarme va sonner maintenant";
		} else {
			msg = "L'alarme sonnera le " + calSet.get(Calendar.DAY_OF_MONTH) + " @ " +
					DateUtils.format(calSet.getTime());
		}
		final Resources resources = getResources();
		new AlertDialog.Builder(this)
				.setMessage(msg)
				.setPositiveButton(resources.getString(android.R.string.ok), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						final AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
						alarmManager.set(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(),
								PendingIntent.getBroadcast(MainActivity.this, 1,
										new Intent(MainActivity.this, AlarmReceiver.class), 0));
						Crouton.makeText(MainActivity.this, msg, Style.INFO)
								.setConfiguration(mCroutonConfiguration).show();
					}
				})
				.setNegativeButton(resources.getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
					}
				})
				.show();
	}

	@Override
	protected void onDestroy() {
		Crouton.clearCroutonsForActivity(this);

		super.onDestroy();
	}
}
