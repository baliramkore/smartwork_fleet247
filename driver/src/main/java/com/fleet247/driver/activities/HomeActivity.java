package com.fleet247.driver.activities;

import android.app.Activity;
import android.app.PendingIntent;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;

import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDelegate;
import android.util.Log;
import android.view.MenuItem;

import com.fleet247.driver.R;
import com.fleet247.driver.fragment.BookingCompletedFragment;
import com.fleet247.driver.fragment.HomeFragment;
import com.fleet247.driver.fragment.ProfileFragment;
import com.fleet247.driver.fragment.TutorialWebView;
import com.fleet247.driver.tracking.ActivityRecognitionService;
import com.fleet247.driver.viewmodels.DriverInfoViewModel;
import com.fleet247.driver.viewmodels.HomeViewModel;
import com.fleet247.driver.viewmodels.UpdateFcmTokenViewModel;
import com.google.android.gms.location.ActivityTransition;
import com.google.android.gms.location.ActivityTransitionRequest;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.ArrayList;
import java.util.List;

import static com.fleet247.driver.fragment.HomeFragment.REQUEST_CHECK_SETTINGS;
import static com.google.android.gms.location.ActivityRecognition.getClient;


public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    BottomNavigationView bottomNavigationView;
    UpdateFcmTokenViewModel updateFcmTokenViewModel;
    DriverInfoViewModel driverInfoViewModel;
    HomeViewModel homeViewModel;
    PendingIntent pendingIntent;
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottomNavigationView=findViewById(R.id.navigation);

        updateFcmTokenViewModel= ViewModelProviders.of(this).get(UpdateFcmTokenViewModel.class);
        driverInfoViewModel=ViewModelProviders.of(this).get(DriverInfoViewModel.class);
        homeViewModel=ViewModelProviders.of(this).get(HomeViewModel.class);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        //For getting FCM Token for notification.
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if(!task.isSuccessful()){
                    return;
                }

                String fcmToken=task.getResult().getToken();
                if (!updateFcmTokenViewModel.isTokenUpdated(fcmToken)){
                    Log.d("FcmToken",fcmToken);
                    updateFcmTokenViewModel.updateFcmToken(driverInfoViewModel.getAccessToken().getValue(),"driver",fcmToken);
                }
                Log.d("FcmToken",fcmToken+" ");
            }
        });


        //if user closes app without entering cash collected in case of cash booking show them the billing screen
        if(homeViewModel.getIsBillingPending()){
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new BookingCompletedFragment())
                    .commit();
        }
        else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new HomeFragment())
                    .commit();
        }

        /*Intent intent=new Intent(this, ActivityRecognitionService.class);
        pendingIntent=PendingIntent.getService(this,25,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        //ActivityTransitionRequest request = buildTransitionRequest();
        // PendingIntent pendingIntent;  // Your pending intent to receive callbacks.
        //Task task = getClient(this)
        //        .requestActivityTransitionUpdates(request, pendingIntent);
        //ActivityTransitionRequest activityTransitionRequest=buildTransitionRequest();

        /*

        task.addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Log.d("Success", "True");
            }
        });

        task.addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.e("Failure", e.getMessage());
                    }
                });
        */

    }

  /*  ActivityTransitionRequest buildTransitionRequest() {
        List transitions = new ArrayList<>();
        transitions.add(new ActivityTransition.Builder()
                .setActivityType(DetectedActivity.IN_VEHICLE)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                .build());
        transitions.add(new ActivityTransition.Builder()
                .setActivityType(DetectedActivity.IN_VEHICLE)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                .build());
        transitions.add(new ActivityTransition.Builder()
                .setActivityType(DetectedActivity.STILL)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                .build());
        transitions.add(new ActivityTransition.Builder()
                .setActivityType(DetectedActivity.STILL)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                .build());
        return new ActivityTransitionRequest(transitions);
    }
    */

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.d("SelectedId",bottomNavigationView.getSelectedItemId()+"");
        switch (item.getItemId()){
            case R.id.action_home:
                FragmentManager fm = getSupportFragmentManager();
                int count = fm.getBackStackEntryCount();

                for(int i = 0; i < count; ++i) {
                    fm.popBackStack();
                }

                break;
            case R.id.action_profile:

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container,new ProfileFragment())
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.action_tutorial:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container,new TutorialWebView())
                        .addToBackStack(null)
                        .commit();
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_CHECK_SETTINGS: //Location Setting data
                switch (resultCode) {
                    case Activity.RESULT_OK:  //start location updates
                        homeViewModel.setLocationStatus(1);
                        break;
                    case Activity.RESULT_CANCELED: // finish the activity because driver is not allowed to use this section without enabling location
                        // The user was asked to change settings, but chose not to
                        homeViewModel.setLocationStatus(2);
                          break;
                    default:
                        break;
                }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
