package com.fleet247.driver.viewmodels;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import com.fleet247.driver.data.models.CustomLatLng;
import com.fleet247.driver.data.models.upcomingbooking.Booking;
import com.fleet247.driver.data.repository.UpcomingBookingsRepository;

import java.util.List;

/**
 * Created by sandeep on 13/11/17.
 */

public class UpcomingBookingViewModel extends AndroidViewModel{

    UpcomingBookingsRepository upcomingBookingsRepository; //Instance of upcomingBookingRepository.
    LiveData<List<Booking>> upcomingBookingList; //List to store upcoming Bookings.
    LiveData<String> error; // String to store upcomingApiError.



    public UpcomingBookingViewModel(@NonNull Application application) {
        super(application);
        upcomingBookingsRepository=UpcomingBookingsRepository.getInstance(application);
        upcomingBookingList=upcomingBookingsRepository.getUpcomingBookings();
        error=upcomingBookingsRepository.getError();
    }

    /**
     *
     * @return list of upcoming bookings.
     */
    public LiveData<List<Booking>> getUpcomingBookingList() {
        return upcomingBookingList;
    }

    /**
     *
     * @return error from upcomingbookingAPI
     */
    public LiveData<String> getError() {
        return error;
    }

    /**
     * Delete the list of upcoming bookings.
     */
    public void deleteUpcomingBookingList(){
        upcomingBookingsRepository.deleteUpcomingBookingList();
    }

    /**
     * Calls method to get bookings from server.
     * @param accessToken
     */
    public void getUpcomingBookings(String accessToken){
        upcomingBookingsRepository.getUpcomingBooking(accessToken);
    }

    public LiveData<CustomLatLng> getPickupLatLng(String bookingId){
        return upcomingBookingsRepository.pickupLatlngs(bookingId);
    }

    /**
     *
     * @return status if booking is cancelled or not.
     */
    public LiveData<String> getCancelledBookingStatus(){
        return upcomingBookingsRepository.getCancelledBookingStatus();
    }

    /**
     * Calls method to cancel a booking with following parameters.
     * @param accessToken
     * @param bookingId
     * @param cancelReason
     */
    public void cancelBooking(String accessToken,String bookingId,String cancelReason){
        upcomingBookingsRepository.cancelBooking(accessToken,bookingId,cancelReason);
    }

    /**
     * calls method to set if start Trip Tutorial.
     */
    public void setShownTripStartedTutorial(){
        upcomingBookingsRepository.setShownTripStartedTutorial();
    }

    /**
     *
     * @return if start trip tutorial is shown or not.
     */
    public boolean getShownTripStratedTutorial(){
        return upcomingBookingsRepository.getShownTripStratedTutorial();
    }

}
