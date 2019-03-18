package com.fleet247.driver.data.models.endride;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sandeep on 17/11/17.
 */

public class Data {
    @SerializedName("InvoiceDetails")
    @Expose
    public InvoiceDetails invoiceDetails;

    public InvoiceDetails getInvoiceDetails() {
        return invoiceDetails;
    }

    public void setInvoiceDetails(InvoiceDetails invoiceDetails) {
        this.invoiceDetails = invoiceDetails;
    }
}
