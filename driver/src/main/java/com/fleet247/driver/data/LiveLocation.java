package com.fleet247.driver.data;

import android.app.Application;
import androidx.lifecycle.LiveData;
import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class LiveLocation extends LiveData<Location>{
    FusedLocationProviderClient client;
    static LiveLocation INSTANCE;
    boolean isOnTrip=false;

    LocationCallback locationCallback=new LocationCallback(){
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult!=null ){
                setValue(locationResult.getLastLocation());
            }
        }
    };

    public void setOnTrip(boolean isOnTrip){
        this.isOnTrip=isOnTrip;
        client.removeLocationUpdates(locationCallback);
        try {
            if (isOnTrip) {
                client.requestLocationUpdates(locationRequestForTripTracking(), locationCallback, null);
            }else {
                client.requestLocationUpdates(locationRequestForVehicleTracking(), locationCallback, null);
            }

        }catch (SecurityException e){
            System.out.print(e.getMessage());
        }
    }

    private LiveLocation(Application application){
        client = LocationServices.getFusedLocationProviderClient(application);
    }

    public static LiveLocation getInstance(Application application){
        if (INSTANCE==null){
            INSTANCE=new LiveLocation(application);
        }
        return INSTANCE;
    }

    @Override
    protected void onActive() {
        try {
            if (isOnTrip) {
                client.requestLocationUpdates(locationRequestForTripTracking(), locationCallback, null);
            }else {
                client.requestLocationUpdates(locationRequestForVehicleTracking(), locationCallback, null);
            }

        }catch (SecurityException e){
            System.out.print(e.getMessage());
        }
    }

    @Override
    protected void onInactive() {client.removeLocationUpdates(locationCallback);
    }

    protected LocationRequest locationRequestForTripTracking() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(3000);
        mLocationRequest.setSmallestDisplacement(20);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return mLocationRequest;
    }

    protected LocationRequest locationRequestForVehicleTracking() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setSmallestDisplacement(40);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return mLocationRequest;
    }
}
