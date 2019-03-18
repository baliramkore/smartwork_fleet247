package com.fleet247.driver.retrofit;

import com.fleet247.driver.data.models.upcomingbooking.UpcomingBookingApiResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by sandeep on 13/11/17.
 */

public interface UpcomingBookingAPI {
    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST(ApiURLs.upcomingBookingURL)
    Call<UpcomingBookingApiResponse> getUpcomingBooking(@Field("access_token")String accessToken);
}
