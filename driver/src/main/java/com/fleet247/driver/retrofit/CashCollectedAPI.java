package com.fleet247.driver.retrofit;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface CashCollectedAPI {

    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST(ApiURLs.cashCollectedURL)
    Call<String> cashCollected(@Field("access_token") String accessToken,
                               @Field("booking_id") String bookingId,
                               @Field("amount_to_be_collected") String amountToBeCalculated,
                               @Field("amount_collected") String cashReceived,
                               @Field("booking_type") String bookingType,
                               @Field("agent_wallet_balance") String agentWalletBalance);

}
