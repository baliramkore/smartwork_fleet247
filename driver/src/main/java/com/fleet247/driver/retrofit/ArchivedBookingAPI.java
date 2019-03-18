package com.fleet247.driver.retrofit;


import com.fleet247.driver.data.models.archivedbooking.ArchivedBookingApiResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by sandeep on 6/2/18.
 */

public interface ArchivedBookingAPI {
    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST(ApiURLs.archivedBookingURL)
    Call<ArchivedBookingApiResponse> getArchivedBookings(@Field("access_token") String accessToken);
}
