package com.fleet247.driver.data.models.archivedbooking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sandeep on 6/2/18.
 */

public class Booking {
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
    @SerializedName("drop_location")
    @Expose
    public String dropLocation;
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
    @SerializedName("Passengers")
    @Expose
    public List<Passenger> passengers = null;

    @SerializedName("payment_type")
    @Expose
    public String paymentTxype;

    @SerializedName("InvoiceDetails")
    @Expose
    public InvoiceDetails invoiceDetails;

    public String getPaymentTxype() {
        return paymentTxype;
    }

    public void setPaymentTxype(String paymentTxype) {
        this.paymentTxype = paymentTxype;
    }

    public InvoiceDetails getInvoiceDetails() {
        return invoiceDetails;
    }

    public void setInvoiceDetails(InvoiceDetails invoiceDetails) {
        this.invoiceDetails = invoiceDetails;
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

    public String getDropLocation() {
        return dropLocation;
    }

    public void setDropLocation(String dropLocation) {
        this.dropLocation = dropLocation;
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
}
