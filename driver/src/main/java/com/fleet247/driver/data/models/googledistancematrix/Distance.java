package com.fleet247.driver.data.models.googledistancematrix;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sandeep on 7/11/17.
 */

public class Distance {

    @SerializedName("text")
    @Expose
    public String text;

    @SerializedName("value")
    @Expose
    public double value;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
