package com.ijunes.laundrytimer;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import timber.log.Timber;

/**
 * Created by jkang on 4/5/16.
 */
public class MainApplication extends Application {

    @Override
    public void onCreate(){
        super.onCreate();
        INSTANCE = this;
        if(BuildConfig.DEBUG){
            Timber.plant(new Timber.DebugTree());
        }
    }
    private static MainApplication INSTANCE;

    public static MainApplication getInstance() {
        return INSTANCE;
    }

    public static MainApplication get() {
        return (MainApplication) INSTANCE.getApplicationContext();
    }


}
