package com.fleet247.driver.data.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.fleet247.driver.data.models.upcomingbooking.Booking;

@Dao
public interface BookingDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertBooking(Booking booking);

    @Delete
    public void deleteBooking(Booking booking);
}
