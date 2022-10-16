package com.highiq.iqmaps;

public class favouritesClass {

    String favName;
    String favDesc;
    String favLat;
    String favLong;
    public favouritesClass(){}

    public favouritesClass(String favName,String favDesc,String favLat,String favLong){
        this.favName = favName;
        this.favDesc = favDesc;
        this.favLat = favLat;
        this.favLong = favLong;
    }
    public String getFavName() {
        return favName;
    }

    public void setFavName(String favName) {
        this.favName = favName;
    }

    public String getFavDesc() {
        return favDesc;
    }

    public void setFavDesc(String favDesc) {
        this.favDesc = favDesc;
    }

    public String getFavLat() {
        return favLat;
    }

    public void setFavLat(String favLat) {
        this.favLat = favLat;
    }

    public String getFavLong() {
        return favLong;
    }

    public void setFavLong(String favLong) {
        this.favLong = favLong;
    }



}
