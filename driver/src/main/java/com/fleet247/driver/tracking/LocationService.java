package com.fleet247.driver.tracking;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Location;
import android.os.IBinder;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.fleet247.driver.data.LiveLocation;
import com.fleet247.driver.data.repository.LoginRepository;
import com.fleet247.driver.data.repository.taskhandler.AccessSharedPreferencesUtill;
import com.fleet247.driver.data.repository.taskhandler.FetchPlacesFromLocationTask;
import com.fleet247.driver.utility.NotificationUtill;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.fleet247.driver.R;
import com.fleet247.driver.activities.MainActivity;
import com.fleet247.driver.data.models.googledistancematrix.DistanceMatrixApiResponse;
import com.fleet247.driver.data.models.login.Driver;
import com.fleet247.driver.data.models.tracking.FirebaseTrackingModel;
import com.fleet247.driver.data.models.upcomingbooking.Booking;
import com.fleet247.driver.data.repository.CurrentBookingRepository;
import com.fleet247.driver.retrofit.ConfigRetrofit;
import com.fleet247.driver.retrofit.GoogleAPI;
import com.fleet247.driver.utility.GsonStringConvertor;
import com.fleet247.driver.utility.NetworkStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sandeep on 13/9/17.
 */

public class LocationService extends Service implements SharedPreferences.OnSharedPreferenceChangeListener,LifecycleOwner,FetchPlacesFromLocationTask.PlacesCallback{


    private LifecycleRegistry mLifecycleRegistry; //for making it lifecycleOwner;

    SharedPreferences trackingSharedPreference,loginInfo;
    LoginRepository loginRepository;
    private static final String TAG="LocationService";
    FirebaseDatabase firebaseDatabase;
    DatabaseReference operatorReference;
    DatabaseReference cityReference;
    Booking booking;
    String senderId;
    FirebaseApp trackingApp;
    GoogleAPI googleApi;
    Location currentLocation;
    String eta;
    int etaCalled=0;
    String tripStatus="1";
    CurrentBookingRepository  currentBookingRepository;
    Driver driverDetails;
    ArrayList<LatLng> pathLatLng;
    Executor locationToPlacesExecutor;
    LiveLocation liveLocation;

    String currentState;
    String currentCity;

    String notificationMessage="You are moving to Pickup guest";

    double distanceTravelled=0;
    int etaCounter=0; // to make sure ETA is calculated after every 90 seconds.

    Timer timer=new Timer();

