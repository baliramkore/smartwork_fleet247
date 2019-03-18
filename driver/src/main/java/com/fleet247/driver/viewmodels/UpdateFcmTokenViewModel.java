package com.fleet247.driver.viewmodels;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.annotation.NonNull;

import com.fleet247.driver.data.repository.UpdateFcmTokenRepository;

/**
 * Created by sandeep on 22/2/18.
 */

public class UpdateFcmTokenViewModel extends AndroidViewModel {

    UpdateFcmTokenRepository updateFcmTokenRepository; //Instance of UpdateFcmTokenRepository

    public UpdateFcmTokenViewModel(@NonNull Application application) {
        super(application);
        updateFcmTokenRepository=UpdateFcmTokenRepository.getInstance(application);
    }

    /**
     * Calls function to update FcmToken in server.
     * @param accessToken
     * @param authType
     * @param fcmToken
     */
    public void updateFcmToken(String accessToken,String authType,String fcmToken){
        updateFcmTokenRepository.updateFcmToken(accessToken,authType,fcmToken,"foreground");
    }

    /**
     * Checks if new fcmToken is equal to the token in server or not.
     * @param fcmToken
     * @return boolean flag if fcmToken needs to be updated in sever or not.
     */
    public boolean isTokenUpdated(String fcmToken){
        return updateFcmTokenRepository.getIsTokenUpdated(fcmToken);
    }
}
