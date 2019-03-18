package com.fleet247.driver.data.models.googledirection;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sandeep on 7/11/17.
 */

public class OverviewPolyline {

    @SerializedName("points")
    @Expose
    public String points;

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }
}
