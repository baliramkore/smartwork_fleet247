package com.fleet247.driver.retrofit;

/**
 * Created by sandeep on 4/11/17.
 */

public class ApiURLs {
    static final String liveUrl="https://fleet247.in/api/driver/v1/";
    static final String testUrl="https://fleet247.in/api/driver/v1/";
    static final String baseURL=liveUrl;
    static final String googleApiBaseURL="https://maps.googleapis.com/maps/api/";

    static final String loginURL="login";
    static final String logoutURL="logout";
    static final String verifyPhoneNoURL="loginCheck";

    static final String upcomingBookingURL="bookings/upcoming";
    static final String driverStartedURL="driver/startedFromGarage";
    static final String driverArrived="driver/arrivedAtPickup";
    static final String rideStarted="driver/startedFromPickup";
    static final String endRideURL="driver/arrivedAtDrop";
    static final String addFirebaseKey="add_firebase_key";
    static final String archivedBookingURL="bookings/archived";
    static final String cancelBooking="driver/cancel";
    static final String cancelledBooking="bookings/cancelled";
    static final String updateFcmToken="updateFCMRegID";
    static final String updatePolylineURL="bookings/recordPolyData";
    static final String storeSignature="bookings/recordSignature";
    static final String cashCollectedURL="driver/amountCollected";

    static final String directionApiURL="directions/json?";
    static final String distanceApiURL="distancematrix/json?";

    public static final String tutorialURL="https://fleet247.in/driver-app-guide";

}
