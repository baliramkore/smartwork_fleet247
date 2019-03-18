package com.fleet247.driver.data.models.registerfcm;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sandeep on 17/11/17.
 */

public class Response {
    @SerializedName("access_token")
    @Expose
    public String accessToken;
    @SerializedName("Message")
    @Expose
    public String message;
    @SerializedName("notification_for")
    @Expose
    public String notificationFor;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("status_color")
    @Expose
    public String statusColor;
    @SerializedName("TripDetails")
    @Expose
    public TripDetails tripDetails;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNotificationFor() {
        return notificationFor;
    }

    public void setNotificationFor(String notificationFor) {
        this.notificationFor = notificationFor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusColor() {
        return statusColor;
    }

    public void setStatusColor(String statusColor) {
        this.statusColor = statusColor;
    }

    public TripDetails getTripDetails() {
        return tripDetails;
    }

    public void setTripDetails(TripDetails tripDetails) {
        this.tripDetails = tripDetails;
    }
}
