package com.fleet247.driver.data.models.googledistancematrix;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sandeep on 7/11/17.
 */

public class Elements {

    @SerializedName("distance")
    @Expose
    Distance distance;

    @SerializedName("duration")
    @Expose
    Duration duration;

    @SerializedName("duration_in_traffic")
    @Expose
    Duration durationInTraffic;

    public Duration getDurationInTraffic() {
        return durationInTraffic;
    }

    public void setDurationInTraffic(Duration durationInTraffic) {
        this.durationInTraffic = durationInTraffic;
    }

    @SerializedName("status")
    @Expose
    String status;

    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
