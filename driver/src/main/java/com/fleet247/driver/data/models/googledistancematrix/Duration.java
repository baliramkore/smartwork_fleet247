package com.fleet247.driver.data.models.googledistancematrix;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sandeep on 7/11/17.
 */

public class Duration {

    @SerializedName("text")
    @Expose
    public String text;

    @SerializedName("value")
    @Expose
    public String value;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
