package com.fleet247.driver.fragment;


import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fleet247.driver.R;
import com.fleet247.driver.utility.NetworkStatus;
import com.fleet247.driver.viewmodels.CurrentBookingViewModel;
import com.fleet247.driver.viewmodels.GoogleMapsDirectionViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class EndJourneyDialog extends DialogFragment implements View.OnClickListener{

    View view;

    EditText endKm;
    EditText stateTax;
    EditText parking;
    EditText tollTax;
    EditText otherTax;
    EditText estimateGarageDistance;
    EditText estimateTimeToReachGarage;
    EditText otp;

    TextView trySignature;

    Button endTrip;

    String endGarageDistance;
    String endGarageTime;

    String journeyDistance;
    String journeyTime;

    OnRideEndListener onRideEndListener;

    CurrentBookingViewModel currentBookingViewModel;
    GoogleMapsDirectionViewModel googleMapsDirectionViewModel;


    public EndJourneyDialog() {
        // Required empty public constructor
    }

    public void setOnRideEndListener(OnRideEndListener onRideEndListener){
        this.onRideEndListener=onRideEndListener;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_end_journey_dialog, container, false);
        endKm=view.findViewById(R.id.end_km);
        stateTax=view.findViewById(R.id.state_tax);
        parking=view.findViewById(R.id.parking_fee);
        tollTax=view.findViewById(R.id.toll_tax);
        otherTax=view.findViewById(R.id.other_tax);
        otp=view.findViewById(R.id.end_otp);
        endTrip=view.findViewById(R.id.endTrip);
        estimateGarageDistance=view.findViewById(R.id.estimate_garage_distance);
        estimateTimeToReachGarage=view.findViewById(R.id.estimate_estimate_time_to_reach_garage);
        trySignature=view.findViewById(R.id.try_signature);


        endTrip.setOnClickListener(this);
        trySignature.setOnClickListener(this);

        endGarageDistance=getArguments().getString("endGarageDistance","n");
        endGarageTime=getArguments().getString("endGarageTime","n");

        journeyDistance=getArguments().getString("journeyDistance","n");

        //estimateGarageDistance.setText(endGarageDistance);
        //estimateTimeToReachGarage.setText(endGarageTime);

        currentBookingViewModel= ViewModelProviders.of(getActivity()).get(CurrentBookingViewModel.class);
        googleMapsDirectionViewModel=ViewModelProviders.of(this).get(GoogleMapsDirectionViewModel.class);

        return view;

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.endTrip :
                if (endKm.getText()!=null && endKm.getText().toString().length()>0
                    && stateTax.getText()!=null && stateTax.getText().toString().length()>0
                    && parking.getText()!=null && parking.getText().toString().length()>0
                    && tollTax.getText()!=null && tollTax.getText().toString().length()>0
                    && otherTax.getText()!=null && otherTax.getText().toString().length()>0
                    && otp.getText()!=null && otherTax.getText().toString().length()>0
                    && Double.parseDouble(endKm.getText().toString())>currentBookingViewModel.getStartKm()){
                if (currentBookingViewModel.isEndOtpVerified(otp.getText().toString())){
                    Log.d("EndOtp",otp.getText().toString());
                    if (NetworkStatus.isInternetConnected(getActivity().getApplication())) {
                        String estGarageDistance=estimateGarageDistance.getText().toString();
                        String estGarageTime=estimateTimeToReachGarage.getText().toString();
                        if (estGarageDistance==null || estGarageDistance.length()==0){
                            estGarageDistance="0";
                        }
                        if (estGarageTime==null || estGarageTime.length()==0){
                            estGarageTime="0";
                        }

                        currentBookingViewModel.setEndData(endKm.getText().toString(),
                                stateTax.getText().toString(),
                                parking.getText().toString(),
                                tollTax.getText().toString(),
                                otherTax.getText().toString(),
                                endGarageDistance,
                                estGarageDistance,
                                estGarageTime,
                                journeyDistance);
                        onRideEndListener.onRideEnd(endKm.getText().toString(),
                                stateTax.getText().toString(),
                                parking.getText().toString(),
                                tollTax.getText().toString(),
                                otherTax.getText().toString(),
                                endGarageDistance,
                                estGarageDistance,
                                estGarageTime,
                                journeyDistance);
                        onRideEndListener = null;
                        dismiss();
                    }else {
                        Snackbar.make(view,"Internet Connection Unavailable",Snackbar.LENGTH_LONG).show();
                    }
                }else {
                    Snackbar.make(view,"Please Enter Correct OTP",Snackbar.LENGTH_LONG).show();
                }

            }
            else {
                if (endKm.getText()==null || endKm.getText().toString().length()==0){
                    Snackbar.make(view,"End Km is Required Field",Snackbar.LENGTH_LONG).show();
                }
                if (Double.parseDouble(endKm.getText().toString())<=currentBookingViewModel.getStartKm()){
                    Snackbar.make(view,"End Km must be greater than Start Km",Snackbar.LENGTH_LONG).show();
                }
                if (stateTax.getText()==null || stateTax.getText().toString().length()==0){
                    Snackbar.make(view,"State Tax Km is Required Field",Snackbar.LENGTH_LONG).show();
                }
                if (parking.getText()==null || parking.getText().toString().length()==0){
                    //parking.setError("Required Field");
                    Snackbar.make(view,"Parking is Required Field",Snackbar.LENGTH_LONG).show();
                }
                if (tollTax.getText()==null || tollTax.getText().toString().length()==0){
                   // tollTax.setError("Required Field");
                    Snackbar.make(view,"Toll Tax is Required Field",Snackbar.LENGTH_LONG).show();
                }
                if (otherTax.getText()==null || otherTax.getText().toString().length()==0){
                    //otherTax.setError("Required Field");
                    Snackbar.make(view,"Other Tax is Required Field",Snackbar.LENGTH_LONG).show();
                }
                if (otp.getText()==null || otp.getText().toString().length()==0){
                    //otp.setError("Required Field");
                    Snackbar.make(view,"OTP is Required Field",Snackbar.LENGTH_LONG).show();
                }
            }

            break;

            case R.id.try_signature:
                if (endKm.getText()!=null && endKm.getText().toString().length()>0
                        && stateTax.getText()!=null && stateTax.getText().toString().length()>0
                        && parking.getText()!=null && parking.getText().toString().length()>0
                        && tollTax.getText()!=null && tollTax.getText().toString().length()>0
                        && otherTax.getText()!=null && otherTax.getText().toString().length()>0
                        && Double.parseDouble(endKm.getText().toString())>currentBookingViewModel.getStartKm()){
                        if (NetworkStatus.isInternetConnected(getActivity().getApplication())) {
                            String estGarageDistance=estimateGarageDistance.getText().toString();
                            String estGarageTime=estimateTimeToReachGarage.getText().toString();
                            if (estGarageDistance==null || estGarageDistance.length()==0){
                                estGarageDistance="0";
                            }
                            if (estGarageTime==null || estGarageTime.length()==0){
                                estGarageTime="0";
                            }

                            currentBookingViewModel.setEndData(endKm.getText().toString(),
                                    stateTax.getText().toString(),
                                    parking.getText().toString(),
                                    tollTax.getText().toString(),
                                    otherTax.getText().toString(),
                                    endGarageDistance,
                                    estGarageDistance,
                                    estGarageTime,
                                    journeyDistance);
                            currentBookingViewModel.setCaptureSignature(true);
                            dismiss();
                        }else {
                            Snackbar.make(view,"Internet Connection Unavailable",Snackbar.LENGTH_LONG).show();
                        }
                }
                else {
                    if (endKm.getText()==null || endKm.getText().toString().length()==0){
                        Snackbar.make(view,"End Km is Required Field",Snackbar.LENGTH_LONG).show();
                    }
                    if (Double.parseDouble(endKm.getText().toString())<=currentBookingViewModel.getStartKm()){
                        Snackbar.make(view,"End Km must be greater than Start Km",Snackbar.LENGTH_LONG).show();
                    }
                    if (stateTax.getText()==null || stateTax.getText().toString().length()==0){
                        Snackbar.make(view,"State Tax Km is Required Field",Snackbar.LENGTH_LONG).show();
                    }
                    if (parking.getText()==null || parking.getText().toString().length()==0){
                        //parking.setError("Required Field");
                        Snackbar.make(view,"Parking is Required Field",Snackbar.LENGTH_LONG).show();
                    }
                    if (tollTax.getText()==null || tollTax.getText().toString().length()==0){
                        // tollTax.setError("Required Field");
                        Snackbar.make(view,"Toll Tax is Required Field",Snackbar.LENGTH_LONG).show();
                    }
                    if (otherTax.getText()==null || otherTax.getText().toString().length()==0){
                        //otherTax.setError("Required Field");
                        Snackbar.make(view,"Other Tax is Required Field",Snackbar.LENGTH_LONG).show();
                    }
                }
        }

    }

    public interface OnRideEndListener{
        void onRideEnd(String endKm,String stateTax,String parking,String tollTax,String otherTax,
                       String garageDistance,String estimateGarageDistance,
                       String estimateTimeToReachGarage,String calculatedTripDistance);
    }


}
