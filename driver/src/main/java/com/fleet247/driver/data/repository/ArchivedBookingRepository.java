package com.fleet247.driver.data.repository;

import androidx.lifecycle.MutableLiveData;

import com.fleet247.driver.data.models.archivedbooking.ArchivedBookingApiResponse;
import com.fleet247.driver.data.models.archivedbooking.Booking;
import com.fleet247.driver.retrofit.ArchivedBookingAPI;
import com.fleet247.driver.retrofit.ConfigRetrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sandeep on 6/2/18.
 */

public class ArchivedBookingRepository {

    MutableLiveData<List<Booking>> archivedBookingList; // List to store Archived Booking
    MutableLiveData<String> error; // String to store error from archived Booking api
    ArchivedBookingAPI archivedBookingAPI; //Retrofit API to get Archived Booking.

    public ArchivedBookingRepository(){
        archivedBookingList=new MutableLiveData<>();
        error=new MutableLiveData<>();
        archivedBookingAPI= ConfigRetrofit.configRetrofit(ArchivedBookingAPI.class);
    }

    /**
     *
     * @return list of Archived Bookings.
     */
    public MutableLiveData<List<Booking>> getArchivedBookingList() {
        return archivedBookingList;
    }

    /**
     *
     * @return Error from Archived Booking API.
     */
    public MutableLiveData<String> getError() {
        return error;
    }


    /**
     * Calls archivedBookingAPI and updates the value of archivedBookingList if request is successful.
     * else update the value of error.
     * @param accessToken
     */
    public void getArchivedBookings(String accessToken){
        archivedBookingAPI.getArchivedBookings(accessToken).enqueue(new Callback<ArchivedBookingApiResponse>() {
            @Override
            public void onResponse(Call<ArchivedBookingApiResponse> call, Response<ArchivedBookingApiResponse> response) {

                switch (response.code()){
                    case 200:
                        archivedBookingList.setValue(response.body().getResponse().getBookings());
                        break;
                    case 204:
                        error.setValue("No Booking Available");
                        break;
                    case 401:
                        error.setValue("Invalid Access Token");
                        break;
                    case 500:
                        error.setValue("Internal Server Error");
                        break;
                    default:
                        error.setValue("Connection Error");
                }
            }

            @Override
            public void onFailure(Call<ArchivedBookingApiResponse> call, Throwable t) {
                error.setValue("Connection Error: "+t.getMessage());
            }
        });
    }
}
