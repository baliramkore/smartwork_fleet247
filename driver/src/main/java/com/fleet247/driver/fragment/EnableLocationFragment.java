package com.fleet247.driver.fragment;


import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.fleet247.driver.R;
import com.fleet247.driver.viewmodels.CurrentBookingViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class EnableLocationFragment extends Fragment implements View.OnClickListener {


    CurrentBookingViewModel currentBookingViewModel;
    View view;
    Button enableLocation;

    public EnableLocationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        currentBookingViewModel = ViewModelProviders.of(getActivity()).get(CurrentBookingViewModel.class);
        view= inflater.inflate(R.layout.enable_location, container, false);
        enableLocation=view.findViewById(R.id.enable_location);
        enableLocation.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        currentBookingViewModel.setEnableLocationClicked(true);
    }
}
