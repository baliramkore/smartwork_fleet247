package com.fleet247.driver.data.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.content.SharedPreferences;
import android.location.Location;
import android.util.Log;

import com.fleet247.driver.data.LiveLocation;
import com.fleet247.driver.data.models.LocationOffTime;
import com.fleet247.driver.data.models.endride.EndRideApiResponse;
import com.fleet247.driver.data.models.endride.InvoiceDetails;
import com.fleet247.driver.data.repository.taskhandler.AccessSharedPreferencesUtill;
import com.fleet247.driver.data.repository.taskhandler.DateTimeUtill;
import com.fleet247.driver.data.repository.taskhandler.SaveLocationOffTimeTask;
import com.fleet247.driver.data.room.dao.LocationOffTimeDAO;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.reflect.TypeToken;
import com.google.maps.android.PolyUtil;
import com.fleet247.driver.data.room.DriverAppDatabase;
import com.fleet247.driver.data.room.dao.LocationDAO;
import com.fleet247.driver.data.models.CustomLatLng;
import com.fleet247.driver.data.models.driverarrived.DriverArrivedApiResponse;
import com.fleet247.driver.data.models.driverstarted.DriverStartedApiResponse;

import com.fleet247.driver.data.models.startride.StartRideApiResponse;
import com.fleet247.driver.data.models.upcomingbooking.Booking;
import com.fleet247.driver.data.repository.taskhandler.DeleteLocationLocallyTask;
import com.fleet247.driver.data.repository.taskhandler.FetchLocationFromPlaceTask;
import com.fleet247.driver.data.repository.taskhandler.LocationUtill;
import com.fleet247.driver.data.repository.taskhandler.SaveLocationLocallyTask;
import com.fleet247.driver.retrofit.ConfigRetrofit;
import com.fleet247.driver.retrofit.DriverStartedAPI;
import com.fleet247.driver.utility.GsonStringConvertor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sandeep on 14/11/17.
 */

public class CurrentBookingRepository implements FetchLocationFromPlaceTask.LocationCallback,SaveLocationOffTimeTask {
    public static CurrentBookingRepository INSTANCE=null;
    SharedPreferences currentBookingPref;
    SharedPreferences trackingBookingPref;
    MutableLiveData<Booking> booking;
    MutableLiveData<Boolean> isCurrentBooking;
    MutableLiveData<String> tripStatus;
    MutableLiveData<DriverStartedApiResponse> driverStartedApiResponse;
    MutableLiveData<String> error;
    MutableLiveData<String> status;
    MutableLiveData<Boolean> hasEnteredAtPickupPoint;
    String garageLocation;
    DriverStartedAPI driverStartedAPI;
    String initialKms;
    String finalKms;
    Location lastLocation;
    LocationDAO locationDAO;
    LiveData<CustomLatLng> pickupLatLng;
    LiveData<CustomLatLng> dropLatLng;

    LiveData<Long> totalLocationOffTime;

    Application application;
    Executor locationExecutor;
    Executor diskIOExecutor;
    Executor locationOnOffExecutor;
    LocationUtill locationUtill;
    InvoiceDetails invoiceDetails;
    MutableLiveData<String> eta;
    LiveLocation liveLocation;
    double totalDistance;
    LocationOffTimeDAO locationOffTimeDAO;
    MutableLiveData<Boolean> locationStatus;


