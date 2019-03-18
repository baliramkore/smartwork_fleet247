package com.fleet247.driver.services;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import com.fleet247.driver.data.models.Signature;
import com.fleet247.driver.data.models.storesignature.StoreSignatureApiResponse;
import com.fleet247.driver.data.repository.LoginRepository;
import com.fleet247.driver.data.repository.SignatureRepository;
import com.fleet247.driver.retrofit.ConfigRetrofit;
import com.fleet247.driver.retrofit.UploadSignatureAPI;
import com.fleet247.driver.utility.GsonStringConvertor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadSignaturesJobService extends JobService {

    SignatureRepository signatureRepository;
    LoginRepository loginRepository;
    Signature signature;

    @Override
    public boolean onStartJob(final JobParameters jobParameters) {
        Log.d("Inside","UploadSignatures");
        signatureRepository=SignatureRepository.getInstance(getApplication());
        loginRepository=LoginRepository.getInstance(getApplication());
        signature=signatureRepository.getSignature();
        UploadSignatureAPI uploadSignatureAPI=ConfigRetrofit.configRetrofit(UploadSignatureAPI.class);
        Log.d("AllSignatures", GsonStringConvertor.gsonToString(signatureRepository.getAllSignatures()));
        if (signature!=null){
            uploadSignatureAPI.storeSignature(loginRepository.getAccessToken().getValue(),
                    signature.getBookingId(),
                    signature.getType(),
                    signature.getEncodedSignatureImage(),
                    signature.getBookingType()).enqueue(new Callback<StoreSignatureApiResponse>() {
                @Override
                public void onResponse(Call<StoreSignatureApiResponse> call, Response<StoreSignatureApiResponse> response) {
                    if(response.isSuccessful()){
                        signatureRepository.deleteSignature(response.body().getData().getBookingId(),response.body().getData().getType());
                        Log.d("AllSignaturesDeleted", GsonStringConvertor.gsonToString(signatureRepository.getAllSignatures()));
                        if (signatureRepository.getSignature()==null){
                            Log.d("UploadSignatureJob","Ended");
                            jobFinished(jobParameters,false);
                        }
                        else {
                            Log.d("UploadSignatureJob","Rescheduled");
                            jobFinished(jobParameters,true);
                        }
                    }
                }

                @Override
                public void onFailure(Call<StoreSignatureApiResponse> call, Throwable t) {
                }
            });
        }
        else {
            return false;
        }

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return true;
    }
}
