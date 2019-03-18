package com.fleet247.driver.eventlistener.eventclass;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;
import com.fleet247.driver.eventlistener.eventinterface.LocationChangeListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sandeep on 8/11/17.
 */

public class LocationChangeClass  {
    static List<LocationChangeListener> locationChangeListeners=new ArrayList<>();
    static String eot;
    static Location currentLatLng;
    static Double distanceTravelled;
    static ArrayList<LatLng> pathLatLng;
    static String encodedPolyline;

    public static void addLocationChangeLisetener(LocationChangeListener locationChangeListener){
        if (locationChangeListener!=null && !locationChangeListeners.contains(locationChangeListener)){
            locationChangeListeners.add(locationChangeListener);
        }

        if (currentLatLng!=null && locationChangeListener!=null){
            locationChangeListener.onLocationChangedFromService(currentLatLng);
        }

        if (eot!=null && locationChangeListener!=null){
            locationChangeListener.onETAchanged(eot);
        }

        if (locationChangeListener!=null){
            locationChangeListener.onDistanceChanged(distanceTravelled);
        }

        if (pathLatLng!=null && locationChangeListener!=null){
            locationChangeListener.onPathLatLngChanged(pathLatLng);
        }
    }

    public static void removeLocationChangeListener(LocationChangeListener locationChangeListener){
        if (locationChangeListener!=null && locationChangeListeners.contains(locationChangeListener)){
            locationChangeListeners.remove(locationChangeListener);
        }
    }


    public static void onLocationChanged(Location latLng){
        currentLatLng=latLng;
        if (locationChangeListeners.size()>0) {
            for (int i = 0; i < locationChangeListeners.size(); i++) {
                locationChangeListeners.get(i).onLocationChangedFromService(latLng);
            }
        }
    }

    public static void onETAChanged(String s){
        eot=s;
        if (locationChangeListeners.size()>0) {
            for (int i = 0; i < locationChangeListeners.size(); i++) {
                locationChangeListeners.get(i).onETAchanged(s);
            }
        }
    }

    public static void onDistanceChanged(Double distance){
        distanceTravelled=distance;
        if (locationChangeListeners.size()>0){
            for (int i=0;i<locationChangeListeners.size();i++){
                locationChangeListeners.get(i).onDistanceChanged(distanceTravelled);
            }
        }
    }

    public static void onPathLatLngChanged(ArrayList<LatLng> latLngArrayList){
        pathLatLng=latLngArrayList;
        if (locationChangeListeners.size()>0){
            for (int i=0;i<locationChangeListeners.size();i++){
                locationChangeListeners.get(i).onPathLatLngChanged(pathLatLng);
            }
        }
    }



}
