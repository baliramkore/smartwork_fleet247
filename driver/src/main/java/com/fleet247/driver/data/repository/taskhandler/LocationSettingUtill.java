package com.fleet247.driver.data.repository.taskhandler;

import android.app.Application;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;

public class LocationSettingUtill {
    LocationRequest mLocationRequest;
    LocationSettingsRequest.Builder builder;
    SettingsClient client;
    Task<LocationSettingsResponse> task;
    public static LocationSettingUtill locationSettingUtill;

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private LocationSettingUtill(Application application){
        createLocationRequest();
        builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        client = LocationServices.getSettingsClient(application);

    }

    public static LocationSettingUtill getInstance(Application application){
        if (locationSettingUtill==null){
            locationSettingUtill=new LocationSettingUtill(application);
        }
        return locationSettingUtill;
    }

    public Task<LocationSettingsResponse> getTask() {
        task = client.checkLocationSettings(builder.build());
        return task;
    }
}
