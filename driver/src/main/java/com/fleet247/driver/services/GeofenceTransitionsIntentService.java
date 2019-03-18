package com.fleet247.driver.services;

import android.app.IntentService;
import android.content.Intent;
import androidx.annotation.Nullable;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.fleet247.driver.data.repository.CurrentBookingRepository;

/**
 * Created by sandeep on 22/12/17.
 */

public class GeofenceTransitionsIntentService extends IntentService {



    CurrentBookingRepository  currentBookingRepository;

    public GeofenceTransitionsIntentService() {
        super("GeofenceTransitionsIntentService");
    }

    public GeofenceTransitionsIntentService(String name) {
        super(name);
    }



    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        currentBookingRepository=CurrentBookingRepository.getInstance(getApplication());
        GeofencingEvent geofencingEvent=GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            currentBookingRepository.setHasEnteredAtPickupPoint(false);
            return;
        }
        switch (geofencingEvent.getGeofenceTransition()){
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                currentBookingRepository.setHasEnteredAtPickupPoint(true);
                break;
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                currentBookingRepository.setHasEnteredAtPickupPoint(false);
                break;
        }

    }
}
