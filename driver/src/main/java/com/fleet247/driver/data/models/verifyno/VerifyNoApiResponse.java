package com.fleet247.driver.data.models.verifyno;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VerifyNoApiResponse {

    @SerializedName("data")
    @Expose
    public Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
