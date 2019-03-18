package com.fleet247.driver.services;

import android.app.job.JobParameters;
import android.app.job.JobService;
import androidx.lifecycle.ViewModelProviders;

import com.fleet247.driver.data.repository.LoginRepository;
import com.fleet247.driver.data.repository.taskhandler.AccessSharedPreferencesUtill;
import com.fleet247.driver.retrofit.ConfigRetrofit;
import com.fleet247.driver.retrofit.UploadSignature;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadSignatureJobService extends JobService {
    LoginRepository loginRepository;
    @Override
    public boolean onStartJob(final JobParameters jobParameters) {
        loginRepository=LoginRepository.getInstance(getApplication());
        UploadSignature uploadSignature= ConfigRetrofit.configRetrofit(UploadSignature.class);
        uploadSignature.storeSignature(loginRepository.getAccessToken().getValue(),
                AccessSharedPreferencesUtill.getJobBookingId(getApplicationContext()),
                "start",
                AccessSharedPreferencesUtill.getStartingSignature(getApplicationContext()),
                "Ajent").enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.code()==204){
                    jobFinished(jobParameters,false);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return true;
    }
}
