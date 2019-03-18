package com.fleet247.driver.viewmodels;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;

import com.fleet247.driver.data.LiveLocation;
import com.fleet247.driver.data.models.endride.InvoiceDetails;
import com.google.android.gms.maps.model.LatLng;
import com.fleet247.driver.data.models.CustomLatLng;
import com.fleet247.driver.data.models.upcomingbooking.Booking;
import com.fleet247.driver.data.repository.CurrentBookingRepository;

/**
 * Created by sandeep on 14/11/17.
 */

public class CurrentBookingViewModel extends AndroidViewModel {

    CurrentBookingRepository currentBookingRepository;
    LiveData<Booking> currentBooking;
    LiveData<String> tripStatus;
    LiveData<String> status;
    LiveData<String> error;
    LiveData<Boolean> hasReachedPickupPoint;
    LiveLocation liveLocation;
    LiveData<String> eta;
    LiveData<Long> totalOffTime;
    MutableLiveData<Boolean> captureSignature;
    MutableLiveData<Boolean> notAtPickupLocation;

    MutableLiveData<Boolean> trigerStartRide;
    MutableLiveData<Boolean> triggerEndRide;

    MutableLiveData<Boolean> isEnableLocationClicked;

    String startKm;

    String endKm;
    String stateTax;
    String parking;
    String tollTax;
    String otherTax;
    String garageDistance;
    String estimateGarageDistance;
    String estimateTimeToReachGarage;
    String calculatedTripDistance;

    public CurrentBookingViewModel(@NonNull Application application) {
        super(application);
        currentBookingRepository=CurrentBookingRepository.getInstance(application);
        currentBooking=currentBookingRepository.getBooking();
        tripStatus=currentBookingRepository.getTripStatus();
        status=currentBookingRepository.getStatus();
        error=currentBookingRepository.getError();
        hasReachedPickupPoint=currentBookingRepository.getHasEnteredAtPickupPoint();
        liveLocation=currentBookingRepository.getLiveLocation();
        totalOffTime=currentBookingRepository.getAlllocationOffTime();
        eta=currentBookingRepository.getEta();
        trigerStartRide=new MutableLiveData<>();
        triggerEndRide=new MutableLiveData<>();
        captureSignature=new MutableLiveData<>();
        isEnableLocationClicked=new MutableLiveData<>();
        notAtPickupLocation=new MutableLiveData<>();
        trigerStartRide.setValue(false);
        triggerEndRide.setValue(false);
        captureSignature.setValue(false);
        isEnableLocationClicked.setValue(false);
    }


    public LiveData<Boolean> getHasReachedPickupPoint() {
        return hasReachedPickupPoint;
    }

    public LiveData<Booking> getCurrentBooking() {
        return currentBooking;
    }

