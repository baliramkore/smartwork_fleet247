package com.fleet247.driver.data.models.storesignature;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StoreSignatureApiResponse {
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
