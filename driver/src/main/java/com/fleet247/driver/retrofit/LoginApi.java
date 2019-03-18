package com.fleet247.driver.retrofit;


import com.fleet247.driver.data.models.login.LoginApiResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by sandeep on 4/11/17.
 */

public interface LoginApi {

    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST(ApiURLs.loginURL)
    Call<LoginApiResponse> performLogin(@Field("contact_no") String contactNo,
                                        @Field("password") String password);
}
