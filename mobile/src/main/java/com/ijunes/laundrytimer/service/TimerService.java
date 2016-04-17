package com.ijunes.laundrytimer.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.widget.RemoteViews;

import com.ijunes.laundrytimer.MainActivity;
import com.ijunes.laundrytimer.R;

import java.util.HashMap;

import timber.log.Timber;

public class TimerService extends Service {
    public static final String START_ACTION = "com.ijunes.laundrytimer.timer.START";
    public static final String PAUSE_ACTION = "com.ijunes.laundrytimer.timer.PAUSE";
    public static final String RESTART_ACTION = "com.ijunes.laundrytimer.timer.RESTART";
    public static final String STOP_ACTION = "com.ijunes.laundrytimer.timer.STOP";
    public static final String UPDATE_BROADCAST = "com.ijunes.laundrytimer.timer.UPDATE";
    private static final String TAG = "LaundryTimerApp";
    private static final int NOTIFICATION_ID = 1;
    private final long mFrequency = 100;    // milliseconds
    private final int UPDATE_ID = 2;
    private Timer timer;
    private LocalBinder localBinder = new LocalBinder();
    private NotificationManager notificationManager;
    private NotificationCompat.Builder builder;

    private CountDownTimer countDownTimer = null;

    private HashMap<Long, Integer> inProgress = new HashMap<>(2);
    private Handler mHandler = new Handler() {
        public void handleMessage(Message m) {
            updateNotification();
            sendMessageDelayed(Message.obtain(this, UPDATE_ID), mFrequency);
        }
    };

    private BroadcastReceiver timerReceiver;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return localBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.d(TimerService.this.getClass().getName() + "created");

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        final IntentFilter filter = new IntentFilter();
        filter.addAction(START_ACTION);
        filter.addAction(PAUSE_ACTION);
        filter.addAction(RESTART_ACTION);
        filter.addAction(STOP_ACTION);
        timerReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Timber.d("Received intent" + intent.getAction());
                switch (intent.getAction()) {
                    case START_ACTION:
                        start(intent.getExtras().getLong("WASHID"), intent.getExtras().getLong("DURATION"));
                        break;
                    case PAUSE_ACTION:
                        pause();
                        break;
                    case RESTART_ACTION:
                        restart();
                        break;
                    case STOP_ACTION:
                        stop();
                        break;
                    default:
                        break;
                }
            }
        };
        registerReceiver(timerReceiver, filter);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void createNotification() {
        Timber.d("creating notification");

        int icon = R.drawable.ic_app_icon_256;
        long when = System.currentTimeMillis();

        Intent stopIntent = new Intent(STOP_ACTION);
        PendingIntent stopPendingIntent = PendingIntent.getBroadcast(this,
                0, stopIntent, 0);

        RemoteViews remoteViews = new RemoteViews("com.ijunes.laundrytimer", R.layout.notification_timer_view);
        builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(icon)
                .setContent(remoteViews)
                .setWhen(when)
                .setOngoing(true)
                .setAutoCancel(false)
                .setOnlyAlertOnce(true)
                .setUsesChronometer(false)
                .setContentIntent(
                        PendingIntent.getActivity(this, 10,
                                new Intent(this, MainActivity.class)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP),
                                0)
                )
                .addAction(R.drawable.ic_action_stop, getString(R.string.stop),
                        stopPendingIntent);

        notificationManager.notify(NOTIFICATION_ID, builder.build());        //TODO: Large icon when bitmap is assigned
    }

    public void updateNotification() {
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    public void showNotification() {
        Timber.d("showing notification");

        updateNotification();
        mHandler.sendMessageDelayed(Message.obtain(mHandler, UPDATE_ID), mFrequency);
    }

    public void hideNotification() {
        Timber.d(TAG, "removing notification");

        notificationManager.cancel(NOTIFICATION_ID);
        mHandler.removeMessages(UPDATE_ID);
    }

    public void start(final long washId, long durationMillis) {
        Timber.d(TAG, "start");
        timer = new Timer(Timer.State.WASH);
        int tick = durationMillis > 60000 ? 2000 : 1000;
        countDownTimer = new CountDownTimer(durationMillis, tick) {
            @Override
            public void onTick(long millisUntilFinished) {
                try {
                    long timeElapsed = 600000 - millisUntilFinished;
                    float percentFinished = ((float) timeElapsed / (float) 600000) * (float) 100;
                    inProgress.put(washId, (int) percentFinished);
                    sendTimerBroadcast(UPDATE_BROADCAST, washId, (int) percentFinished);

                } catch(Exception e){
                    Timber.d(e.getMessage());
                }
            }

            @Override
            public void onFinish() {

            }
        };
        countDownTimer.start();
        createNotification();
        showNotification();
    }

    public void getProgress(){}

    public void restart() {
        Timber.d(TAG, "start");
        timer = new Timer(Timer.State.DRY);
        timer.startCycle();
        createNotification();
        showNotification();
    }

    public void pause() {
        Timber.d(TAG, "pause");
        timer.pause();
    }

    public void stop() {
        Timber.d(TAG, "stop");
        timer.reset();

        hideNotification();
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
        return (num < 10) ? "0" + num : Long.valueOf(num).toString();
    }

    public CountDownTimer getCountDownTimer() {
        return countDownTimer;
    }

    public class LocalBinder extends Binder {
        public TimerService getService() {
            return TimerService.this;
        }
    }

    private void sendTimerBroadcast(String message, long washId, int percentComplete){
        Intent updateIntent = new Intent(message);
        updateIntent.putExtra("WASH_ID", washId);
        updateIntent.putExtra("PROGRESS", percentComplete);
        PendingIntent startPendingIntent = PendingIntent.getBroadcast(this,
                0, updateIntent, 0);

        try {
            startPendingIntent.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }

}
