package com.highiq.iqmaps;

public class ReadSettings {
    String phone;
    String address;
    String distanceMeasurement;
    boolean restaurants;
    boolean bars;
    boolean sports;
    boolean groceries;
    boolean history;

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getDistanceMeasurement() {
        return distanceMeasurement;
    }

    public boolean isRestaurants() {
        return restaurants;
    }

    public boolean isBars() {
        return bars;
    }

    public boolean isSports() {
        return sports;
    }

    public boolean isGroceries() {
        return groceries;
    }

    public boolean isHistory() {
        return history;
    }
}
