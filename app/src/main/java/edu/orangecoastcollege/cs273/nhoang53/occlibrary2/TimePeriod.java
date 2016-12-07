package edu.orangecoastcollege.cs273.nhoang53.occlibrary2;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Long Truong on 11/29/2016.
 */

public class TimePeriod implements Parcelable{
    private String startTime;
    private String endTime;
    private boolean isBooked;

    public TimePeriod(String startTime, String endTime, boolean isBooked) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.isBooked = isBooked;
    }

    protected TimePeriod(Parcel in) {
        startTime = in.readString();
        endTime = in.readString();
        isBooked = in.readByte() != 0;
    }

    public static final Creator<TimePeriod> CREATOR = new Creator<TimePeriod>() {
        @Override
        public TimePeriod createFromParcel(Parcel in) {
            return new TimePeriod(in);
        }

        @Override
        public TimePeriod[] newArray(int size) {
            return new TimePeriod[size];
        }
    };

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(startTime);
        parcel.writeString(endTime);
        parcel.writeByte((byte) (isBooked ? 1 : 0));
    }
}
