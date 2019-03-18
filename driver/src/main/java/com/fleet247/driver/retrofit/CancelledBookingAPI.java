package com.fleet247.driver.retrofit;


import com.fleet247.driver.data.models.cancelledbookings.CancelledBookingApiResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface CancelledBookingAPI {
    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST(ApiURLs.cancelledBooking)
    Call<CancelledBookingApiResponse> getCancelledBooking(@Field("access_token") String accessToken);
}
