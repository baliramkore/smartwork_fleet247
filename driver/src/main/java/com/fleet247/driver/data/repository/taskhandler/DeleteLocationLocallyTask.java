package com.fleet247.driver.data.repository.taskhandler;

import com.fleet247.driver.data.room.dao.LocationDAO;
import com.fleet247.driver.data.models.CustomLatLng;

public class DeleteLocationLocallyTask implements Runnable{
    private LocationDAO locationDAO;
    private CustomLatLng customLatLng;

    public DeleteLocationLocallyTask(LocationDAO locationDAO,CustomLatLng customLatLng){
        this.locationDAO=locationDAO;
        this.customLatLng=customLatLng;
    }

    @Override
    public void run() {
        locationDAO.deleteBookings(customLatLng.getBookingId());
    }
}
