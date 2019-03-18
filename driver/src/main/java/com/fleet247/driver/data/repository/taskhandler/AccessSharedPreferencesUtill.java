package com.fleet247.driver.data.repository.taskhandler;

import android.content.Context;
import android.content.SharedPreferences;

import com.fleet247.driver.data.models.endride.InvoiceDetails;
import com.fleet247.driver.data.models.login.Driver;
import com.fleet247.driver.data.models.upcomingbooking.Booking;
import com.fleet247.driver.utility.GsonStringConvertor;

import static android.content.Context.MODE_PRIVATE;

public class AccessSharedPreferencesUtill {

    static SharedPreferences currentPref;
    static SharedPreferences driverPref;
    static SharedPreferences jobPref;
    static SharedPreferences fcmTokenPref;
    static SharedPreferences trackPref;
    static SharedPreferences activeInactivePref;
    static SharedPreferences invoicePref;

    public static boolean hasCurrentBooking(Context context){
        if (currentPref==null){
            currentPref=context.getSharedPreferences("currentBookingPref", MODE_PRIVATE);
        }
        return currentPref.getBoolean("isActive",false);
    }

    public static void setShownUpcommingTutorial(Context context){
        createDriverPred(context);

        driverPref.edit().putBoolean("shownUpcomingTutorial",true).apply();
    }

    public static boolean getShowUpcommingTutorial(Context context){
        createDriverPred(context);
        return driverPref.getBoolean("shownUpcomingTutorial",false);
    }

    public static void setShownStartTripTutorial(Context context){
        createDriverPred(context);
        driverPref.edit().putBoolean("shownStartTripTutorial",true).apply();
    }

    public static boolean getShownStartTripTutorial(Context context){
        createDriverPred(context);
        return  driverPref.getBoolean("shownStartTripTutorial",false);
    }

    private static void createJobPref(Context context){
        if (jobPref==null){
            jobPref=context.getSharedPreferences("jobPref",Context.MODE_PRIVATE);
        }
    }

    private static void createTrackPref(Context context){
        if(trackPref==null){
            trackPref=context.getSharedPreferences("trackingPref",MODE_PRIVATE);
        }
    }

    private static void createNotificationPref(Context context){
        if(fcmTokenPref==null){
            fcmTokenPref=context.getSharedPreferences("fcmPref", Context.MODE_PRIVATE);
        }
    }

    private static void createDriverPred(Context context){
        if (driverPref==null){
            driverPref=context.getSharedPreferences("driverPref", Context.MODE_PRIVATE);
        }
    }

    public static boolean isActive(Context context){
        createDriverPred(context);
        return driverPref.getBoolean("isActive",false);
    }

    public static void saveImage(Context context,String image,String type){
        createJobPref(context);
        if (type.equals("start")) {
            jobPref.edit().putString("signature_start", image).apply();
        }else {
            jobPref.edit().putString("signature_end", image).apply();
        }
    }

    public static String getStartingSignature(Context context){
        createJobPref(context);
        return jobPref.getString("signature_start",null);
    }

    public static String getEndingSignature(Context context){
        createJobPref(context);
        return jobPref.getString("signature_end",null);
    }

    public static void clearData(Context context){
        createJobPref(context);
        jobPref.edit().clear().apply();
    }

    public static void setJobBookingId(Context context,String bookingId){
        createJobPref(context);
        jobPref.edit().putString("booking_id",bookingId).apply();
    }

    public static String getJobBookingId(Context context){
        createJobPref(context);
        return jobPref.getString("booking_id",null);
    }

    public static void setStateAndCity(Context context,String state,String city){
        createTrackPref(context);
        trackPref.edit()
                .putString("state",state)
                .putString("city",city)
                .apply();
    }

    public static String getState(Context context){
        createTrackPref(context);
        return trackPref.getString("state",null);
    }

