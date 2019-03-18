package com.fleet247.driver.data.models.cancelledbookings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Booking {
    @SerializedName("booking_id")
    @Expose
    public Integer bookingId;
    @SerializedName("reference_no")
    @Expose
    public String referenceNo;
    @SerializedName("pickup_datetime")
    @Expose
    public String pickupDatetime;
    @SerializedName("pickup_location")
    @Expose
    public String pickupLocation;
    @SerializedName("drop_location")
    @Expose
    public String dropLocation ;
    @SerializedName("tour_type")
    @Expose
    public String tourType;
    @SerializedName("city_name")
    @Expose
    public String cityName;
    @SerializedName("package_name")
    @Expose
    public String packageName;
    @SerializedName("cancel_reason")
    @Expose
    public String cancelReason;
    @SerializedName("Passengers")
    @Expose
    public List<Passenger> passengers = null;

    @SerializedName("payment_type")
    @Expose
    public String paymentType;

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
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

    public String getDropLocation() {
        return dropLocation;
    }

    public void setDropLocation(String dropLocation) {
        this.dropLocation = dropLocation;
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

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<Passenger> passengers) {
        this.passengers = passengers;
    }
}
