package com.fleet247.driver.data.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import android.util.Log;

import com.fleet247.driver.data.LiveLocation;
import com.fleet247.driver.data.models.login.Driver;
import com.fleet247.driver.data.models.login.LoginApiResponse;
import com.fleet247.driver.data.models.verifyno.VerifyNoApiResponse;
import com.fleet247.driver.data.repository.taskhandler.AccessSharedPreferencesUtill;
import com.fleet247.driver.retrofit.ConfigRetrofit;
import com.fleet247.driver.retrofit.LoginApi;
import com.fleet247.driver.retrofit.LogoutAPI;
import com.fleet247.driver.retrofit.VerifyPhoneNoAPI;
import com.fleet247.driver.utility.GsonStringConvertor;
import com.google.firebase.auth.FirebaseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sandeep on 4/11/17.
 */

public class LoginRepository {

    public static LoginRepository INSTANCE;

    private MutableLiveData<Driver> driver;  //LiveData to store drivers information
    private boolean isLogin;                 //flag to know if driver is logged in or not
    private LoginApi loginApi;               //Retrofit API to send OTP to registered mobile no.
    private MutableLiveData<String> loginStatus;
    private SharedPreferences sharedPreferences; //SharedPreference to store driver's details.
    private MutableLiveData<String> error;       //LiveDate to store error during the time of loginapi call
    private MutableLiveData<String> verificationStatus;
    private MutableLiveData<String> accessToken;  //Token which allows access of driver in the Fleet247 system.
    private MutableLiveData<String> logoutStatus; //Status during after logout is called.
    private Application application;              //Application object for context
    private VerifyPhoneNoAPI verifyPhoneNoAPI;       //API to verify OTP

    private MutableLiveData<VerifyNoApiResponse> verifyNoResponse; //LiveData to save response of verifyNoApi
    private MutableLiveData<String> verifyNoError;  //LiveData to save verifyNoError

    private MutableLiveData<String> otp;  //LiveData to save otp from loginAPI.

    private LoginRepository(Application application){
        this.application=application;
      driver=new MutableLiveData<>();
      loginStatus=new MutableLiveData<>();
      loginApi= ConfigRetrofit.configRetrofit(LoginApi.class);
      verifyPhoneNoAPI=ConfigRetrofit.configRetrofit(VerifyPhoneNoAPI.class);
      error=new MutableLiveData<>();
      sharedPreferences=application.getSharedPreferences("driverPref", Context.MODE_PRIVATE);
      isLogin=sharedPreferences.getBoolean("isLogin",false);
      verificationStatus=new MutableLiveData<>();
      accessToken=new MutableLiveData<>();
      logoutStatus=new MutableLiveData<>();
      otp=new MutableLiveData<>();
      verifyNoResponse=new MutableLiveData<>();
      verifyNoError=new MutableLiveData<>();
      if (sharedPreferences.getString("driverInfo",null)!=null) {
          driver.setValue(GsonStringConvertor.stringToGson(sharedPreferences.getString("driverInfo", null), Driver.class));
          accessToken.setValue(sharedPreferences.getString("accessToken",null));
      }
    }

    /**
     *
     * @param application
     * @return LoginRepository
     * This static function is used to create only one INSTANCE of LoginRepository class.
     */
    public static LoginRepository getInstance(Application application){
        if (INSTANCE==null){
            INSTANCE=new LoginRepository(application);
        }
        return INSTANCE;
    }

    /**
     *
     * @param status
     * Saves if driver is currently active or not
     */
    public void setActiveStatus(boolean status){
        sharedPreferences.edit().putBoolean("isActive",status).apply();
    }

    /**
     *
     * @return boolean
     * Returns if driver is currently active or not
     */
    public boolean getActiveStatus(){
        return sharedPreferences.getBoolean("isActive",false);
    }

    /**
     *
     * @return boolean
     * Returns if driver is currently logged in or not.
     */
    public boolean getIsLogin(){
        return  sharedPreferences.getBoolean("isLogin",false);
    }

