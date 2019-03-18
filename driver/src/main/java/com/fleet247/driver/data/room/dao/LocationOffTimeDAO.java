package com.fleet247.driver.data.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.fleet247.driver.data.models.LocationOffTime;

import java.util.List;

@Dao
public interface LocationOffTimeDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOffTime(LocationOffTime locationOffTime);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateOfftime(LocationOffTime locationOffTime);

    @Query("SELECT offTimeId FROM LOCATIONOFFTIME ORDER BY startTime DESC LIMIT 1")
    int getIdOfLastOffTime();

    @Query("SELECT * FROM LOCATIONOFFTIME ORDER BY startTime DESC LIMIT 1")
    LocationOffTime getLastOffTime();

    @Query("SELECT * FROM LOCATIONOFFTIME")
    List<LocationOffTime> getAllOffTime();

    @Query("DELETE FROM LOCATIONOFFTIME")
    void deleteLocationOffTime();

    @Query("SELECT sum(duration) FROM LOCATIONOFFTIME")
    LiveData<Long> getTotalDuration();

}
