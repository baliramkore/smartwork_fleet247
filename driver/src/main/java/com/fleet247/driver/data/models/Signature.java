package com.fleet247.driver.data.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.annotation.NonNull;

@Entity(primaryKeys = {"bookingId","type","bookingType"})
public class Signature {
    @NonNull
    private String bookingId;
    @NonNull
    private String type;
    private long dateTimeStamp;
    @NonNull
    private String bookingType;
    private String encodedSignatureImage;

    public Signature(){
    }

    @Ignore
    public Signature(String bookingId,String type,String bookingType,long dateTimeStamp,String encodedSignatureImage){
        this.bookingId=bookingId;
        this.type=type;
        this.bookingType=bookingType;
        this.dateTimeStamp=dateTimeStamp;
        this.encodedSignatureImage=encodedSignatureImage;
    }

    @NonNull
    public String getBookingType() {
        return bookingType;
    }

    public void setBookingType(@NonNull String bookingType) {
        this.bookingType = bookingType;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getDateTimeStamp() {
        return dateTimeStamp;
    }

    public void setDateTimeStamp(long dateTimeStamp) {
        this.dateTimeStamp = dateTimeStamp;
    }

    public String getEncodedSignatureImage() {
        return encodedSignatureImage;
    }

    public void setEncodedSignatureImage(String encodedSignatureImage) {
        this.encodedSignatureImage = encodedSignatureImage;
    }
}
