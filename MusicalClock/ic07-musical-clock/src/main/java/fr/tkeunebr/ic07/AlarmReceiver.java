package fr.tkeunebr.ic07;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		final Intent serviceIntent = new Intent(context, AlarmService.class);
		serviceIntent.putExtra(AlarmService.KEY_START_PLAYING, true);
		context.startService(serviceIntent);
	}
}
