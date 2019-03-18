package com.fleet247.driver.utility;

import android.app.ActivityManager;
import android.content.Context;

import com.fleet247.driver.tracking.LocationService;

public class IsServiceRunning {

    public static boolean isServiceRunningInForeground(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(LocationService.class.getName())) {
                if (service.foreground) {
                    return true;
                }

            }
        }
        return false;
    }
}