    public LiveData<String> getTripStatus() {
        return tripStatus;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void setTripStatus(String tripStatus){
        currentBookingRepository.setTripStatus(tripStatus);
    }

    public double getStartKm(){
        return currentBookingRepository.getStartKm();
    }

    public LiveData<String> getStatus() {
        return status;
    }

    public boolean getIsCurrentBooking(){
        return currentBookingRepository.getIsCurrentBooking().getValue();
    }

    public LiveLocation getLiveLocation() {
        return liveLocation;
    }

    public LiveData<String> getEta() {
        return eta;
    }

    public void reSetStatus(){
        currentBookingRepository.reSetStatus();
    }

    public void setCurrentBooking(String currentBooking) {
        currentBookingRepository.setIsCurrentBooking(true);
        currentBookingRepository.setBooking(currentBooking);
    }

    public void initiateDriverStarted(String accessToken, String bookingId, android.location.Location garageLocation, String startKm,String bookingType){
        currentBookingRepository.initiateDriver(accessToken, bookingId, garageLocation,startKm,bookingType);
    }

    public void initiateDriverArrived(String accessToken,String bookingId,String garageDistance,String measuredGarageDistance){
        currentBookingRepository.initiatedArrived(accessToken, bookingId, garageDistance,measuredGarageDistance);
    }

    public void initiateRideStarted(String accessToken, String bookingId, android.location.Location startLocation, String startKm){
        currentBookingRepository.initiateStartRide(accessToken, bookingId, startLocation,startKm);
    }

    public void initiateEndRide(String accessToken,
                                String bookingId,
                                android.location.Location dropLocation,
                                String dropKmReading,
                                String calDistanceStartGaragetoDropGarage,
                                String calDistanceStartGarageToDropSelf,
                                String calDistancePickupToDropGoogle,
                                String estDistanceDropToEndGarage,
                                String estTimeDropToEndGarage,
                                String stateTax,
                                String parking,
                                String tollTax,
                                String extras){
        currentBookingRepository.initiateEndRide(accessToken, bookingId, dropLocation,
                dropKmReading,calDistanceStartGaragetoDropGarage,calDistanceStartGarageToDropSelf,
                calDistancePickupToDropGoogle,estDistanceDropToEndGarage,estTimeDropToEndGarage,stateTax,parking,
                tollTax,extras);
    }

    public void deleteCurrentBookingData(){
        currentBookingRepository.deleteData();
    }

    public LatLng getGarageLocation(){
       return currentBookingRepository.getGarageLocation();
    }

    public void setGarageLocation(LatLng garageLocation){
        currentBookingRepository.setGarageLocation(garageLocation);
    }

    public boolean isStartOtpVerified(String startOtp){
        if (startOtp.equals(currentBookingRepository.getStartOtp())){
            return true;
        }else {
            return false;
        }
    }

    public boolean isEndOtpVerified(String endOtp){
        if (endOtp.equals(currentBookingRepository.getEndOtp())){
            return true;
        }else {
            return false;
        }
    }

    public LatLng getStartingLoction(){
        return currentBookingRepository.getStartingLocation();
    }

    public void setStartingLocation(LatLng startingLocation){
        currentBookingRepository.setStartingLocation(startingLocation);
    }

    public void setIsPaused(boolean isPaused){
        currentBookingRepository.setPaused(isPaused);
    }

    public boolean getIsPaused(){
        return currentBookingRepository.getPaused();
    }

    public String getPolyLine(){
        return currentBookingRepository.getPolyLine();
    }

    public LiveData<CustomLatLng> getPickupLatLng(Booking booking){
        return currentBookingRepository.getPickupLatLng(booking);
    }

    public LiveData<CustomLatLng> getDropLatLng(Booking booking){
        return currentBookingRepository.getDropLatLng(booking);
    }

    public InvoiceDetails getInvoiceDetails(){
        return currentBookingRepository.getInvoiceDetails();
    }

    public double getDistanceTravelled(){
        return currentBookingRepository.getTotalDistance();
    }

    public void setStartData(String startKm){
        this.startKm=startKm;
        setTrigerStartRide(true);
    }

    public void setEndData(String endKm,String stateTax,String parking,String tollTax,String otherTax,
                           String garageDistance,String estimateGarageDistance,
                           String estimateTimeToReachGarage,String calculatedTripDistance){
        this.endKm=endKm;
        this.startKm=stateTax;
        this.stateTax=stateTax;
        this.parking=parking;
        this.tollTax=tollTax;
        this.otherTax=otherTax;
        this.garageDistance=garageDistance;
        this.estimateGarageDistance=estimateGarageDistance;
        this.estimateTimeToReachGarage=estimateTimeToReachGarage;
        this.calculatedTripDistance=calculatedTripDistance;
        setTriggerEndRide(true);
    }

    public String getEndKm() {
        return endKm;
    }

    public String getStateTax() {
        return stateTax;
    }

    public String getParking() {
        return parking;
    }

    public String getTollTax() {
        return tollTax;
    }

    public String getOtherTax() {
        return otherTax;
    }

    public String getGarageDistance() {
        return garageDistance;
    }

    public String getEstimateGarageDistance() {
        return estimateGarageDistance;
    }

    public String getEstimateTimeToReachGarage() {
        return estimateTimeToReachGarage;
    }

    public String getCalculatedTripDistance() {
        return calculatedTripDistance;
    }

    public MutableLiveData<Boolean> getTrigerStartRide() {
        return trigerStartRide;
    }

    public void setTrigerStartRide(Boolean status) {
        this.trigerStartRide.setValue(status);
    }

    public MutableLiveData<Boolean> getTriggerEndRide() {
        return triggerEndRide;
    }

    public void setTriggerEndRide(Boolean status) {
        this.triggerEndRide.setValue(status);
    }

    public String getStartKms(){
        return startKm;
    }

    public MutableLiveData<Boolean> getCaptureSignature() {
        return captureSignature;
    }

    public void setCaptureSignature(Boolean status) {
        this.captureSignature.setValue(status);
    }

    public void saveSignature(String image,String type){
        currentBookingRepository.saveSignature(image,type);
    }

    public LiveData<Long> getTotalLocationOffTime(){

        return totalOffTime;
    }

    public void deleteLocationoffTime(){
        currentBookingRepository.deleteLocationOffData();
    }

    public void saveStartSignatoryName(String name){
        currentBookingRepository.saveStartSignatoryName(name);
    }

    public void saveEndSignatoryName(String name){
        currentBookingRepository.saveEndSignatoryName(name);
    }

    public String getStartSignatoryName(){
        return currentBookingRepository.getStartSignatoryName();
    }

    public String getEndSignatoryName(){
        return currentBookingRepository.getEndSignatoryName();
    }

    public void setBillingBookingDetails(Booking booking,InvoiceDetails invoiceDetails){
        currentBookingRepository.setBillingBookingDetails(booking,invoiceDetails);
    }

    public MutableLiveData<Boolean> getLocationStatus(){
        return currentBookingRepository.getLocationStatus();
    }

    public LiveData<Boolean> getEnableLocationClicked(){
        return isEnableLocationClicked;
    }

    public void setEnableLocationClicked(boolean isClicked){
        isEnableLocationClicked.setValue(isClicked);
    }

    public LiveData<Boolean> getNotAtPickupLocation() {
        return notAtPickupLocation;
    }

    public void setNotAtPickupLocation(boolean status) {
        notAtPickupLocation.setValue(status);
    }
}
