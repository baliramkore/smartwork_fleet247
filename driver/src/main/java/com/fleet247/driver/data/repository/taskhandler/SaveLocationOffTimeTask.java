package com.fleet247.driver.data.repository.taskhandler;

import androidx.lifecycle.LiveData;

import com.fleet247.driver.data.models.LocationOffTime;

public interface SaveLocationOffTimeTask {

    interface GetAllLocationOffTime{
        void allLocationOffTime(LiveData<Long> totalTime);
    }

    interface GetLocationOffTime{
        void locationOffTime(LocationOffTime locationOffTime);
    }

    void saveLocationOffTime(LocationOffTime locationOffTime);
    int getLocationOffTimeId();
    void deleteLocationOffData();
    void updateLocationOnOffEndTime();
    LiveData<Long> getAlllocationOffTime();
}