    private CurrentBookingRepository(Application application){
        this.application=application;
        isCurrentBooking=new MutableLiveData<>();
        booking=new MutableLiveData<>();
        tripStatus=new MutableLiveData<>();
        error=new MutableLiveData<>();
        driverStartedApiResponse=new MutableLiveData<>();
        status=new MutableLiveData<>();
        pickupLatLng=new MutableLiveData<>();
        dropLatLng=new MutableLiveData<>();
        locationStatus=new MutableLiveData<>();
        garageLocation=new String();
        locationDAO= DriverAppDatabase.getDriverDatabase(application.getApplicationContext()).locationDAO();
        locationOffTimeDAO=DriverAppDatabase.getDriverDatabase(application.getApplicationContext()).locationOffTimeDAO();
        Log.d("PickupLatLngs",GsonStringConvertor.gsonToString(locationDAO.getPickupLatLngs("8945e380613d55b5fc2968c42b1a58fd")));
        hasEnteredAtPickupPoint=new MutableLiveData<>();
        driverStartedAPI= ConfigRetrofit.configRetrofit(DriverStartedAPI.class);
        currentBookingPref=application.getSharedPreferences("currentBookingPref", MODE_PRIVATE);
        trackingBookingPref=application.getSharedPreferences("trackingPref",MODE_PRIVATE);
        isCurrentBooking.setValue(currentBookingPref.getBoolean("isActive",false));
        tripStatus.setValue(currentBookingPref.getString("tripStatus","0"));
        if (!currentBookingPref.getString("currentBooking","n").equals("n")){
            booking.setValue(GsonStringConvertor.stringToGson(currentBookingPref.getString("currentBooking","n"),Booking.class));
        }

        locationUtill=LocationUtill.getInstance(application);
        locationExecutor= Executors.newSingleThreadExecutor();
        diskIOExecutor=Executors.newSingleThreadExecutor();
        locationOnOffExecutor=Executors.newSingleThreadExecutor();
        invoiceDetails =new InvoiceDetails();
        eta=new MutableLiveData<>();
        liveLocation=LiveLocation.getInstance(application);
        totalDistance=Double.parseDouble(trackingBookingPref.getString("totalDistance","0"));
        totalLocationOffTime=locationOffTimeDAO.getTotalDuration();
        if(currentBookingPref.getString("last_location",null)!=null) {
            Log.d("LastLocation","sandeep "+currentBookingPref.getString("last_location","lastLocation"));
            lastLocation = GsonStringConvertor.stringToGson(currentBookingPref.getString("last_location",null),Location.class);
        }
    }


    public MutableLiveData<Boolean> getLocationStatus() {
        return locationStatus;
    }

    public void setLocationStatus(Boolean status) {
        this.locationStatus.setValue(status);
    }

    public MutableLiveData<String> getEta() {
        return eta;
    }

    public void setEta(String eta,String type) {
        switch (type) {
            case "FOREGROUND": this.eta.setValue(eta);
            break;
            case "BACKGROUND": this.eta.postValue(eta);
        }
    }

    public void setDistance(double distance){
        this.totalDistance=distance;
        trackingBookingPref.edit().putString("totalDistance",String.valueOf(totalDistance)).apply();
    }

    public double getTotalDistance() {
        return Double.parseDouble(trackingBookingPref.getString("totalDistance","0"));
    }

    public static CurrentBookingRepository getInstance(Application application){
        if (INSTANCE==null){
            INSTANCE=new CurrentBookingRepository(application);
        }
        return INSTANCE;
    }


    public MutableLiveData<Boolean> getHasEnteredAtPickupPoint() {
        return hasEnteredAtPickupPoint;
    }

    public Boolean getPaused() {
        return trackingBookingPref.getBoolean("isPaused",false);
    }

    public void setPaused(boolean isPaused){
        trackingBookingPref.edit().putBoolean("isPaused",isPaused).commit();
    }

    public void setHasEnteredAtPickupPoint(Boolean hasEnteredAtPickupPoint) {
        this.hasEnteredAtPickupPoint.postValue(hasEnteredAtPickupPoint);
    }

    public MutableLiveData<String> getError() {
        return error;
    }

    public LiveData<String> getStatus() {
        return status;
    }

    public LiveLocation getLiveLocation() {
        return liveLocation;
    }

    public LatLng getGarageLocation() {
        Log.d("GarageLocation",currentBookingPref.getString("garage","n"));
        if (!currentBookingPref.getString("garage","n").equals("n")){
           return GsonStringConvertor.stringToGson(
                   currentBookingPref.getString("garage","n"),LatLng.class);
        }
        return null;
    }

