package com.fleet247.driver.data.models.startride;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sandeep on 16/11/17.
 */

public class TripDetails {

    @SerializedName("end_otp")
    @Expose
    public String endOtp;

    public String getEndOtp() {
        return endOtp;
    }

    public void setEndOtp(String endOtp) {
        this.endOtp = endOtp;
    }

}
