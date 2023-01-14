package de.hdmstuttgart.meinprojekt.model;

import android.util.Log;

import java.util.Locale;

public class CalculateTime {
    private long mTimeLeftInMillis;
    private long hoursSet;
    private long minutesSet;
    private long timeSet;


    public long getmTimeLeftInMillis() {
        return mTimeLeftInMillis;
    }

    public long getHoursSet() {
        return hoursSet;
    }

    public long getMinutesSet() {
        return minutesSet;
    }

    public long getTimeSet() {
        return timeSet;
    }

    public void calculateTime(long minutes, long hours) {
        this.minutesSet =  minutes * 60000;
        this.hoursSet = hours * 3600000;
        Log.d("CalculateTime","minutesSet: " + minutesSet);
        Log.d("CalculateTime","hoursSet: " + hoursSet);
        this.mTimeLeftInMillis = minutesSet + hoursSet;
        this.timeSet =  mTimeLeftInMillis;
        Log.d("CalculateTime","timeset" + timeSet);
    }

    public void calculateTotalTime(long hour, long minute) {
        mTimeLeftInMillis = hour + minute;
        this.timeSet = (int) mTimeLeftInMillis;
    }



}
