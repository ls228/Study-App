package de.hdmstuttgart.meinprojekt.ui.home;

import static android.content.Context.MODE_PRIVATE;

import static de.hdmstuttgart.meinprojekt.ui.home.StudyTimer.mEndTime;
import static de.hdmstuttgart.meinprojekt.ui.home.StudyTimer.mTimeLeftInMillis;
import static de.hdmstuttgart.meinprojekt.ui.home.StudyTimer.mTimerRunning;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import de.hdmstuttgart.meinprojekt.R;

public class HomeFragment extends Fragment {

    private long mStartTimeInMillis;

    private Button bButtonStartPause;
    private Button bButtonReset;
    private Button bButtonSetTime;

    StudyTimer studyTimer;
    ToDoCounter toDoCounter;

    private String errorMessage = "Please enter a positive number!";


    /**
     * sets the layout of the fragment
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        toDoCounter = new ToDoCounter(this, view);
        studyTimer = new StudyTimer(this, view);

        bButtonStartPause = view.findViewById(R.id.button_start_pause);
        bButtonReset = view.findViewById(R.id.button_reset);
        bButtonSetTime = view.findViewById(R.id.button_set_time);

        bButtonStartPause.setOnClickListener(v -> {

            if (mTimerRunning) {
                studyTimer.pauseTimer();
            } else if (mTimeLeftInMillis == 0) {
                    Toast toastMessage = Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG);
                    toastMessage.show();
                }else
                studyTimer.startTimer();
        });

        bButtonReset.setOnClickListener(v -> {
            studyTimer.resetTimer();
            studyTimer.mProgressBar.setMax(0);
        });


        bButtonSetTime.setOnClickListener(v ->{
            studyTimer.resetTimer();
            studyTimer.mProgressBar.setMax(0);
            updateWatchInterface();
        });

        return view;
    }




    /**
     * in this method the visibility of the buttons is set
     */

    public void updateWatchInterface() {
        if (mTimerRunning) {
            bButtonStartPause.setText(R.string.buttonPause);
            studyTimer.hourPicker.setVisibility(View.INVISIBLE);
            studyTimer.minutePicker.setVisibility(View.INVISIBLE);
            bButtonReset.setVisibility(View.INVISIBLE);
            studyTimer.mCountDownText.setVisibility(View.VISIBLE);
        } else {
            bButtonStartPause.setText(R.string.buttonStart);
            studyTimer.hourPicker.setVisibility(View.VISIBLE);
            studyTimer.minutePicker.setVisibility(View.VISIBLE);
            studyTimer.mCountDownText.setVisibility(View.INVISIBLE);
            bButtonStartPause.setVisibility(View.VISIBLE);
            bButtonReset.setVisibility(View.INVISIBLE);
            bButtonSetTime.setVisibility(View.INVISIBLE);
        }
    }

    void updateWatchInterfacePause() {
        bButtonStartPause.setText(R.string.buttonStart);
        bButtonReset.setVisibility(View.VISIBLE);
        studyTimer.mCountDownText.setVisibility(View.VISIBLE);
        studyTimer.minutePicker.setVisibility(View.INVISIBLE);
        studyTimer.hourPicker.setVisibility(View.INVISIBLE);
    }

    void updateWatchInterfaceFinish() {
        studyTimer.mCountDownText.setText(R.string.done);
        studyTimer.mCountDownText.setVisibility(View.VISIBLE);
        bButtonSetTime.setText(R.string.set);
        bButtonSetTime.setVisibility(View.VISIBLE);
        bButtonStartPause.setVisibility(View.INVISIBLE);
        bButtonReset.setVisibility(View.INVISIBLE);
        studyTimer.hourPicker.setVisibility(View.INVISIBLE);
        studyTimer.minutePicker.setVisibility(View.INVISIBLE);
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

        studyTimer.saveTimerProgressBar();

        toDoCounter.progressToDos();

        studyTimer.updateCountDownText();

        if(!mTimerRunning && mTimeLeftInMillis > 0){
            updateWatchInterfacePause();
        }else {
            updateWatchInterface();
        }

        if (mTimerRunning) {
            mEndTime = prefs.getLong("endTime", 0);
            mTimeLeftInMillis = mEndTime - System.currentTimeMillis();

            if (mTimeLeftInMillis < 0) {
                mTimeLeftInMillis = 0;
                mTimerRunning = false;
                studyTimer.updateCountDownText();
                updateWatchInterface();
            } else {
                studyTimer.startTimer();
            }
        }
    }
}

