package com.highiq.iqmaps;

public class SettingsClass {
    String phone;
    String address;
    String distanceMeasurement;
    String mode;
    String landmark;

    public SettingsClass(String phone, String address, String distanceMeasurement, String mode, String landmark) {
        this.phone = phone;
        this.address = address;
        this.distanceMeasurement = distanceMeasurement;
        this.mode = mode;
        this.landmark = landmark;
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

    public void setDistanceMeasurement(String distanceMeasurement) { this.distanceMeasurement = distanceMeasurement; }

    public String getMode() { return mode; }

    public void setMode(String mode) { this.mode = mode; }

    public String getLandmark() { return landmark; }

    public void setLandmark(String landmark) { this.landmark = landmark; }
}