    public static String getCity(Context context){
        createTrackPref(context);
        return trackPref.getString("city",null);
    }

    public static void clearNotificationPref(Context context){
        createNotificationPref(context);
        fcmTokenPref.edit().clear().apply();

    }

    public static void clearPreference(Context context){
        if (jobPref==null){
            jobPref=context.getSharedPreferences("jobPref",Context.MODE_PRIVATE);
        }
        if (currentPref==null){
            currentPref=context.getSharedPreferences("currentBookingPref", MODE_PRIVATE);
        }
        if (driverPref==null){
            driverPref=context.getSharedPreferences("driverPref", Context.MODE_PRIVATE);
        }
        if (fcmTokenPref==null){
            fcmTokenPref=context.getSharedPreferences("fcmPref", Context.MODE_PRIVATE);
        }
        if (trackPref==null){
            trackPref=context.getSharedPreferences("trackingPref",MODE_PRIVATE);
        }

        jobPref.edit().clear().apply();
        currentPref.edit().clear().apply();
        driverPref.edit().clear().apply();
        fcmTokenPref.edit().clear().apply();
        trackPref.edit().clear().apply();
    }

    private static void createActiveInactivePref(Context context){
        if(activeInactivePref==null){
            activeInactivePref=context.getSharedPreferences("activeInactivePref",MODE_PRIVATE);
        }
    }

    public static void setActiveCityState(Context context,String currentCity,String currentState){
        createActiveInactivePref(context);
        activeInactivePref.edit().putString("currentCity",currentCity)
                .putString("currentState",currentState).apply();
    }

    public static String getActiveCity(Context context){
        createActiveInactivePref(context);
        return activeInactivePref.getString("currentCity",null);
    }

    public static String getActiveState(Context context){
        createActiveInactivePref(context);
        return activeInactivePref.getString("currentState",null);
    }

    public static void setIsFreshLogin(Context context,boolean isFreshLogin){
        createDriverPred(context);
        driverPref.edit().putBoolean("isFreshLogin",isFreshLogin).apply();
    }

    public static boolean getIsFreshLogin(Context context){
        createDriverPred(context);
        return driverPref.getBoolean("isFreshLogin",true);
    }

    public static void createInvoicePref(Context context){
        if (invoicePref==null){
            invoicePref=context.getSharedPreferences("invoice_pref",MODE_PRIVATE);
        }
    }


    public static void setBillingData(Context context,Booking booking,InvoiceDetails invoiceDetails){
        createInvoicePref(context);
        invoicePref.edit().putString("booking_details",GsonStringConvertor.gsonToString(booking))
                .putString("invoice_details",GsonStringConvertor.gsonToString(invoiceDetails))
                .putBoolean("is_billing_pending",true)
                .commit();
    }

    public static boolean isBillingPending(Context context){
        createInvoicePref(context);
        return invoicePref.getBoolean("is_billing_pending",false);
    }

    public static InvoiceDetails getInvoiceDetails(Context context){
        createInvoicePref(context);
        InvoiceDetails invoiceDetails= GsonStringConvertor.stringToGson(invoicePref.getString("invoice_details",null),InvoiceDetails.class);
        return invoiceDetails;
    }

    public static Booking getBillingBookingDetails(Context context){
        createInvoicePref(context);
        Booking bookingeDetails= GsonStringConvertor.stringToGson(invoicePref.getString("booking_details",null),Booking.class);
        return bookingeDetails;
    }

    public static void clearBillingData(Context context){
        createInvoicePref(context);
        invoicePref.edit().clear().apply();
    }

    public static String getAccessToken(Context context){
        createDriverPred(context);
        return driverPref.getString("accessToken",null);
    }


    public static String getOperatorContact(Context context){
        createDriverPred(context);
        Driver driver=GsonStringConvertor.stringToGson(driverPref.getString("driverInfo",null),Driver.class);
        return driver.getDriverContact();
    }
}
