package com.fleet247.driver.fragment;


import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fleet247.driver.R;
import com.fleet247.driver.adapter.CancelledBookingAdapter;
import com.fleet247.driver.data.models.cancelledbookings.Booking;
import com.fleet247.driver.utility.GsonStringConvertor;
import com.fleet247.driver.viewmodels.CancelledBookingsViewModels;
import com.fleet247.driver.viewmodels.DriverInfoViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CancelledBookingsFragment extends Fragment implements CancelledBookingAdapter.RecyclerViewEventListener,
OnRefreshListener{

    View view;
    RecyclerView recyclerView;
    ProgressBar progressBar;

    List<Booking> cancelledBookingList;
    CancelledBookingAdapter cancelledBookingAdapter;
    CancelledBookingsViewModels cancelledBookingsViewModels;
    DriverInfoViewModel driverInfoViewModel;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView noBookingAvailable;


    public CancelledBookingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_cancelled_bookings, container, false);
        recyclerView=view.findViewById(R.id.cancelled_recycleview);
        swipeRefreshLayout=view.findViewById(R.id.cancelled_swipe_refresh_layout);
        noBookingAvailable=view.findViewById(R.id.no_booking_available);

        noBookingAvailable.setVisibility(View.GONE);

        cancelledBookingList=new ArrayList<>();
        cancelledBookingsViewModels= ViewModelProviders.of(this).get(CancelledBookingsViewModels.class);
        driverInfoViewModel=ViewModelProviders.of(this).get(DriverInfoViewModel.class);
        cancelledBookingAdapter=new CancelledBookingAdapter(this,cancelledBookingList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(cancelledBookingAdapter);

        cancelledBookingsViewModels.getCancelledBookingList().observe(this, new Observer<List<Booking>>() {
            @Override
            public void onChanged(@Nullable List<Booking> bookings) {
                Log.d("Bookings", GsonStringConvertor.gsonToString(bookings));
                swipeRefreshLayout.setRefreshing(false);
                if (bookings!=null && bookings.size()>0){
                    noBookingAvailable.setVisibility(View.GONE);
                    cancelledBookingList.clear();
                    cancelledBookingList.addAll(bookings);
                    cancelledBookingAdapter.notifyDataSetChanged();
                }
            }
        });

        cancelledBookingsViewModels.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                swipeRefreshLayout.setRefreshing(false);
                if (s!=null && s.length()>0){
                    noBookingAvailable.setVisibility(View.VISIBLE);
                    Snackbar.make(view,s,Snackbar.LENGTH_LONG).show();
                }
            }
        });


        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);
        cancelledBookingsViewModels.getCancelledBookings(driverInfoViewModel.getAccessToken().getValue());

        return view;
    }


    @Override
    public void onDetailsClicked(Booking booking) {

    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        cancelledBookingsViewModels.getCancelledBookings(driverInfoViewModel.getAccessToken().getValue());
    }
}
