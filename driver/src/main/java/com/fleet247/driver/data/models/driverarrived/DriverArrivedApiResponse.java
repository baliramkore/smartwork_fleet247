package com.fleet247.driver.data.models.driverarrived;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sandeep on 15/11/17.
 */

public class DriverArrivedApiResponse {

    @SerializedName("data")
    @Expose
    public Data response;

    public Data getResponse() {
        return response;
    }

    public void setResponse(Data response) {
        this.response = response;
    }
}
