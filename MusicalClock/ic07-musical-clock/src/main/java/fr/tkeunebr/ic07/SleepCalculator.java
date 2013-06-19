package fr.tkeunebr.ic07;

import java.util.Calendar;
import java.util.Date;

public class SleepCalculator {
	private static final int SLEEP_CYCLE_LENGTH_MILLIS = 90 * 60 * 1000;
	private final Calendar mCalendar;
	private final int mDeltaMinutes;

	public SleepCalculator(Calendar calendar, final int timeToFallAsleep) {
		mCalendar = calendar;
		mDeltaMinutes = timeToFallAsleep;
	}

	public Calendar computeWakeUpTime() {
		final long now = new Date().getTime();
		final long timeDiff = mCalendar.getTimeInMillis() - now;
		if (timeDiff > 0 && timeDiff > SLEEP_CYCLE_LENGTH_MILLIS) {
			final int deltaMillis = mDeltaMinutes * 60 * 1000;
			final int nbCycles = (int) (timeDiff - deltaMillis) / SLEEP_CYCLE_LENGTH_MILLIS;
			final long actualSleepTime = now + deltaMillis + (nbCycles * SLEEP_CYCLE_LENGTH_MILLIS);
			mCalendar.setTime(new Date(actualSleepTime));
		}
		return mCalendar;
	}
}
