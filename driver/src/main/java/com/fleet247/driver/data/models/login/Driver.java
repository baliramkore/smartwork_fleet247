package com.fleet247.driver.data.models.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sandeep on 4/11/17.
 */

public class Driver {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("driver_name")
    @Expose
    public String driverName;
    @SerializedName("driver_contact")
    @Expose
    public String driverContact;
    @SerializedName("driver_email")
    @Expose
    public String driverEmail;
    @SerializedName("operator_id")
    @Expose
    public String operatorId;
    @SerializedName("licence_no")
    @Expose
    public String licenceNo;

    @SerializedName("operator_contact_name")
    @Expose
    public String operatorName;

    @SerializedName("operator_contact_no")
    @Expose
    public String operatorContact;

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getLicenceNo() {
        return licenceNo;
    }

    public void setLicenceNo(String licenceNo) {
        this.licenceNo = licenceNo;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverContact() {
        return driverContact;
    }

    public void setDriverContact(String driverContact) {
        this.driverContact = driverContact;
    }

    public String getDriverEmail() {
        return driverEmail;
    }

    public void setDriverEmail(String driverEmail) {
        this.driverEmail = driverEmail;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getOperatorContact() {
        return operatorContact;
    }

    public void setOperatorContact(String operatorContact) {
        this.operatorContact = operatorContact;
    }
}
