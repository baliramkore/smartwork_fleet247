package com.fleet247.driver.data.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.util.Log;

import com.fleet247.driver.data.repository.taskhandler.AccessSharedPreferencesUtill;
import com.fleet247.driver.retrofit.CancelBookingAPI;
import com.google.android.gms.maps.model.LatLng;
import com.fleet247.driver.data.room.DriverAppDatabase;
import com.fleet247.driver.data.room.dao.BookingDAO;
import com.fleet247.driver.data.room.dao.LocationDAO;
import com.fleet247.driver.data.models.CustomLatLng;
import com.fleet247.driver.data.models.upcomingbooking.Booking;
import com.fleet247.driver.data.models.upcomingbooking.UpcomingBookingApiResponse;
import com.fleet247.driver.data.repository.taskhandler.FetchLocationFromPlaceTask;
import com.fleet247.driver.data.repository.taskhandler.SaveLocationLocallyTask;
import com.fleet247.driver.retrofit.ConfigRetrofit;
import com.fleet247.driver.retrofit.UpcomingBookingAPI;
import com.fleet247.driver.utility.GsonStringConvertor;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sandeep on 13/11/17.
 */

public class UpcomingBookingsRepository implements FetchLocationFromPlaceTask.LocationCallback{

    public static UpcomingBookingsRepository INSTANCE=null; // Instance of UpcomingBookingRepository.
    private MutableLiveData<List<Booking>> upcomingBookings; // LiveData to store upComingBookings.
    private MutableLiveData<String> error; // LiveData to store upcomingBooking error;
    private UpcomingBookingAPI upcomingBookingAPI; // Retrofit API to get upcomingBooking.
    private Application application; // Application Context.
    private BookingDAO bookingDAO; // Booking Data access Object.
    private LocationDAO locationDAO; // Location Data Access Object.
    private LiveData<CustomLatLng> pickupLatLng; // Pickup LatLng object.
    private LiveData<CustomLatLng> dropLatLng; // Drop LatLng object.
    private Executor locationExecutor; // LocationExecutor to get lat lng of pickup location in background.
    private Executor diskIOExecutor;  // Disk IO Executor to save data in local database from different thread.
    private CancelBookingAPI cancelBookingAPI; // Retrofit API to cancel booking.
    private MutableLiveData<String> cancelledBookingStatus; // Response from cancelBookingAPI

    private UpcomingBookingsRepository(Application application){
        upcomingBookings=new MutableLiveData<>();
        this.application=application;
        error=new MutableLiveData<>();
        bookingDAO= DriverAppDatabase.getDriverDatabase(application.getApplicationContext()).bookingDAO();
        locationDAO=DriverAppDatabase.getDriverDatabase(application.getApplicationContext()).locationDAO();
        upcomingBookingAPI= ConfigRetrofit.configRetrofit(UpcomingBookingAPI.class);
        pickupLatLng=new MutableLiveData<>();
        dropLatLng=new MutableLiveData<>();
        locationExecutor=Executors.newSingleThreadExecutor();
        diskIOExecutor=Executors.newSingleThreadExecutor();
        cancelBookingAPI=ConfigRetrofit.configRetrofit(CancelBookingAPI.class);
        cancelledBookingStatus=new MutableLiveData<>();
    }


    /**
     *
     * @param application
     * @return Instance of UpcomingBookingRepository
     * It follows Singleton Pattern.
     */
    public static UpcomingBookingsRepository getInstance(Application application){
        INSTANCE=new UpcomingBookingsRepository(application);
        return INSTANCE;
    }

    public static void deleteInstance(){
        INSTANCE=null;
    }

    /**
     *
     * @return if booking is cancelled or not.
     */
    public LiveData<String> getCancelledBookingStatus(){
        return cancelledBookingStatus;
    }

    /**
     *
     * @return list of upcoming bookings
     */
    public LiveData<List<Booking>> getUpcomingBookings() {
        return upcomingBookings;
    }

    /**
     *
     * @return error in case if getting upcoming booking call is unsuccessful.
     */
    public LiveData<String> getError() {
        return error;
    }

    /**
     * delete upcoming bookings.
     */
    public void deleteUpcomingBookingList(){
        upcomingBookings.setValue(null);
    }

