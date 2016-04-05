package com.ijunes.laundrytimer.service;


import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class TimerService extends Service{
    public class LocalBinder extends Binder {
        TimerService getService() {
            return TimerService.this;
        }
    }

    private LocalBinder localBinder = new LocalBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return localBinder;
    }
}
