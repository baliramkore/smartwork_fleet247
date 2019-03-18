package com.fleet247.driver.data.models.archivedbooking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sandeep on 6/2/18.
 */

public class ArchivedBookingApiResponse {
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
