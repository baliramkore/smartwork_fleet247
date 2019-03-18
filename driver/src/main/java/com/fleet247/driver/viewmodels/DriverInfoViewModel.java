package com.fleet247.driver.viewmodels;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import com.fleet247.driver.data.LiveLocation;
import com.fleet247.driver.data.models.login.Driver;
import com.fleet247.driver.data.models.verifyno.VerifyNoApiResponse;
import com.fleet247.driver.data.repository.LoginRepository;

/**
 * Created by sandeep on 4/11/17.
 */

public class DriverInfoViewModel extends AndroidViewModel {

    LiveData<String> error; //Error from Login
    LiveData<Driver> driver; //LiveData holding driver Info
    LiveData<String> loginStatus; //LiveData to store if driver is logged in or not.
    LiveData<String> accessToken;//AccessToken

    LiveData<String> verifyContactNoError; //Error from verifyContactApi
    LiveData<VerifyNoApiResponse> verifyNoData;// Response from verifyNoAPI
    LiveData<String> otp; //otp


    LoginRepository loginRepository;


    public DriverInfoViewModel(@NonNull Application application) {
        super(application);
        loginRepository=LoginRepository.getInstance(application);
        error=loginRepository.getError();
        loginStatus=loginRepository.getLoginStatus();
        driver=loginRepository.getDriver();
        accessToken=loginRepository.getAccessToken();
        verifyContactNoError=loginRepository.getVerifyNoError();
        verifyNoData=loginRepository.getVerifyNoResponse();
        otp=loginRepository.getOtp();

    }

    /**
     *
     * @return AccessToken
     */
    public LiveData<String> getAccessToken() {
        return accessToken;
    }

    /**
     *
     * @return boolean flag if driver is Logged in.
     */
    public boolean getIsLogin(){
        return loginRepository.getIsLogin();
    }

    /**
     *
     * @param licenceNo verified contactNo of driver
     * @param password  otp received by driver
     */
    public void performLogin(String licenceNo,String password){
        loginRepository.performLogin(licenceNo,password);
    }

    /**
     *
     * @param contactNo contactNo entered by driver.
     */
    public void verifyContactNo(String contactNo){
        loginRepository.verifyContactNo(contactNo);
    }

    /**
     *
     * @return error from verifycontactNo Api
     */
    public LiveData<String> getVerifyContactNoError() {
        return verifyContactNoError;
    }

    /**
     *
     * @return successful response from verifyNoApi
     */
    public LiveData<VerifyNoApiResponse> getVerifyNoData() {
        return verifyNoData;
    }

    /**
     *
     * @param accessToken
     * performs logout of driver from app.
     */
    public void performLogout(String accessToken){
        loginRepository.performLogout(accessToken);
    }

    public LiveData<String > getLogoutStatus(){
        return loginRepository.getLogoutStatus();
    }

  /*  public void verifyCode(String vCode){
        loginRepository.verifyCode(vCode);
    }

    public void resendOTP(String phoneNo){
        loginRepository.resendOTP(phoneNo);
    }
    */

    /**
     *
     * @return error from loginApi
     */
    public LiveData<String> getError() {
        return error;
    }

    /**
     *
     * @return driverInfo
     */
    public LiveData<Driver> getDriver() {
        return driver;
    }

    /**
     *
     * @return if driver log in is successful or not.
     */
    public LiveData<String> getLoginStatus() {
        return loginStatus;
    }

    public LiveData<String> getVerificationStatus(){
        return loginRepository.getVerificationStatus();
    }

    /**
     *
     * @param status
     * sets if driver is active for taking bookings or not.
     */
    public void setIsActive(boolean status){
        loginRepository.setActiveStatus(status);
    }

    /**
     *
     * @return if driver is cative to take bookings or not.
     */
    public boolean getIsActive(){
        return loginRepository.getActiveStatus();
    }

    /**
     *
     * @return if driver is completing a trip.
     */
    public boolean hasCurrentBooking(){
        return loginRepository.hasCurrentBooking();
    }

    /**
     * sets if driver is using app first time after login.
     */
    public void setFreshLogin(){
        loginRepository.setIsFreshLogin();
    }

    /**
     *
     * @return if driver is using app first time after login
     */
    public boolean isFreshLogin(){
        return loginRepository.getIsFreshLogin();
    }

    /**
     * sets if upcoming booking tutorial has been shown to driver or not.
     */
    public void setShownUpcomingTutorial(){
        loginRepository.setShownUpcomingTutorial();
    }

    /**
     *
     * @return if upcoming booking tutorial has been shown to driver or not.
     */
    public boolean getShownUpcomingTutorial(){
        return loginRepository.getShownUpcomingTutorial();
    }

    /**
     *
     * @return otp
     */
    public LiveData<String> getOtp() {
        return otp;
    }

    /**
     *
     * @param otp
     * set value of otp
     */
    public void setOtp(String otp){
        loginRepository.setOtp(otp);
    }
}
