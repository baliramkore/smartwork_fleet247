package com.fleet247.driver.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.fleet247.driver.tracking.LocationService;

/**
 * Created by sandeep on 24/1/18.
 */

public class OnBootBroadCastReceiver extends BroadcastReceiver {
    SharedPreferences preferences; //Preferences to get reference of currentBookingPref

    /**
     * Start LocationService if driver has active booking.
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        preferences=context.getSharedPreferences("currentBookingPref",Context.MODE_PRIVATE);
        if(preferences.getBoolean("isActive",false)){
            Log.d("isActive","True");
            context.startService(new Intent(context,LocationService.class));
        }
        else {
            Log.d("isActive","False");
        }
    }
}
