package fr.tkeunebr.ic07;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends Activity {
	private DatePicker mDatePicker;
	private TimePicker mTimePicker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
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
		Calendar calNow = Calendar.getInstance();
		Calendar calSet = (Calendar) calNow.clone();

		calSet.set(Calendar.HOUR_OF_DAY, mTimePicker.getCurrentHour());
		calSet.set(Calendar.MINUTE, mTimePicker.getCurrentMinute());
		calSet.set(Calendar.SECOND, 0);
		calSet.set(Calendar.MILLISECOND, 0);

		if (calSet.compareTo(calNow) <= 0) {
			//Today Set time passed, count to tomorrow
			calSet.add(Calendar.DATE, 1);
		}

		// TODO: use date + time to set alarm
		Toast.makeText(this, "Alarm is set@ " + calSet.getTime(), Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 1, intent, 0);
		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), pendingIntent);
	}
}
