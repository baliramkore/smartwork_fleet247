package com.fleet247.driver.fragment;


import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fleet247.driver.R;
import com.fleet247.driver.utility.NetworkStatus;
import com.fleet247.driver.viewmodels.CurrentBookingViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class StartJourneyDialog extends DialogFragment implements View.OnClickListener{

    View view;

    EditText startKm;
    EditText otp;
    Button startJourney;
    String startOtp;
    CurrentBookingViewModel currentBookingViewModel;
    TextView trySignature;
    StartRideListener startRideListener;

    public StartJourneyDialog() {
        // Required empty public constructor
    }


    public void addStartRideListener(StartRideListener startRideListener){
        this.startRideListener=startRideListener;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_start_journey_dialog, container, false);
        startKm=view.findViewById(R.id.start_km);
        otp=view.findViewById(R.id.start_otp);
        startJourney=view.findViewById(R.id.startTrip);
        trySignature=view.findViewById(R.id.try_signature);
        startJourney.setOnClickListener(this);
        trySignature.setOnClickListener(this);
        currentBookingViewModel= ViewModelProviders.of(getActivity()).get(CurrentBookingViewModel.class);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.startTrip:
                if (NetworkStatus.isInternetConnected(getActivity().getApplication())) {
                    if (startKm.getText() != null && startKm.getText().toString().length() > 0
                            && otp.getText() != null && otp.getText().length() > 0) {
                        if (currentBookingViewModel.isStartOtpVerified(otp.getText().toString())) {
                            currentBookingViewModel.setStartData(startKm.getText().toString());
                            startRideListener.onStartRideClicked(startKm.getText().toString());
                            dismiss();
                        } else {
                            Snackbar.make(view, "Please Enter Correct OTP", Snackbar.LENGTH_LONG).show();
                        }
                    } else {
                        if (startKm.getText() == null || startKm.getText().toString().length() == 0) {
                            Snackbar.make(view, "Start Km is Required Field", Snackbar.LENGTH_LONG).show();
                        } else if (otp.getText() == null || otp.getText().toString().length() == 0) {
                            Snackbar.make(view, "OTP is Required Field", Snackbar.LENGTH_LONG).show();
                        }
                    }
                }
                else {
                    Snackbar.make(view, "Internet Connection Unavailable", Snackbar.LENGTH_LONG).show();
                }
                break;
            case R.id.try_signature:
                if (NetworkStatus.isInternetConnected(getActivity().getApplication())) {
                    if (startKm.getText() != null && startKm.getText().toString().length() > 0) {
                        currentBookingViewModel.setStartData(startKm.getText().toString());
                        currentBookingViewModel.setCaptureSignature(true);
                        dismiss();
                    } else {
                        if (startKm.getText() == null || startKm.getText().toString().length() == 0) {
                            Snackbar.make(view, "Start Km is Required Field", Snackbar.LENGTH_LONG).show();
                        }
                    }
                }
                else {
                    Snackbar.make(view, "Internet Connection Unavailable", Snackbar.LENGTH_LONG).show();
                }
                break;
        }

    }

    public interface StartRideListener{
        void onStartRideClicked(String startkm);
    }
}
