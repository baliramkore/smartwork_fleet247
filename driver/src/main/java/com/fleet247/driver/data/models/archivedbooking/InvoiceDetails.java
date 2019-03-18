package com.fleet247.driver.data.models.archivedbooking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sandeep on 17/11/17.
 */

public class InvoiceDetails {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("booking_id")
    @Expose
    public String bookingId;
    @SerializedName("operator_client_rate_id")
    @Expose
    public String operatorClientRateId;
    @SerializedName("pickup_date")
    @Expose
    public String pickupDate;
    @SerializedName("pickup_time")
    @Expose
    public String pickupTime;
    @SerializedName("drop_date")
    @Expose
    public String dropDate;
    @SerializedName("drop_time")
    @Expose
    public String dropTime;
    @SerializedName("hrs_done")
    @Expose
    public Double hrsDone;
    @SerializedName("allowed_hrs")
    @Expose
    public Double allowedHrs;
    @SerializedName("extra_hrs")
    @Expose
    public Double extraHrs;
    @SerializedName("hr_rate")
    @Expose
    public Double hrRate;
    @SerializedName("extra_hrs_charge")
    @Expose
    public Double extraHrsCharge;
    @SerializedName("start_km")
    @Expose
    public Double startKm;
    @SerializedName("end_km")
    @Expose
    public Double endKm;
    @SerializedName("kms_done")
    @Expose
    public Double kmsDone;
    @SerializedName("allowed_kms_per_day")
    @Expose
    public Double allowedKmsPerDay;
    @SerializedName("allowed_kms")
    @Expose
    public Double allowedKms;
    @SerializedName("extra_kms")
    @Expose
    public Double extraKms;
    @SerializedName("km_rate")
    @Expose
    public Double kmRate;
    @SerializedName("extra_kms_charge")
    @Expose
    public Double extraKmsCharge;
    @SerializedName("driver_allowance_per_day")
    @Expose
    public Double driverAllowancePerDay;
    @SerializedName("driver_allowance")
    @Expose
    public Double driverAllowance;
    @SerializedName("state_tax")
    @Expose
    public Double stateTax;
    @SerializedName("parking")
    @Expose
    public Double parking;
    @SerializedName("toll_tax")
    @Expose
    public Double tollTax;
    @SerializedName("extras")
    @Expose
    public Double extras;
    @SerializedName("base_rate_per_day")
    @Expose
    public Double baseRatePerDay;
    @SerializedName("base_rate")
    @Expose
    public Double baseRate;
    @SerializedName("total_ex_tax")
    @Expose
    public Double totalExTax;
    @SerializedName("tax_rate")
    @Expose
    public Double taxRate;
    @SerializedName("igst")
    @Expose
    public Double igst;
    @SerializedName("cgst")
    @Expose
    public Object cgst;
    @SerializedName("sgst")
    @Expose
    public Object sgst;
    @SerializedName("total_after_tax")
    @Expose
    public Double totalAfterTax;
    @SerializedName("adjustment")
    @Expose
    public Double adjustment;
    @SerializedName("comments")
    @Expose
    public String comments;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getOperatorClientRateId() {
        return operatorClientRateId;
    }

    public void setOperatorClientRateId(String operatorClientRateId) {
        this.operatorClientRateId = operatorClientRateId;
    }

    public String getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(String pickupDate) {
        this.pickupDate = pickupDate;
    }

    public String getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(String pickupTime) {
        this.pickupTime = pickupTime;
    }

    public String getDropDate() {
        return dropDate;
    }

    public void setDropDate(String dropDate) {
        this.dropDate = dropDate;
    }

    public String getDropTime() {
        return dropTime;
    }

    public void setDropTime(String dropTime) {
        this.dropTime = dropTime;
    }

    public Double getHrsDone() {
        return hrsDone;
    }

    public void setHrsDone(Double hrsDone) {
        this.hrsDone = hrsDone;
    }

    public Double getAllowedHrs() {
        return allowedHrs;
    }

    public void setAllowedHrs(Double allowedHrs) {
        this.allowedHrs = allowedHrs;
    }

    public Double getExtraHrs() {
        return extraHrs;
    }

