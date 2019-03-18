package com.fleet247.driver.data.models.updatefcmmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sandeep on 23/10/17.
 */

public class User {
    @SerializedName("user_id")
    @Expose
    public String userId;
    @SerializedName("message")
    @Expose
    public String message;
}
