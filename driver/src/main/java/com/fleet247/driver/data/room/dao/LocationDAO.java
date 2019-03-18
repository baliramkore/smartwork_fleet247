package com.fleet247.driver.data.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.fleet247.driver.data.models.CustomLatLng;

@Dao
public interface LocationDAO {

    /**
     * Insert custom lat lng into the database.
     * @param customLatLng
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLocation(CustomLatLng customLatLng);

    /**
     *
     * @param bookingID
     * @return LatLng of Pickup Location of provided bookingId.
     */
    @Query("SELECT * FROM CUSTOMLATLNG WHERE bookingId=:bookingID and locationType='PICKUP'")
    LiveData<CustomLatLng> getPickupLatLng(String bookingID);

    /**
     *
     * @param bookingID
     * @return LatLng of Drop Location of provided bookingId.
     */
    @Query("SELECT * FROM CUSTOMLATLNG WHERE bookingId=:bookingID and locationType='DROP'")
    LiveData<CustomLatLng> getDropLatLng(String bookingID);

    /**
     *
     * @param bookingID of which pickup location is required.
     * @return pickup LatLng of provided bookingId.
     */
    @Query("SELECT * FROM CUSTOMLATLNG WHERE bookingId=:bookingID and locationType='PICKUP'")
    CustomLatLng getPickupLatLngs(String bookingID);

    /**
     *
     * @param bookingID  of which drop location is required.
     * @return drop LatLng of provided bookingId.
     */
    @Query("SELECT * FROM CUSTOMLATLNG WHERE bookingId=:bookingID and locationType='DROP'")
    CustomLatLng getDropLatLngs(String bookingID);

    /**
     *
     * @param bookingId of row to be deleted
     */
    @Query("DELETE FROM CUSTOMLATLNG WHERE bookingId=:bookingId")
    void deleteBookings(String bookingId);

    @Delete
    void deleteBooking(CustomLatLng customLatLng);
}
