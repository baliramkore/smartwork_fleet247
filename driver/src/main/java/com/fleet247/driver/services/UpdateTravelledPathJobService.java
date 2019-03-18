package com.fleet247.driver.services;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.fleet247.driver.data.repository.CurrentBookingRepository;
import com.fleet247.driver.data.repository.LoginRepository;
import com.fleet247.driver.retrofit.ConfigRetrofit;
import com.fleet247.driver.retrofit.RecordPolyLineDataAPI;
import com.fleet247.driver.tracking.LocationService;
import com.fleet247.driver.utility.IsServiceRunning;
import com.google.maps.android.PolyUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateTravelledPathJobService extends JobService {
    @Override
    public boolean onStartJob(final JobParameters jobParameters) {

        Log.d("PolyLineJob","Scheduled");

        if (!IsServiceRunning.isServiceRunningInForeground(getApplicationContext(),LocationService.class) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d("IsServiceRunning","False");
            startForegroundService(new Intent(this, LocationService.class));
        }else if(!IsServiceRunning.isServiceRunningInForeground(getApplicationContext(),LocationService.class)) {
            Log.d("IsServiceRunning","False");
            startService(new Intent(this,LocationService.class));
        }
        else {
            Log.d("IsServiceRunning","True");
        }

        CurrentBookingRepository currentBookingRepository=CurrentBookingRepository.getInstance(getApplication());
        LoginRepository loginRepository=LoginRepository.getInstance(getApplication());
        RecordPolyLineDataAPI recordPolyLineDataAPI= ConfigRetrofit.configRetrofit(RecordPolyLineDataAPI.class);
        recordPolyLineDataAPI.updatePolyLine(loginRepository.getAccessToken().getValue(),currentBookingRepository.getBooking().getValue().getBookingId(),currentBookingRepository.getNewPolyline(),currentBookingRepository.getBooking().getValue().getBookingType()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.code()==204){
                    jobFinished(jobParameters,false);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                    jobFinished(jobParameters,true);
            }
        });


        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return true;
    }
}
