package com.fleet247.driver.data.repository.taskhandler;

import com.fleet247.driver.data.room.dao.LocationDAO;
import com.fleet247.driver.data.models.CustomLatLng;

public class SaveLocationLocallyTask implements Runnable{

    LocationDAO locationDAO;
    CustomLatLng customLatLng;

    public SaveLocationLocallyTask(LocationDAO locationDAO, CustomLatLng customLatLng) {
        this.locationDAO=locationDAO;
        this.customLatLng=customLatLng;
    }

    @Override
    public void run() {
        locationDAO.insertLocation(customLatLng);
    }
}
