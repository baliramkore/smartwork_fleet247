package com.fleet247.driver.data.models.googledirection;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sandeep on 7/11/17.
 */

public class GoogleDirectionApiResonse {

    @SerializedName("routes")
    @Expose
    public List<Routes> routes;

    @SerializedName("status")
    @Expose
    public String status;

    public List<Routes> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Routes> routes) {
        this.routes = routes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
