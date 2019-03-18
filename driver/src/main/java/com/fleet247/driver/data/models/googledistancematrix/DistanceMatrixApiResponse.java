package com.fleet247.driver.data.models.googledistancematrix;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sandeep on 7/11/17.
 */

public class DistanceMatrixApiResponse {
    @SerializedName("status")
    @Expose
    public String status;

    @SerializedName("rows")
    @Expose
    public List<Rows> rows;

    public List<Rows> getRows() {
        return rows;
    }

    public void setRows(List<Rows> rows) {
        this.rows = rows;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