    public void setExtraHrs(Double extraHrs) {
        this.extraHrs = extraHrs;
    }

    public Double getHrRate() {
        return hrRate;
    }

    public void setHrRate(Double hrRate) {
        this.hrRate = hrRate;
    }

    public Double getExtraHrsCharge() {
        return extraHrsCharge;
    }

    public void setExtraHrsCharge(Double extraHrsCharge) {
        this.extraHrsCharge = extraHrsCharge;
    }

    public Double getStartKm() {
        return startKm;
    }

    public void setStartKm(Double startKm) {
        this.startKm = startKm;
    }

    public Double getEndKm() {
        return endKm;
    }

    public void setEndKm(Double endKm) {
        this.endKm = endKm;
    }

    public Double getKmsDone() {
        return kmsDone;
    }

    public void setKmsDone(Double kmsDone) {
        this.kmsDone = kmsDone;
    }

    public Double getAllowedKmsPerDay() {
        return allowedKmsPerDay;
    }

    public void setAllowedKmsPerDay(Double allowedKmsPerDay) {
        this.allowedKmsPerDay = allowedKmsPerDay;
    }

    public Double getAllowedKms() {
        return allowedKms;
    }

    public void setAllowedKms(Double allowedKms) {
        this.allowedKms = allowedKms;
    }

    public Double getExtraKms() {
        return extraKms;
    }

    public void setExtraKms(Double extraKms) {
        this.extraKms = extraKms;
    }

    public Double getKmRate() {
        return kmRate;
    }

    public void setKmRate(Double kmRate) {
        this.kmRate = kmRate;
    }

    public Double getExtraKmsCharge() {
        return extraKmsCharge;
    }

    public void setExtraKmsCharge(Double extraKmsCharge) {
        this.extraKmsCharge = extraKmsCharge;
    }

    public Double getDriverAllowancePerDay() {
        return driverAllowancePerDay;
    }

    public void setDriverAllowancePerDay(Double driverAllowancePerDay) {
        this.driverAllowancePerDay = driverAllowancePerDay;
    }

    public Double getDriverAllowance() {
        return driverAllowance;
    }

    public void setDriverAllowance(Double driverAllowance) {
        this.driverAllowance = driverAllowance;
    }

    public Double getStateTax() {
        return stateTax;
    }

    public void setStateTax(Double stateTax) {
        this.stateTax = stateTax;
    }

    public Double getParking() {
        return parking;
    }

    public void setParking(Double parking) {
        this.parking = parking;
    }

    public Double getTollTax() {
        return tollTax;
    }

    public void setTollTax(Double tollTax) {
        this.tollTax = tollTax;
    }

    public Double getExtras() {
        return extras;
    }

    public void setExtras(Double extras) {
        this.extras = extras;
    }

    public Double getBaseRatePerDay() {
        return baseRatePerDay;
    }

    public void setBaseRatePerDay(Double baseRatePerDay) {
        this.baseRatePerDay = baseRatePerDay;
    }

    public Double getBaseRate() {
        return baseRate;
    }

    public void setBaseRate(Double baseRate) {
        this.baseRate = baseRate;
    }

    public Double getTotalExTax() {
        return totalExTax;
    }

    public void setTotalExTax(Double totalExTax) {
        this.totalExTax = totalExTax;
    }

    public Double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(Double taxRate) {
        this.taxRate = taxRate;
    }

    public Double getIgst() {
        return igst;
    }

    public void setIgst(Double igst) {
        this.igst = igst;
    }

    public Object getCgst() {
        return cgst;
    }

    public void setCgst(Object cgst) {
        this.cgst = cgst;
    }

    public Object getSgst() {
        return sgst;
    }

    public void setSgst(Object sgst) {
        this.sgst = sgst;
    }

    public Double getTotalAfterTax() {
        return totalAfterTax;
    }

    public void setTotalAfterTax(Double totalAfterTax) {
        this.totalAfterTax = totalAfterTax;
    }

    public Double getAdjustment() {
        return adjustment;
    }

    public void setAdjustment(Double adjustment) {
        this.adjustment = adjustment;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
