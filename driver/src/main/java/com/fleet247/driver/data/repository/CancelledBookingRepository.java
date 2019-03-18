package com.fleet247.driver.data.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.util.Log;

import com.fleet247.driver.data.models.cancelledbookings.Booking;
import com.fleet247.driver.data.models.cancelledbookings.CancelledBookingApiResponse;
import com.fleet247.driver.retrofit.CancelledBookingAPI;
import com.fleet247.driver.retrofit.ConfigRetrofit;
import com.fleet247.driver.utility.GsonStringConvertor;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CancelledBookingRepository {

    private static CancelledBookingRepository INSTANCE; // Instance of CancelledBookingRepository.
    private Application application; //Application object for context.
    private CancelledBookingAPI cancelledBookingAPI; //Retrofit Api to get Cancelled booking list from server.
    private MutableLiveData<List<Booking>> cancelledBookings; // List of Cancelled Bookings.
    private MutableLiveData<String> error; // Error from cancelledBookingAPI.

    private CancelledBookingRepository(Application application){
        this.application=application;
        cancelledBookingAPI= ConfigRetrofit.configRetrofit(CancelledBookingAPI.class);
        cancelledBookings=new MutableLiveData<>();
        error=new MutableLiveData<>();
    }

    /**
     * Create a single Instance of CancelledBookingRepository.
     * It follows Singleton pattern.
     * @param application
     * @return
     */
    public static CancelledBookingRepository getInstance(Application application){
        if (INSTANCE==null){
            INSTANCE=new CancelledBookingRepository(application);
        }
        return INSTANCE;
    }

    /**
     *
     * @return list of cancelled Bookings.
     */
    public LiveData<List<Booking>> getCancelledBookingList(){
        return cancelledBookings;
    }

    /**
     *
     * @return error from cancelledBookingAPI.
     */
    public LiveData<String> getError(){
        return error;
    }

    /**
     * This method calls cancelledBooking Api and updates cancelledBookings if request is successful.
     * else it updates the value of error.
     * @param accessToken
     */
    public void getCancelledBookings(String accessToken){
        cancelledBookingAPI.getCancelledBooking(accessToken).enqueue(new Callback<CancelledBookingApiResponse>() {
            @Override
            public void onResponse(Call<CancelledBookingApiResponse> call, Response<CancelledBookingApiResponse> response) {
                switch (response.code()){
                    case 200: cancelledBookings.setValue(response.body().getData().getBookings());
                        Log.d("CancelledBookings", GsonStringConvertor.gsonToString(response.body().getData().getBookings()));
                        break;
                    case 204: error.setValue("No Bookings Available");
                        break;
                    case 401: error.setValue("Invalid Access Token");
                        break;
                    case 500: error.setValue("Internal Server Error");
                        break;
                    default:error.setValue("Connection Error");
                }
            }

            @Override
            public void onFailure(Call<CancelledBookingApiResponse> call, Throwable t) {
                error.setValue("Connection Error: "+t.getMessage());
            }
        });
    }

}
