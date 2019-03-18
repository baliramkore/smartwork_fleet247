package com.fleet247.driver.data.models.archivedbooking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sandeep on 6/2/18.
 */

public class Data {

    @SerializedName("Bookings")
    @Expose
    public List<Booking> bookings = null;

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }
}
