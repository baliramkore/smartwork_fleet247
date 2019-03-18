package com.fleet247.driver.data.models.driverarrived;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sandeep on 16/11/17.
 */

public class TripDetails {
    @SerializedName("start_otp")
    @Expose
    public String startOtp;

    public String getStartOtp() {
        return startOtp;
    }

    public void setStartOtp(String startOtp) {
        this.startOtp = startOtp;
    }

}
