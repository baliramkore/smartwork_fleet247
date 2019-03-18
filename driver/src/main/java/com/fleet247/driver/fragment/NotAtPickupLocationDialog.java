package com.fleet247.driver.fragment;


import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.fleet247.driver.R;
import com.fleet247.driver.viewmodels.CurrentBookingViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotAtPickupLocationDialog extends DialogFragment implements View.OnClickListener {

    View view;
    Button cancelButton;
    Button okButton;

    CurrentBookingViewModel currentBookingViewModel;


    public NotAtPickupLocationDialog() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.not_at_pickup_location_dialog,container,false);

        currentBookingViewModel=ViewModelProviders.of(getActivity()).get(CurrentBookingViewModel.class);
        cancelButton=view.findViewById(R.id.button_cancel);
        okButton=view.findViewById(R.id.button_ok);

        okButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.button_ok:
                currentBookingViewModel.setNotAtPickupLocation(true);
                dismiss();
                break;

            case R.id.button_cancel:
                dismiss();
                break;
        }
    }
}
