package com.fleet247.driver.data.models.googledistancematrix;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sandeep on 7/11/17.
 */

public class Rows {

    @SerializedName("elements")
    @Expose
    public List<Elements> elements = null;

    public List<Elements> getElements() {
        return elements;
    }

    public void setElements(List<Elements> elements) {
        this.elements = elements;
    }
}
