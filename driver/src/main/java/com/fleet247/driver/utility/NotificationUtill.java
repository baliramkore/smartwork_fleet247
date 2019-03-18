package com.fleet247.driver.utility;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationUtill {

    public static NotificationManager mNotificationManager;
    public static String trackingChanneId ="FDT";
    public static String trackingChannelName ="OnTripDriverTracking";
    public static String trackingChanneldiscriptuon ="For tracking driver when he is completing a trip";
    static int trackingChannelImportance =NotificationManager.IMPORTANCE_DEFAULT;

    public static String activeDriverTrackingChannelId="ADT";
    public static String activeDriverTrackingChannelName="ActiveDriverTracking";
    public static String activeDriverTrackingChannelDiscription="For tracking driver when he has enabled active switch";
    static int activeChannelImportance =NotificationManager.IMPORTANCE_DEFAULT;

    public static String bookingChanneId="D2";
    private static String bookingChannelName="NewBooking";
    private static String bookingChanneldiscriptuon="New Booking Notification For Driver";
    private static int bookingChannelImportance= NotificationManager.IMPORTANCE_HIGH;

    public static void createOnRideServiceNotificationChannel(Context context){
        if(mNotificationManager==null) {
            mNotificationManager =
                    (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d("NotificationChannel","Creating");
            NotificationChannel notificationChannel=new NotificationChannel(trackingChanneId, trackingChannelName, trackingChannelImportance);
            notificationChannel.setDescription(trackingChanneldiscriptuon);
            //notificationChannel.enableLights(true);
            //notificationChannel.enableVibration(true);
            notificationChannel.setLightColor(Color.GREEN);
            mNotificationManager.createNotificationChannel(notificationChannel);
            Log.d("NotificationChannel","Created");
        }
    }

    public static void createOnActiveServiceNotificationChannel(Context context){
        if (mNotificationManager==null) {
            mNotificationManager =
                    (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d("NotificationChannel","Creating");
            NotificationChannel notificationChannel=new NotificationChannel(activeDriverTrackingChannelId ,activeDriverTrackingChannelName,activeChannelImportance);
            notificationChannel.setDescription(activeDriverTrackingChannelDiscription);
            //notificationChannel.enableLights(true);
            //notificationChannel.enableVibration(true);
            notificationChannel.setLightColor(Color.GREEN);
            mNotificationManager.createNotificationChannel(notificationChannel);
            Log.d("NotificationChannel","Created");
        }
    }

    public static void createBookingNotificationChannel(Context context){
        if (mNotificationManager==null) {
            mNotificationManager =
                    (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d("NotificationChannel","Creating");
            NotificationChannel notificationChannel=new NotificationChannel(bookingChanneId,bookingChannelName,bookingChannelImportance);
            notificationChannel.setDescription(bookingChanneldiscriptuon);
            //notificationChannel.enableLights(true);
            //notificationChannel.enableVibration(true);
            notificationChannel.setLightColor(Color.GREEN);
            mNotificationManager.createNotificationChannel(notificationChannel);
            Log.d("DriverChannel","Created");
        }
    }

}
