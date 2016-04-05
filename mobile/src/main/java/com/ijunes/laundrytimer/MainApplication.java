package com.ijunes.laundrytimer;

import android.app.Application;
import android.os.Bundle;

import timber.log.Timber;

/**
 * Created by jkang on 4/5/16.
 */
public class MainApplication extends Application {

    @Override
    public void onCreate(){
        super.onCreate();
        if(BuildConfig.DEBUG){
            Timber.plant(new Timber.DebugTree());
        }
    }
}
