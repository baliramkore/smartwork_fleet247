package com.fleet247.driver.data.models.driverarrived;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {
    @SerializedName("TripDetails")
    @Expose
    public TripDetails tripDetails;

    public TripDetails getTripDetails() {
        return tripDetails;
    }

    public void setTripDetails(TripDetails tripDetails) {
        this.tripDetails = tripDetails;
    }

}
