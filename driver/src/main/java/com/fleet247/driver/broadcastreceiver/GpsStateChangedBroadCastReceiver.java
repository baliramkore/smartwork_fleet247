package com.fleet247.driver.broadcastreceiver;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.fleet247.driver.data.models.LocationOffTime;
import com.fleet247.driver.data.repository.CurrentBookingRepository;
import com.fleet247.driver.data.repository.taskhandler.AccessSharedPreferencesUtill;
import com.fleet247.driver.data.room.DriverAppDatabase;
import com.fleet247.driver.tracking.ActiveDriverTrackingService;
import com.fleet247.driver.tracking.LocationService;
import com.fleet247.driver.utility.IsServiceRunning;
import com.fleet247.driver.viewmodels.CurrentBookingViewModel;

import java.util.Calendar;

public class GpsStateChangedBroadCastReceiver extends BroadcastReceiver {
    /**
     * In this method check if GPS is turned on or off and if driver is having a active trip or is active to take bookings
     * then set GPS status in Repository.
     * It also saves timestamp of when gps is turned off and then turned off to calculate the total time for which gps was turned off.
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("GPS","Triggered");
        Application application=(Application)context.getApplicationContext();
        CurrentBookingRepository currentBookingRepository=CurrentBookingRepository.getInstance(application);
        LocationManager locationManager=(LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            Log.d("LocationStatus","Enabled");
            currentBookingRepository.setLocationStatus(true);
            if (AccessSharedPreferencesUtill.isActive(context.getApplicationContext())){
                if (AccessSharedPreferencesUtill.hasCurrentBooking(context.getApplicationContext())){
                    if (!IsServiceRunning.isServiceRunningInForeground(context.getApplicationContext(),LocationService.class) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        context.startForegroundService(new Intent(context, LocationService.class));
                    }else if(!IsServiceRunning.isServiceRunningInForeground(context.getApplicationContext(),LocationService.class)) {
                        context.startService(new Intent(context,LocationService.class));
                    }
                    if (currentBookingRepository.getGpsStatus()!=null && currentBookingRepository.getGpsStatus().equals("DISABLED")) {
                        currentBookingRepository.updateLocationOnOffEndTime();
                        currentBookingRepository.setGpsStatus("ENABLED");
                    }
                }else {
                    if (!IsServiceRunning.isServiceRunningInForeground(context.getApplicationContext(),ActiveDriverTrackingService.class) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        context.startForegroundService(new Intent(context, ActiveDriverTrackingService.class));
                    }else if(!IsServiceRunning.isServiceRunningInForeground(context.getApplicationContext(),ActiveDriverTrackingService.class)){
                        context.startService(new Intent(context,ActiveDriverTrackingService.class));
                    }
                }
            }
            else if (AccessSharedPreferencesUtill.hasCurrentBooking(context.getApplicationContext())){
                if (!IsServiceRunning.isServiceRunningInForeground(context.getApplicationContext(),LocationService.class) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(new Intent(context, LocationService.class));
                }else if(!IsServiceRunning.isServiceRunningInForeground(context.getApplicationContext(),LocationService.class)){
                    context.startService(new Intent(context,LocationService.class));
                }
                if (currentBookingRepository.getGpsStatus()!=null && currentBookingRepository.getGpsStatus().equals("DISABLED")) {
                    currentBookingRepository.updateLocationOnOffEndTime();
                    currentBookingRepository.setGpsStatus("ENABLED");
                }
            }

        }else if(AccessSharedPreferencesUtill.hasCurrentBooking(context.getApplicationContext()) ||
                AccessSharedPreferencesUtill.isActive(context.getApplicationContext())){
            Log.d("LocationStatus","Disabled");
            currentBookingRepository.setLocationStatus(false);
            Toast.makeText(context,"Please Enable Location for proper functioning of Fleet247 driver app",Toast.LENGTH_LONG).show();
            Toast.makeText(context,"Please Enable Location for proper functioning of Fleet247 driver app",Toast.LENGTH_LONG).show();

            if (AccessSharedPreferencesUtill.hasCurrentBooking(context)){
                if (currentBookingRepository.getGpsStatus()==null || currentBookingRepository.getGpsStatus().equals("ENABLED")) {
                    LocationOffTime locationOffTime=new LocationOffTime();
                    locationOffTime.setBookingId(Integer.parseInt(currentBookingRepository.getBooking().getValue().getBookingId()));
                    locationOffTime.setStartTime(Calendar.getInstance().getTimeInMillis());
                    currentBookingRepository.saveLocationOffTime(locationOffTime);
                    currentBookingRepository.setGpsStatus("DISABLED");
                }
            }

        }
    }
}
