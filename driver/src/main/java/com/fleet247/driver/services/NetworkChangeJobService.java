package com.fleet247.driver.services;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.fleet247.driver.data.repository.taskhandler.AccessSharedPreferencesUtill;
import com.fleet247.driver.tracking.ActiveDriverTrackingService;
import com.fleet247.driver.tracking.LocationService;

public class NetworkChangeJobService extends JobService{
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        if (AccessSharedPreferencesUtill.isActive(getApplicationContext())){
            if (AccessSharedPreferencesUtill.hasCurrentBooking(getApplicationContext())){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(new Intent(this, LocationService.class));
                }else {
                    startService(new Intent(this,LocationService.class));
                }
            }else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(new Intent(this, ActiveDriverTrackingService.class));
                }else {
                    startService(new Intent(this,ActiveDriverTrackingService.class));
                }
            }
        }
        Log.d("NetworkChange","Received");
        jobFinished(jobParameters,false);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return true;
    }
}
