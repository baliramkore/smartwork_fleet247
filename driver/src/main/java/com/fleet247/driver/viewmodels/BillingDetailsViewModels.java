package com.fleet247.driver.viewmodels;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;

import com.fleet247.driver.data.models.endride.InvoiceDetails;
import com.fleet247.driver.data.models.upcomingbooking.Booking;
import com.fleet247.driver.data.repository.BillingRepository;

public class BillingDetailsViewModels extends AndroidViewModel {

    BillingRepository billingRepository;

    MutableLiveData<String> cash;
    LiveData<String> status;

    public BillingDetailsViewModels(@NonNull Application application) {
        super(application);
        billingRepository=BillingRepository.getInstance(application);
        cash=new MutableLiveData<>();
        status=billingRepository.getStatus();
    }

    public InvoiceDetails getInvoiceDetails(){
        return billingRepository.getInvoiceDetails();
    }

    public Booking getBookingDetails(){
        return billingRepository.getBookingDetails();
    }


    public void setCashCollected(String cash){
        billingRepository.updateCashReceived(cash);
    }


    public LiveData<String> getStatus() {
        return status;
    }

    public void setStatus(String status){
        billingRepository.setStatus(status);
    }

    public boolean isBookingBillingPending(){
        return billingRepository.isBookingBillingPending();
    }

    public String getOperatorNumber(){
        return billingRepository.getOpetatorNumber();
    }
}
