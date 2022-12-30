package de.hdmstuttgart.meinprojekt.ui.home;

import static de.hdmstuttgart.meinprojekt.ui.home.HomeFragment.timeSet;

import android.os.CountDownTimer;

public class StudyTimer {

    public CountDownTimer mCountDownTimer;

    public static boolean mTimerRunning;

    public static long mTimeLeftInMillis;
    public static long mEndTime;


    private final HomeFragment homeFragment;

    public StudyTimer(HomeFragment homeFragment) {
        this.homeFragment = homeFragment;
    }


    public void startTimer() {
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;

        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                int progress = timeSet - (int) (millisUntilFinished);
                homeFragment.mProgressBar.setProgress(progress);
                homeFragment.updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                homeFragment.updateWatchInterfaceFinish();
            }
        }.start();

        mTimerRunning = true;
        homeFragment.updateWatchInterface();
    }

    public void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        homeFragment.updateWatchInterfacePause();
        //System.out.println("Checked: " + countStatus.getValue());
        //System.out.println("Unchecked: " + countStatusUnchecked.getValue());
    }

    public void resetTimer() {
        mTimeLeftInMillis = 0;
        mTimerRunning = false;
        homeFragment.hourPicker.setValue(0);
        homeFragment.minutePicker.setValue(0);
        homeFragment.updateCountDownText();
        homeFragment.updateWatchInterface();
    }

}
