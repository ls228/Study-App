package de.hdmstuttgart.meinprojekt.ui.home;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Locale;

import de.hdmstuttgart.meinprojekt.R;

public class StudyTimer {

    public CountDownTimer mCountDownTimer;

    public static boolean mTimerRunning;

    public static long mTimeLeftInMillis;
    public static long mEndTime;


    public NumberPicker hourPicker;
    public NumberPicker minutePicker;

    private long hoursSet;
    private long minutesSet;
    public static int timeSet;

    public TextView mCountDownText;

    public ProgressBar mProgressBar;

    private final HomeFragment homeFragment;
    View view;

    public StudyTimer(HomeFragment homeFragment, View view) {
        this.homeFragment = homeFragment;
        this.view = view;

        hourPicker = view.findViewById(R.id.number_picker_h);
        minutePicker = view.findViewById(R.id.number_picker_min);

        mProgressBar = view.findViewById(R.id.progress_bar);

        mCountDownText = view.findViewById(R.id.text_view_countdown);

        hourPicker.setMinValue(0);
        hourPicker.setMaxValue(12);
        hourPicker.setValue(0);

        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(60);
        minutePicker.setValue(0);

        hourPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                hoursSet = (long) newVal * 3600000;
                calculateTotalTime();
            }
            private void calculateTotalTime() {
                mTimeLeftInMillis = hoursSet + minutesSet;
                timeSet = (int) mTimeLeftInMillis;
                mProgressBar.setMax(timeSet);
            }
        });

        minutePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                minutesSet = (long) newVal * 60000;
                calculateTotalTime();
            }
            private void calculateTotalTime () {
                mTimeLeftInMillis = hoursSet + minutesSet;
                timeSet = (int) mTimeLeftInMillis;
                mProgressBar.setMax(timeSet);
            }
        });

    }

    public void saveTimerProgressBar(){
        int progress = timeSet - (int) (mTimeLeftInMillis);
        mProgressBar.setMax(timeSet);
        mProgressBar.setProgress(progress);
    }

    void updateCountDownText() {
        int hours = (int) (mTimeLeftInMillis / 1000) / 3600;
        int minutes = (int) ((mTimeLeftInMillis / 1000) % 3600) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;



        String timeLeftFormatted;
        if (hours > 0) {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%d:%02d:%02d", hours, minutes, seconds);
        } else {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%02d:%02d", minutes, seconds);
        }


        if(hours == 0 && minutes == 0 && seconds == 0) {
            mCountDownText.setText(R.string.done);
        } else {
            mCountDownText.setText(timeLeftFormatted);
        }
    }


    public void startTimer() {
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;

        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                int progress = timeSet - (int) (millisUntilFinished);
                mProgressBar.setProgress(progress);
                updateCountDownText();
            }


            @Override
            public void onFinish() {
                mTimerRunning = false;
                homeFragment.updateWatchInterface();
                resetTimer();
                //mProgressBar.setMax(0);
            }
        }.start();

        mTimerRunning = true;
        homeFragment.updateWatchInterface();
    }

    public void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        homeFragment.updateWatchInterfacePause();
    }

    public void resetTimer() {
        mTimeLeftInMillis = 0;
        mTimerRunning = false;
        hourPicker.setValue(0);
        minutePicker.setValue(0);
        //updateCountDownText();
        homeFragment.updateWatchInterface();
    }

}
