package fr.tkeunebr.ic07;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		Toast.makeText(context, "startingService", Toast.LENGTH_SHORT).show();
		Intent serviceIntent = new Intent(context, AlarmService.class);
		context.startService(serviceIntent);
	}
}
