package fr.tkeunebr.ic07;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.Vibrator;

public class AlarmService extends Service {
	private static final int NOTIFICATION_ID = 1;
	private MediaPlayer mPlayer;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		final Context context = getApplicationContext();

		final Intent notifIntent = new Intent(this, MainActivity.class);
		notifIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

		final PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				notifIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		final Notification notification = new Notification.Builder(context)
				.setContentTitle(context.getResources().getString(R.string.app_name))
				.setContentText("coucou Content") // Music name
				.setOngoing(true)
				.setContentIntent(pendingIntent)
				.setTicker("test")
				.setSmallIcon(R.drawable.ic_launcher)
				.build();

		startForeground(NOTIFICATION_ID, notification);

		final Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(2000);

		mPlayer = MediaPlayer.create(context, R.raw.classic_short_1);
		mPlayer.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK);
		mPlayer.start();

		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		if (mPlayer != null) {
			mPlayer.release();
			mPlayer = null;
		}
	}
}
