package com.fleet247.driver.fragment;


import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.LifecycleRegistryOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
public class VerificationFragment extends Fragment implements View.OnClickListener{

    LifecycleRegistry lifecycleRegistry=new LifecycleRegistry(this);

    DriverInfoViewModel driverInfoViewModel;
    EditText verificationText;
    Button verifyButton;
    ProgressBar progressBar;
    TextView resendOtp;
    EditText contactNo;
    static Boolean isTimeValid=true;

   /* static CountDownTimer countDownTimer=new CountDownTimer(10*60*1000,1000) {
        @Override
        public void onTick(long l) {
            Log.d("Tick",""+l);
        }

        @Override
        public void onFinish() {
            Log.d("time", "Ended");
            isTimeValid=false;
            Log.d("IsTimeValid", isTimeValid+"");
        }
    };
 */


    public VerificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view=inflater.inflate(R.layout.fragment_verification, container, false);
      /*  progressBar=view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        driverInfoViewModel = ViewModelProviders.of(getActivity()).get(DriverInfoViewModel.class);
        verificationText=(EditText)view.findViewById(R.id.verification_code);
        verifyButton=(Button)view.findViewById(R.id.verify_btn);
        resendOtp=view.findViewById(R.id.resend_otp);
        contactNo=view.findViewById(R.id.login_mobile_no);

        verifyButton.setOnClickListener(this);
        resendOtp.setOnClickListener(this);


        isTimeValid=true;
        countDownTimer.cancel();
        countDownTimer.start();

        contactNo.setText(getArguments().getString("contactNo","Not Available"));
        contactNo.setEnabled(false);

        driverInfoViewModel.getVerificationStatus().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                progressBar.setVisibility(View.GONE);
                if (s.equals("successful")){
                    countDownTimer.cancel();
                    progressBar.setVisibility(View.GONE);
                    startActivity(new Intent(getActivity(),HomeActivity.class));
                    getActivity().finish();
                }
                else if (s.equals("unsuccessful")){
                    Toast.makeText(getActivity(),"Please Enter Correct Verification Code",Toast.LENGTH_LONG).show();
                }
            }
        });

        driverInfoViewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                progressBar.setVisibility(View.GONE);
                if (s.equals("Verification Unsuccessful \nPlease try again")) {
                    Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
                }
                else if (s.contains("Connection Error")){
                    Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
                }
            }
        });

        driverInfoViewModel.getLoginStatus().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                progressBar.setVisibility(View.GONE);
                if (s.equals("otp sent successfully")){
                    Toast.makeText(getActivity(), "Verification Code sent successfully", Toast.LENGTH_SHORT).show();
                    isTimeValid=true;
                    countDownTimer.cancel();
                    countDownTimer.start();

                }
            }
        });
*/
        return view;

    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return lifecycleRegistry;
    }

    @Override
    public void onClick(View view) {
       /* int id=view.getId();
         switch (id){
             case R.id.verify_btn:
                 if (verificationText.getText().toString().equals("")){
                     verificationText.setError("Please Enter Verification code");

                 }
                 else if (!isTimeValid){
                     Toast.makeText(getActivity(),"Verification Code Expired",Toast.LENGTH_LONG).show();
                 }
                 else {
                     progressBar.setVisibility(View.VISIBLE);
                     driverInfoViewModel.verifyCode(verificationText.getText().toString());
                 }
                 break;
             case R.id.resend_otp:
                 progressBar.setVisibility(View.VISIBLE);
                 driverInfoViewModel.resendOTP(driverInfoViewModel.getDriver().getValue().getDriverContact());

         }
         */
    }

}
