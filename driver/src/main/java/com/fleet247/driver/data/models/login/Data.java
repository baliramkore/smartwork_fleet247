package com.fleet247.driver.data.models.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sandeep on 4/11/17.
 */

public class Data {
    @SerializedName("access_token")
    @Expose
    public String accessToken;
    @SerializedName("Driver")
    @Expose
    public Driver driver;

    @SerializedName("error")
    @Expose
    public String error;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
