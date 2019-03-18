package com.fleet247.driver.fragment;

import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.LifecycleRegistryOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fleet247.driver.R;
import com.fleet247.driver.activities.HomeActivity;
import com.fleet247.driver.data.models.verifyno.VerifyNoApiResponse;
import com.fleet247.driver.viewmodels.DriverInfoViewModel;


public class EnterLoginDetailsFragment extends Fragment implements View.OnClickListener {

    EditText loginNo;
    EditText password;
    Button login;
    DriverInfoViewModel driverInfoViewModel;
    ProgressBar progressBar;
    VerifyNoApiResponse verifyNoData;
    TextView passwordText;
    TextView resendOtp;
    View toastErrorView;
    TextView toastError;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_enter_login_details, container, false);
        progressBar=view.findViewById(R.id.progressBar);
        loginNo =view.findViewById(R.id.licence_no);
        login=view.findViewById(R.id.login_btn);
        password=view.findViewById(R.id.password);
        passwordText=view.findViewById(R.id.password_text);
        resendOtp=view.findViewById(R.id.resend_otp);
        toastErrorView=inflater.inflate(R.layout.toast_layout_error,(ViewGroup)view.findViewById(R.id.toast_layout_error));
        toastError=toastErrorView.findViewById(R.id.toast_error_message);
        progressBar.setVisibility(View.GONE);
        driverInfoViewModel = ViewModelProviders.of(getActivity()).get(DriverInfoViewModel.class);

        if (verifyNoData==null){
            password.setVisibility(View.GONE);
            passwordText.setVisibility(View.GONE);
            resendOtp.setVisibility(View.GONE);
        }

        driverInfoViewModel.getLoginStatus().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if (s.equals("successful")){
                    progressBar.setVisibility(View.GONE);
                    startActivity(new Intent(getActivity(), HomeActivity.class));
                }
            }
        });

        driverInfoViewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(),s,Toast.LENGTH_SHORT).show();
            }
        });

        driverInfoViewModel.getVerifyNoData().observe(this, new Observer<VerifyNoApiResponse>() {
            @Override
            public void onChanged(@Nullable VerifyNoApiResponse verifyNoApiResponse) {
                if (verifyNoApiResponse!=null) {
                    progressBar.setVisibility(View.GONE);
                    verifyNoData = verifyNoApiResponse;
                    loginNo.setEnabled(false);
                    password.setVisibility(View.VISIBLE);
                    passwordText.setVisibility(View.VISIBLE);
                    resendOtp.setVisibility(View.VISIBLE);
                    login.setTag("login");
                    Toast toast=Toast.makeText(getActivity(),"OTP has been sent Successfully to your mobile no",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM,0,300);

                    OtpBottomSheet otpBottomSheet=new OtpBottomSheet();
                    otpBottomSheet.show(getFragmentManager(),"otp");

                    toast.show();
                }
            }
        });

        driverInfoViewModel.getVerifyContactNoError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                progressBar.setVisibility(View.GONE);
                Toast toast=new Toast(getActivity().getApplicationContext());
                toast.setGravity(Gravity.CENTER,0,0);
                toastError.setText(s);
                toast.setView(toastErrorView);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        driverInfoViewModel.getOtp().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if (s!=null) {
                    password.setText(s);
                }
            }
        });

        login.setOnClickListener(this);
        resendOtp.setOnClickListener(this);
        return view;

    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        int loginFlag=0;
        switch (id){
            case R.id.login_btn:
              /*  if (password.getText().toString().equals("")){
                    password.setError(getString(R.string.please_enter_password));
                    loginFlag++;
                }*/
                if ( login.getTag().toString().equals("verify")){
                    if (loginNo.getText()==null || loginNo.getText().toString().length()==0){
                        loginNo.setError(getString(R.string.please_enter_licence_no));

                    }
                    else if (loginNo.getText()!=null && loginNo.getText().length()<10){
                        loginNo.setError("Invalid Contact No");
                    }
                    else {
                        progressBar.setVisibility(View.VISIBLE);
                        driverInfoViewModel.verifyContactNo(loginNo.getText().toString());
                    }
                }
                else if(login.getTag().toString().equals("login")){
                    if(password.getText().toString()==null || password.getText().toString().length()<6){
                        Toast toast=new Toast(getActivity().getApplicationContext());
                        toast.setGravity(Gravity.CENTER,0,300);
                        toastError.setText("Please Enter 6 digit OTP\"");
                        toast.setView(toastErrorView);
                        toast.setDuration(Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    else if(driverInfoViewModel.getOtp().getValue()!=null&&password.getText().toString().equals(driverInfoViewModel.getOtp().getValue())) {
                        progressBar.setVisibility(View.VISIBLE);
                        driverInfoViewModel.performLogin(loginNo.getText().toString(), password.getText().toString());
                    }
                    else {
                        Toast toast=new Toast(getActivity().getApplicationContext());
                        toast.setGravity(Gravity.CENTER,0,-300);
                        toastError.setText("Please Enter Correct OTP");
                        toast.setView(toastErrorView);
                        toast.setDuration(Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
                break;
            case R.id.resend_otp:
                progressBar.setVisibility(View.VISIBLE);
                driverInfoViewModel.verifyContactNo(loginNo.getText().toString());
        }
    }
}
