package com.fleet247.driver.data.models.upcomingbooking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sandeep on 13/11/17.
 */

public class Data {
    @SerializedName("UpcomingBookings")
    @Expose
    public List<Booking> bookings = null;


    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }
}
