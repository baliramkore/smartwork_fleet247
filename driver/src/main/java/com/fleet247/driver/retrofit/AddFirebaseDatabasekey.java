package com.fleet247.driver.retrofit;


import com.fleet247.driver.data.models.registerfcm.RegisterFirebaseDatabaseResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by sandeep on 17/11/17.
 */

public interface AddFirebaseDatabasekey {
    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST(ApiURLs.addFirebaseKey)
    Call<RegisterFirebaseDatabaseResponse> addFirebaseKey(@Field("access_token") String accessToken,
                                                          @Field("booking_id") String bookingId,
                                                          @Field("firebase_key") String firebaseKey);
}
