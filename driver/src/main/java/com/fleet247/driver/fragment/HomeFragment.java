package com.fleet247.driver.fragment;


import android.Manifest;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.fleet247.driver.data.repository.taskhandler.LocationSettingUtill;
import com.fleet247.driver.tracking.ActiveDriverTrackingService;
import com.fleet247.driver.utility.JobSchedularUtility;
import com.fleet247.driver.viewmodels.HomeViewModel;
import com.fleet247.driver.viewmodels.SignatureCaptureViewModel;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationSettingsResponse;
import com.fleet247.driver.R;
import com.fleet247.driver.activities.MainActivity;
import com.fleet247.driver.utility.GsonStringConvertor;
import com.fleet247.driver.viewmodels.CurrentBookingViewModel;
import com.fleet247.driver.viewmodels.DriverInfoViewModel;
import com.fleet247.driver.viewmodels.UpcomingBookingViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;


/**
 * Fragment to show home/landing screen of the screen.
 */
public class HomeFragment extends Fragment implements View.OnClickListener,CompoundButton.OnCheckedChangeListener {

    View view;
    LinearLayout upcomingBookingLay; //layout represents upcoming bookings.
    CardView currentBookingLay;  //layout represents current bookings.
    LinearLayout archivedBookingLay; //layout represents archived bookings.
    LinearLayout cancelledBookingLay;
    TextView driverName; //TextView driver name.
    CurrentBookingViewModel currentBookingViewModel; //current booking ViewModel to get current booking data.
    DriverInfoViewModel driverInfoViewModel;  //driver info ViewModel to get driver information.
    UpcomingBookingViewModel upcomingBookingViewModel;  //
    ProgressBar progressBar;
    Switch activeInactiveSwitch;
    String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
    HomeViewModel homeViewModel;
    SignatureCaptureViewModel signatureCaptureViewModel;
    private final int PERMISSION_CODE = 101;
    TextView contactUs;

    public static final int REQUEST_CHECK_SETTINGS = 3;