    public void setGarageLocation(LatLng garageLocation){
        Log.d("GarageLocation",GsonStringConvertor.gsonToString(garageLocation));
        currentBookingPref.edit().putString("garage",GsonStringConvertor.gsonToString(garageLocation)).apply();
        Log.d("GarageLocation",currentBookingPref.getString("garage","n"));
    }

    public LatLng getStartingLocation(){
        if (!currentBookingPref.getString("startingLocation","n").equals("n")){
            return GsonStringConvertor.stringToGson(currentBookingPref.getString("startingLocation","n"),LatLng.class);
        }
        return null;
    }

    public void setStartingLocation(LatLng startingLocation){
        currentBookingPref.edit().putString("startingLocation",GsonStringConvertor.gsonToString(startingLocation)).apply();
    }

    public LiveData<Booking> getBooking() {
        if (!currentBookingPref.getString("bookingInfo","n").equals("n")) {
            booking.setValue(GsonStringConvertor.stringToGson(currentBookingPref.getString("bookingInfo", "n"), Booking.class));
        }
        return booking;
    }

    public MutableLiveData<String> getTripStatus() {
        return tripStatus;
    }

    public void setTripStatus(String tripStatus){
        this.tripStatus.setValue(tripStatus);
        currentBookingPref.edit().putString("tripStatus",tripStatus).apply();
    }

    public LiveData<Boolean> getIsCurrentBooking() {
        return isCurrentBooking;
    }

    public String getInitialKms() {
        return initialKms;
    }

    public String getFinalKms() {
        return finalKms;
    }

    public void setBooking(String booking) {
        this.booking.setValue(GsonStringConvertor.stringToGson(booking,Booking.class));
        currentBookingPref.edit().putString("bookingInfo",booking).apply();
    }

    public double getStartKm(){
        double startKm=Double.parseDouble(currentBookingPref.getString("start_km","0"));
       // Log.d("StartKm",startKm+"");
        return startKm;
    }

    public void setIsCurrentBooking(boolean isCurrentBooking) {
        this.isCurrentBooking.setValue(isCurrentBooking);
        currentBookingPref.edit().putBoolean("isActive",isCurrentBooking).apply();
    }


    public void setInitialKms(String initialKms) {
        this.initialKms = initialKms;
        currentBookingPref.edit().putString("initialKms",initialKms).apply();
    }

    public void setFinalKms(String finalKms) {
        this.finalKms = finalKms;
        currentBookingPref.edit().putString("finalKms",finalKms).apply();
    }

    public void reSetStatus(){
        status.setValue("Empty");
    }


