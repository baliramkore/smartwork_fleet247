package com.fleet247.driver.viewmodels;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import com.fleet247.driver.data.models.archivedbooking.Booking;
import com.fleet247.driver.data.repository.ArchivedBookingRepository;

import java.util.List;

/**
 * Created by sandeep on 7/2/18.
 */

public class ArchivedBookingViewModel extends AndroidViewModel {

    ArchivedBookingRepository archivedBookingRepository; //Archived booking repository reference
    LiveData<List<Booking>> archivedBookingList; // Archived Bookings List.
    LiveData<String> error; // Archived Booking error.

    public ArchivedBookingViewModel(@NonNull Application application) {
        super(application);
        archivedBookingRepository=new ArchivedBookingRepository();
        archivedBookingList=archivedBookingRepository.getArchivedBookingList();
        error=archivedBookingRepository.getError();
    }

    /**
     *
     * @return list of archived bookings.
     */
    public LiveData<List<Booking>> getArchivedBookingList() {
        return archivedBookingList;
    }

    /**
     *
     * @return error from network call(If Any)
     */
    public LiveData<String> getError() {
        return error;
    }

    /**
     * Calls method of repository to get archived booking list from server.
     * @param accessToken
     */
    public void getArchivedBookings(String accessToken){
        archivedBookingRepository.getArchivedBookings(accessToken);
    }
}
