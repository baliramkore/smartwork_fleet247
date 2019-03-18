package com.fleet247.driver.activities;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fleet247.driver.fragment.NotAtPickupLocationDialog;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fleet247.driver.R;
import com.fleet247.driver.data.repository.taskhandler.LocationSettingUtill;
import com.fleet247.driver.eventlistener.eventclass.LocationChangeClass;
import com.fleet247.driver.fragment.BookingCompletedFragment;
import com.fleet247.driver.fragment.EnableLocationFragment;
import com.fleet247.driver.fragment.EndJourneyDialog;
import com.fleet247.driver.fragment.JourneyStartedFromGarageDialog;
import com.fleet247.driver.fragment.PhoneBottomSheet;
import com.fleet247.driver.fragment.StartJourneyDialog;
import com.fleet247.driver.data.models.CustomLatLng;
import com.fleet247.driver.data.models.distancetime.EndDistanceTime;
import com.fleet247.driver.data.models.upcomingbooking.Booking;
import com.fleet247.driver.services.FloatingHeadDialog;
import com.fleet247.driver.services.GeofenceTransitionsIntentService;
import com.fleet247.driver.tracking.ActiveDriverTrackingService;
import com.fleet247.driver.tracking.LocationService;
import com.fleet247.driver.utility.GsonStringConvertor;
import com.fleet247.driver.utility.IsServiceRunning;
import com.fleet247.driver.utility.JobSchedularUtility;
import com.fleet247.driver.utility.NetworkStatus;
import com.fleet247.driver.utility.RegisterBroadcastReceiverUtill;
import com.fleet247.driver.viewmodels.CurrentBookingViewModel;
import com.fleet247.driver.viewmodels.DriverInfoViewModel;
import com.fleet247.driver.viewmodels.GoogleMapsDirectionViewModel;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener, ResultCallback<LocationSettingsResult>
        , StartJourneyDialog.StartRideListener,EndJourneyDialog.OnRideEndListener, JourneyStartedFromGarageDialog.OnStartFromGarageClicked {

    GoogleMap mMap;  //to plot locations of vehicle, pickupLocation and deop location
    ImageView navigationButton; //button to start navigation through google maps.
    Button actionButton; //Button to get driver's action like started from garage, arrived at pickup location etc.
    TextView userNames;//TextView to show names of the users.
    TextView tripType;// TexView to denote "PICKUP" user or "DROP" user.
    LatLng dropCityLatLng, pickUpCityLatLng; //Lat-Lng of pickup and drop cities.
    Marker pickUpMarker;//Google map marker to plot pickup location.
    Marker dropMarker; //Google map marker to plot drop location.
    Marker currentMarker; //Google map marker to plot current location of the vehicle.
    String eta; // Estimate time of arrival at pickup or drop location.
    Booking booking; // Booking object which driver can start or has already started.
    TextView pickupPoint;// TextView which shows pickup or drop location according to the status of trip.
    String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};//permission array for location.
    CurrentBookingViewModel currentBookingViewModel;// currentBookingViewModel object to store data of a current booking.
    DriverInfoViewModel driverInfoViewModel;//object that contains information about driver.
    GoogleMapsDirectionViewModel googleMapsDirectionViewModel; //viewModel to interact with google apis.
    private final int PERMISSION_CODE = 101;
    GoogleApiClient mGoogleApiClient;// googleApiClient to access location services.
    final int REQUEST_CHECK_SETTINGS = 2; //Request code for checking settings.
    final int REQUEST_OVERLAY_SETTING = 11;//Request code for requesting screen overlay permission.
    protected LocationSettingsRequest mLocationSettingsRequest; //object to access LocationSettingRequest.
    String[] status = {"Start from garage", "Arrived at Pickup Location", "Start Trip", "End Trip"};//status shown on action button.
    int tripStatus; // integer to save trip status in the form of integer.
    Location currentLocation;// Location object to store currentLocation.
    LatLng garageLocation;  // LatLng object to store garageLocation.
    StartJourneyDialog startJourneyDialog; // Dialog to show when driver starts journey.
    EndJourneyDialog endJourneyDialog; // Dialog to show when driver clicks on end journey.
    SharedPreferences trackPreference; // SharedPreference to store tracking information.
    int enableButton = 0;
    int enableButtonDrop = 0;  //to enable action button after start trip is called and drop location and currebt location are plotted and animated
    double measuredDistance = 0; //distance travelled calculated by using location locally.

    boolean hasEnteredPickupGeoFence = false; //to know whether driver has entered in 500 m range of pickup location.
    private GeofencingClient geofencingClient;//GeofencingClient to add geofence to pickuplocation.
    List<Geofence> geofenceList; //Geofence list to add location to geofence;
    PendingIntent mGeofencePendingIntent; //Pending intent for GeofenceTransitionService
    ArrayList<LatLng> pathLatLng;
    SupportMapFragment mapFragment;
    NotAtPickupLocationDialog notAtPickupLocationDialog;

    View toastErrorView;
    TextView toastErrorMessage;
    Toast errorToast;

    public static final int SIGNATURE_CAPTURE_ACTIVITY_REQUEST_CODE=1;

    JourneyStartedFromGarageDialog journeyStartedFromGarageDialog;//Dialog to show JourneyStartedFromGarageDialog.

    float density;
    int mapBorder;
    boolean isMapReady=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationButton = findViewById(R.id.startNavigationButton);
        actionButton = findViewById(R.id.tripActionButton);
        userNames = findViewById(R.id.userName);
        pickupPoint = findViewById(R.id.location);
        pickupPoint = findViewById(R.id.location);
        tripType = findViewById(R.id.tripType);

        toastErrorView=getLayoutInflater().inflate(R.layout.toast_layout_error,(ViewGroup)findViewById(R.id.toast_layout_error));
        toastErrorMessage=toastErrorView.findViewById(R.id.toast_error_message);

        pickupPoint.setOnClickListener(this);

        googleMapsDirectionViewModel = ViewModelProviders.of(this).get(GoogleMapsDirectionViewModel.class);
        currentBookingViewModel = ViewModelProviders.of(this).get(CurrentBookingViewModel.class);
        driverInfoViewModel = ViewModelProviders.of(this).get(DriverInfoViewModel.class);

        trackPreference = getSharedPreferences("trackingPref", MODE_PRIVATE);

        startJourneyDialog = new StartJourneyDialog();
        startJourneyDialog.addStartRideListener(this);

        journeyStartedFromGarageDialog=new JourneyStartedFromGarageDialog();
        journeyStartedFromGarageDialog.registerOnStartFromGarageClicked(this);

        notAtPickupLocationDialog=new NotAtPickupLocationDialog();

        geofencingClient = LocationServices.getGeofencingClient(this);
        geofenceList=new ArrayList<>();

        endJourneyDialog = new EndJourneyDialog();
        endJourneyDialog.setOnRideEndListener(this);

        actionButton.setEnabled(false);
        status=getResources().getStringArray(R.array.booking_status);

        errorToast=new Toast(getApplicationContext());
        errorToast.setDuration(Toast.LENGTH_LONG);
        errorToast.setGravity(Gravity.CENTER,0,300);
        errorToast.setView(toastErrorView);

        Intent intent = getIntent();
        if (intent != null) {
            if (!intent.getBooleanExtra("fromNotification",false)) {
                booking = GsonStringConvertor.stringToGson(intent.getStringExtra("bookingInfo"), Booking.class);
            }
        } else {
            finish();
        }

        if (currentBookingViewModel.getCurrentBooking().getValue()!=null){
            booking=currentBookingViewModel.getCurrentBooking().getValue();
        }

        Log.d("booking",GsonStringConvertor.gsonToString(booking));

       // Log.d("Passenger", "sandeep" + GsonStringConvertor.gsonToString(booking.getPassengers()));

        currentBookingViewModel.getTripStatus().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                tripStatus = Integer.parseInt(s);
                actionButton.setText(status[tripStatus]);

                if (tripStatus >= 1) {
                    if(!JobSchedularUtility.isPolylineJobScheduled(getApplication())) {
                        JobSchedularUtility.scheduleUpdatePolylineChangeJob(getApplication());
                    }
                    if(!IsServiceRunning.isServiceRunningInForeground(MainActivity.this,LocationService.class) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                        startForegroundService(new Intent(MainActivity.this, LocationService.class));
                    }
                    else if(!IsServiceRunning.isServiceRunningInForeground(MainActivity.this,LocationService.class)){
                        startService(new Intent(MainActivity.this, LocationService.class));
                    }
                    try {
                        RegisterBroadcastReceiverUtill.unRegisterGpsStateChangedProvider(getApplication());
                        Log.d("LocationReceiver","Unregistered");
                    }catch (Exception e){
                        Log.d("LocationReceiver",e.getMessage());
                    }

                    RegisterBroadcastReceiverUtill.registerGpsStateChangedProvider(getApplication());
                }
                if (Integer.parseInt(s) < 3) {
                    tripType.setText(getString(R.string.pickup_text));
                    pickupPoint.setText(booking.getPickupLocation());
                } else {
                    tripType.setText(getString(R.string.drop_text));
                    if (booking.getDropLocation() != null && booking.getDropLocation().length() > 1) {
                        pickupPoint.setText(booking.getDropLocation());
                        if (dropCityLatLng!=null) {
                            trackPreference.edit().putString("dropLocation", dropCityLatLng.latitude + "," + dropCityLatLng.longitude)
                                    .commit();
                             }
                            plotLocation(dropCityLatLng, "drop");

                    } else {
                        pickupPoint.setText(booking.getTourType());
                        actionButton.setText(status[tripStatus]);
                        actionButton.setEnabled(true);
                        eta = null;
                    }
                    if (pickUpMarker != null) {
                        Log.d("PickupMarker","Not Null");
                        pickUpMarker.remove();
                    }else {
                        Log.d("PickupMarker","Null");
                    }
                    if (currentMarker!=null){
                        currentMarker.hideInfoWindow();
                    }
                }

            }
        });

        currentBookingViewModel.getPickupLatLng(booking).observe(this, new Observer<CustomLatLng>() {
            @Override
            public void onChanged(@Nullable CustomLatLng customLatLng) {
                Log.d("CustomLatLng",GsonStringConvertor.gsonToString(customLatLng));
                if (customLatLng!=null) {
                    pickUpCityLatLng = new LatLng(customLatLng.getLatitude(), customLatLng.getLongitude());
                    Log.d("CustomLat",GsonStringConvertor.gsonToString(pickUpCityLatLng));
                    if (mMap!=null && tripStatus<=2 ){
                        //here drop location means where driver has to go for pickup or drop
                        Log.d("Pickup","Enabled");
                        trackPreference.edit().putString("dropLocation",customLatLng.getLatitude()+","+customLatLng.getLongitude())
                                .commit();
                        plotLocation(pickUpCityLatLng,"pickup");
                    }
                    else {
                        Log.d("Pickup","disabled");
                    }
                }
            }
        });

        currentBookingViewModel.getDropLatLng(booking).observe(this, new Observer<CustomLatLng>() {
            @Override
            public void onChanged(@Nullable CustomLatLng customLatLng) {
                if (customLatLng!=null) {
                    dropCityLatLng = new LatLng(customLatLng.getLatitude(), customLatLng.getLongitude());
                    if (mMap!=null && tripStatus>2){
                        //here drop location means where driver has to go for pickup or drop
                        trackPreference.edit().putString("dropLocation",customLatLng.getLatitude()+","+customLatLng.getLongitude())
                                .commit();
                        plotLocation(dropCityLatLng,"drop");
                        if (pickUpMarker != null) {
                            pickUpMarker.remove();
                            Log.d("PickupMarker","Removed");
                        }
                    }
                }
            }
        });




        currentBookingViewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                actionButton.setEnabled(true);
                if (s != null) {
                    toastErrorMessage.setText(s);
                    errorToast.show();
                }
            }
        });


        currentBookingViewModel.getHasReachedPickupPoint().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean ){
                    Toast.makeText(MainActivity.this, "Geofencing Status Entered in Fence", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(MainActivity.this, "Geofencing Status not in Fence", Toast.LENGTH_LONG).show();
                }
                hasEnteredPickupGeoFence = aBoolean;
            }
        });

        currentBookingViewModel.getNotAtPickupLocation().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean!=null && aBoolean){
                    actionButton.setEnabled(false);
                    if (currentBookingViewModel.getGarageLocation() != null) {
                        googleMapsDirectionViewModel.getEstimateDistanceTime(currentBookingViewModel.getGarageLocation(),
                                currentMarker.getPosition(), getResources().getString(R.string.distance_matrix_key));
                        Log.d("garageLocation", "Not Null");
                    } else {
                        initiatedArrived("0"); //If garage location is unavailable.
                    }
                    currentBookingViewModel.setNotAtPickupLocation(false);
                }
            }
        });

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);

        userNames.setText(booking.getPassengers().get(0).peopleName);

        googleMapsDirectionViewModel.getPolyLine().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if (s != null) {
                    setPolyLine(s);
                }
            }
        });

        googleMapsDirectionViewModel.getEndDistanceTime().observe(this, new Observer<EndDistanceTime>() {
            @Override
            public void onChanged(@Nullable EndDistanceTime endDistanceTime) {
                if (!endJourneyDialog.isAdded()&&endDistanceTime!=null && endDistanceTime.getJourneyDistanceTime()!=null && endDistanceTime.getGarageDistanceTime()!=null){
                    Log.d("End Estimate Distance",GsonStringConvertor.gsonToString(endDistanceTime));
                    Bundle bundle = new Bundle();
                    bundle.putString("endGarageDistance", endDistanceTime.getGarageDistanceTime().getEstimateDistance());
                    bundle.putString("endGarageTime",endDistanceTime.getGarageDistanceTime().getEstimateTime());
                    bundle.putString("journeyDistance", endDistanceTime.getJourneyDistanceTime().getEstimateDistance());
                    bundle.putString("journeyTime",endDistanceTime.getJourneyDistanceTime().getEstimateTime());
                    endJourneyDialog.setArguments(bundle);
                    endJourneyDialog.show(getSupportFragmentManager(), "End Dialog");
                }
            }
        });

        currentBookingViewModel.getHasReachedPickupPoint().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                hasEnteredPickupGeoFence=aBoolean;
                Log.d("Driver Arrived",aBoolean+"");
            }
        });

        currentBookingViewModel.getStatus().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                Log.d("Status", s);
                switch (s) {
                    case "driver started successfully":
                        tripStatus = 1;
                        actionButton.setText(status[tripStatus]);
                        currentBookingViewModel.setGarageLocation(garageLocation);
                        currentBookingViewModel.setTripStatus("" + tripStatus);
                        currentBookingViewModel.setCurrentBooking(GsonStringConvertor.gsonToString(booking));
                        trackPreference.edit().putString("bookingId", booking.getBookingId())
                        .putString("bookingInfo",GsonStringConvertor.gsonToString(booking))
                        .putString("driverDetails",GsonStringConvertor.gsonToString(driverInfoViewModel.getDriver().getValue()))
                        .putString("tripStatus", "1").commit();
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                            startForegroundService(new Intent(MainActivity.this, LocationService.class));
                        }
                        else {
                            startService(new Intent(MainActivity.this, LocationService.class));
                        }
                        actionButton.setEnabled(true);
                        currentBookingViewModel.reSetStatus();
                        geofenceList.add(new Geofence.Builder()
                                .setRequestId("pickupFence")
                                .setCircularRegion(
                                        pickUpMarker.getPosition().latitude,
                                        pickUpMarker.getPosition().longitude,
                                        500)
                                .setExpirationDuration(7200000)
                                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                                .build());
                        try {
                            RegisterBroadcastReceiverUtill.unRegisterGpsStateChangedProvider(getApplication());
                            Log.d("LocationReceiver","Unregistered");
                        }catch (Exception e){
                            Log.d("LocationReceiver",e.getMessage());
                        }

                        RegisterBroadcastReceiverUtill.registerGpsStateChangedProvider(getApplication());
                        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            Log.d("GeoFence","Added");
                            geofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
                                    .addOnSuccessListener(MainActivity.this, new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("Geofencing ","Added");
                                        }
                                    })
                                    .addOnFailureListener(MainActivity.this, new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d("Geofencing ","Error");
                                        }
                                    });
                        }
                        else {
                            Log.d("Geofencing ","Permission Error");
                        }

                        if (driverInfoViewModel.getIsActive()){
                            stopService(new Intent(MainActivity.this, ActiveDriverTrackingService.class));
                        }

                        JobSchedularUtility.scheduleUpdatePolylineChangeJob(getApplication());

                        break;
                    case "driver arrived at pickup point":
                        try {
                            RegisterBroadcastReceiverUtill.unRegisterGpsStateChangedProvider(getApplication());
                            Log.d("LocationReceiver","Unregistered");
                        }catch (Exception e){
                            Log.d("LocationReceiver",e.getMessage());
                        }

                        RegisterBroadcastReceiverUtill.registerGpsStateChangedProvider(getApplication());
                        ArrayList<String> geofenceList=new ArrayList<>();
                        geofenceList.add("pickupFence");
                        geofencingClient.removeGeofences(geofenceList);
                        tripStatus=2;
                        trackPreference.edit().putString("tripStatus","2").commit();
                        actionButton.setText(status[tripStatus]);
                        currentBookingViewModel.setTripStatus(""+tripStatus);
                        actionButton.setEnabled(true);
                        currentBookingViewModel.reSetStatus();
                        break;

                    case "ride started":
                        try {
                            RegisterBroadcastReceiverUtill.unRegisterGpsStateChangedProvider(getApplication());
                            Log.d("LocationReceiver","Unregistered");
                        }catch (Exception e){
                            Log.d("LocationReceiver",e.getMessage());
                        }

                        RegisterBroadcastReceiverUtill.registerGpsStateChangedProvider(getApplication());
                        tripStatus=3;
                        trackPreference.edit().putString("tripStatus","3").commit();
                        currentBookingViewModel.setStartingLocation(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()));
                        actionButton.setText(status[tripStatus]);
                        currentBookingViewModel.setTripStatus(""+tripStatus);
                        if(booking.getDropLocation()!=null && booking.getDropLocation().length()>1) {
                            pickupPoint.setText(booking.getDropLocation());
                        }
                        else {
                            pickupPoint.setText(booking.getTourType());
                        }
                        //actionButton.setEnabled(true);
                        enableButtonDrop=1;
                        LocationChangeClass.onETAChanged("Current Location");
                        if(currentMarker!=null){
                            currentMarker.setTitle("Current Location");
                            currentMarker.hideInfoWindow();
                        }
                        currentBookingViewModel.reSetStatus();
                        //JobSchedularUtility.scheduleUploadStartSignatureJob(getApplication());
                        if (!JobSchedularUtility.isUploadSignaturesJobScheduled(getApplication())){
                            JobSchedularUtility.scheduleUploadSignaturesJob(getApplication());
                            Log.d("UploadSignatureJobStart","Not Scheduled");
                        }
                        else {
                            Log.d("UploadSignatureJobStart","Not Scheduled");
                        }
                        break;
                    case "ride ended":
                        if (!JobSchedularUtility.isUploadSignaturesJobScheduled(getApplication())){
                            JobSchedularUtility.scheduleUploadSignaturesJob(getApplication());
                            Log.d("UploadSignatureJobEnd","Not Scheduled");
                        }
                        else {
                            Log.d("UploadSignatureJobEnd","Scheduled");
                        }
                        try {
                            RegisterBroadcastReceiverUtill.unRegisterGpsStateChangedProvider(getApplication());
                            Log.d("LocationReceiver","Unregistered");
                        }catch (Exception e){
                            Log.d("LocationReceiver",e.getMessage());
                        }
                        currentBookingViewModel.deleteLocationoffTime();
                        JobSchedularUtility.deleteUpdatePolylineChangeJob(getApplication());

                        tripStatus=0;
                        currentBookingViewModel.setTripStatus(""+tripStatus);
                        stopService(new Intent(MainActivity.this,LocationService.class));
                        stopService(new Intent(MainActivity.this,FloatingHeadDialog.class));
                        if (driverInfoViewModel.getIsActive()){
                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                                startForegroundService(new Intent(MainActivity.this, ActiveDriverTrackingService.class));
                            }
                            else {
                                startService(new Intent(MainActivity.this, ActiveDriverTrackingService.class));
                            }

                        }
                        Bundle bundle=new Bundle();
                        BookingCompletedFragment bookingCompletedFragment=new BookingCompletedFragment();
                        bundle.putString("booking",GsonStringConvertor.gsonToString(currentBookingViewModel.getCurrentBooking().getValue()));
                        bundle.putString("invoiceDetails",GsonStringConvertor.gsonToString(currentBookingViewModel.getInvoiceDetails()));
                        bookingCompletedFragment.setArguments(bundle);
                        currentBookingViewModel.setBillingBookingDetails(currentBookingViewModel.getCurrentBooking().getValue(),currentBookingViewModel.getInvoiceDetails());
                        currentBookingViewModel.deleteCurrentBookingData();
                        actionButton.setEnabled(true);
                        currentBookingViewModel.getLiveLocation().removeObservers(MainActivity.this);
                        trackPreference.edit().clear().commit();
                        currentBookingViewModel.reSetStatus();
                        getSupportFragmentManager().beginTransaction().add(R.id.map_container,bookingCompletedFragment)
                                .commit();

                }
            }
        });

        googleMapsDirectionViewModel.getDistance().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if (s!=null){
                    Log.d("Estimate Distance",s);
                    initiatedArrived(s);
                }
            }
        });

        currentBookingViewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

                if (!actionButton.isEnabled()) {
                    actionButton.setEnabled(true);
                    toastErrorMessage.setText(s + " Check Internet Connection");
                    errorToast.show();
                }
            }
        });

        currentBookingViewModel.getEta().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                eta=s;
                if (s!=null && currentMarker!=null && tripStatus<3){
                    currentMarker.setTitle(eta);
                    currentMarker.showInfoWindow();
                }
                else if (s!=null && currentMarker!=null){
                    currentMarker.setTitle(eta);
                    currentMarker.hideInfoWindow();
                }
            }
        });

        currentBookingViewModel.getTrigerStartRide().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean){
                    Log.d("StartKm",currentBookingViewModel.getStartKms());
                    currentBookingViewModel.setTrigerStartRide(false);
                }
            }
        });

        currentBookingViewModel.getTriggerEndRide().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean) {
                    Log.d("EndKm", currentBookingViewModel.getEndKm());
                    Log.d("StateTax",currentBookingViewModel.getStateTax());
                    Log.d("TollTax",currentBookingViewModel.getTollTax());
                    Log.d("OtherTax",currentBookingViewModel.getOtherTax());
                    currentBookingViewModel.setTriggerEndRide(false);
                }
            }
        });

        currentBookingViewModel.getCaptureSignature().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean){
                    Intent signatureIntent=new Intent(MainActivity.this, SignatureCaptureActivity.class);
                    if (tripStatus==2){
                        signatureIntent.putExtra("type","start");
                    }
                    else {
                        signatureIntent.putExtra("type","end");
                    }
                    startActivityForResult(signatureIntent,SIGNATURE_CAPTURE_ACTIVITY_REQUEST_CODE);
                    currentBookingViewModel.setCaptureSignature(false);
                }
            }
        });

        currentBookingViewModel.getTotalLocationOffTime().observe(this, new Observer<Long>() {
            @Override
            public void onChanged(@Nullable Long aLong) {
                Log.d("TotalLocation off Time",aLong+"");
            }
        });

        currentBookingViewModel.getLocationStatus().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if(aBoolean==null){
                    return;
                }

                if(aBoolean){
                    Log.d("Location","Enabled");
                    getSupportFragmentManager().popBackStackImmediate();
                }
                else {
                    Log.d("Location","Disabled");
                    getSupportFragmentManager().beginTransaction().add(R.id.map_container,new EnableLocationFragment())
                            .addToBackStack(null)
                            .commit();
                }
            }
        });

        currentBookingViewModel.getEnableLocationClicked().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if(aBoolean){
                    checkPermission();
                }
            }
        });

        googleMapsDirectionViewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s!=null){
                    toastErrorMessage.setText(s);
                    errorToast.show();
                    if (!actionButton.isEnabled()){
                        actionButton.setEnabled(true);
                    }
                }
            }
        });

        tripStatus=Integer.parseInt(currentBookingViewModel.getTripStatus().getValue());
        actionButton.setText(status[tripStatus]);
        navigationButton.setOnClickListener(this);
        actionButton.setOnClickListener(this);
        userNames.setOnClickListener(this);
        checkPermission();

    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(geofenceList);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        mGeofencePendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
        return mGeofencePendingIntent;
    }



    public void setPolyLine(String polyLine){
        mMap.addPolyline(new PolylineOptions().addAll(PolyUtil.decode(polyLine)));
        List<LatLng> latLngList=PolyUtil.decode(polyLine);
        pickUpCityLatLng=latLngList.get(0);
        dropCityLatLng=latLngList.get(latLngList.size()-1);
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(pickUpCityLatLng);
        builder.include(dropCityLatLng);
        CameraUpdate cameraUpdate=CameraUpdateFactory.newLatLngBounds(builder.build(),150);
        mMap.animateCamera(cameraUpdate);
        pickUpMarker.setPosition(pickUpCityLatLng);
        dropMarker.setPosition(dropCityLatLng);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        float density=getResources().getDisplayMetrics().density;
        mMap=googleMap;
        int paddingBottom=(int)(140*density);
        int paddingLeft=(int)(30*density);
        mapBorder=(int)(75*density);
        mMap.setPadding(paddingLeft,mapBorder,paddingLeft,paddingBottom);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(28.596501, 76.990833),12));
        if (tripStatus<=2) {
            Log.d("PlotPickup","True");
           plotLocation(pickUpCityLatLng,"pickup");
        }if (tripStatus>=3 && booking.getDropLocation()!=null && booking.getDropLocation().length()>0){
            plotLocation(dropCityLatLng,"drop");
        }

        if (currentLocation!=null) {
            if (currentMarker == null) {
                currentMarker = mMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.taxi_dark_128))
                        .position(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude())));
            } else {
                currentMarker.setPosition(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
            }

            currentMarker.setRotation(currentLocation.getBearing());
            if (eta!=null) {
                currentMarker.setTitle(eta);
                currentMarker.showInfoWindow();
            }
        }
        else {
            Log.d("Current Location","Unavailable");
        }
        isMapReady=true;

    }

    @Override
    public void onClick(View view) {
        Log.d("View",""+view.getId());
        switch (view.getId()){
            case R.id.startNavigationButton:
                if (pickupPoint.getText().toString().length()>1 && !pickupPoint.getText().toString().equals("Local")) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (Settings.canDrawOverlays(MainActivity.this)){
                            Uri gmmIntentUri = Uri.parse("google.navigation:q=+" + pickupPoint.getText().toString() + "&mode=d\"");
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            startActivity(mapIntent);
                            Intent intent=new Intent(MainActivity.this,FloatingHeadDialog.class);
                            SharedPreferences dialogPref=getSharedPreferences("dialogPref",MODE_PRIVATE);
                            dialogPref.edit().putString("bookingInfo",GsonStringConvertor.gsonToString(booking)).commit();
                            startService(intent);
                        }
                        else {
                            startActivityForResult( new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,Uri.parse("package:"+getPackageName())),REQUEST_OVERLAY_SETTING);
                        }
                    }
                    else {
                        Uri gmmIntentUri = Uri.parse("google.navigation:q=+" + pickupPoint.getText().toString() + "&mode=d\"");
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);
                        Intent intent=new Intent(MainActivity.this,FloatingHeadDialog.class);
                        intent.putExtra("bookingInfo",GsonStringConvertor.gsonToString(booking));
                        startService(intent);
                    }
                }else {
                    toastErrorMessage.setText("Address Unavailable");
                    errorToast.show();
                }
                break;
            case R.id.tripActionButton:
                if (NetworkStatus.isInternetConnected(getApplication())) {
                    Log.d("Trip Status",""+tripStatus);
                    switch (tripStatus) {

                        case 0: if (currentLocation!=null && !journeyStartedFromGarageDialog.isAdded()) {
                                journeyStartedFromGarageDialog.show(getSupportFragmentManager(),"start from garage");
                            }
                            else if(currentLocation==null){
                            toastErrorMessage.setText("Location Unavailable");
                            errorToast.show();
                            }
                            break;
                        case 1:
                            if (!hasEnteredPickupGeoFence){
                                if (!notAtPickupLocationDialog.isAdded()) {
                                    //Toast.makeText(MainActivity.this, "You are not within 500m range of Pickup Location", Toast.LENGTH_LONG).show();
                                    notAtPickupLocationDialog.show(getSupportFragmentManager(), "NotAtPickupDialog");
                                }
                            }
                            else {
                                actionButton.setEnabled(false);
                                if (currentBookingViewModel.getGarageLocation() != null) {
                                    googleMapsDirectionViewModel.getEstimateDistanceTime(currentBookingViewModel.getGarageLocation(),
                                            currentMarker.getPosition(), getResources().getString(R.string.distance_matrix_key));
                                    Log.d("garageLocation", "Not Null");
                                } else {
                                    initiatedArrived("0"); //If garage location is unavailable.
                                }
                            }
                            break;
                        case 2:
                                if (!startJourneyDialog.isAdded()) {
                                    startJourneyDialog.show(getSupportFragmentManager(), "Start Dialog");
                                }
                                break;
                        case 3:
                            googleMapsDirectionViewModel.getEndEstimateDistanceTime(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()),
                                currentBookingViewModel.getStartingLoction(),currentBookingViewModel.getGarageLocation(),getResources().getString(R.string.distance_matrix_key));
                            try {
                                //Polyline polyline = mMap.addPolyline(new PolylineOptions().addAll(PolyUtil.decode(currentBookingViewModel.getPolyLine())).width(5).color(Color.BLACK));
                                Log.d("PolyLine", currentBookingViewModel.getPolyLine() + " s");
                            }catch (Exception e){
                                Log.e("Polyline","Not available");
                            }
                            break;

                    }
                }
                else {
                    toastErrorMessage.setText("Internet Connection Unavailable");
                    errorToast.show();
                }
                break;
            case R.id.location:
                //currentBookingViewModel.deleteCurrentBookingData();
                //trackPreference.edit().clear().commit();
                break;
            case R.id.userName:
                Log.d("User","Clicked");
                PhoneBottomSheet phoneBottomSheet=new PhoneBottomSheet();
                Bundle args=new Bundle();
                args.putString("bookingInfo",GsonStringConvertor.gsonToString(booking));
                phoneBottomSheet.setArguments(args);
                phoneBottomSheet.show(getSupportFragmentManager(),"phoneBottomSheet");
                //bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                //startService(new Intent(this, LocationService.class));
                break;
        }
    }

    void initiatedArrived(String distance){
        currentBookingViewModel.initiateDriverArrived(driverInfoViewModel.getAccessToken().getValue()
                ,booking.getBookingId()
                ,distance
                ,String.format("%.2f",currentBookingViewModel.getDistanceTravelled()/1000));
    }

    void plotLocation(final LatLng latLng, String type) {
        if (latLng == null || mMap==null || !isMapReady) {
            Log.d("LatLng","Is Null");
            return;
        }
        if (type.equals("pickup")) {
            if(pickUpMarker==null) {
                pickUpMarker = mMap.addMarker(new MarkerOptions()
                        .title(booking.getPickupLocation())
                        .snippet("Pickup Location")
                        .position(latLng));
            }else {
                pickUpMarker.setPosition(latLng);
            }
            pickUpMarker.showInfoWindow();
        } else if (type.equals("drop")) {
            if(dropMarker==null) {
                dropMarker = mMap.addMarker(new MarkerOptions()
                        .title(booking.getDropLocation())
                        .snippet("Drop Location")
                        .position(latLng));
            }else {
                dropMarker.setPosition(latLng);
            }
            dropMarker.showInfoWindow();
            if (pickUpMarker!=null){
                pickUpMarker.remove();
                Log.d("PickupMarker","Not Null");
            }
        }
        if (currentLocation != null && mMap!=null) {

            mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    builder.include(latLng);
                    builder.include(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(builder.build(),mapBorder);
                    mMap.animateCamera(cameraUpdate, new GoogleMap.CancelableCallback() {
                        @Override
                        public void onFinish() {
                            Log.d("Maps Animation", "Finished");
                            if (enableButton == 0 && currentLocation != null) {
                                Log.d("CurrentLocation", "Available" + GsonStringConvertor.gsonToString(currentLocation));
                                enableButton = 1;

                                actionButton.setEnabled(true);
                                if (tripStatus==0 && currentLocation!=null && !journeyStartedFromGarageDialog.isAdded()) {
                                    journeyStartedFromGarageDialog.show(getSupportFragmentManager(),"start from garage");
                                    journeyStartedFromGarageDialog.setCancelable(false);
                                }
                                else if (currentLocation==null){
                                    toastErrorMessage.setText("Location Unavailable");
                                    errorToast.show();
                                }
                            }
                            if (enableButtonDrop==1){
                                enableButtonDrop=0;
                                actionButton.setEnabled(true);
                                if (tripStatus==0 && currentLocation!=null && !journeyStartedFromGarageDialog.isAdded()) {
                                    journeyStartedFromGarageDialog.show(getSupportFragmentManager(),"start from garage");
                                    journeyStartedFromGarageDialog.setCancelable(false);
                                }
                                else if (currentLocation==null){
                                    toastErrorMessage.setText("Location Unavailable");
                                    errorToast.show();
                                }
                            }

                        }

                        @Override
                        public void onCancel() {
                            if (enableButton == 0 && currentLocation != null) {
                                Log.d("CurrentLocation", "Available");
                                enableButton = 1;
                                actionButton.setEnabled(true);
                                if (tripStatus==0 && currentLocation!=null && !journeyStartedFromGarageDialog.isAdded()) {
                                    journeyStartedFromGarageDialog.show(getSupportFragmentManager(),"start from garage");
                                    journeyStartedFromGarageDialog.setCancelable(false);
                                }
                                else if (currentLocation==null){
                                    toastErrorMessage.setText("Location Unavailable");
                                    errorToast.show();
                                }
                            }
                            if (enableButtonDrop==1){
                                enableButtonDrop=0;
                                actionButton.setEnabled(true);
                                if (tripStatus==0 && currentLocation!=null && !journeyStartedFromGarageDialog.isAdded()) {
                                    journeyStartedFromGarageDialog.show(getSupportFragmentManager(),"start from garage");
                                    journeyStartedFromGarageDialog.setCancelable(false);
                                }
                                else if (currentLocation==null){
                                    toastErrorMessage.setText("Location Unavailable");
                                    errorToast.show();
                                }
                            }
                        }
                    });

                }
            });
        }
    else{
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,12));
        }
    }

    @Override
    public void onStartRideClicked(String startkm) {
        Log.d("StartKm",startkm);
        actionButton.setEnabled(false);
        currentBookingViewModel.initiateRideStarted(
                driverInfoViewModel.getAccessToken().getValue(),
                booking.getBookingId(),
                currentLocation,
                startkm
                );
    }

    @Override
    public void onRideEnd(String endKm, String stateTax, String parking, String tollTax,String otherTax,String garageDistance
    ,String estimateGarageDistance,String estimateTimeToReachGarage,String calculatedTripDistance) {
        actionButton.setEnabled(false);
        currentBookingViewModel.initiateEndRide(driverInfoViewModel.getAccessToken().getValue(),
                booking.getBookingId(),
                currentLocation,
                endKm,
                garageDistance,
                String.format("%.2f",currentBookingViewModel.getDistanceTravelled()/1000),
                calculatedTripDistance,
                estimateGarageDistance,
                estimateTimeToReachGarage,
                stateTax,
                parking,
                tollTax,
                otherTax
                );
    }


    @Override
    public void onStartFromGarageClicked(String startKm) {
        Log.d("StartKm",startKm);
        actionButton.setEnabled(false);
        garageLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        currentBookingViewModel.initiateDriverStarted(driverInfoViewModel.getAccessToken().getValue(),
                booking.getBookingId(), currentLocation,startKm,booking.getBookingType());
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        currentBookingViewModel.getLiveLocation().removeObservers(this);
        Log.d("DestroyedMainActivity","True");
    }

    /**
     * Function to check location permission.
     */
    void checkPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Permissions Required For Tracking ");
                builder.setMessage("location details are required to track and find Estimate time of arrival");
                builder.setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("Permission Request", " Accepted");
                        ActivityCompat.requestPermissions(MainActivity.this,permissions, PERMISSION_CODE);
                    }
                });
                builder.setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("Permission Request", "Denied");
                        toastErrorMessage.setText("Please Grant Permission To Continue");
                        errorToast.show();
                        finish();
                    }
                });
                builder.show();
            }
            else {
                ActivityCompat.requestPermissions(this,permissions, PERMISSION_CODE);
            }
        }
        else {
            Log.d("Permission","Available");
            startTracking(); //permission available start tracking.
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_CODE:
                Log.d("Permission","Both "+ GsonStringConvertor.gsonToString(permissions)+" "+ GsonStringConvertor.gsonToString(grantResults));
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startTracking();
                } else if (grantResults[0]==PackageManager.PERMISSION_DENIED){
                    Log.d("Permission","Denied");
                    toastErrorMessage.setText("Please Grant Permission To Continue");
                    errorToast.show();
                    finish();
                /*  Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    this.startActivity(intent);
                    */
                }
                return;
        }
    }

    /**
     * Function to start tracking.
     */
    void startTracking(){
        LocationSettingUtill locationSettingUtill=LocationSettingUtill.getInstance(getApplication());
        Task<LocationSettingsResponse> task=locationSettingUtill.getTask();
        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                currentBookingViewModel.getLiveLocation().observe(MainActivity.this, new Observer<Location>() {
                    @Override
                    public void onChanged(@Nullable Location location) {
                        onLocationChanged(location);
                    }
                });
            }
        });
        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    try {
                        resolvable.startResolutionForResult(MainActivity.this,
                                REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException e1) {
                        e1.printStackTrace();
                    }
                }
                else {
                    Log.e("Issue","Cannot Start Location Update");
                }
            }
        });
    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();

        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS: //gps is enabled start location updates
                Log.i("TAG", "All location settings are satisfied.");
                startLocationUpdates();
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED: //gps is closed show the dialog to enable the gps in screen.
                Log.i("TAG", "Location settings are not satisfied. Show the user a dialog to" +
                        "upgrade location settings ");
                try {
                    status.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    Log.i("TAG", "PendingIntent unable to execute request.");

                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE://gps of device cannot be used by the app. kill the activity
                Log.i("TAG", "Location settings are inadequate, and cannot be fixed here. Dialog " +
                        "not created.");
                finish();
                break;
        }
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data){
        switch (reqCode){
            case REQUEST_CHECK_SETTINGS: //Location Setting data
                switch (resultCode) {
                    case Activity.RESULT_OK:  //start location updates
                        currentBookingViewModel.setEnableLocationClicked(false);
                        startLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED: // finish the activity because driver is not allowed to use this section without enabling location
                        // The user was asked to change settings, but chose not to
                        Log.e("result", "cancelled");
                        toastErrorMessage.setText("Please Enable Location To Continue");
                        errorToast.show();
                        if(!currentBookingViewModel.getEnableLocationClicked().getValue()){
                            finish();
                        }
                        break;
                    default:
                        break;
                }
                break;
            case REQUEST_OVERLAY_SETTING:// Overlay Permission to show an icon on the screen to return back to app
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (Settings.canDrawOverlays(MainActivity.this)){ //Create screen overlay and start navigation to the desired location
                        Uri gmmIntentUri = Uri.parse("google.navigation:q=+" + pickupPoint.getText().toString() + "&mode=d\"");
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);
                        Intent intent=new Intent(MainActivity.this,FloatingHeadDialog.class);
                        intent.putExtra("bookingInfo",GsonStringConvertor.gsonToString(booking));
                        startService(intent);
                    }else {// Don't create overlay just start navigation on google maps
                        Uri gmmIntentUri = Uri.parse("google.navigation:q=+" + pickupPoint.getText().toString() + "&mode=d\"");
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);
                    }
                }
                // for versions older then Android M.
                Uri gmmIntentUri = Uri.parse("google.navigation:q=+" + pickupPoint.getText().toString() + "&mode=d\"");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
                break;

            case SIGNATURE_CAPTURE_ACTIVITY_REQUEST_CODE:
                if (resultCode==RESULT_OK) {
                    if (tripStatus==2){
                        Log.d("SignatureCapture","Main");
                        onStartRideClicked(currentBookingViewModel.getStartKms());
                    }
                    else if (tripStatus==3){
                        onRideEnd(currentBookingViewModel.getEndKm(),
                                currentBookingViewModel.getStateTax(),
                                currentBookingViewModel.getParking(),
                                currentBookingViewModel.getTollTax(),
                                currentBookingViewModel.getOtherTax(),
                                currentBookingViewModel.getGarageDistance(),
                                currentBookingViewModel.getEstimateGarageDistance(),
                                currentBookingViewModel.getEstimateTimeToReachGarage(),
                                currentBookingViewModel.getCalculatedTripDistance());
                    }
                }
        }
    }


    /**
     *Function to start location update for the current location of taxi.
     * it checks for the permission
     * if there is any current booking then LocationService is already running so disconnect mGoogleApiClient to save battery
     * if not a current booking then start location update
     */
    protected void startLocationUpdates() {
        //start location updates.
       currentBookingViewModel.getLiveLocation().observe(this, new Observer<Location>() {
           @Override
           public void onChanged(@Nullable Location location) {
              onLocationChanged(location);
           }
       });
    }

    /**
     * Function to stop Location updates
     */
    protected void stopLocationUpdates() {
        currentBookingViewModel.getLiveLocation().removeObservers(this);
    }

    /**
     * Function to get current location updates
     * @param location : current location of taxi
     * Both vehicle and destination are shown by using Latlng bounds.
     */
    public void onLocationChanged(Location location) {
        LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
        currentLocation=location;
        Log.d("CurrentLocation",GsonStringConvertor.gsonToString(location));
        if (mMap==null){                            //check if map is ready
            return;
        }
        if (currentMarker==null){ //create new marker and set its location
            currentMarker=mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.taxi_dark_128))
                    .position(latLng));
        }
        else { // update marker position
            currentMarker.setPosition(latLng);
        }

        if (eta!=null && tripStatus<3){
            currentMarker.setTitle(eta);
            currentMarker.showInfoWindow();
        }
        else if (eta!=null && tripStatus==3){
            currentMarker.setTitle(eta);
            currentMarker.hideInfoWindow();
        }

        //rotation to move taxi in the direction of motion of vehicle.
        currentMarker.setRotation(location.getBearing());



       if (tripStatus<3 &&pickUpMarker!=null && pickUpMarker.getPosition()!=null) {
           Log.d("ServiceBound","true");
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(latLng);
           builder.include(pickUpMarker.getPosition());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(builder.build(),mapBorder);
            mMap.animateCamera(cameraUpdate, new GoogleMap.CancelableCallback() {
                @Override
                public void onFinish() {
                    Log.d("Maps Animation","Finished");
                    if (enableButton==0 && currentLocation!=null){
                        Log.d("CurrentLocation","Available");
                        enableButton=1;                 //
                        actionButton.setEnabled(true);  //action button enabled after latlngs are animated.
                        if (tripStatus==0 && currentLocation!=null && !journeyStartedFromGarageDialog.isAdded()) {
                            journeyStartedFromGarageDialog.show(getSupportFragmentManager(),"start from garage");
                            journeyStartedFromGarageDialog.setCancelable(false);
                        }
                        else if (currentLocation==null){
                            toastErrorMessage.setText("Location Unavailable");
                            errorToast.show();
                        }
                    }

                }

                @Override
                public void onCancel() {

                }
            });
        }
        else {
           Log.d("ServiceBound","false");
       }
    }

    @Override
    public void onBackPressed() {
        Log.d("EnableLocationClicked",currentBookingViewModel.getEnableLocationClicked().getValue().toString());
        if(currentBookingViewModel.getLocationStatus()!=null && currentBookingViewModel.getLocationStatus().getValue()!=null&&!currentBookingViewModel.getLocationStatus().getValue()){
            Log.d("BackPress","Return");
            return;
        }
        super.onBackPressed();
    }
}
