package com.fleet247.driver.eventlistener.eventinterface;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by sandeep on 8/11/17.
 */

public interface LocationChangeListener {

    void onLocationChangedFromService(Location latLng);
    void onETAchanged(String s);
    void onDistanceChanged(Double distance);
    void onPathLatLngChanged(ArrayList<LatLng> pathLatLng);
}
