package com.fleet247.driver.data.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.fleet247.driver.data.models.endride.InvoiceDetails;
import com.fleet247.driver.data.models.upcomingbooking.Booking;
import com.fleet247.driver.data.repository.taskhandler.AccessSharedPreferencesUtill;
import com.fleet247.driver.retrofit.CashCollectedAPI;
import com.fleet247.driver.retrofit.ConfigRetrofit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BillingRepository {

    private static BillingRepository INSTANCE;
    Application application;
    CashCollectedAPI cashCollectedAPI;
    MutableLiveData<String> status;


    private BillingRepository(Application application){
        cashCollectedAPI= ConfigRetrofit.configRetrofit(CashCollectedAPI.class);
        this.application=application;
        status=new MutableLiveData<>();
    }

    public static BillingRepository getInstance(Application application){
        if(INSTANCE==null) {
            INSTANCE = new BillingRepository(application);
        }
        return INSTANCE;
    }


    public boolean isBookingBillingPending(){
       return AccessSharedPreferencesUtill.isBillingPending(application);
    }

    public InvoiceDetails getInvoiceDetails(){
        return AccessSharedPreferencesUtill.getInvoiceDetails(application);
    }

    public Booking getBookingDetails(){
        return AccessSharedPreferencesUtill.getBillingBookingDetails(application);
    }

    public boolean isBillingPending(){
        return AccessSharedPreferencesUtill.isBillingPending(application);
    }

    public void clearBillingDetails(){
        AccessSharedPreferencesUtill.clearBillingData(application);
    }


    public void updateCashReceived(String cash){
        cashCollectedAPI.cashCollected(AccessSharedPreferencesUtill.getAccessToken(application),AccessSharedPreferencesUtill.getBillingBookingDetails(application).getBookingId(),AccessSharedPreferencesUtill.getInvoiceDetails(application).getAmountToBeCollected(),cash,getBookingDetails().getBookingType()
        ,getInvoiceDetails().getAgenWalletBalance())
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()){
                            AccessSharedPreferencesUtill.clearBillingData(application);
                            status.setValue("Booking Successful");
                        }
                        else {
                            status.setValue("Error");
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        status.setValue("Error");
                    }
                });
    }

    public LiveData<String> getStatus(){
        return status;
    }

    public void setStatus(String status){
        this.status.setValue(status);
    }

    public String getOpetatorNumber(){
        return AccessSharedPreferencesUtill.getOperatorContact(application);
    }
}
