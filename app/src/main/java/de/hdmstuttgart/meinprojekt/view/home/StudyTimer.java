package de.hdmstuttgart.meinprojekt.view.home;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Locale;

import de.hdmstuttgart.meinprojekt.R;

public class StudyTimer {

    public static boolean mTimerRunning;
    public static long mTimeLeftInMillis;
    public static long mEndTime;
    public CountDownTimer mCountDownTimer;
    public NumberPicker hourPicker;
    public NumberPicker minutePicker;

    public TextView mCountDownText;

    public ProgressBar mProgressBar;

    IOnFinish iOnFinish;

    View view;

    public StudyTimer(View view, IOnFinish iOnFinish) {
        this.view = view;
        this.iOnFinish = iOnFinish;

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
    }

    public void saveTimerProgressBar(int timeSet) {
        int progress = timeSet - (int) (mTimeLeftInMillis);
        mProgressBar.setMax(timeSet);
        mProgressBar.setProgress(progress);
    }

    String updateCountDownText() {
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
        return timeLeftFormatted;

    }


    public void startTimer(int timeSet) {
        Log.d("StudyTimer","Study timer started with: " + timeSet);
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;

        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 50) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                int progress = timeSet - (int) (millisUntilFinished);
                mProgressBar.setProgress(progress);
                String timeLeft = updateCountDownText();
                mCountDownText.setText(timeLeft);
            }


            @Override
            public void onFinish() {
                mTimerRunning = false;
                resetTimer();
                iOnFinish.onFinish();
            }
        }.start();

        mTimerRunning = true;
    }

    public void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
    }

    public void resetTimer() {
        mTimeLeftInMillis = 0;
        mTimerRunning = false;
        hourPicker.setValue(0);
        minutePicker.setValue(0);
        mProgressBar.setProgress(0);
    }

    public void stopTimer() {
        Log.d("StudyTimer", "Stop Timer");
        resetTimer();
        mTimeLeftInMillis = 0;
        mTimerRunning = false;
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }


}
