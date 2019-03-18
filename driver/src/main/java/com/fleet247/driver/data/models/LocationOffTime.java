package com.fleet247.driver.data.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity()
public class LocationOffTime {
    @PrimaryKey(autoGenerate = true)
    int offTimeId;
    long startTime;
    long endTime;
    long duration;
    int bookingId;

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getOffTimeId() {
        return offTimeId;
    }

    public void setOffTimeId(int offTimeId) {
        this.offTimeId = offTimeId;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
