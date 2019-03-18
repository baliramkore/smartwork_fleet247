package com.fleet247.driver.fragment;


import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fleet247.driver.R;
import com.fleet247.driver.activities.MainActivity;
import com.fleet247.driver.adapter.CallUserAdapter;
import com.fleet247.driver.adapter.UpcomingBookingAdapter;
import com.fleet247.driver.data.models.upcomingbooking.Booking;
import com.fleet247.driver.data.repository.taskhandler.DateTimeUtill;
import com.fleet247.driver.utility.GsonStringConvertor;
import com.fleet247.driver.utility.NetworkStatus;
import com.fleet247.driver.viewmodels.CurrentBookingViewModel;
import com.fleet247.driver.viewmodels.DriverInfoViewModel;
import com.fleet247.driver.viewmodels.UpcomingBookingViewModel;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpcomingBookingsFragment extends Fragment implements UpcomingBookingAdapter.RecyclerViewEventListener,
CallUserAdapter.CallUserEventListener,CancellationReasonDialog.CancelBookingListener{


    View view;
    UpcomingBookingViewModel upcomingBookingViewModel;
    DriverInfoViewModel driverInfoViewModel;
    List<Booking> bookingList;
    UpcomingBookingAdapter upcomingBookingAdapter;
    CurrentBookingViewModel currentBookingViewModel;
    RecyclerView recyclerView;
    Toolbar toolbar;
    ProgressBar progressBar;
    CancellationReasonDialog cancellationReasonDialog;
    TextView noBookingAvailable;

    public UpcomingBookingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.statusGrey));
        view= inflater.inflate(R.layout.fragment_upcoming_bookings, container, false);
        toolbar=view.findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.upcoming_booking_text));
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        upcomingBookingViewModel= ViewModelProviders.of(this).get(UpcomingBookingViewModel.class);
        driverInfoViewModel=ViewModelProviders.of(getActivity()).get(DriverInfoViewModel.class);
        recyclerView=view.findViewById(R.id.upcoming_recycleview);
        progressBar=view.findViewById(R.id.progressBar);
        noBookingAvailable= view.findViewById(R.id.no_booking_available);

        progressBar.setVisibility(View.VISIBLE);
        noBookingAvailable.setVisibility(View.GONE);

        cancellationReasonDialog=new CancellationReasonDialog();

        bookingList=new ArrayList<>();
        upcomingBookingAdapter=new UpcomingBookingAdapter(this,bookingList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(upcomingBookingAdapter);

        upcomingBookingViewModel.getUpcomingBookingList().observe(this, new Observer<List<Booking>>() {
            @Override
            public void onChanged(@Nullable List<Booking> bookings) {
                Log.d("UpcomingBookings", GsonStringConvertor.gsonToString(bookings));
                progressBar.setVisibility(View.GONE);
                if (bookings.size()>0) {
                    noBookingAvailable.setVisibility(View.GONE);
                    bookingList.clear();
                    bookingList.addAll(bookings);
                    upcomingBookingAdapter.notifyDataSetChanged();
                    showTutorial();
                }
            }
        });

        upcomingBookingViewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                progressBar.setVisibility(View.GONE);
                if (s!=null){
                    if(s.equals("No Booking Available")){
                        bookingList.clear();
                        upcomingBookingAdapter.notifyDataSetChanged();
                    }
                    noBookingAvailable.setVisibility(View.VISIBLE);
                    Snackbar.make(view,s,Snackbar.LENGTH_LONG).show();
                }
            }
        });

        upcomingBookingViewModel.getCancelledBookingStatus().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if (s!=null){
                    progressBar.setVisibility(View.GONE);
                    Snackbar.make(view,s,Snackbar.LENGTH_LONG).show();
                    progressBar.setVisibility(View.VISIBLE);
                    upcomingBookingViewModel.getUpcomingBookings(driverInfoViewModel.getAccessToken().getValue());
                }
            }
        });


        return view;
    }

    void showTutorial(){
        if(!upcomingBookingViewModel.getShownTripStratedTutorial()) {
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    TapTargetView.showFor(getActivity(), TapTarget.forView(getActivity().findViewById(R.id.start_booking), "Click Here", "To Start Booking")
                            .outerCircleColor(R.color.showcase_green)
                            .targetRadius(70)
                            .transparentTarget(true)
                            .titleTextSize(40)
                            .descriptionTextSize(30)
                            .descriptionTextColor(android.R.color.white)
                            .icon(getActivity().getResources().getDrawable(R.drawable.ic_baseline_touch_app_24px)));
                    upcomingBookingViewModel.setShownTripStartedTutorial();
                }
            }, 1500);
        }
    }

    @Override
    public void onDetailsClicked(Booking booking) {
        if (NetworkStatus.isInternetConnected(getActivity().getApplication())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Log.d("Battery Optimization","Enabled");
                Calendar calendar=Calendar.getInstance();
                Date currentDate=new Date(System.currentTimeMillis());
                Log.d("PickUpDateTime",booking.getPickupDatetime().trim());
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                try {
                    Date date=simpleDateFormat.parse(booking.getPickupDatetime());
                    Log.d("FormatedDate",simpleDateFormat.format(date));
                    if (date.after(currentDate)){
                        long diff=date.getTime()-currentDate.getTime();
                        long hours=diff/(60*60*1000);
                        int basehour;
                        Log.d("Tour Type",booking.getTourType());
                        if (booking.getTourType().equals("Local")){
                            basehour=4;
                        }
                        else {
                            basehour=24;
                        }
                        if (hours<=basehour){
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            intent.putExtra("bookingInfo", GsonStringConvertor.gsonToString(booking));
                            startActivity(intent);
                        }else {
                            Snackbar.make(view,"You can start trip "+basehour+" hours before Pickup Time",Snackbar.LENGTH_LONG).show();
                            Log.d("Start Booking","False");
                        }
                    }
                    else if (date.before(currentDate)){
                        Log.d("Start Booking","True");
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.putExtra("bookingInfo", GsonStringConvertor.gsonToString(booking));
                        startActivity(intent);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                /*PowerManager powerManager=(PowerManager) getActivity().getSystemService(Context.POWER_SERVICE);;
                if (powerManager.isIgnoringBatteryOptimizations(getActivity().getPackageName())){

                }else {
                    Intent intent=new Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
                    getActivity().startActivity(intent);
                    Toast.makeText(getActivity(),getString(R.string.battery_optimization_message),Toast.LENGTH_LONG).show();
                    Toast.makeText(getActivity(),getString(R.string.battery_optimization_message),Toast.LENGTH_LONG).show();
                }*/
            }
        }
        else {
            Toast.makeText(getActivity(),"Internet Connection Unavailable",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCancelledClicked(Booking booking) {
        if (!cancellationReasonDialog.isAdded()){
            Bundle bundle=new Bundle();
            bundle.putString("booking_id",booking.getBookingId());
            cancellationReasonDialog.setArguments(bundle);
            cancellationReasonDialog.addCancelBookingListener(this);
            cancellationReasonDialog.show(getActivity().getSupportFragmentManager(),"cancellation_dialog");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        upcomingBookingViewModel.getUpcomingBookings(driverInfoViewModel.getAccessToken().getValue());
        Log.d("FormattedDate",DateTimeUtill.getDate());
        Log.d("FormattedTime",DateTimeUtill.getTime());
    }

    @Override
    public void onCallClicked(String contactNo) {

    }

    @Override
    public void onConfirmCancelClicked(String cancellationReason) {
        progressBar.setVisibility(View.VISIBLE);
        upcomingBookingViewModel.cancelBooking(driverInfoViewModel.getAccessToken().getValue(),upcomingBookingViewModel.getUpcomingBookingList().getValue().get(0).getBookingId(),cancellationReason);
    }
}
