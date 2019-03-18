package com.fleet247.driver.data.models.tracking;

import android.util.Log;

import com.fleet247.driver.utility.GsonStringConvertor;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sandeep on 15/9/17.
 */
@IgnoreExtraProperties
public class FirebaseTrackingModel {
    String accessToken;
    String senderId;
    double latitude;
    double longitude;
    String eta;
    String bearing;
    String tripStatus;
    String driverStatus;
    String driverName;
    String bookingId;
    String taxiModel;
    String taxiType;
    String taxiRegNo;
    String bookingRefNo;
    String pickupLocation;

    public FirebaseTrackingModel(){}

    public FirebaseTrackingModel(String accessToken,String senderId, double latitude, double longitude,String eta,String bearing,String tripStatus,String driverStatus,
                                 String driverName,String bookingId,String taxiModel,String taxiType,String taxiRegNo,String bookingRefNo,String pickupLocation) {
        this.accessToken=accessToken;
        this.senderId = senderId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.eta=eta;
        this.bearing=bearing;
        this.tripStatus=tripStatus;
        this.driverStatus=driverStatus;
        this.driverName=driverName;
        this.bookingId=bookingId;
        this.taxiModel=taxiModel;
        this.taxiType=taxiType;
        this.taxiRegNo=taxiRegNo;
        this.bookingRefNo=bookingRefNo;
        this.pickupLocation=pickupLocation;
    }

    @Exclude
    public Map<String,Object> toMap(){
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("accessToken",accessToken);
        hashMap.put("senderId",senderId);
        hashMap.put("latitude",latitude);
        hashMap.put("longitude",longitude);
        hashMap.put("eta",eta);
        hashMap.put("bearing",bearing);
        hashMap.put("tripStatus",tripStatus);
        hashMap.put("driverStatus",driverStatus);
        hashMap.put("driverName",driverName);
        hashMap.put("bookingId",bookingId);
        hashMap.put("taxiModel",taxiModel);
        hashMap.put("taxiType",taxiType);
        hashMap.put("taxiRegNo",taxiRegNo);
        hashMap.put("bookingRefNo",bookingRefNo);
        hashMap.put("pickupLocation",pickupLocation);
        return hashMap;
    }



    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getEta() {
        return eta;
    }

    public void setEta(String eta) {
        this.eta = eta;
    }

    public String getBearing() {
        return bearing;
    }

    public void setBearing(String bearing) {
        this.bearing = bearing;
    }

    public String getTripStatus() {
        return tripStatus;
    }

    public void setTripStatus(String tripStatus) {
        this.tripStatus = tripStatus;
    }

    public String getDriverStatus() {
        return driverStatus;
    }

    public void setDriverStatus(String driverStatus) {
        this.driverStatus = driverStatus;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getTaxiModel() {
        return taxiModel;
    }

    public void setTaxiModel(String taxiModel) {
        this.taxiModel = taxiModel;
    }

    public String getTaxiType() {
        return taxiType;
    }

    public void setTaxiType(String taxiType) {
        this.taxiType = taxiType;
    }

    public String getTaxiRegNo() {
        return taxiRegNo;
    }

    public void setTaxiRegNo(String taxiRegNo) {
        this.taxiRegNo = taxiRegNo;
    }
}


