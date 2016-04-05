package com.ijunes.laundrytimer.event;

import com.hwangjr.rxbus.Bus;
import com.hwangjr.rxbus.RxBus;

public enum EventBus {

    INSTANCE;

    private static Bus defaultBus = RxBus.get();

    public Bus getBus() {
        return defaultBus;
    }
}