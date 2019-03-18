package com.fleet247.driver.data.models.tracking;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class FirebaseTrackingModels {
        String accessToken;
        String senderId;
        double latitude;
        double longitud;
        String eta;
        String bearing;
        String tripStatus;
        String driverStatus;
        String driverName;
        String bookingId;
        String name;

        public FirebaseTrackingModels(String accessToken,String senderId, double latitude, double longitude,String eta,String bearing,String tripStatus,String driverStatus,String driverName,String bookingId) {
            this.accessToken=accessToken;
            this.senderId = senderId;
            this.latitude = latitude;
            this.longitud = longitude;
            this.eta=eta;
            this.bearing=bearing;
            this.tripStatus=tripStatus;
            this.driverStatus=driverStatus;
            this.driverName=driverName;
            this.bookingId=bookingId;
            this.name="Sandeep";

        }

        @Exclude
        public Map<String,Object> toMap(){
            HashMap<String,Object> hashMap=new HashMap<>();
            hashMap.put("senderId",senderId);
            hashMap.put("latitude",latitude);
            hashMap.put("longitud",longitud);
            hashMap.put("eta",eta);
            hashMap.put("bearing",bearing);
            hashMap.put("tripStatus",tripStatus);
            hashMap.put("driverStatus",driverStatus);
            hashMap.put("driverName",driverName);
            hashMap.put("bookingId",bookingId);
            hashMap.put("name",name);
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
            return longitud;
        }

        public void setLongitude(double longitude) {
            this.longitud = longitude;
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

}
