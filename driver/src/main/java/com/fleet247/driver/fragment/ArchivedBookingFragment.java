package com.fleet247.driver.fragment;


import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fleet247.driver.R;
import com.fleet247.driver.adapter.ArchivedBookingAdapter;
import com.fleet247.driver.data.models.archivedbooking.Booking;
import com.fleet247.driver.utility.GsonStringConvertor;
import com.fleet247.driver.viewmodels.ArchivedBookingViewModel;
import com.fleet247.driver.viewmodels.CurrentBookingViewModel;
import com.fleet247.driver.viewmodels.DriverInfoViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArchivedBookingFragment extends Fragment implements ArchivedBookingAdapter.RecyclerViewEventListener ,SwipeRefreshLayout.OnRefreshListener {

    ArchivedBookingViewModel archivedBookingViewModel; //ArchivedBookingViewModel instance to get archived bookings list.
    DriverInfoViewModel driverInfoViewModel; //DriverInfoViewModel instance to get driver info and accessToken
    ProgressBar progressBar;
    View view;
    List<Booking> bookingList;
    ArchivedBookingAdapter archivedBookingAdapter;
    CurrentBookingViewModel currentBookingViewModel;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView noBookingAvailable;


    public ArchivedBookingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_archived_booking, container, false);
        progressBar=view.findViewById(R.id.progressBar);
        recyclerView=view.findViewById(R.id.archived_recycleview);
        swipeRefreshLayout=view.findViewById(R.id.archived_swipe_refresh_layout);
        noBookingAvailable=view.findViewById(R.id.no_booking_available);

        noBookingAvailable.setVisibility(View.GONE);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);

        archivedBookingViewModel= ViewModelProviders.of(this).get(ArchivedBookingViewModel.class);
        driverInfoViewModel=ViewModelProviders.of(this).get(DriverInfoViewModel.class);

        bookingList=new ArrayList<>();
        archivedBookingAdapter =new ArchivedBookingAdapter(this,bookingList);
        //progressBar.setVisibility(View.VISIBLE);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(archivedBookingAdapter);

        archivedBookingViewModel.getArchivedBookingList().observe(this, new Observer<List<Booking>>() {
            @Override
            public void onChanged(@Nullable List<Booking> bookings) {
                swipeRefreshLayout.setRefreshing(false);
                Log.d("Booking", GsonStringConvertor.gsonToString(bookings));
                if (bookings.size()>0) {
                    noBookingAvailable.setVisibility(View.GONE);
                    bookingList.clear();
                    bookingList.addAll(bookings);
                    archivedBookingAdapter.notifyDataSetChanged();
                }
            }
        });

        archivedBookingViewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                swipeRefreshLayout.setRefreshing(false);
                if (s!=null && s.length()>0) {
                    Snackbar.make(view, s, Snackbar.LENGTH_LONG).show();
                    Log.e("Error", s);
                    noBookingAvailable.setVisibility(View.VISIBLE);
                }
            }
        });

        archivedBookingViewModel.getArchivedBookings(driverInfoViewModel.getAccessToken().getValue());
        return view;
    }


    @Override
    public void onDetailsClicked(Booking booking) {
        Log.d("Booking",GsonStringConvertor.gsonToString(booking));
        Bundle bundle=new Bundle();
        bundle.putString("booking",GsonStringConvertor.gsonToString(booking));
        FareDetailsFragment fareDetailsFragment=new FareDetailsFragment();
        fareDetailsFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.container,fareDetailsFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onRefresh() {
        archivedBookingViewModel.getArchivedBookings(driverInfoViewModel.getAccessToken().getValue());
    }
}
