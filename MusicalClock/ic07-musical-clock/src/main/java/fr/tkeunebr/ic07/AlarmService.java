package fr.tkeunebr.ic07;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.Vibrator;

public class AlarmService extends Service {
	public static final String KEY_START_PLAYING = "start_playing";
	public static final String KEY_STOP_PLAYING = "stop_playing";
	private static final int NOTIFICATION_ID = 1;
	private MediaPlayer mPlayer;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent.hasExtra(KEY_STOP_PLAYING) && intent.getBooleanExtra(KEY_STOP_PLAYING, false)) {
			mPlayer.stop();
			stopSelf();
		} else if (intent.hasExtra(KEY_START_PLAYING) && intent.getBooleanExtra(KEY_START_PLAYING, false)) {
			final Context context = getApplicationContext();
			final Resources resources = context.getResources();

			final MusicAdapter adapter = new MusicAdapter(context);

			final Intent notifIntent = new Intent(this, MainActivity.class);
			notifIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

			final PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
					notifIntent,
					PendingIntent.FLAG_UPDATE_CURRENT);

			final Intent stopServiceIntent = new Intent(this, AlarmService.class);
			stopServiceIntent.putExtra(KEY_STOP_PLAYING, true);
			final PendingIntent serviceIntent = PendingIntent.getService(context, 0,
					stopServiceIntent,
					PendingIntent.FLAG_UPDATE_CURRENT);

			final Notification notification = new Notification.Builder(context)
					.setContentTitle(resources.getString(R.string.app_name))
					.setContentText("Wake up :-)")
					.setSmallIcon(R.drawable.ic_launcher)
					.addAction(R.drawable.ic_action_cancel, "Arrêter", serviceIntent)
					.setOngoing(true)
					.setContentIntent(pendingIntent)
					.setTicker("Wake up :-)")
					.build();

			startForeground(NOTIFICATION_ID, notification);

			final Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
			vibrator.vibrate(2000);

			mPlayer = MediaPlayer.create(context, adapter.getAlarmAsResource());
			mPlayer.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK);
			mPlayer.start();
		}

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
