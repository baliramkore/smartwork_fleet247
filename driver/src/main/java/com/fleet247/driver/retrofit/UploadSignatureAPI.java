package com.fleet247.driver.retrofit;

import com.fleet247.driver.data.models.storesignature.StoreSignatureApiResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface UploadSignatureAPI {
    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST(ApiURLs.storeSignature)
    Call<StoreSignatureApiResponse> storeSignature(@Field("access_token") String accessToken,
                                                   @Field("booking_id") String bookingId,
                                                   @Field("type") String type,
                                                   @Field("base64image") String base64Image,
                                                   @Field("booking_type") String bookingType);
}
