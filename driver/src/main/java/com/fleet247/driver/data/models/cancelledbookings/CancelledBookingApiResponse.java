package com.fleet247.driver.data.models.cancelledbookings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CancelledBookingApiResponse {

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