    public void initiateDriver(String accessToken,String bookingId,Location garageLocation,String startKm,String bookingType){
        driverStartedAPI.triggerDriverStarted(accessToken,bookingId,startKm,Double.toString(garageLocation.getLatitude()),Double.toString(garageLocation.getLongitude()), DateTimeUtill.getDate(),DateTimeUtill.getTime(),bookingType).enqueue(new Callback<DriverStartedApiResponse>() {
            @Override
            public void onResponse(Call<DriverStartedApiResponse> call, Response<DriverStartedApiResponse> response) {

                switch (response.code()){
                    case 200:
                        break;
                    case 204:
                        status.setValue("driver started successfully");
                        liveLocation.setOnTrip(true); //to decrease location update time as we need to track trip as we just need to see active drivers.
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
            public void onFailure(Call<DriverStartedApiResponse> call, Throwable t) {
                error.setValue("Connection Error:"+t.getMessage());
            }
        });
    }

    public void initiatedArrived(String accessToken,String bookingId,String garageDistance,String measuredGarageDistance){
        driverStartedAPI.initiateDriverArrived(accessToken, bookingId, garageDistance,measuredGarageDistance,DateTimeUtill.getDate(),DateTimeUtill.getTime(),getNewPolyline(),booking.getValue().getBookingType()).enqueue(new Callback<DriverArrivedApiResponse>() {
            @Override
            public void onResponse(Call<DriverArrivedApiResponse> call, Response<DriverArrivedApiResponse> response) {

                switch (response.code()){
                    case 200:
                        status.setValue("driver arrived at pickup point");
                        Log.d("StartOtp",response.body().getResponse().getTripDetails().getStartOtp());
                        setStartOtp(response.body().getResponse().getTripDetails().getStartOtp());
                        break;
                    case 204:
                        status.setValue("Empty Response Error");
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
            public void onFailure(Call<DriverArrivedApiResponse> call, Throwable t) {
                error.setValue("Connection Error");
            }
        });
    }

    public void initiateStartRide(String accessToken,
                                  String bookingId,
                                  Location startLocation,
                                  final String startKm){
        driverStartedAPI.initiateStartRide(accessToken, bookingId, Double.toString(startLocation.getLatitude()),Double.toString(startLocation.getLongitude()),startKm,DateTimeUtill.getDate(),DateTimeUtill.getTime()
        ,getNewPolyline(),getStartSignatoryName(),booking.getValue().getBookingType()).enqueue(new Callback<StartRideApiResponse>() {
            @Override
            public void onResponse(Call<StartRideApiResponse> call, Response<StartRideApiResponse> response) {

                switch (response.code()){
                    case 200:
                        status.setValue("ride started");
                        setEndOtp(response.body().getResponse().getTripDetails().getEndOtp());
                        Log.d("EndOtp",response.body().getResponse().getTripDetails().getEndOtp());
                        currentBookingPref.edit().putString("start_km",startKm).commit();
                        break;
                    case 204:
                        error.setValue("data not found");
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
            public void onFailure(Call<StartRideApiResponse> call, Throwable t) {
                error.setValue("Connection Error");
            }
        });
    }

    public void initiateEndRide(String accessToken,
                                String bookingId,
                                Location dropLocation,
                                String dropKmReading,
                                String calDistanceStartGaragetoDropGarage,
                                String calDistanceStartGarageToDropSelf,
                                String calDistancePickupToDropGoogle,
                                String estDistanceDropToEndGarage,
                                String estTimeDropToEndGarage,
                                String stateTax,
                                String parking,
                                String tollTax,
                                String extras){
        driverStartedAPI.initiateEndRide(accessToken, bookingId, Double.toString(dropLocation.getLatitude()),
                Double.toString(dropLocation.getLongitude()),dropKmReading,calDistanceStartGaragetoDropGarage,
                calDistanceStartGarageToDropSelf,calDistancePickupToDropGoogle,estDistanceDropToEndGarage,estTimeDropToEndGarage,stateTax,parking,
                tollTax,extras,DateTimeUtill.getDate(),DateTimeUtill.getTime(),getNewPolyline(),getEndSignatoryName(),booking.getValue().getBookingType()).enqueue(new Callback<EndRideApiResponse>() {
            @Override
            public void onResponse(Call<EndRideApiResponse> call, Response<EndRideApiResponse> response) {
               // Log.d("Response",GsonStringConvertor.gsonToString(response));
                switch (response.code()){
                    case 200:
                        invoiceDetails =response.body().getResponse().getInvoiceDetails();
                        Log.d("InvoiceDetails",GsonStringConvertor.gsonToString(invoiceDetails));
                        status.setValue("ride ended");
                        liveLocation.setOnTrip(false); //to reduce location update time
                        break;
                    case 204:
                        error.setValue("data not found");
                        break;
                    case 401:
                        error.setValue("Invalid Access Token");
                        break;
                    case 500:
                        error.setValue("Internal Server Error");
                        break;
                    default:
                        Log.d("Error","Connect Error");
                        error.setValue("Connect Error");
                }

            }

            @Override
            public void onFailure(Call<EndRideApiResponse> call, Throwable t) {
               // Log.d("Error","Connection Error");
                error.setValue("Connec Error "+t.getMessage());
            }
        });
    }

    public void deleteData(){
        currentBookingPref.edit().clear().commit();
        DeleteLocationLocallyTask deleteLocationLocallyTask=new DeleteLocationLocallyTask(locationDAO,pickupLatLng.getValue());
        diskIOExecutor.execute(deleteLocationLocallyTask);
        status.setValue("new");
        tripStatus.setValue("0");
        Log.d("tripStatus",tripStatus.getValue());
        booking.setValue(null);
        isCurrentBooking.setValue(false);
        locationOffTimeDAO.deleteLocationOffTime();
        //locationUtill.stopLocationUpdates();
        INSTANCE=null;
    }

    public LiveData<CustomLatLng> getPickupLatLng(Booking booking){
        pickupLatLng=locationDAO.getPickupLatLng(booking.getBookingId());
        if (pickupLatLng.getValue()==null){
           // Log.d("PickupLatLng","Empty");
            FetchLocationFromPlaceTask fetchLocationFromPlaceTask=new FetchLocationFromPlaceTask(application,this,booking);
            locationExecutor.execute(fetchLocationFromPlaceTask);
        }
        return pickupLatLng;
    }

    public LiveData<CustomLatLng> getDropLatLng(Booking booking){
        dropLatLng=locationDAO.getDropLatLng(booking.getBookingId());
        if (dropLatLng.getValue()==null && booking.getDropLocation()!=null && booking.getDropLocation().length()>0){
            FetchLocationFromPlaceTask fetchLocationFromPlaceTask=new FetchLocationFromPlaceTask(application,this,booking);
            locationExecutor.execute(fetchLocationFromPlaceTask);
        }
        return dropLatLng;
    }

    public void setStartOtp(String startOtp){
        currentBookingPref.edit().putString("startOtp",startOtp).apply();
    }

    public String getStartOtp(){
        return currentBookingPref.getString("startOtp","unavailable");
    }

    public String getEndOtp(){
        return currentBookingPref.getString("endOtp","unavailable");
    }

    public void setEndOtp(String endOtp){
        currentBookingPref.edit().putString("endOtp",endOtp).apply();
    }

    public void setPolyLine(Location location){
        if (lastLocation==null ||currentBookingPref.getString("polyline_new",null)==null ){
            List<LatLng> polyLatLng=new ArrayList<>();
            polyLatLng.add(new LatLng(location.getLatitude(),location.getLongitude()));
            currentBookingPref.edit().putString("polyline",PolyUtil.encode(polyLatLng)).apply();
           // Log.d("PolyLine","Created");
            currentBookingPref.edit().putString("polyline_new",GsonStringConvertor.gsonToString(polyLatLng)).apply();
            currentBookingPref.edit().putString("last_location",GsonStringConvertor.gsonToString(lastLocation)).apply();
            lastLocation=location;
        }else
        if (lastLocation.distanceTo(location)>=7){
            String polyLine=currentBookingPref.getString("polyline_new",null);
            List<LatLng> polyLatLng=GsonStringConvertor.gson.fromJson(polyLine,new TypeToken<List<LatLng>>(){}.getType());
            polyLatLng.add(new LatLng(location.getLatitude(),location.getLongitude()));
            currentBookingPref.edit().putString("polyline_new",GsonStringConvertor.gsonToString(polyLatLng)).apply();
            currentBookingPref.edit().putString("last_location",GsonStringConvertor.gsonToString(lastLocation)).apply();
            //Log.d("PolyLine","Appended");
            lastLocation=location;

        }
        else {
           //    Log.d("PolyLine","Inspected");
        }

    }

    public void setGpsStatus(String status){
        currentBookingPref.edit().putString("gpsStatus",status).apply();
    }

    public String getGpsStatus(){
        return currentBookingPref.getString("gpsStatus",null);
    }

    public String getPolyLine(){
        return currentBookingPref.getString("polyline",null);
    }

    public String getNewPolyline(){
        String polyLine=currentBookingPref.getString("polyline_new",null);
        List<LatLng> polyLatLng=GsonStringConvertor.gson.fromJson(polyLine,new TypeToken<List<LatLng>>(){}.getType());
        if (polyLatLng==null || polyLatLng.size()==0){
            return "";
        }
        return PolyUtil.encode(polyLatLng);
    }

    @Override
    public void pickupLocationCoordinate(LatLng pickupLocation,String bookingId) {
        Log.d("PickupLocation", GsonStringConvertor.gsonToString(pickupLocation));
        SaveLocationLocallyTask saveLocationLocallyTask=new SaveLocationLocallyTask(locationDAO,new CustomLatLng(bookingId,"PICKUP",pickupLocation.latitude,pickupLocation.longitude));
        diskIOExecutor.execute(saveLocationLocallyTask);
    }

    @Override
    public void dropLocationCoordinate(LatLng dropLocation,String bookingId) {
        Log.d("DropLocation", GsonStringConvertor.gsonToString(dropLocation));
        SaveLocationLocallyTask saveLocationLocallyTask=new SaveLocationLocallyTask(locationDAO,new CustomLatLng(bookingId,"DROP",dropLocation.latitude,dropLocation.longitude));
        diskIOExecutor.execute(saveLocationLocallyTask);

    }

    public com.fleet247.driver.data.models.endride.InvoiceDetails getInvoiceDetails(){
        return invoiceDetails;
    }

    public void saveSignature(String image,String type){
        AccessSharedPreferencesUtill.setJobBookingId(application.getApplicationContext(),booking.getValue().getBookingId());
        AccessSharedPreferencesUtill.saveImage(application.getApplicationContext(),image,type);
    }

    @Override
    public void saveLocationOffTime(final LocationOffTime locationOffTime) {
        Runnable saveLocationOffTimeRunnable=new Runnable() {
            @Override
            public void run() {
                locationOffTimeDAO.insertOffTime(locationOffTime);
            }
        };
        locationOnOffExecutor.execute(saveLocationOffTimeRunnable);
    }

    @Override
    public int getLocationOffTimeId() {
        return 0;
    }

    @Override
    public void deleteLocationOffData() {
        Runnable deleteLocatioOffData=new Runnable() {
            @Override
            public void run() {
                locationOffTimeDAO.deleteLocationOffTime();
                setGpsStatus(null);
            }
        };

        locationOnOffExecutor.execute(deleteLocatioOffData);
    }

    @Override
    public void updateLocationOnOffEndTime() {
        Runnable getLastOffLocation=new Runnable() {
            @Override
            public void run() {
                final LocationOffTime locationOffTime=locationOffTimeDAO.getLastOffTime();
                locationOffTime.setEndTime(Calendar.getInstance().getTimeInMillis());
                locationOffTime.setDuration(locationOffTime.getEndTime()-locationOffTime.getStartTime());
                locationOffTimeDAO.updateOfftime(locationOffTime);
            }
        };

        locationOnOffExecutor.execute(getLastOffLocation);
    }

    @Override
    public LiveData<Long> getAlllocationOffTime() {
        return locationOffTimeDAO.getTotalDuration();
    }

    public void saveStartSignatoryName(String name){
        currentBookingPref.edit().putString("start_signatory_name",name).commit();
    }

    public String getStartSignatoryName(){
        return currentBookingPref.getString("start_signatory_name",null);
    }

    public void saveEndSignatoryName(String name){
        currentBookingPref.edit().putString("end_signatory_name",name).commit();
    }

    public String getEndSignatoryName(){
        return currentBookingPref.getString("end_signatory_name",null);
    }

    public void setBillingBookingDetails(Booking booking,InvoiceDetails invoiceDetails){
        AccessSharedPreferencesUtill.setBillingData(application,booking,invoiceDetails);
    }

}
