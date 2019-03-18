package com.fleet247.driver.data.room;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import com.fleet247.driver.data.models.LocationOffTime;
import com.fleet247.driver.data.models.Signature;
import com.fleet247.driver.data.room.dao.BookingDAO;
import com.fleet247.driver.data.room.dao.LocationDAO;
import com.fleet247.driver.data.models.CustomLatLng;
import com.fleet247.driver.data.models.upcomingbooking.Booking;
import com.fleet247.driver.data.room.dao.LocationOffTimeDAO;
import com.fleet247.driver.data.room.dao.SignatureDAO;

@Database(entities = {Booking.class,CustomLatLng.class, Signature.class, LocationOffTime.class},version = 11,exportSchema = false)
public abstract class DriverAppDatabase extends RoomDatabase {

    public static DriverAppDatabase INSTANCE;

    public static DriverAppDatabase getDriverDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (DriverAppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context,
                            DriverAppDatabase.class, "driver_database")
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }

        return INSTANCE;
    }

    public abstract BookingDAO bookingDAO();

    public abstract LocationDAO locationDAO();

    public abstract SignatureDAO signatureDAO();

    public abstract LocationOffTimeDAO locationOffTimeDAO();

}
