package com.fleet247.driver.data.repository.taskhandler;

import android.app.Application;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class FetchPlacesFromLocationTask  implements Runnable{

    Application application;
    PlacesCallback placesCallback;
    Location location;

    public FetchPlacesFromLocationTask(Application application, PlacesCallback placesCallback, Location location){
        this.application=application;
        this.placesCallback=placesCallback;
        this.location=location;
    }

    @Override
    public void run() {
        Geocoder geocoder = new Geocoder(application, Locale.getDefault());
        List<Address> addressList=null;
        if (geocoder.isPresent()){
            try {
                addressList=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            } catch (IOException e) {
                e.printStackTrace();
                placesCallback.placeError(e.getMessage());
                return;
            }

            if (addressList!=null && addressList.size()>0){
                placesCallback.placeAddress(addressList.get(0),location);
            }
            else {
                placesCallback.placeError("Address not found");
            }
        }
    }

    public interface PlacesCallback{
        void placeAddress(Address address,Location location);
        void placeError(String error);
    }
}
