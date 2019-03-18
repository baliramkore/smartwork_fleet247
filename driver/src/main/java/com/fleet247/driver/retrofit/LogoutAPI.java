package com.fleet247.driver.retrofit;


import com.fleet247.driver.data.models.login.LoginApiResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface LogoutAPI {

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST(ApiURLs.logoutURL)
    Call<LoginApiResponse> performLogout(@Field("access_token") String accessToken);
}
