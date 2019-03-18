package com.fleet247.driver.activities;

import android.Manifest;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.PowerManager;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.fleet247.driver.R;
import com.fleet247.driver.fragment.EnterLoginDetailsFragment;
import com.fleet247.driver.utility.RegisterBroadcastReceiverUtill;
import com.fleet247.driver.viewmodels.DriverInfoViewModel;
import com.google.firebase.FirebaseApp;

public class LoginActivity extends AppCompatActivity {
    FrameLayout frameLayout;

    String[] permission={Manifest.permission.RECEIVE_SMS};
    private final int PERMISSION_REQUEST_CODE=11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //LocaleChangeUtill.changeLocale(this,"hi");
        setContentView(R.layout.activity_login);

        frameLayout=(FrameLayout)findViewById(R.id.container_login_details);
        Fragment enterLoginDetailsFragment=new EnterLoginDetailsFragment();
        FirebaseApp.initializeApp(this);

        //if driver is already logged in start HomeActivity else show login screen.
        DriverInfoViewModel employeeViewModel= ViewModelProviders.of(this).get(DriverInfoViewModel.class);
        if (employeeViewModel.getIsLogin()) {
            Log.d("Info","access_token "+employeeViewModel.getAccessToken().getValue()+"  "+Boolean.toString(employeeViewModel.getIsLogin()));
            startActivity(new Intent(LoginActivity.this,HomeActivity.class));
            finish();
        }else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container_login_details,enterLoginDetailsFragment)
                    .commit();

        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(this,permission,PERMISSION_REQUEST_CODE);
        }
        else {
            //Register Receive message broadcast receiver to read message for auto filling OTP.
            try{
                RegisterBroadcastReceiverUtill.unregisterMessageReceivedReceiver(getApplication());
            }catch (Exception exception){
                Log.d("Exception","Unregistered");
            }
            RegisterBroadcastReceiverUtill.registerMessageReceivedReceiver(getApplication());
        }

        //Sample Login OTP message.
        String message="Dear Sandeep Joshi,\n" +
                "Use 228460 as an OTP to login into your Fleet247 Driver App.\n" +
                "\n" +
                "For any assistance, please call us at 9899701993.\n" +
                "\n" +
                "Sandeep Ltd\n" +
                "Thank You ";

    }



    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case PERMISSION_REQUEST_CODE:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    RegisterBroadcastReceiverUtill.registerMessageReceivedReceiver(getApplication());
                }
                else {
                    Toast.makeText(this,"Auto Detect OTP Disabled",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
                == PackageManager.PERMISSION_GRANTED) {
            try{
                RegisterBroadcastReceiverUtill.unregisterMessageReceivedReceiver(getApplication());
            }catch (Exception exception){
                Log.d("Exception","Unregistered");
            }
        }
    }
}
