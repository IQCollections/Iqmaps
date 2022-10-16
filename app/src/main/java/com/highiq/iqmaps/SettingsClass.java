package com.highiq.iqmaps;

public class SettingsClass {
    String phone;
    String address;
    String distanceMeasurement;
    boolean restaurants;
    boolean bars;
    boolean sports;
    boolean groceries;
    boolean history;

    public SettingsClass(String phone, String address, String distanceMeasurement, boolean restaurants, boolean bars, boolean sports, boolean groceries, boolean history) {
        this.phone = phone;
        this.address = address;
        this.distanceMeasurement = distanceMeasurement;
        this.restaurants = restaurants;
        this.bars = bars;
        this.sports = sports;
        this.groceries = groceries;
        this.history = history;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistanceMeasurement() {
        return distanceMeasurement;
    }

    public void setDistanceMeasurement(String distanceMeasurement) {
        this.distanceMeasurement = distanceMeasurement;
    }

    public boolean getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(boolean restaurants) {
        this.restaurants = restaurants;
    }

    public boolean getBars() {
        return bars;
    }

    public void setBars(boolean bars) {
        this.bars = bars;
    }

    public boolean getSports() {
        return sports;
    }

    public void setSports(boolean sports) {
        this.sports = sports;
    }

    public boolean getGroceries() {
        return groceries;
    }

    public void setGroceries(boolean groceries) {
        this.groceries = groceries;
    }

    public boolean getHistory() {
        return history;
    }

    public void setHistory(boolean history) {
        this.history = history;
    }
}
