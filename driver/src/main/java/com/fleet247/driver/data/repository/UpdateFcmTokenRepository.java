package com.fleet247.driver.data.repository;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.fleet247.driver.data.models.updatefcmmodel.UpdateFcmTokenApiResponse;
import com.fleet247.driver.retrofit.ConfigRetrofit;
import com.fleet247.driver.retrofit.UpdateFcmTokenAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sandeep on 22/2/18.
 */

public class UpdateFcmTokenRepository {

    private SharedPreferences fcmPref; //sharedPreference to save fcm token.
    private UpdateFcmTokenAPI updateFcmTokenAPI; //RetrofitAPi to save FCMToken into sever.
    private String fcmToken; //String to store fcmToken.

    public static UpdateFcmTokenRepository INSTANCE;

    private UpdateFcmTokenRepository(Application application) {
        fcmPref=application.getSharedPreferences("fcmPref", Context.MODE_PRIVATE);
        updateFcmTokenAPI= ConfigRetrofit.configRetrofit(UpdateFcmTokenAPI.class);
        fcmToken=fcmPref.getString("fcmToken","n");
    }

    /**
     * Creates a single instance of UpdateFcmTokenRepository.
     * It is used to follow singleton pattern.
     * @param application
     * @return Instance of UpdateTokenRepository.
     */
    public static UpdateFcmTokenRepository getInstance(Application application){
        if (INSTANCE==null){
            INSTANCE=new UpdateFcmTokenRepository(application);
        }
        return INSTANCE;
    }


    /**
     * Checks if new token is updated to server or not
     * @param fcmToken
     * @return boolean flag if token needs to be updated to sever or not.
     */
    public boolean getIsTokenUpdated(String fcmToken){
        if (fcmToken.equals(fcmPref.getString("fcmToken","n"))){
            return true;
        }else {
            return false;
        }
    }

    /**
     * It updates fcmToken value in server and if request is successful it saves this token as fcmToken locally in sharedPreference for further reference.
     * @param accessToken
     * @param authType
     * @param fcmToken
     * @param type
     */
    public void updateFcmToken(String accessToken, String authType, final String fcmToken, String type){
        updateFcmTokenAPI.updateFcmToken(accessToken, authType, fcmToken).enqueue(new Callback<UpdateFcmTokenApiResponse>() {
            @Override
            public void onResponse(Call<UpdateFcmTokenApiResponse> call, Response<UpdateFcmTokenApiResponse> response) {

                if (response.code()==204) {
                    fcmPref.edit().putBoolean("isTokenUpdated", true).
                            putString("fcmToken",fcmToken).apply();
                }
            }

            @Override
            public void onFailure(Call<UpdateFcmTokenApiResponse> call, Throwable t) {

            }
        });
    }



}
