package com.fleet247.driver.data.models.googledirection;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sandeep on 7/11/17.
 */

public class Routes {

    @SerializedName("overview_polyline")
    @Expose
    OverviewPolyline overviewPolyline;

    public OverviewPolyline getOverviewPolyline() {
        return overviewPolyline;
    }

    public void setOverviewPolyline(OverviewPolyline overviewPolyline) {
        this.overviewPolyline = overviewPolyline;
    }
}
