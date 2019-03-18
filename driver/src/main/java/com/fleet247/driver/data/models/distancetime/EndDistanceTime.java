package com.fleet247.driver.data.models.distancetime;

/**
 * Created by sandeep on 30/1/18.
 */

public class EndDistanceTime {
    EstimateDistanceTime journeyDistanceTime;
    EstimateDistanceTime garageDistanceTime;

    public EndDistanceTime() {
    }

    public EndDistanceTime(EstimateDistanceTime journeyDistanceTime, EstimateDistanceTime garageDistanceTime) {
        this.journeyDistanceTime = journeyDistanceTime;
        this.garageDistanceTime = garageDistanceTime;
    }

    public EstimateDistanceTime getJourneyDistanceTime() {
        return journeyDistanceTime;
    }

    public void setJourneyDistanceTime(EstimateDistanceTime journeyDistanceTime) {
        this.journeyDistanceTime = journeyDistanceTime;
    }

    public EstimateDistanceTime getGarageDistanceTime() {
        return garageDistanceTime;
    }

    public void setGarageDistanceTime(EstimateDistanceTime garageDistanceTime) {
        this.garageDistanceTime = garageDistanceTime;
    }
}