    public HomeFragment() {
        // Required empty public constructor
    }

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getActivity()==null){
            Log.d("Activity","Null");
        }
        else {
            Log.d("Activity","Not Null");
        }
        currentBookingViewModel= ViewModelProviders.of(this).get(CurrentBookingViewModel.class);
        driverInfoViewModel=ViewModelProviders.of(getActivity()).get(DriverInfoViewModel.class);
        upcomingBookingViewModel=ViewModelProviders.of(this).get(UpcomingBookingViewModel.class);
        signatureCaptureViewModel=ViewModelProviders.of(this).get(SignatureCaptureViewModel.class);
        homeViewModel=ViewModelProviders.of(getActivity()).get(HomeViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_landing, container, false);
        upcomingBookingLay=view.findViewById(R.id.upcoming_booking_lay);
        currentBookingLay=view.findViewById(R.id.current_booking_lay);
        archivedBookingLay=view.findViewById(R.id.archived_booking_lay);
        cancelledBookingLay=view.findViewById(R.id.cancelled_booking_lay);
        driverName=view.findViewById(R.id.driver_name);
        activeInactiveSwitch=view.findViewById(R.id.active_inactive_switch);
        progressBar=view.findViewById(R.id.progressBar);
        contactUs=view.findViewById(R.id.contact_support);
        progressBar.setVisibility(View.GONE);

        currentBookingLay.setOnClickListener(this);
        upcomingBookingLay.setOnClickListener(this);
        archivedBookingLay.setOnClickListener(this);
        cancelledBookingLay.setOnClickListener(this);
        contactUs.setOnClickListener(this);

        activeInactiveSwitch.setOnCheckedChangeListener(this);

        driverName.setText(driverInfoViewModel.getDriver().getValue().getDriverName());


        //Show tutorial for upcoming booking button.
        if(!driverInfoViewModel.getShownUpcomingTutorial()) {

            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    TapTargetView.showFor(getActivity(), TapTarget.forView(view.findViewById(R.id.upcoming_booking_lay), "Click Here", "To Start Upcoming Booking")
                            .outerCircleColor(R.color.showcase_green)
                            .drawShadow(true)
                            .titleTextSize(40)
                            .descriptionTextSize(30)
                            .descriptionTextColor(android.R.color.white)
                            .icon(getResources().getDrawable(R.drawable.ic_baseline_touch_app_24px))
                            .cancelable(true));

                    driverInfoViewModel.setShownUpcomingTutorial();
                }
            }, 1500);

        }

        //if driver is completing a trip move to main activity.
      if (currentBookingViewModel.getIsCurrentBooking()){
            Intent intent=new Intent(getActivity(), MainActivity.class);
            intent.putExtra("bookingInfo", GsonStringConvertor.gsonToString(currentBookingViewModel.getCurrentBooking().getValue()));
            Log.d("bookingInfo",GsonStringConvertor.gsonToString(currentBookingViewModel.getCurrentBooking().getValue()));
            startActivity(intent);
            //getActivity().finish();

      }
      else {
          currentBookingLay.setVisibility(View.GONE);
      }

      Log.d("SavedSignature",GsonStringConvertor.gsonToString(signatureCaptureViewModel.getSignature()));
      Log.d("SavedSignature",GsonStringConvertor.gsonToString(signatureCaptureViewModel.getAllSignature()));
      Log.d("IsUploadJobScheduled",JobSchedularUtility.isUploadSignaturesJobScheduled(getActivity().getApplication())+" S");


      // if it is a fresh login make driver active for getting new bookings.
      if (driverInfoViewModel.isFreshLogin()){
          activeInactiveSwitch.setChecked(true);
          activeInactiveSwitch.setText(getString(R.string.driver_status_active));
          driverInfoViewModel.setFreshLogin();
      }
      else if (driverInfoViewModel.getIsActive()){
          activeInactiveSwitch.setChecked(true);
          activeInactiveSwitch.setText(getString(R.string.driver_status_active));
         // getActivity().startService(new Intent(getActivity(),ActiveDriverTrackingService.class));
      }else{
          activeInactiveSwitch.setText(getString(R.string.driver_status_inactive));
      }

      /*upcomingBookingViewModel.getUpcomingBookingList().observe(this, new Observer<List<Booking>>() {
          @Override
          public void onChanged(@Nullable List<Booking> bookings) {
              if (bookings!=null && bookings.size()>0){
                  progressBar.setVisibility(View.GONE);
                  Log.d("Booking","Available");
                  Intent intent=new Intent(getActivity(), MainActivity.class);
                  intent.putExtra("bookingInfo", GsonStringConvertor.gsonToString(bookings.get(0)));
                  upcomingBookingViewModel.deleteUpcomingBookingList();
                  startActivity(intent);

                /* Fragment fragment=new CurrentBookingFragment();
                 Bundle args=new Bundle();
                 args.putString("bookingInfo",GsonStringConvertor.gsonToString(bookings.get(0)));
                 fragment.setArguments(args);
                  getFragmentManager().beginTransaction()
                          .replace(R.id.container,fragment)
                          .addToBackStack(null)
                          .commit();
                  upcomingBookingViewModel.deleteUpcomingBookingList();
                  //Snackbar.make(view,"No Booking Available",Snackbar.LENGTH_LONG).show();
              }
          }
      });*/


      /*upcomingBookingViewModel.getError().observe(this, new Observer<String>() {
          @Override
          public void onChanged(@Nullable String s) {
              if (s!=null && s.length()>0){
                  progressBar.setVisibility(View.GONE);
                  Snackbar.make(view,s,Snackbar.LENGTH_LONG).show();
              }
          }
      });*/


      //LocationStatus is if user has enabled location from enable location popup.
      //LocationStatus value is set in HomeActivity.
      //1 represents user has enabled location.
      //2 represents user has disabled location.
      homeViewModel.getLocationStatus().observe(this, new Observer<Integer>() {
          @Override
          public void onChanged(@Nullable Integer integer) {
              if (integer==1){
                  JobSchedularUtility.scheduleNetworkChangeJob(getActivity().getApplication());

                  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                      getActivity().startForegroundService(new Intent(getActivity(),ActiveDriverTrackingService.class));
                  }
                  else {
                      getActivity().startService(new Intent(getActivity(),ActiveDriverTrackingService.class));
                  }
                  driverInfoViewModel.setIsActive(true);
                  activeInactiveSwitch.setText(getString(R.string.driver_status_active));
                  homeViewModel.setLocationStatus(0);
              }
              else if (integer==2){
                  activeInactiveSwitch.setChecked(false);
                  Log.e("result", "cancelled");
                  Log.d("Location","Disabled");
                  Toast.makeText(getActivity(),"Please Enable Location To Continue",Toast.LENGTH_LONG).show();
                  homeViewModel.setLocationStatus(0);
              }

          }
      });

    return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.backgroungBlue));
        if (currentBookingViewModel.getIsCurrentBooking()){
            currentBookingLay.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.current_booking_lay:
               if (currentBookingViewModel.getIsCurrentBooking()){
                    Intent intent=new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("bookingInfo", GsonStringConvertor.gsonToString(currentBookingViewModel.getCurrentBooking().getValue()));
                    Log.d("bookingInfo",GsonStringConvertor.gsonToString(currentBookingViewModel.getCurrentBooking().getValue()));
                    startActivity(intent);
                }
                else {
                   currentBookingLay.setVisibility(View.GONE);
               }
               break;

            case R.id.upcoming_booking_lay:
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container,new UpcomingBookingsFragment())
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.archived_booking_lay:
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container,new ArchivedBookingFragment())
                        .addToBackStack(null)
                        .commit();
                //Snackbar.make(view,"Coming Soon",Snackbar.LENGTH_LONG).show();
                break;

            case R.id.cancelled_booking_lay:
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container,new CancelledBookingsFragment())
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.contact_support:
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "+918030656503"));
                startActivity(intent);

        }

    }

    /**
     *
     * @param compoundButton
     * @param b
     * Switch changed listener.
     */
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
            driverInfoViewModel.setIsActive(true);
            if (!currentBookingViewModel.getIsCurrentBooking()) {
               checkPermission();
            }
        }else {
            JobSchedularUtility.deleteNetworkChangeJob(getActivity().getApplication());
            driverInfoViewModel.setIsActive(false);
            getActivity().stopService(new Intent(getActivity(), ActiveDriverTrackingService.class));
            activeInactiveSwitch.setText(getString(R.string.driver_status_inactive));
        }
    }


    /**
     * Check for the Location Permission for starting driver's location for tracking and making him/her active.
     */
    void checkPermission(){
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)){
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Permissions Required For Tracking ");
                builder.setMessage("location details are required to track and find Estimate time of arrival");
                builder.setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("Permission Request", " Accepted");
                        requestPermissions(permissions, PERMISSION_CODE);
                    }
                });
                builder.setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("Permission Request", "Denied");
                        Toast.makeText(getActivity(),"Please Grant Permission To Continue",Toast.LENGTH_LONG).show();
                        activeInactiveSwitch.setChecked(false);
                    }
                });
                builder.show();
            }
            else {
                this.requestPermissions(permissions, PERMISSION_CODE);
            }
        }
        else {
            Log.d("Permission","Available");
            startDriverTrackingService(); //permission available start tracking.
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_CODE:
                Log.d("Permission","Both "+ GsonStringConvertor.gsonToString(permissions)+" "+ GsonStringConvertor.gsonToString(grantResults));
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //if Location Permission is granted we will start driverTrackingService.
                    startDriverTrackingService();
                } else if (grantResults[0]==PackageManager.PERMISSION_DENIED){
                    Log.d("Permission","Denied");
                    //if LocationPermission is denied we will set activeInactiveSwitch to false.
                    activeInactiveSwitch.setChecked(false);

                    Toast.makeText(getActivity(),"Please Grant Permission To Continue",Toast.LENGTH_LONG).show();
                /*  Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    this.startActivity(intent);
                    */
                }
        }
    }


    /**
     * This method checks if location is enabled
     * if location is enabled it starts ActiveDriverTrackingService
     * else it shows enable location popup to driver.
     */
    void startDriverTrackingService(){
        LocationSettingUtill locationSettingUtill=LocationSettingUtill.getInstance(getActivity().getApplication());
        Task<LocationSettingsResponse> task=locationSettingUtill.getTask();
        task.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                JobSchedularUtility.scheduleNetworkChangeJob(getActivity().getApplication());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    getActivity().startForegroundService(new Intent(getActivity(),ActiveDriverTrackingService.class));
                }
                else {
                   getActivity().startService(new Intent(getActivity(),ActiveDriverTrackingService.class));
                }
                driverInfoViewModel.setIsActive(true);
                Log.d("Location","Enabled");
                activeInactiveSwitch.setText(getString(R.string.driver_status_active));
            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    try {
                        resolvable.startResolutionForResult(getActivity(),
                                REQUEST_CHECK_SETTINGS);
                        Log.d("Location","Resolution Required");
                    } catch (IntentSender.SendIntentException e1) {
                        e1.printStackTrace();
                        Log.d("Location","Disabled");
                        activeInactiveSwitch.setChecked(false);
                    }
                }
                else {
                    Log.d("Location","Disabled");
                    Log.e("Issue","Cannot Start Location Update");
                    activeInactiveSwitch.setChecked(false);
                }
            }
        });


    }
}
