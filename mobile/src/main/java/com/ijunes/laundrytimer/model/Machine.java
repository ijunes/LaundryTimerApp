package com.ijunes.laundrytimer.model;

import android.location.Location;

/**
 * Created by jkang on 4/7/16.
 */
public class Machine {

    private long id;
    private String name;
    private Location location;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
