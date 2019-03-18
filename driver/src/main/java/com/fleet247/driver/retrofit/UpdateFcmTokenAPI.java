package com.fleet247.driver.retrofit;




import com.fleet247.driver.data.models.updatefcmmodel.UpdateFcmTokenApiResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by sandeep on 23/10/17.
 */

public interface UpdateFcmTokenAPI {

    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST(ApiURLs.updateFcmToken)
    Call<UpdateFcmTokenApiResponse> updateFcmToken(@Field("access_token") String accessToken,
                                                   @Field("auth_type") String authType,
                                                   @Field("fcm_regid") String fcmToken);
}
