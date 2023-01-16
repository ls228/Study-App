package de.hdmstuttgart.meinprojekt.view.home;

import static androidx.test.InstrumentationRegistry.getContext;

import static de.hdmstuttgart.meinprojekt.view.home.TimerStatus.RESET;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
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

    public TextView mCountDownText;

    public ProgressBar mProgressBar;

    private final HomeFragment homeFragment;

    //private float mValue = 0;

    //private ValueAnimator mAnimator;
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

        /*
        mAnimator = ValueAnimator.ofFloat(0, mValue*100);
        mAnimator.setDuration(500);
        mAnimator.setInterpolator( new DecelerateInterpolator());// set the duration of the animation to 10 seconds

        mAnimator.addUpdateListener(animation ->
                mValue = (float) animation.getAnimatedValue());

    public void setmValue(float mValue) {
        mAnimator.setFloatValues(mValue);
        mAnimator.start();
    }

    public void stopAnimation(){
        if(mAnimator.isRunning()){
            mAnimator.cancel();
        }
    }*/
    }

    public void saveTimerProgressBar(int timeSet) {
        int progress = timeSet - (int) (mTimeLeftInMillis);
        mProgressBar.setMax(timeSet);
        mProgressBar.setProgress(progress);
        //setmValue(progress);
        //startAnimation(timeSet);
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


        if (seconds == 0 && !mTimerRunning) {
            HomeFragment.timeUp = true;
            allDone();
        } else {
            mCountDownText.setText(timeLeftFormatted);
        }

    }

    public void allDone() {

        if (!HomeFragment.allTodosChecked) {
            mTimerRunning = false;
            mProgressBar.setProgress(0);
            resetTimer();
            homeFragment.updateWatchInterface(RESET);
        } else if (HomeFragment.timeUp && HomeFragment.allTodosChecked) {

            mTimerRunning = false;
            resetTimer();
            homeFragment.doneAnimation();
            //mProgressBar.setProgress(0);
        }
    }

    public void startTimer(int timeSet) {
        System.out.println("Study timer started with: " + timeSet);
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;

        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                int progress = timeSet - (int) (millisUntilFinished);
                mProgressBar.setProgress(progress);
                updateCountDownText();
                /*
                if(!(mAnimator.isRunning())){
                    setmValue(mValue);}*/

                //startAnimation(timeSet);
            }


            @Override
            public void onFinish() {
                mTimerRunning = false;
                allDone();
                resetTimer();
                homeFragment.updateWatchInterface(RESET);
                //stopAnimation();
            }
        }.start();

        mTimerRunning = true;
        //homeFragment.updateWatchInterface();
    }

    public void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        //homeFragment.updateWatchInterfacePause();
    }

    public void resetTimer() {
        mTimeLeftInMillis = 0;
        mTimerRunning = false;
        hourPicker.setValue(0);
        minutePicker.setValue(0);
        mProgressBar.setProgress(0);
        //homeFragment.updateWatchInterface();
    }

    public void stopTimer() {
        System.out.println("Stop Timer");
        resetTimer();
        mTimeLeftInMillis = 0;
        mTimerRunning = false;
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }


}
