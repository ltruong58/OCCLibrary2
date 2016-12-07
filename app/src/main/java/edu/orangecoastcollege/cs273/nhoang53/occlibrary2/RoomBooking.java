package edu.orangecoastcollege.cs273.nhoang53.occlibrary2;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.Time;
import android.util.Log;

import java.util.Date;

/**
 * Created by Long Truong on 11/27/2016.
 */

public class RoomBooking implements Parcelable{

    private int mId;
    private int mRoomId;
    private int mStudentId;
    private String mDate; // mmddyyyy
    private String mStartTime;
    private float mHoursUsed;

    public RoomBooking(int mId, int mRoomId, int mStudentId, String mDate, String mStartTime, float mHoursUsed) {
        this.mId = mId;
        this.mRoomId = mRoomId;
        this.mStudentId = mStudentId;
        this.mDate = mDate;
        this.mStartTime = mStartTime;
        this.mHoursUsed = mHoursUsed;
    }

    public RoomBooking(int mRoomId, int mStudentId, String mDate, String mStartTime, float mHoursUsed) {
        this.mRoomId = mRoomId;
        this.mStudentId = mStudentId;
        this.mDate = mDate;
        this.mStartTime = mStartTime;
        this.mHoursUsed = mHoursUsed;
    }

    protected RoomBooking(Parcel in) {
        mId = in.readInt();
        mRoomId = in.readInt();
        mStudentId = in.readInt();
        mDate = in.readString();
        mStartTime = in.readString();
        mHoursUsed = in.readFloat();
    }

    public static final Creator<RoomBooking> CREATOR = new Creator<RoomBooking>() {
        @Override
        public RoomBooking createFromParcel(Parcel in) {
            return new RoomBooking(in);
        }

        @Override
        public RoomBooking[] newArray(int size) {
            return new RoomBooking[size];
        }
    };

    public int getmId() {
        return mId;
    }

    public int getmRoomId() {
        return mRoomId;
    }

    public void setmRoomId(int mRoomId) {
        this.mRoomId = mRoomId;
    }

    public int getmStudentId() {
        return mStudentId;
    }

    public void setmStudentId(int mStudentId) {
        this.mStudentId = mStudentId;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public String getmStartTime() {
        return mStartTime;
    }

    public void setmStartTime(String mStartTime) {
        this.mStartTime = mStartTime;
    }

    public float getmHoursUsed() {
        return mHoursUsed;
    }

    public void setmHoursUsed(int mHoursUsed) {
        this.mHoursUsed = mHoursUsed;
    }

    public Date getTime()
    {
        Date date;
        String sDate = getmDate();
        String sTime = getmStartTime();
        int month = Integer.parseInt(sDate.substring(0,sDate.indexOf('/')));
        int day = Integer.parseInt(sDate.substring(sDate.indexOf('/')+ 1,sDate.lastIndexOf('/')));

        int year = Integer.parseInt(sDate.substring(sDate.lastIndexOf('/') + 1,sDate.length()));
        int hour = Integer.parseInt(sTime.substring(0,sTime.indexOf(':')));
        int minute = Integer.parseInt(sTime.substring(sTime.indexOf(':') + 1,sTime.lastIndexOf(':')));
        Log.i("m: ", String.valueOf(month));
        Log.i("d: ", String.valueOf(day));
        Log.i("y: ", String.valueOf(year));
        Log.i("h: ", String.valueOf(hour));
        Log.i("mi: ", String.valueOf(minute));

        date = new Date(year, month, day, hour, minute);
        return date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mId);
        parcel.writeInt(mRoomId);
        parcel.writeInt(mStudentId);
        parcel.writeString(mDate);
        parcel.writeString(mStartTime);
        parcel.writeFloat(mHoursUsed);
    }
}
