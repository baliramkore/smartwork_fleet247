package com.fleet247.driver.data.models.cancelledbookings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Passenger {
    @SerializedName("people_name")
    @Expose
    public String peopleName;
    @SerializedName("people_email")
    @Expose
    public String peopleEmail;
    @SerializedName("people_contact")
    @Expose
    public String peopleContact;

    public String getPeopleName() {
        return peopleName;
    }

    public void setPeopleName(String peopleName) {
        this.peopleName = peopleName;
    }

    public String getPeopleEmail() {
        return peopleEmail;
    }

    public void setPeopleEmail(String peopleEmail) {
        this.peopleEmail = peopleEmail;
    }

    public String getPeopleContact() {
        return peopleContact;
    }

    public void setPeopleContact(String peopleContact) {
        this.peopleContact = peopleContact;
    }
}
