package com.fleet247.driver.utility;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.provider.Telephony;

import com.fleet247.driver.broadcastreceiver.GpsStateChangedBroadCastReceiver;
import com.fleet247.driver.broadcastreceiver.MessageReceiver;
import com.fleet247.driver.broadcastreceiver.NetworkStateChangedBroadcastReceiver;

public class RegisterBroadcastReceiverUtill {
    static BroadcastReceiver internetConnectionChanged;
    static GpsStateChangedBroadCastReceiver locationProviderStatusChanged;
    static IntentFilter locationStatusFilter;
    static NetworkStateChangedBroadcastReceiver networkStateChangedBroadcastReceiver;
    static IntentFilter networkIntentFilter;
    static MessageReceiver messageReceiver;
    static IntentFilter messageIntentFilter;


    public static void registerGpsStateChangedProvider(Application application){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            return;
        }
        if (locationProviderStatusChanged==null){
            locationProviderStatusChanged=new GpsStateChangedBroadCastReceiver();
        }
        if (locationStatusFilter==null){
            locationStatusFilter=new IntentFilter();
            locationStatusFilter.addAction(LocationManager.PROVIDERS_CHANGED_ACTION);
        }

        application.registerReceiver(locationProviderStatusChanged,locationStatusFilter);
    }

    public static void unRegisterGpsStateChangedProvider(Application application){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            return;
        }
        application.unregisterReceiver(locationProviderStatusChanged);
    }

    public static void registerNetworkChangeProvider(Application application){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            return;
        }
        if (networkStateChangedBroadcastReceiver==null) {
            networkStateChangedBroadcastReceiver = new NetworkStateChangedBroadcastReceiver();
        }
        if (networkIntentFilter==null){
            networkIntentFilter=new IntentFilter();
            networkIntentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        }
        application.registerReceiver(networkStateChangedBroadcastReceiver,networkIntentFilter);
    }

    public static void registerMessageReceivedReceiver(Application application){
        if(messageReceiver==null){
            messageReceiver=new MessageReceiver();
        }
        if (messageIntentFilter==null){
            messageIntentFilter=new IntentFilter();
            messageIntentFilter.addAction(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);
        }

        application.registerReceiver(messageReceiver,messageIntentFilter);
    }

    public static void unregisterMessageReceivedReceiver(Application application){

        if(messageReceiver==null){
            messageReceiver=new MessageReceiver();
        }

        application.unregisterReceiver(messageReceiver);
    }

}
