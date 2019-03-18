package com.fleet247.driver.viewmodels;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.annotation.NonNull;

import com.fleet247.driver.data.repository.BillingRepository;

public class HomeViewModel extends AndroidViewModel{
    MutableLiveData<Integer> locationStatus; //location status for saving if user has enabled location or not from popup 1. Yes, 2. No
    BillingRepository billingRepository;//To get Billing information if user closed app without entering cash collected in case of Cash Booking.

    public HomeViewModel(@NonNull Application application) {
        super(application);
        billingRepository=BillingRepository.getInstance(application);
        locationStatus=new MutableLiveData<>();
    }


    /**
     *
     * @param value
     * set the value of location status 1: true, 2: false. 0: initial State.
     */
    public void setLocationStatus(int value){
        locationStatus.setValue(value);
    }

    /**
     *
     * @return locationStatus LiveData.
     */
    public LiveData<Integer> getLocationStatus() {
        return locationStatus;
    }

    /**
     *
     * @return if driver needs to enter cash received from user or not.
     */
    public boolean getIsBillingPending(){
        return billingRepository.isBillingPending();
    }
}
