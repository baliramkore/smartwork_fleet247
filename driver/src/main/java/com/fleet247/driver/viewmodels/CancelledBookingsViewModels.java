package com.fleet247.driver.viewmodels;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import com.fleet247.driver.data.models.cancelledbookings.Booking;
import com.fleet247.driver.data.repository.CancelledBookingRepository;

import java.util.List;

public class CancelledBookingsViewModels extends AndroidViewModel {

    private CancelledBookingRepository cancelledBookingRepository; //Instance of cancelledBooking Repository
    private LiveData<List<Booking>> cancelledBookingList; //List to store cancelled Bookings.
    private LiveData<String> error; //String to save error from server.

    public CancelledBookingsViewModels(@NonNull Application application) {
        super(application);
        cancelledBookingRepository=CancelledBookingRepository.getInstance(application);
        cancelledBookingList=cancelledBookingRepository.getCancelledBookingList();
        error=cancelledBookingRepository.getError();
    }

    /**
     *
     * @return observable list of cancelled bookings.
     */
    public LiveData<List<Booking>> getCancelledBookingList(){
        return cancelledBookingList;
    }

    /**
     * Calls method of cancelledRepository to get cancelled bookings from server.
     * @param accessToken
     */
    public void getCancelledBookings(String accessToken){
        cancelledBookingRepository.getCancelledBookings(accessToken);
    }

    /**
     *
     * @return observable error from network call(If Any).
     */
    public LiveData<String> getError(){
        return error;
    }

}
