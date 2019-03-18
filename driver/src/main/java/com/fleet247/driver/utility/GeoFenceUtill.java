package com.fleet247.driver.utility;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.fleet247.driver.services.GeofenceTransitionsIntentService;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class GeoFenceUtill {

    private static List<Geofence> geofenceList;
    private static PendingIntent mGeofencePendingIntent;

    private static GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(geofenceList);
        return builder.build();
    }

    private static PendingIntent getGeofencePendingIntent(Context context) {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(context, GeofenceTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        mGeofencePendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
        return mGeofencePendingIntent;
    }

    private static void createGeoFenceList(LatLng latLng){
        geofenceList.add(new Geofence.Builder()
                .setRequestId("pickupFence")
                .setCircularRegion(
                        latLng.latitude,
                        latLng.longitude,
                        500)
                .setExpirationDuration(7200000)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                .build());
    }

}
