package com.fleet247.driver.data.models.upcomingbooking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sandeep on 13/11/17.
 */

public class UpcomingBookingApiResponse {
    @SerializedName("data")
    @Expose
    public Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
