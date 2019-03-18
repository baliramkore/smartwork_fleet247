package com.fleet247.driver.tracking;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.Observer;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Location;
import android.os.IBinder;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.fleet247.driver.R;
import com.fleet247.driver.activities.HomeActivity;
import com.fleet247.driver.data.LiveLocation;
import com.fleet247.driver.data.models.login.Driver;
import com.fleet247.driver.data.models.tracking.FirebaseTrackingModel;
import com.fleet247.driver.data.repository.LoginRepository;
import com.fleet247.driver.data.repository.taskhandler.AccessSharedPreferencesUtill;
import com.fleet247.driver.data.repository.taskhandler.FetchPlacesFromLocationTask;
import com.fleet247.driver.utility.GsonStringConvertor;
import com.fleet247.driver.utility.NotificationUtill;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ActiveDriverTrackingService extends Service implements LifecycleOwner,FetchPlacesFromLocationTask.PlacesCallback{

    private LifecycleRegistry mLifecycleRegistry; //for making it lifecycleOwner;

    FirebaseDatabase trackingDatabase;
    FirebaseApp trackingApp;
    DatabaseReference cityReference;
    DatabaseReference operatorReference;
    Executor locationToPlaceHandler;
    LoginRepository loginRepository;
    Driver driver;
    LiveLocation liveLocation;
    String currentState;
    String currentCity;

    @Override
    public void onCreate() {
        super.onCreate();
        mLifecycleRegistry=new LifecycleRegistry(this);// for providing lifecycle to get event from viewModel or repository.
        mLifecycleRegistry.markState(Lifecycle.State.CREATED); // to notify that service is created.

        loginRepository=LoginRepository.getInstance(getApplication());
        driver=loginRepository.getDriver().getValue();

        trackingApp=FirebaseApp.getInstance();
        trackingDatabase=FirebaseDatabase.getInstance(trackingApp);
        cityReference =trackingDatabase.getReference().child("states");
        operatorReference=trackingDatabase.getReference().child("operators").child(driver.getOperatorId()).child("drivers").child(driver.getId());

        currentCity= AccessSharedPreferencesUtill.getActiveCity(getApplicationContext());
        currentState=AccessSharedPreferencesUtill.getActiveState(getApplicationContext());
        locationToPlaceHandler= Executors.newSingleThreadExecutor();
        liveLocation=LiveLocation.getInstance(getApplication());
        liveLocation.observe(this, new Observer<Location>() {
            @Override
            public void onChanged(@Nullable Location location) {
                locationToPlaceHandler.execute(new FetchPlacesFromLocationTask(getApplication(),ActiveDriverTrackingService.this,location));
            }
        });
        Log.d("ActiveService","Created");
        NotificationUtill.createOnActiveServiceNotificationChannel(this);
        Intent intent1 = new Intent(this, HomeActivity.class);
        intent1.putExtra("fromNotification",true);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent1, 0);

        Notification notification = new NotificationCompat.Builder(this,NotificationUtill.activeDriverTrackingChannelId)
                    .setContentTitle("Tracking Enabled")
                .setContentText("You are Active to take bookings")
                .setSmallIcon(R.drawable.fleetnotificationlogo)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.fleet247))
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1001, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mLifecycleRegistry.markState(Lifecycle.State.STARTED); // To notify that the service is started.


        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        mLifecycleRegistry.markState(Lifecycle.State.DESTROYED); // To notify that the service is destroyed.

        liveLocation.removeObservers(this);
        if(currentCity!=null && currentState!=null) {
            cityReference.child(currentState).child(currentCity).child(driver.getOperatorId()).child("drivers").child(driver.getId())
                    .setValue(null);
        }
        operatorReference.setValue(null);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return mLifecycleRegistry;
    }

    @Override
    public void placeAddress(Address address,Location location) {
        Log.d("Address", GsonStringConvertor.gsonToString(address));
        Log.d("Location",GsonStringConvertor.gsonToString(location)+" "+location.getLatitude()+" "+location.getLongitude());
        FirebaseTrackingModel firebaseTrackingModel = new FirebaseTrackingModel(
                loginRepository.getAccessToken().getValue(),
                null,
                location.getLatitude(),
                location.getLongitude(),
                null,
                null,
                null,
                "Active",
                driver.getDriverName(),
                null,
                null,
                null,
                null,
                null,
                null);


        if(address.getAdminArea()!=null && address.getLocality()!=null) {
            if (currentState!=null && !address.getAdminArea().equalsIgnoreCase(currentState)){
                cityReference.child(currentState).child(currentCity).child(driver.getOperatorId()).child("drivers").child(driver.getId())
                        .setValue(null);
            }
            else if (currentCity!=null && !address.getLocality().equalsIgnoreCase(currentCity)){
                cityReference.child(currentState).child(currentCity).child(driver.getOperatorId()).child("drivers").child(driver.getId())
                        .setValue(null);
            }
            currentState = address.getAdminArea();
            currentCity = address.getLocality();
            cityReference.child(currentState).child(currentCity).child(driver.getOperatorId()).child("drivers").child(driver.getId())
                    .setValue(firebaseTrackingModel.toMap());
            AccessSharedPreferencesUtill.setActiveCityState(getApplicationContext(),currentCity,currentState);
        }
        operatorReference.setValue(firebaseTrackingModel.toMap());
    }

    @Override
    public void placeError(String error) {
        Log.d("Error",error);
    }
}
