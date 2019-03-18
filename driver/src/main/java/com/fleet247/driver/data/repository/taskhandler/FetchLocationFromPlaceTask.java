package com.fleet247.driver.data.repository.taskhandler;

import android.app.Application;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.fleet247.driver.data.models.upcomingbooking.Booking;
import com.fleet247.driver.utility.GsonStringConvertor;

import java.util.List;
import java.util.Locale;

public class FetchLocationFromPlaceTask implements Runnable {
    private  Application application;
    private LocationCallback locationCallback;
    private Booking booking;
    public FetchLocationFromPlaceTask(Application application,LocationCallback locationCallback,Booking booking){
        this.application=application;
        this.locationCallback=locationCallback;
        this.booking=booking;
    }

    @Override
    public void run() {
        getPickupLocation(booking.getPickupLocation());
        getDropLocation(booking.getDropLocation());
    }

    void getPickupLocation(String pickUpLocation){
        Geocoder geocoder=new Geocoder(application, Locale.getDefault());
        List<Address> addressList;
        if (geocoder.isPresent()) {
            try {
                addressList = geocoder.getFromLocationName(pickUpLocation, 1);
                Log.d("Address", GsonStringConvertor.gsonToString(addressList) + " Address");
                if (addressList.size() > 0) {
                    locationCallback.pickupLocationCoordinate(new LatLng(addressList.get(0).getLatitude(),
                            addressList.get(0).getLongitude()),booking.getBookingId());
                }
            }catch (Exception e){

            }
        }
    }

    void getDropLocation(String dropLocation){
        if (dropLocation==null){
            return;
        }
        Geocoder geocoder=new Geocoder(application, Locale.getDefault());
        List<Address> addressList;
        if (geocoder.isPresent()) {
            try {
                addressList = geocoder.getFromLocationName(dropLocation, 1);
                Log.d("Address", GsonStringConvertor.gsonToString(addressList) + " Address");
                if (addressList.size() > 0) {
                    locationCallback.dropLocationCoordinate(new LatLng(addressList.get(0).getLatitude(),
                            addressList.get(0).getLongitude()),booking.getBookingId());
                }
            }catch (Exception e){

            }
        }
    }

    public interface LocationCallback{
        void pickupLocationCoordinate(LatLng pickupLocation,String bookingId);
        void dropLocationCoordinate(LatLng dropLocation,String bookingId);
    }

}