    /**
     *
     * @param accessToken
     *Get upcomingBooking from server and set the value of upcoming bookings if request is successful
     * else set the value of error.
     */
    public void getUpcomingBooking(String accessToken){
        upcomingBookingAPI.getUpcomingBooking(accessToken).enqueue(new Callback<UpcomingBookingApiResponse>() {
            @Override
            public void onResponse(Call<UpcomingBookingApiResponse> call, Response<UpcomingBookingApiResponse> response) {

                switch (response.code()) {
                    case 200:
                        upcomingBookings.postValue(response.body().getData().getBookings());
                        getLocation(response.body().getData().getBookings().get(0));
                        break;
                    case 204:
                        error.setValue("No Booking Available");
                        break;
                    case 401:
                        error.setValue("Invalid Access Token");
                        break;
                    case 500:
                        error.setValue("Tnternal Server Error");
                        break;
                    default:error.setValue("Connection Error");
                }

            }

            @Override
            public void onFailure(Call<UpcomingBookingApiResponse> call, Throwable t) {
                error.postValue("Connection Error "+t.getMessage());
            }
        });

    }

    /**
     * Start locationExecutor for getting lat lang of pickup Location.
     * @param booking
     */
    public void getLocation(Booking booking){
        Runnable runnable=new FetchLocationFromPlaceTask(application,this,booking);
        locationExecutor.execute(runnable);
    }

    /**
     * Callback from FetchLocationFromPlace Task.
     * @param pickupLocation
     * @param bookingId
     * save pickupLocation with bookingId in local database using diskIOExecutor.
     */
    @Override
    public void pickupLocationCoordinate(LatLng pickupLocation,String bookingId) {
        Log.d("PickupLocation", GsonStringConvertor.gsonToString(pickupLocation));
        SaveLocationLocallyTask saveLocationLocallyTask=new SaveLocationLocallyTask(locationDAO,new CustomLatLng(bookingId,"PICKUP",pickupLocation.latitude,pickupLocation.longitude));
        diskIOExecutor.execute(saveLocationLocallyTask);
       // DeleteLocationLocallyTask deleteLocationLocallyTask=new DeleteLocationLocallyTask(locationDAO,new CustomLatLng(bookingId,"PICKUP",pickupLocation.latitude,pickupLocation.longitude));
       // diskIOExecutor.execute(deleteLocationLocallyTask);
    }

    /**
     * Callback from FetchLocationFromPlace Task.
     * @param dropLocation
     * @param bookingId
     * save dropLocation with bookingId in local database using diskIOExecutor.
     */
    @Override
    public void dropLocationCoordinate(LatLng dropLocation,String bookingId) {
        Log.d("DropLocation", GsonStringConvertor.gsonToString(dropLocation));
        SaveLocationLocallyTask saveLocationLocallyTask=new SaveLocationLocallyTask(locationDAO,new CustomLatLng(bookingId,"DROP",dropLocation.latitude,dropLocation.longitude));
        diskIOExecutor.execute(saveLocationLocallyTask);
      //  DeleteLocationLocallyTask deleteLocationLocallyTask=new DeleteLocationLocallyTask(locationDAO,new CustomLatLng(bookingId,"DROP",dropLocation.latitude,dropLocation.longitude));
      //  diskIOExecutor.execute(deleteLocationLocallyTask);

    }

    /**
     *
     * @param bookingId
     * @return pickupLatLng from database.
     */
    public LiveData<CustomLatLng> pickupLatlngs(String bookingId){
        Log.d("PickupLatLg", GsonStringConvertor.gsonToString(locationDAO.getPickupLatLng(bookingId)));
        Log.d("DropLatLg", GsonStringConvertor.gsonToString(locationDAO.getDropLatLngs(bookingId)));
        return locationDAO.getPickupLatLng(bookingId);
    }

    /**
     * Calls cancel booking API and updates the value of cancelledBookingStatus if request is successful
     * else update the value of error.
     * @param accessToken
     * @param bookingId
     * @param cancelReason
     */
    public void cancelBooking(String accessToken,String bookingId,String cancelReason){
        cancelBookingAPI.cancelBooking(accessToken,bookingId,cancelReason,upcomingBookings.getValue().get(0).getBookingType()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                switch (response.code()){
                    case 204:
                        cancelledBookingStatus.setValue("Booking Cancelled Successfully");
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
            public void onFailure(Call<String> call, Throwable t) {
                error.setValue("Connection Error: "+t.getMessage());
            }
        });
    }

    /**
     * set true if Start trip tutorial is shown.
     */
    public void setShownTripStartedTutorial(){
        AccessSharedPreferencesUtill.setShownStartTripTutorial(application);
    }

    /**
     *
     * @return if Start trip tutorial screen is shown or not
     */
    public boolean getShownTripStratedTutorial(){
        return AccessSharedPreferencesUtill.getShownStartTripTutorial(application);
    }
}
