package com.fleet247.driver.retrofit;

import com.fleet247.driver.data.models.driverarrived.DriverArrivedApiResponse;
import com.fleet247.driver.data.models.driverstarted.DriverStartedApiResponse;
import com.fleet247.driver.data.models.endride.EndRideApiResponse;
import com.fleet247.driver.data.models.startride.StartRideApiResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by sandeep on 15/11/17.
 */

public interface DriverStartedAPI {

    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST(ApiURLs.driverStartedURL)
    Call<DriverStartedApiResponse> triggerDriverStarted(@Field("access_token") String accessToken,
                                                        @Field("booking_id") String bookingId,
                                                        @Field("start_garage_km_reading") String startKm,
                                                        @Field("start_garage_lat") String startGarageLat,
                                                        @Field("start_garage_lng") String startGarageLng,
                                                        @Field("date") String date,
                                                        @Field("time") String time,
                                                        @Field("booking_type") String bookingType);
    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST(ApiURLs.driverArrived)
    Call<DriverArrivedApiResponse> initiateDriverArrived(@Field("access_token") String accessToken,
                                                         @Field("booking_id") String bookingId,
                                                         @Field("cal_distance_start_garage_to_arrived_google") String calDistanceStartGarageToArriveGoogle,
                                                         @Field("cal_distance_start_garage_to_arrived_self") String calDistanceStartGarageToArrivedSelf,
                                                         @Field("date") String date,
                                                         @Field("time") String time,
                                                         @Field("encoded_polyline") String encodedPolyline,
                                                         @Field("booking_type") String bookingType);
    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST(ApiURLs.rideStarted)
    Call<StartRideApiResponse> initiateStartRide(@Field("access_token") String accessToken,
                                                 @Field("booking_id") String bookingId,
                                                 @Field("pickup_lat") String pickupLat,
                                                 @Field("pickup_lng") String pickupLng,
                                                 @Field("pickup_km_reading") String pickupKmReading,
                                                 @Field("date") String date,
                                                 @Field("time") String time,
                                                 @Field("encoded_polyline") String encodedPolyline,
                                                 @Field("signatory_name") String signatoryName,
                                                 @Field("booking_type") String bookingType);
    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST(ApiURLs.endRideURL)
    Call<EndRideApiResponse> initiateEndRide(@Field("access_token") String accessToken,
                                             @Field("booking_id") String bookingId,
                                             @Field("drop_lat") String dropLat,
                                             @Field("drop_lng") String dropLng,
                                             @Field("drop_km_reading") String dropKmReading,
                                             @Field("cal_distance_start_garage_to_drop_google") String calDistanceStartGarageToDropGarage,
                                             @Field("cal_distance_start_garage_to_drop_self") String calDistanceStartGarageToDropSelf,
                                             @Field("cal_distance_pickup_to_drop_google") String calDistancePickupToDropGoogle,
                                             @Field("est_distance_drop_to_end_garage") String estDistanceDropToEndGarage,
                                             @Field("est_time_drop_to_end_garage") String estTimeDropToEndGarage,
                                             @Field("state_tax") String stateTax,
                                             @Field("parking") String parking,
                                             @Field("toll_tax") String tollTax,
                                             @Field("extras") String extras,
                                             @Field("date") String date,
                                             @Field("time") String time,
                                             @Field("encoded_polyline") String encodedPolyline,
                                             @Field("signatory_name") String signatoryName,
                                             @Field("booking_type") String bookingType);
}
