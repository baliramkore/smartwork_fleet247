package com.fleet247.driver.retrofit;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RecordPolyLineDataAPI {
    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST(ApiURLs.updatePolylineURL)
    Call<String> updatePolyLine(@Field("access_token") String accessToken,
                                @Field("booking_id") String bookingId,
                                @Field("encoded_polyline") String encodedPolyLine,
                                @Field("booking_type") String bookingType);

}