    /**
     *
     * @return
     * Returns MutableLiveData object of Driver.
     */
    public MutableLiveData<Driver> getDriver() {
        return driver;
    }

    public MutableLiveData<String> getLoginStatus() {
        return loginStatus;
    }

    /**
     *
     * @return
     * Returns error from loginAPI.
     */
    public MutableLiveData<String> getError() {
        return error;
    }

    /**
     *
     * @return
     * Returns AccessToken
     */
    public MutableLiveData<String> getAccessToken() {
        return accessToken;
    }

    /**
     *
     * @return
     * Returns response from verification API
     */
    public MutableLiveData<String> getVerificationStatus() {
        return verificationStatus;
    }

    /**
     *
     * @return logout Response LiveData object.
     */
    public LiveData<String> getLogoutStatus(){
        return logoutStatus;
    }

    /**
     * sets if driver has seen the tutorial for upcoming bookings after login.
     */
    public void setShownUpcomingTutorial(){
        AccessSharedPreferencesUtill.setShownUpcommingTutorial(application);
    }

    /**
     *
     * @return boolean flag is driver has shown tutorial for upcoming bookings or not.
     */
    public boolean getShownUpcomingTutorial(){
        return AccessSharedPreferencesUtill.getShowUpcommingTutorial(application);
    }

    public void setShownTripStartedTutorial(){
        AccessSharedPreferencesUtill.setShownStartTripTutorial(application);
    }

    public boolean getShownTripStratedTutorial(){
        return AccessSharedPreferencesUtill.getShownStartTripTutorial(application);
    }


    /**
     *
     * @param licenceNo Verified mobile no of user
     * @param password  OTP received on registered mobile no
     * perform login using credentials
     */
    public void performLogin(String licenceNo,String password){
        loginApi.performLogin(licenceNo,password).enqueue(new Callback<LoginApiResponse>() {
            @Override
            public void onResponse(Call<LoginApiResponse> call, Response<LoginApiResponse> response) {

                    switch (response.code()){
                        case 200:
                            Log.d("Data",GsonStringConvertor.gsonToString(response));
                            driver.setValue(response.body().getResponse().getDriver());
                            accessToken.setValue(response.body().getResponse().getAccessToken());
                            AccessSharedPreferencesUtill.clearNotificationPref(application);
                            AccessSharedPreferencesUtill.setIsFreshLogin(application,true);
                            loginStatus.setValue("successful");
                            sharedPreferences.edit().putBoolean("isLogin",true)
                                    .putString("accessToken",accessToken.getValue())
                                    .putString("driverInfo",GsonStringConvertor.gsonToString(driver.getValue()))
                                    .commit();
                            break;
                        case 400:
                            error.setValue("Invalid Login credentials");
                            break;
                        case 500:
                            Log.d("Data",GsonStringConvertor.gsonToString(response));
                            error.setValue("Server Error");
                    }
            }

            @Override
            public void onFailure(Call<LoginApiResponse> call, Throwable t) {
                error.setValue("Connection Error: "+t.getMessage());
            }
        });
    }


    /**
     *
     * @param contactNo contact No entered by driver.
     * this method is used to verify if contact number entered by driver is registered with fleet247.
     */
    public void verifyContactNo(String contactNo){

        verifyPhoneNoAPI.verifyPhoneNo(contactNo).enqueue(new Callback<VerifyNoApiResponse>() {
            @Override
            public void onResponse(Call<VerifyNoApiResponse> call, Response<VerifyNoApiResponse> response) {
                if(response.isSuccessful()){
                    verifyNoResponse.setValue(response.body());
                }
                else if (response.code()==400){
                    verifyNoError.setValue("Invalid Login credentials");
                }
            }

            @Override
            public void onFailure(Call<VerifyNoApiResponse> call, Throwable t) {
                verifyNoError.setValue("Connection Error "+t.getMessage());
            }
        });
    }

    /**
     *
     * @return response from verifyNoApi call.
     */
    public MutableLiveData<VerifyNoApiResponse> getVerifyNoResponse() {
        return verifyNoResponse;
    }

