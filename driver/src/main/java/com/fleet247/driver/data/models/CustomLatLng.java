package com.fleet247.driver.data.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(primaryKeys = {"bookingId","locationType"})
public class CustomLatLng {
    @NonNull
    String bookingId;
    @NonNull
    String locationType;
    double latitude;
    double longitude;

    public CustomLatLng() {
    }

    @Ignore
    public CustomLatLng(String bookingId, String locationType, double latitude, double longitude) {
        this.bookingId = bookingId;
        this.locationType = locationType;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }
}
