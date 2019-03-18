package com.fleet247.driver.data.models.verifyno;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Driver {
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("driver_contact")
    @Expose
    public String driverContact;
    @SerializedName("password")
    @Expose
    public String password;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDriverContact() {
        return driverContact;
    }

    public void setDriverContact(String driverContact) {
        this.driverContact = driverContact;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
