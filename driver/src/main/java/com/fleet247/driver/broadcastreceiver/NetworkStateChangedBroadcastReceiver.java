package com.fleet247.driver.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.fleet247.driver.data.repository.taskhandler.AccessSharedPreferencesUtill;
import com.fleet247.driver.tracking.ActiveDriverTrackingService;
import com.fleet247.driver.tracking.LocationService;
import com.fleet247.driver.utility.NetworkStatus;

public class NetworkStateChangedBroadcastReceiver extends BroadcastReceiver{

    /**
     * If driver in completing a trip or is active check if Internet is enabled and LocationService is running.
     * If it is not start LocationService
     * @param context
     * @param intent
     */

    @Override
    public void onReceive(Context context, Intent intent) {
        if (NetworkStatus.isInternetConnected(context.getApplicationContext())){
            Log.d("Internet","Connected");
            if (AccessSharedPreferencesUtill.isActive(context.getApplicationContext())){
                if (AccessSharedPreferencesUtill.hasCurrentBooking(context.getApplicationContext())){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        context.startForegroundService(new Intent(context, LocationService.class));
                    }else {
                        context.startService(new Intent(context,LocationService.class));
                    }
                }else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        context.startForegroundService(new Intent(context, ActiveDriverTrackingService.class));
                    }else {
                        context.startService(new Intent(context,ActiveDriverTrackingService.class));
                    }
                }
            }
            else if (AccessSharedPreferencesUtill.hasCurrentBooking(context.getApplicationContext())){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(new Intent(context, LocationService.class));
                }else {
                    context.startService(new Intent(context,LocationService.class));
                }
            }

        }else if(AccessSharedPreferencesUtill.hasCurrentBooking(context.getApplicationContext()) ||
                AccessSharedPreferencesUtill.isActive(context.getApplicationContext())){
            Toast.makeText(context,"Please Enable Internet Connection",Toast.LENGTH_LONG).show();
        }
    }

}
