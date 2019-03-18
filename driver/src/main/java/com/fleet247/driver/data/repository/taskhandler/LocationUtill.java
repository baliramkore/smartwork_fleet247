package com.fleet247.driver.data.repository.taskhandler;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class LocationUtill extends LocationCallback {

    static LocationUtill locationUtill;
    MutableLiveData<Location> location;
    Location currentLocation;
    FusedLocationProviderClient client;

    private LocationUtill(Application application){
        client = LocationServices.getFusedLocationProviderClient(application);
        location=new MutableLiveData<>();
    }

    public LiveData<Location> getLocation(){
        return location;
    }

    public static  LocationUtill getInstance(Application application){
        if (locationUtill==null){
            locationUtill=new LocationUtill(application);
        }
        return locationUtill;
    }
    public void startLocationUpdate (){
        try {
            client.requestLocationUpdates(createLocationRequest(), this,null);
        }catch (SecurityException e){
            System.out.print(e.getMessage());
        }
    }



    @Override
    public void onLocationResult(LocationResult locationResult) {
        if (locationResult!=null){
            location.setValue(locationResult.getLastLocation());
        }
    }


    public Location getCurrentLocation() {
        return currentLocation;
    }

    protected LocationRequest createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(3000);
        //mLocationRequest.setSmallestDisplacement(15);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return mLocationRequest;
    }

    public void stopLocationUpdates() {
       client.removeLocationUpdates(this);
    }
}
