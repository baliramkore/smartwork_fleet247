package com.fleet247.driver.data.models.startride;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sandeep on 16/11/17.
 */

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
