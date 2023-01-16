package de.hdmstuttgart.meinprojekt.view.home;

import static android.content.Context.MODE_PRIVATE;
import static android.webkit.ConsoleMessage.MessageLevel.LOG;

import static de.hdmstuttgart.meinprojekt.view.home.StudyTimer.mEndTime;
import static de.hdmstuttgart.meinprojekt.view.home.StudyTimer.mTimeLeftInMillis;
import static de.hdmstuttgart.meinprojekt.view.home.StudyTimer.mTimerRunning;
import static de.hdmstuttgart.meinprojekt.view.home.TimerStatus.PAUSE;
import static de.hdmstuttgart.meinprojekt.view.home.TimerStatus.RESET;
import static de.hdmstuttgart.meinprojekt.view.home.TimerStatus.RUNNING;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;


import de.hdmstuttgart.meinprojekt.R;

public class HomeFragment extends Fragment {

    private long mStartTimeInMillis;

    StudyTimer studyTimer;
    ToDoCounter toDoCounter;

    Button bButtonStart;
    Button bButtonPause;
    Button bButtonReset;

    public static boolean timeUp = false;
    public static boolean allTodosChecked = false;

    private AlertDialog.Builder builder;
    private DialogDone dialogDone;

    private long hoursSet;
    private long minutesSet;
    private long timeSet;
    private int newTime;

    private int mhour = 0;
    private int mMinute = 0;

    TimerStatus status;


    /**
     * sets the layout of the fragment
     */

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        toDoCounter = new ToDoCounter(this, view);
        studyTimer = new StudyTimer(this, view);

        bButtonStart = view.findViewById(R.id.button_start);
        bButtonPause = view.findViewById(R.id.button_pause);
        bButtonReset = view.findViewById(R.id.button_reset);


        bButtonStart.setOnClickListener(v -> {
            mhour = studyTimer.hourPicker.getValue();
            mMinute = studyTimer.minutePicker.getValue();
            System.out.println("Hour: " + mhour + " Minute: "+ mMinute);
            newTime = (int) calculateTime(mMinute,mhour);
            mTimeLeftInMillis = newTime;
            System.out.println("calculated time: " + newTime);
            studyTimer.mProgressBar.setMax(newTime);


            System.out.println("time left in millis: "+ mTimeLeftInMillis);
            if (mTimeLeftInMillis == 0) {
                Toast toastMessage = Toast.makeText(requireContext(), "Please enter a positive number!", Toast.LENGTH_LONG);
                toastMessage.show();
            } else
                updateWatchInterface(RUNNING);
                studyTimer.startTimer(newTime);
        });

        bButtonReset.setOnClickListener(v -> {
            mhour = 0;
            mMinute = 0;
            studyTimer.resetTimer();
            updateWatchInterface(RESET);
            studyTimer.mProgressBar.setMax(0);
        });

        bButtonPause.setOnClickListener(v -> {
            studyTimer.pauseTimer();
            updateWatchInterface(PAUSE);
        });


        return view;
    }

    private long calculateTime(int minutes, int hours) {
        System.out.println("in calculate minute, hour: " + minutes+" "+hours);
        minutesSet =  (long) minutes * 60000;
        hoursSet = (long) hours * 3600000;
        System.out.println("in long minute: " + minutesSet +"in long hour: " + hoursSet);

        timeSet = minutesSet + hoursSet;

        return timeSet;

    }


    public void doneAnimation() {
        builder = new AlertDialog.Builder(getContext());
        dialogDone = new DialogDone(getView(),builder);
        dialogDone.done();
        updateWatchInterface(RESET);

    }


    void updateWatchInterface(TimerStatus status) {
        System.out.println("switch");
        switch (status) {
            case RUNNING:
                System.out.println("Running Interface");
                bButtonStart.setVisibility(View.INVISIBLE);
                bButtonPause.setVisibility(View.VISIBLE);
                studyTimer.hourPicker.setVisibility(View.INVISIBLE);
                studyTimer.minutePicker.setVisibility(View.INVISIBLE);
                bButtonReset.setVisibility(View.INVISIBLE);
                studyTimer.mCountDownText.setVisibility(View.VISIBLE);
                break;
            case PAUSE:
                System.out.println("Pause Interface");
                bButtonStart.setVisibility(View.VISIBLE);
                bButtonPause.setVisibility(View.INVISIBLE);
                bButtonReset.setVisibility(View.VISIBLE);
                studyTimer.mCountDownText.setVisibility(View.VISIBLE);
                studyTimer.minutePicker.setVisibility(View.INVISIBLE);
                studyTimer.hourPicker.setVisibility(View.INVISIBLE);
                break;
            case RESET:
                System.out.println("Reset Interface");
                studyTimer.hourPicker.setVisibility(View.VISIBLE);
                studyTimer.minutePicker.setVisibility(View.VISIBLE);
                studyTimer.mCountDownText.setVisibility(View.INVISIBLE);
                bButtonStart.setVisibility(View.VISIBLE);
                bButtonPause.setVisibility(View.INVISIBLE);
                bButtonReset.setVisibility(View.INVISIBLE);
                break;
        }
    }


    /**
     * this method is called when the app is closed, it saves the current state of the timer
     * this allows the app to restore the timer's state  when the app is restarted, so that the
     * user can continue using the timer where they left off;
     * it cancels the CountDownTimer
     */
    @Override
    public void onStop() {
        super.onStop();

        SharedPreferences prefs = getContext().getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong("startTimeInMillis", mStartTimeInMillis);
        editor.putLong("millisLeft", mTimeLeftInMillis);
        editor.putBoolean("timerRunning", mTimerRunning);
        editor.putLong("endTime", mEndTime);

        editor.apply();

        if (studyTimer.mCountDownTimer != null) {
            studyTimer.mCountDownTimer.cancel();
        }

    }

    /**
     * this method is called when the app is opened; it retrieves the saved state of the timer,
     * if the timer was running when the app was last used it calculates the remaining time and
     * starts the timer again so that it continues counting down,
     * it is responsible for restoring the state of the CountDownTimer
     */
    @Override
    public void onStart() {
        super.onStart();

        SharedPreferences prefs = getContext().getSharedPreferences("prefs", MODE_PRIVATE);

        mStartTimeInMillis = prefs.getLong("startTimeInMillis", 600000);
        mTimeLeftInMillis = prefs.getLong("millisLeft", mStartTimeInMillis);
        mTimerRunning = prefs.getBoolean("timerRunning", false);

        studyTimer.saveTimerProgressBar(newTime);

        toDoCounter.progressToDos();

        studyTimer.updateCountDownText();

        if (!mTimerRunning && mTimeLeftInMillis > 0) {
            updateWatchInterface(PAUSE);

        } else {
            updateWatchInterface(RESET);
        }

        if (mTimerRunning) {
            mEndTime = prefs.getLong("endTime", 0);
            mTimeLeftInMillis = mEndTime - System.currentTimeMillis();

            if (mTimeLeftInMillis < 0) {
                mTimeLeftInMillis = 0;
                mTimerRunning = false;
                studyTimer.updateCountDownText();
                updateWatchInterface(RESET);
            } else {
                studyTimer.startTimer(newTime);
                updateWatchInterface(RUNNING);
            }
        }


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("onDestroy");
        studyTimer.stopTimer();
    }
}

