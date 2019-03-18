package com.fleet247.driver.data.models.tracking;

/**
 * Created by sandeep on 13/9/17.
 */

public class Location {
    String lat;
    String lng;

    public Location(String lat, String lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}
