package de.hdmstuttgart.meinprojekt.ui.home;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.content.Context.MODE_PRIVATE;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.TimeAnimator;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.time.OffsetTime;
import java.util.Calendar;
import java.util.Locale;

import de.hdmstuttgart.meinprojekt.R;
import de.hdmstuttgart.meinprojekt.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private CountDownTimer mCountDownTimer;

    private TextView mTextViewCountDown;
    private Button mButtonStartPause;
    private Button mButtonReset;

    private boolean mTimerRunning;

    private long mStartTimeInMillis;
    private long mTimeLeftInMillis;
    private long mEndTime;

    NumberPicker numberHourPicker;
    NumberPicker numberMinutePicker;

    long selectedTimeInSeconds;
    long hour;
    long minute;

    private FragmentHomeBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mTextViewCountDown = view.findViewById(R.id.text_view_countdown);

        mButtonStartPause = view.findViewById(R.id.button_start_pause);
        mButtonReset = view.findViewById(R.id.button_reset);

        numberHourPicker = view.findViewById(R.id.number_picker_h);
        numberMinutePicker = view.findViewById(R.id.number_picker_min);

        numberHourPicker.setMinValue(0);
        numberHourPicker.setMaxValue(12);
        numberHourPicker.setValue(0);

        numberMinutePicker.setMinValue(0);
        numberMinutePicker.setMaxValue(60);
        numberMinutePicker.setValue(0);


        numberHourPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                hour = Long.valueOf(newVal) * 3600000;
                calculateTotalTime();
                // When the picker value changes, update the timer

            }
            private void calculateTotalTime() {
                selectedTimeInSeconds = hour + minute;
            }
        });


        numberMinutePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                minute = Long.valueOf(newVal) * 60000;
               calculateTotalTime();
                // When the picker value changes, update the timer
            }
            private void calculateTotalTime () {
                selectedTimeInSeconds = hour + minute;
            }
        });

        setTime(selectedTimeInSeconds);

        mButtonStartPause.setOnClickListener(v -> {

            if (mTimerRunning) {
                pauseTimer();
            } else {
                startTimer();
            }
        });

        mButtonReset.setOnClickListener(v -> resetTimer());
        return view;
    }


    private void setTime(long timeInMillis) {
        mStartTimeInMillis = timeInMillis;
        resetTimer();
    }

    private void startTimer() {
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;

        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                updateWatchInterface();
            }
        }.start();

        mTimerRunning = true;
        updateWatchInterface();
    }

    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        updateWatchInterface();
    }

    private void resetTimer() {
        mTimeLeftInMillis = mStartTimeInMillis;
        mTimerRunning = false;
        updateCountDownText();
        resetWatchInterface();
    }

    private void updateCountDownText() {
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

        mTextViewCountDown.setText(timeLeftFormatted);
    }

    private void updateWatchInterface() {
        if (mTimerRunning) {
            mButtonStartPause.setText("Pause");
            numberHourPicker.setVisibility(View.INVISIBLE);
            numberMinutePicker.setVisibility(View.INVISIBLE);
            mTextViewCountDown.setVisibility(View.VISIBLE);
        } else {
            mButtonStartPause.setText("Start");
            mButtonReset.setVisibility(View.VISIBLE);


            if (mTimeLeftInMillis < 1000) {
                mButtonStartPause.setVisibility(View.INVISIBLE);
            } else {
                mButtonStartPause.setVisibility(View.VISIBLE);
            }

            if (mTimeLeftInMillis < mStartTimeInMillis) {
                mButtonReset.setVisibility(View.VISIBLE);
            } else {
                mButtonReset.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void resetWatchInterface() {

            mButtonReset.setVisibility(View.INVISIBLE);
            numberHourPicker.setVisibility(View.VISIBLE);
            numberMinutePicker.setVisibility(View.VISIBLE);
            mTextViewCountDown.setVisibility(View.INVISIBLE);
            mButtonStartPause.setText("Start");
            mButtonStartPause.setVisibility(View.VISIBLE);

    }

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

        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        SharedPreferences prefs = getContext().getSharedPreferences("prefs", MODE_PRIVATE);

        mStartTimeInMillis = prefs.getLong("startTimeInMillis", 600000);
        mTimeLeftInMillis = prefs.getLong("millisLeft", mStartTimeInMillis);
        mTimerRunning = prefs.getBoolean("timerRunning", false);

        updateCountDownText();
        updateWatchInterface();

        if (mTimerRunning) {
            mEndTime = prefs.getLong("endTime", 0);
            mTimeLeftInMillis = mEndTime - System.currentTimeMillis();

            if (mTimeLeftInMillis < 0) {
                mTimeLeftInMillis = 0;
                mTimerRunning = false;
                updateCountDownText();
                updateWatchInterface();
            } else {
                startTimer();
            }
        }
    }

}