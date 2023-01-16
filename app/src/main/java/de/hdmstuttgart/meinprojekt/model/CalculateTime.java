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

    public long calculateTime(long minutes, long hours) {
        minutesSet =  minutes * 60000;
        hoursSet = hours * 3600000;

        timeSet = minutesSet + hoursSet;

        return timeSet;

    }

    public void calculateTotalTime(long hour, long minute) {
        mTimeLeftInMillis = hour + minute;
        this.timeSet = (int) mTimeLeftInMillis;
    }



}
