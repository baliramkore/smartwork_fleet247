package com.fleet247.driver.retrofit;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface CancelBookingAPI {
    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST(ApiURLs.cancelBooking)
    Call<String> cancelBooking(@Field("access_token") String accessToken,
                               @Field("booking_id") String bookingId,
                               @Field("cancel_reason") String cancelReason,
                               @Field("booking_type") String bookingType);
}
