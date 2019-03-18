package com.fleet247.driver.services;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.ResultReceiver;
import androidx.annotation.Nullable;
import android.util.Log;

import com.fleet247.driver.utility.GsonStringConvertor;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by sandeep on 6/11/17.
 */

public class FetchLocationIntentService extends IntentService {
    List<Address> addressList;
    ResultReceiver resultReceiver;

    public FetchLocationIntentService() {
        super("FetchLocation");
    }

    public FetchLocationIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Geocoder geocoder=new Geocoder(this, Locale.getDefault());
        resultReceiver=intent.getParcelableExtra("receiver");

        if (geocoder.isPresent()) {
            try {
              addressList = geocoder.getFromLocationName(intent.getStringExtra("location"), 1);
              Log.d("Address",GsonStringConvertor.gsonToString(addressList)+" Address");
                if (addressList.size()>0){
                    Log.d("Location", GsonStringConvertor.gsonToString(addressList));
                    Bundle bundle=new Bundle();
                    bundle.putBoolean("isSuccessful",true);
                    bundle.putString("lat",addressList.get(0).getLatitude()+"");
                    bundle.putString("lng",addressList.get(0).getLongitude()+"");
                    resultReceiver.send(Integer.parseInt(intent.getStringExtra("resultCode")),bundle);
                }
                else {
                    Bundle bundle=new Bundle();
                    bundle.putBoolean("isSuccessful",false);
                    bundle.putString("error","Unable to find Location");
                    resultReceiver.send(Integer.parseInt(intent.getStringExtra("resultCode")),bundle);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            Bundle bundle=new Bundle();
            bundle.putBoolean("isSuccessful",false);
            bundle.putString("error","geoCoder Unavailable");
            resultReceiver.send(intent.getIntExtra("resultCode",0),bundle);
        }
    }
}
