package com.fleet247.driver.data.models.updatefcmmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sandeep on 23/10/17.
 */

public class Response {

    @SerializedName("access_token")
    @Expose
    public String accessToken;
    @SerializedName("user")
    @Expose
    public User user;
}
