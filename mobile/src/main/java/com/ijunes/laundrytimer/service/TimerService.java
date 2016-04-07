package com.ijunes.laundrytimer.service;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;

import com.ijunes.laundrytimer.MainActivity;
import com.ijunes.laundrytimer.R;

import timber.log.Timber;

public class TimerService extends Service {
    private static final String TAG = "StopwatchService";
    private static final int NOTIFICATION_ID = 1;
    // Timer to update the ongoing notification
    private final long mFrequency = 100;    // milliseconds
    private final int TICK_WHAT = 2;
    private Timer timer;
    private LocalBinder localBinder = new LocalBinder();
    private NotificationManager notificationManager;
    private NotificationCompat.Builder builder;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message m) {
            updateNotification();
            sendMessageDelayed(Message.obtain(this, TICK_WHAT), mFrequency);
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return localBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.d(TimerService.this.getClass().getName() + "created");

        timer = new Timer();
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        createNotification();
    }

    public void createNotification() {
        Timber.d("creating notification");

        int icon = R.drawable.ic_app_icon_256;
        CharSequence tickerText = "Stopwatch";
        long when = System.currentTimeMillis();
        builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(icon)
                .setContentText(tickerText)
                .setWhen(when)
                .setOngoing(true)
                .setAutoCancel(false)
                .setOnlyAlertOnce(true)
                .setContentIntent(
                        PendingIntent.getActivity(this, 10,
                                new Intent(this, MainActivity.class)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP),
                                0)
                );
        notificationManager.notify(NOTIFICATION_ID, builder.build());        //TODO: Large icon when bitmap is assigned
    }

    public void updateNotification() {

        // the next two lines initialize the Notification, using the configurations above
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    public void showNotification() {
        Timber.d("showing notification");

        updateNotification();
        mHandler.sendMessageDelayed(Message.obtain(mHandler, TICK_WHAT), mFrequency);
    }

    public void hideNotification() {
        Timber.d(TAG, "removing notification");

        notificationManager.cancel(NOTIFICATION_ID);
        mHandler.removeMessages(TICK_WHAT);
    }

    public void start() {
        Timber.d(TAG, "start");
        timer.startCycle();

        showNotification();
    }

    public void pause() {
        Timber.d(TAG, "pause");
        timer.pause();

    }

    private String formatElapsedTime(long now) {
        long hours = 0, minutes = 0, seconds = 0, tenths = 0;
        StringBuilder sb = new StringBuilder();

        if (now < 1000) {
            tenths = now / 100;
        } else if (now < 60000) {
            seconds = now / 1000;
            now -= seconds * 1000;
            tenths = (now / 100);
        } else if (now < 3600000) {
            hours = now / 3600000;
            now -= hours * 3600000;
            minutes = now / 60000;
            now -= minutes * 60000;
            seconds = now / 1000;
            now -= seconds * 1000;
            tenths = (now / 100);
        }

        if (hours > 0) {
            sb.append(hours).append(":")
                    .append(formatDigits(minutes)).append(":")
                    .append(formatDigits(seconds)).append(".")
                    .append(tenths);
        } else {
            sb.append(formatDigits(minutes)).append(":")
                    .append(formatDigits(seconds)).append(".")
                    .append(tenths);
        }

        return sb.toString();
    }

    private String formatDigits(long num) {
        return (num < 10) ? "0" + num : new Long(num).toString();
    }

    public class LocalBinder extends Binder {
        public TimerService getService() {
            return TimerService.this;
        }
    }


}
