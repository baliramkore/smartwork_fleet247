package com.fleet247.driver.data.models.upcomingbooking;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sandeep on 13/11/17.
 */

@Entity
public class Booking {

    @NonNull
    @PrimaryKey
    @SerializedName("booking_id")
    @Expose
    public String bookingId;
    @SerializedName("reference_no")
    @Expose
    public String referenceNo;
    @SerializedName("pickup_datetime")
    @Expose
    public String pickupDatetime;
    @SerializedName("pickup_location")
    @Expose
    public String pickupLocation;
    @SerializedName("trip_status")
    @Expose
    public String tripStatus;
    @SerializedName("tour_type")
    @Expose
    public String tourType;
    @SerializedName("city_name")
    @Expose
    public String cityName;
    @SerializedName("package_name")
    @Expose
    public String packageName;

    @SerializedName("taxi_type_assigned")
    @Expose
    public String taxiTypeAssigned;

    @SerializedName("taxi_model")
    @Expose
    public String taxiModel;

    @SerializedName("registration_number")
    @Expose
    public String registrationNumber;

    @Ignore
    @SerializedName("Passengers")
    @Expose
    public List<Passenger> passengers = null;

    @SerializedName("drop_location")
    @Expose
    public String dropLocation;

    @SerializedName("payment_type")
    @Expose
    public String paymentType;

    @SerializedName("booking_type")
    @Expose
    public String bookingType;

    public String getBookingType() {
        return bookingType;
    }

    public void setBookingType(String bookingType) {
        this.bookingType = bookingType;
    }

    public Booking(){}

    @Ignore
    public Booking(String bookingId,String referenceNo,String pickupDatetime,String pickupLocation,String tripStatus,
                   String tourType,String cityName,String packageName,List<Passenger> passengerList,String dropLocation){
        this.bookingId=bookingId;
        this.referenceNo=referenceNo;
        this.pickupDatetime=pickupDatetime;
        this.pickupLocation=pickupLocation;
        this.tripStatus=tripStatus;
        this.tourType=tourType;
        this.cityName=cityName;
        this.packageName=packageName;
        this.passengers=passengerList;
        this.dropLocation=dropLocation;
    }

    public String getDropLocation() {
        return dropLocation;
    }

    public void setDropLocation(String dropLocation) {
        this.dropLocation = dropLocation;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public String getPickupDatetime() {
        return pickupDatetime;
    }

    public void setPickupDatetime(String pickupDatetime) {
        this.pickupDatetime = pickupDatetime;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public String getTripStatus() {
        return tripStatus;
    }

    public void setTripStatus(String tripStatus) {
        this.tripStatus = tripStatus;
    }

    public String getTourType() {
        return tourType;
    }

    public void setTourType(String tourType) {
        this.tourType = tourType;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<Passenger> passengers) {
        this.passengers = passengers;
    }
    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getTaxiTypeAssigned() {
        return taxiTypeAssigned;
    }

    public void setTaxiTypeAssigned(String taxiTypeAssigned) {
        this.taxiTypeAssigned = taxiTypeAssigned;
    }

    public String getTaxiModel() {
        return taxiModel;
    }

    public void setTaxiModel(String taxiModel) {
        this.taxiModel = taxiModel;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

}
