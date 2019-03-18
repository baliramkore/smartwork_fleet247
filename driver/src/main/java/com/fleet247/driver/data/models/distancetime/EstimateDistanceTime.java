package com.fleet247.driver.data.models.distancetime;

/**
 * Created by sandeep on 30/1/18.
 */

public class EstimateDistanceTime {

    String estimateDistance;
    String estimateTime;

    public EstimateDistanceTime() {
    }

    public EstimateDistanceTime(String estimateDistance, String estimateTime) {
        this.estimateDistance = estimateDistance;
        this.estimateTime = estimateTime;
    }

    public String getEstimateDistance() {
        return estimateDistance;
    }

    public void setEstimateDistance(String estimateDistance) {
        this.estimateDistance = estimateDistance;
    }

    public String getEstimateTime() {
        return estimateTime;
    }

    public void setEstimateTime(String estimateTime) {
        this.estimateTime = estimateTime;
    }
}
