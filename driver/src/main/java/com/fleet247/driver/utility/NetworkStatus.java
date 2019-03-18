package com.fleet247.driver.utility;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by sandeep on 13/9/17.
 */

public class NetworkStatus {
    public static  boolean isInternetConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo[] = cm.getAllNetworkInfo();

        int i;
        for (i = 0; i < networkInfo.length; ++i) {
            if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                return true;
            }
        }
        return false;

    }
}