    @Override
    public void onCreate() {
        super.onCreate();

        mLifecycleRegistry=new LifecycleRegistry(this);// for providing lifecycle to get event from viewModel or repository.
        mLifecycleRegistry.markState(Lifecycle.State.CREATED); // to notify that service is created.

        Log.d(TAG,"Inside OnCreate");
        pathLatLng=new ArrayList<>();
        googleApi=ConfigRetrofit.configGoogleRetrofit(GoogleAPI.class);

        locationToPlacesExecutor= Executors.newSingleThreadExecutor();
        loginRepository=LoginRepository.getInstance(getApplication());

        currentState= AccessSharedPreferencesUtill.getState(getApplicationContext());
        currentCity=AccessSharedPreferencesUtill.getCity(getApplicationContext());

      /* FirebaseOptions options = new FirebaseOptions.Builder()
                .setApplicationId("1:846362637861:android:dfcf6a4ea1b68e1e") // Required for Analytics.
                .setApiKey("AIzaSyBgXCStOyBQsA7RiaP-r8bh5b01Xnvs-Vk") // Required for Auth.
                .setDatabaseUrl("https://taxivaxi-tracking-project.firebaseio.com/") // Required for RTDB.
                .build();
        //sendTrckingLinkAPI= ConfigRetrofit.configRetrofit(SendTrckingLinkAPI.class);

        List<FirebaseApp> firebaseAppList=FirebaseApp.getApps(this);
        boolean flag=false;
        for (int i=0;i<firebaseAppList.size();i++){
            Log.d("FirebaseAppName",firebaseAppList.get(i).getName());
            if (firebaseAppList.get(i).getName().equals("tracking")){
                trackingApp = FirebaseApp.getInstance("tracking");
                flag=true;
                break;
            }
        }
        if (!flag){
            FirebaseApp.initializeApp(this , options, "tracking");
        }
        */

        trackingSharedPreference =getSharedPreferences("trackingPref",MODE_PRIVATE);
        loginInfo=getSharedPreferences("driverPref",MODE_PRIVATE);

        trackingSharedPreference.registerOnSharedPreferenceChangeListener(this);
        senderId= GsonStringConvertor.stringToGson(loginInfo.getString("driverInfo","n"),Driver.class).getId();
        trackingApp = FirebaseApp.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance(trackingApp);
        currentBookingRepository=CurrentBookingRepository.getInstance(getApplication());
        tripStatus=trackingSharedPreference.getString("tripStatus","1");
        booking=currentBookingRepository.getBooking().getValue();
        driverDetails=loginRepository.getDriver().getValue();
        operatorReference = firebaseDatabase.getReference("operators").child(driverDetails.getOperatorId()).child("drivers").child(driverDetails.getId());
        cityReference=firebaseDatabase.getReference().child("states");

        distanceTravelled=currentBookingRepository.getTotalDistance();
        liveLocation=LiveLocation.getInstance(getApplication());
        if (liveLocation==null){
            Log.e("LiveData","Null");
        }else {
            Log.d("LiveData","Not Null");
        }
        liveLocation.observe( this, new Observer<Location>() {
            @Override
            public void onChanged(@Nullable Location location) {
                onLocationChanged(location);
            }
        });

        currentBookingRepository.getTripStatus().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if(s!=null && s.length()>0) {
                    switch (Integer.parseInt(s)) {
                        case 1: notificationMessage = "You are moving to Pickup Guest";
                                tripStatus=s;
                                break;
                        case 2: notificationMessage="You are waiting for Guest at Pickup Point";
                                tripStatus=s;
                                break;
                        case 3: notificationMessage="You are moving to Drop Guest";
                                tripStatus=s;
                                break;
                    }
                }
            }
        });

        scheduleEOT();

    }

    @Override
    public int onStartCommand(Intent intent,int flags, int startId) {

        mLifecycleRegistry.markState(Lifecycle.State.STARTED); // To notify that the service is started.

            Log.d(TAG,"Inside OnStartCommand");
            Log.d(TAG,"Inside OnSendTrack");

            Intent intent1 = new Intent(this, MainActivity.class);
            intent1.putExtra("fromNotification",true);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent1, 0);

            NotificationUtill.createOnRideServiceNotificationChannel(this);

            Notification notification = new NotificationCompat.Builder(this,NotificationUtill.trackingChanneId)
                    .setContentTitle("Tracking Enabled")
                    .setContentText(notificationMessage)
                    .setSmallIcon(R.drawable.fleetnotificationlogo)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.fleet247))
                    .setContentIntent(pendingIntent)
                    .build();
            startForeground(1001, notification);

            if (isServiceRunningInForeground(this,LocationService.class)){
                Log.d("IsForeground","True");
            }
            else {
                Log.d("IsForeground","False");
            }

        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        mLifecycleRegistry.markState(Lifecycle.State.DESTROYED); // To notify that the service is destroyed.

        Log.d(TAG,"Destroyed");
        timer.cancel();
        liveLocation.removeObservers(this);
        operatorReference.setValue(null);
        if(currentState!=null && currentCity!=null) {
            cityReference.child(currentState).child(currentCity).child(driverDetails.getOperatorId()).child("drivers").child(driverDetails.getId())
                    .setValue((null));
        }
        trackingSharedPreference.unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroy();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onLocationChanged(Location location) {
       // Log.d("Location","Sandeep "+GsonStringConvertor.gsonToString(location));
        if(location.getAccuracy()>50){
            return;
        }
        if (currentLocation!=null) {
            distanceTravelled = distanceTravelled + currentLocation.distanceTo(location);
        }
        currentLocation=location;
        currentBookingRepository.setDistance(distanceTravelled);
        if (etaCalled==0){
            etaCalled=1;
            getEOT();
        }
        if (NetworkStatus.isInternetConnected(getApplication())) {
           FirebaseTrackingModel firebaseTrackingModel=new FirebaseTrackingModel(
                   loginRepository.getAccessToken().getValue(),
                   null,
                   location.getLatitude(),
                   location.getLongitude(),
                   eta,
                   Double.toString(currentLocation.getBearing()),
                   tripStatus,
                   "On Ride",
                   driverDetails.getDriverName(),
                   booking.getBookingId(),
                   booking.getTaxiModel(),
                   booking.getTaxiTypeAssigned(),
                   booking.getRegistrationNumber(),
                   booking.getReferenceNo(),
                   booking.getPickupLocation());
          // ("Firebase",GsonStringConvertor.gsonToString(firebaseTrLog.dackingModel));
           // Log.i("Status","Internet Connection"+ GsonStringConvertor.gsonToString(location));
           if (currentCity!=null && currentState!=null) {
               cityReference.child(currentState).child(currentCity).child(driverDetails.getOperatorId()).child("drivers").child(driverDetails.getId())
                       .setValue(firebaseTrackingModel.toMap());
               Map<String,String > map=new HashMap<>();
               map.put("Name","Sandeep");
               operatorReference.setValue(firebaseTrackingModel.toMap());
             //  Log.d("FirebaseModels",GsonStringConvertor.gsonToString(firebaseTrackingModel));
           }
        }
        else {
            Log.i("Status","No Internet Connection");
        }
    }

    void scheduleEOT(){
        timer.scheduleAtFixedRate(
                new TimerTask() {
            @Override
            public void run() {
                getEOT();
            }
        },4000,30000);
    }

    void getEOT(){
        if (currentLocation==null){
            Log.d("Current Location","null");
            return;
        }
       locationToPlacesExecutor.execute(new FetchPlacesFromLocationTask(getApplication(),LocationService.this,currentLocation));
       currentBookingRepository.setPolyLine(currentLocation);

       if (etaCounter!=0){
           etaCounter--;
           return;
       }
       else {
           etaCounter = 3;
           if (tripStatus.equals("3")) {
               eta = "Current Location";
               Log.d("TripType1", booking.getTourType() + " " + tripStatus);
               currentBookingRepository.setEta(eta, "BACKGROUND");
           } else {
               Log.d("TripType2", booking.getTourType() + " " + tripStatus);
               String pickUp = currentLocation.getLatitude() + "," + currentLocation.getLongitude();
               String drop = trackingSharedPreference.getString("dropLocation", "n");
               googleApi.getEstimateDistanceAndTime(pickUp, drop, "now", "best_guess", getResources().getString(R.string.distance_matrix_key)).enqueue(
                       new Callback<DistanceMatrixApiResponse>() {
                           @Override
                           public void onResponse(Call<DistanceMatrixApiResponse> call, Response<DistanceMatrixApiResponse> response) {
                               if (response.isSuccessful() && response.body().getStatus().equals("OK") && response.body().getRows().get(0).getElements() != null) {
                                   eta = response.body().getRows().get(0).getElements().get(0).getDurationInTraffic().getText();
                                   currentBookingRepository.setEta(eta, "FOREGROUND");
                                   Log.d("EAT", eta);
                                   if (NetworkStatus.isInternetConnected(getApplication())) {
                                       locationToPlacesExecutor.execute(new FetchPlacesFromLocationTask(getApplication(), LocationService.this, currentLocation));
                                       Log.i("Status", "Internet Connection" + GsonStringConvertor.gsonToString(currentLocation));
                                   }
                               }
                           }

                           @Override
                           public void onFailure(Call<DistanceMatrixApiResponse> call, Throwable t) {

                           }
                       }
               );
           }
       }

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Log.d("LocationService",s);
        switch (s){
            case "dropLocation":
                eta="Current Location";
                currentBookingRepository.setEta(eta,"BACKGROUND");
                break;
            case "tripStatus": tripStatus=trackingSharedPreference.getString(s,"0");
            if (tripStatus.equals("3")) {
                eta="Current Location";
                currentBookingRepository.setEta(eta,"BACKGROUND");
                Log.d("TripType5",booking.getTourType()+" "+tripStatus);
            }
            else {
                Log.d("TripType6",booking.getTourType()+" "+tripStatus);
                etaCounter=0;
                getEOT();
            }
            break;
        }

    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return mLifecycleRegistry;
    }

    @Override
    public void placeAddress(Address address, Location location) {
       // Log.d("Address",address.toString());
        FirebaseTrackingModel firebaseTrackingModel = new FirebaseTrackingModel(
                loginRepository.getAccessToken().getValue(),
                null,
                location.getLatitude(),
                location.getLongitude(),
                eta,
                Float.toString(location.getBearing()),
                tripStatus,
                "On Ride",
                driverDetails.getDriverName(),
                booking.getBookingId(),
                booking.getTaxiModel(),
                booking.getTaxiTypeAssigned(),
                booking.getRegistrationNumber(),
                booking.getReferenceNo(),
                booking.getPickupLocation());

        if(address.getAdminArea()!=null && address.getLocality()!=null) {
            if (currentState!=null && !address.getAdminArea().equalsIgnoreCase(currentState)){
                cityReference.child(currentState).child(currentCity).child(driverDetails.getOperatorId()).child("drivers").child(driverDetails.getId())
                        .setValue(null);
            }
            else if (currentCity!=null && !address.getLocality().equalsIgnoreCase(currentCity)){
                cityReference.child(currentState).child(currentCity).child(driverDetails.getOperatorId()).child("drivers").child(driverDetails.getId())
                        .setValue(null);
            }
            currentState = address.getAdminArea();
            currentCity = address.getLocality();
            cityReference.child(currentState).child(currentCity).child(driverDetails.getOperatorId()).child("drivers").child(driverDetails.getId())
                    .setValue(firebaseTrackingModel.toMap());
            AccessSharedPreferencesUtill.setStateAndCity(getApplicationContext(),currentState,currentCity);
        }

        operatorReference.setValue(firebaseTrackingModel.toMap());
    }

    @Override
    public void placeError(String error) {

    }

    public static boolean isServiceRunningInForeground(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(LocationService.class.getName())) {
                if (service.foreground) {
                    return true;
                }

            }
        }
        return false;
    }
}
