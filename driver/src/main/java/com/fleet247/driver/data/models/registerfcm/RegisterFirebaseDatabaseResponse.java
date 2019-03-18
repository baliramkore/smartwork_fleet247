package com.fleet247.driver.data.models.registerfcm;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sandeep on 17/11/17.
 */

public class RegisterFirebaseDatabaseResponse {

    @SerializedName("success")
    @Expose
    public String success;
    @SerializedName("error")
    @Expose
    public String error;
    @SerializedName("data")
    @Expose
    public Response response;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