    public void setVerifyNoResponse(MutableLiveData<VerifyNoApiResponse> verifyNoResponse) {
        this.verifyNoResponse = verifyNoResponse;
    }

    /**
     *
     * @return  error from verifyNoAPI
     */
    public MutableLiveData<String> getVerifyNoError() {
        return verifyNoError;
    }

    public void setVerifyNoError(MutableLiveData<String> verifyNoError) {
        this.verifyNoError = verifyNoError;
    }

    /*   public void resendOTP(String phoneNo){
        loginApi.performLogin(phoneNo).enqueue(new Callback<LoginApiResponse>() {
            @Override
            public void onResponse(Call<LoginApiResponse> call, Data<LoginApiResponse> data) {
                if (data.isSuccessful()){
                    switch (data.code()){
                        case 200:
                            driver.setValue(data.body().getData().getDriver());
                            accessToken.setValue(data.body().getData().getAccessToken());
                            loginStatus.setValue("otp sent successfully");
                            break;
                        case 400:
                            error.setValue("");
                            break;
                        case 500:
                            error.setValue("");
                    }
                }
                else {
                    error.setValue("Connection Error");
                }
            }

            @Override
            public void onFailure(Call<LoginApiResponse> call, Throwable t) {
                error.setValue("Connection Error: "+t.getMessage());
            }
        });
    }


    public void verifyCode(String verificationCode){
        if (verificationCode.equals(driver.getValue().getId())){
            verificationStatus.setValue("successful");
            sharedPreferences.edit().putBoolean("isLogin",true)
                    .putString("accessToken",accessToken.getValue())
                    .putString("driverInfo",GsonStringConvertor.gsonToString(driver.getValue()))
                    .commit();
        }
        else {
            verificationStatus.setValue("unsuccessful");
        }
    }
*/

    /**
     *
     * @param accessToken
     * Calls logoutAPI and whatever(Successful or unSuccessful) the response is delete all databases and sharedPreferences to logout user
     */
 public void performLogout(String accessToken){
     LogoutAPI logoutAPI=ConfigRetrofit.configRetrofit(LogoutAPI.class);

     logoutAPI.performLogout(accessToken).enqueue(new Callback<LoginApiResponse>() {
         @Override
         public void onResponse(Call<LoginApiResponse> call, Response<LoginApiResponse> response) {
             switch (response.code()){
                 case 200:
                     break;
                 case 204:
                     AccessSharedPreferencesUtill.clearPreference(application);
                     logoutStatus.setValue("successful");
                     INSTANCE=null;
                     break;
                 case 400:
                     FirebaseAuth.getInstance().signOut();
                     AccessSharedPreferencesUtill.clearPreference(application);
                     logoutStatus.setValue("successful");
                     INSTANCE=null;
                     break;
                 case 401:
                     AccessSharedPreferencesUtill.clearPreference(application);
                     logoutStatus.setValue("successful");
                     INSTANCE=null;
                     break;
                 case 500:
                     AccessSharedPreferencesUtill.clearPreference(application);
                     logoutStatus.setValue("successful");
                     INSTANCE=null;
                     break;
             }
         }

         @Override
         public void onFailure(Call<LoginApiResponse> call, Throwable t) {
            error.setValue("Connection Error");
         }
     });
 }

    /**
     *
     * @return if driver is completing a trip.
     */
 public boolean hasCurrentBooking(){
     return AccessSharedPreferencesUtill.hasCurrentBooking(application);
 }

    /**
     * flag to know that user has visited app more then once after new login.
     */
 public void setIsFreshLogin(){
     AccessSharedPreferencesUtill.setIsFreshLogin(application,false);
 }

    /**
     *
     * @return boolean flag if its user first time in the app.
     */
 public boolean getIsFreshLogin(){
     return AccessSharedPreferencesUtill.getIsFreshLogin(application);
 }

    /**
     *
     * @return LiveData to get otp from
     */
    public LiveData<String> getOtp() {
        return otp;
    }

    /**
     *
     * @param otp
     * sets value of otp.
     */
    public void setOtp(String otp) {
        this.otp.setValue(otp);
    }
}
