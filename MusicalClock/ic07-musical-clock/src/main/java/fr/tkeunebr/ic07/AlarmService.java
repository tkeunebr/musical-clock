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
import android.widget.RemoteViews;

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
		final MusicAdapter adapter = new MusicAdapter(context);

		final Intent notifIntent = new Intent(this, MainActivity.class);
		notifIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

		final PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				notifIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		final RemoteViews view = new RemoteViews(getPackageName(), R.layout.notification_layout);
		final Notification notification = new Notification.Builder(context)
				.setSmallIcon(R.drawable.ic_launcher)
				.setOngoing(true)
				.setContentIntent(pendingIntent)
				.setTicker("Wake up :-)")
				.setContent(view)
				.build();

		startForeground(NOTIFICATION_ID, notification);

		final Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(2000);

		mPlayer = MediaPlayer.create(context, adapter.getAlarmAsResource());
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
