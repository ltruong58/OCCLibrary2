package edu.orangecoastcollege.cs273.nhoang53.occlibrary2;

/**
 * Created by Long Truong on 11/27/2016.
 */

public class RoomBooking {

    private int mId;
    private int mRoomId;
    private int mStudentId;
    private String mDate; // mmddyyyy
    private String mStartTime;
    private int mHoursUsed;

    public RoomBooking(int mId, int mRoomId, int mStudentId, String mDate, String mStartTime, int mHoursUsed) {
        this.mId = mId;
        this.mRoomId = mRoomId;
        this.mStudentId = mStudentId;
        this.mDate = mDate;
        this.mStartTime = mStartTime;
        this.mHoursUsed = mHoursUsed;
    }

    public RoomBooking(int mRoomId, int mStudentId, String mDate, String mStartTime, int mHoursUsed) {
        this(-1, mRoomId, mStudentId, mDate, mStartTime, mHoursUsed);
    }

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

    public int getmHoursUsed() {
        return mHoursUsed;
    }

    public void setmHoursUsed(int mHoursUsed) {
        this.mHoursUsed = mHoursUsed;
    }
}
