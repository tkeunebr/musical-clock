package fr.tkeunebr.ic07;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class MainActivity extends Activity {
    private DatePicker mDatePicker;
    private TimePicker mTimePicker;
    private SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
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
        // Fetch the user's preferences
        final String music = mPrefs.getString(PreferencesActivity.KEY_PREF_RINGTONE_STYLE,
                getString(R.string.pref_music_random));
        Toast.makeText(this, music, Toast.LENGTH_SHORT).show();


        final Calendar calSet = Calendar.getInstance();

        calSet.set(Calendar.YEAR, mDatePicker.getYear());
        calSet.set(Calendar.MONTH, mDatePicker.getMonth());
        calSet.set(Calendar.DAY_OF_MONTH, mDatePicker.getDayOfMonth());
        calSet.set(Calendar.HOUR_OF_DAY, mTimePicker.getCurrentHour());
        calSet.set(Calendar.MINUTE, mTimePicker.getCurrentMinute());
        calSet.set(Calendar.SECOND, 0);
        calSet.set(Calendar.MILLISECOND, 0);

        Crouton.makeText(this, "Alarm is set the " + calSet.get(Calendar.DAY_OF_MONTH) + " @ " + calSet.getTime(),
                Style.INFO).show();
        final AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(),
                PendingIntent.getBroadcast(this, 1, new Intent(this, AlarmReceiver.class), 0));
    }

    @Override
    protected void onDestroy() {
        Crouton.clearCroutonsForActivity(this);

        super.onDestroy();
    }
}
