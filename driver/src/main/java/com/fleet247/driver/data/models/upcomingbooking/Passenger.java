package com.fleet247.driver.data.models.upcomingbooking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sandeep on 10/11/17.
 */

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

    public Passenger() {
    }

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
