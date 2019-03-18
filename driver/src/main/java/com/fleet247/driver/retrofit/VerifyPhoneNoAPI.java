package com.fleet247.driver.retrofit;

import com.fleet247.driver.data.models.verifyno.VerifyNoApiResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface VerifyPhoneNoAPI {

    @FormUrlEncoded
    @POST(ApiURLs.verifyPhoneNoURL)
    Call<VerifyNoApiResponse> verifyPhoneNo(@Field("contact_no") String contactNo);
}
