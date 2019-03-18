package com.fleet247.driver.fragment;


import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fleet247.driver.R;
import com.fleet247.driver.viewmodels.DriverInfoViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class OtpBottomSheet extends BottomSheetDialogFragment implements View.OnClickListener {

    DriverInfoViewModel driverInfoViewModel;
    View view;
    EditText otpView;
    ProgressBar progressBar;
    Button submitOtpButton;
    TextView fetchingOtp;

    public OtpBottomSheet() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        driverInfoViewModel=ViewModelProviders.of(getActivity()).get(DriverInfoViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_otp_bottom_sheet, container, false);
        otpView=view.findViewById(R.id.otp);
        fetchingOtp=view.findViewById(R.id.fetching_otp);
        progressBar=view.findViewById(R.id.otp_progressbar);
        submitOtpButton=view.findViewById(R.id.submit_otp_button);

        submitOtpButton.setOnClickListener(this);

        driverInfoViewModel.getOtp().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if (s!=null) {

                    progressBar.setVisibility(View.INVISIBLE);
                    otpView.setText(s);
                    fetchingOtp.setText("OTP Received");
                }
            }
        });

        return view;
    }


    @Override
    public void onClick(View view) {
        Log.d("Button","Clicked");
        if(otpView.getText()==null || otpView.getText().length()<6){
            Log.d("Button","OClicked");
            otpView.setError("Please Enter 6 Digit Otp");
        }
        else {
            driverInfoViewModel.setOtp(otpView.getText().toString());
            dismiss();
        }

    }
}
