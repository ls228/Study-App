package de.hdmstuttgart.meinprojekt.ui.home;

import static android.content.Context.MODE_PRIVATE;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.Locale;

import de.hdmstuttgart.meinprojekt.R;
import de.hdmstuttgart.meinprojekt.ui.todo.ToDoViewModel;

public class HomeFragment extends Fragment {

    private CountDownTimer mCountDownTimer;

    private TextView mCountDownText;
    private Button bButtonStartPause;
    private Button bButtonReset;
    private Button bButtonSetTime;

    private boolean mTimerRunning;

    private long mStartTimeInMillis;
    private long mTimeLeftInMillis;
    private long mEndTime;

    private NumberPicker HourPicker;
    private NumberPicker MinutePicker;

    private long hoursSet;
    private long minutesSet;
    private int timeSet;

    private ProgressBar mProgressBar;
    private ProgressBar mProgressBarToDo;
    private HomeViewModel viewModel;

    LiveData<Integer> countStatus;
    //LiveData<Integer> countStatusUnchecked;
    private int countChecked;
    private int countUnchecked;


    @SuppressLint("SuspiciousIndentation")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        try{
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        //count progress bar
        countStatus = this.getHomeViewModel().getCountStatusLD();
        //countStatusUnchecked = this.getHomeViewModel().getCountStatusUnchecked();

            if (countStatus != null) {
                countStatus.observe((LifecycleOwner) getContext(), list -> {
                    countChecked = countStatus.getValue();
                    System.out.println(countChecked);
                });
            }

            /*
            if(countStatusUnchecked!=null){
                countStatusUnchecked.observe((LifecycleOwner) getContext(), list -> {
                    countUnchecked = countStatusUnchecked.getValue();
                    System.out.println(countUnchecked);

                });
            }*/

        }catch (NullPointerException e){
            Log.e(TAG, "onAttach: NullPointerException: "
                    + e.getMessage());
        }

        mCountDownText = view.findViewById(R.id.text_view_countdown);

        bButtonStartPause = view.findViewById(R.id.button_start_pause);
        bButtonReset = view.findViewById(R.id.button_reset);
        bButtonSetTime = view.findViewById(R.id.button_set_time);

        HourPicker = view.findViewById(R.id.number_picker_h);
        MinutePicker = view.findViewById(R.id.number_picker_min);

        mProgressBar = view.findViewById(R.id.progress_bar);
        mProgressBarToDo = view.findViewById(R.id.progress_bar_count_todo);

        HourPicker.setMinValue(0);
        HourPicker.setMaxValue(12);
        HourPicker.setValue(0);

        MinutePicker.setMinValue(0);
        MinutePicker.setMaxValue(60);
        MinutePicker.setValue(0);

        //mProgressBar.setProgress(0);

        /**
         * set hours with number picker and calculate total time
         */
        HourPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                hoursSet = Long.valueOf(newVal) * 3600000;
                calculateTotalTime();
            }
            private void calculateTotalTime() {
                mTimeLeftInMillis = hoursSet + minutesSet;
                timeSet = (int) mTimeLeftInMillis;
                mProgressBar.setMax((int) mTimeLeftInMillis);
            }
        });

        /**
         * set minutes with number picker and calculate total time
         */
        MinutePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                minutesSet = Long.valueOf(newVal) * 60000;
               calculateTotalTime();
            }
            private void calculateTotalTime () {
                mTimeLeftInMillis = hoursSet + minutesSet;
                timeSet = (int) mTimeLeftInMillis;
                mProgressBar.setMax((int) mTimeLeftInMillis);
            }
        });

        setTime(mTimeLeftInMillis);

        bButtonStartPause.setOnClickListener(v -> {

            if (mTimerRunning) {
                pauseTimer();
            } else {
                if (mTimeLeftInMillis == 0) {
                    Toast toastMessage = Toast.makeText(requireContext(), "Please enter a positive number!", Toast.LENGTH_LONG);
                    toastMessage.show();
                }else
                startTimer();
            }
        });

        bButtonReset.setOnClickListener(v -> {
            resetTimer();
            mProgressBar.setMax(0);
        });

        bButtonSetTime.setOnClickListener(v -> setTime(mTimeLeftInMillis));

        /*if(count != null){
            int countToDos = count.getValue();

            mProgressBarToDo.setMax(countToDos);
            mProgressBarToDo.setProgress(countToDos);
        }*/

        /*count.observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer value) {
                int countToDos = count.getValue();
                mProgressBarToDo.setMax(countToDos);
                mProgressBarToDo.setProgress(countToDos);
            }
        });*/




        bButtonSetTime.setOnClickListener(v ->{
            resetTimer();
            mProgressBar.setMax(0);
            updateWatchInterface();
        });

        return view;
    }

    private HomeViewModel getHomeViewModel()
    {
        if(viewModel==null)
        {
            throw new NullPointerException();
        }
        return viewModel;
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
                int progress = timeSet - (int) (millisUntilFinished);
                mProgressBar.setProgress(progress);
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                updateWatchInterfaceFinish();
            }
        }.start();

        mTimerRunning = true;
        updateWatchInterface();
    }

    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        updateWatchInterfacePause();
        System.out.println("Checked: " + countStatus.getValue());
       // System.out.println("Unchecked: " + countStatusUnchecked.getValue());
    }

    private void resetTimer() {
        mTimeLeftInMillis = 0;
        hoursSet = 0;
        minutesSet = 0;
        mTimerRunning = false;
        HourPicker.setValue(0);
        MinutePicker.setValue(0);
        updateCountDownText();
        updateWatchInterface();
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

        mCountDownText.setText(timeLeftFormatted);
    }

    /**
     * in this method the visibility of the buttons is set
     */
    private void updateWatchInterface() {
        if (mTimerRunning) {
            bButtonStartPause.setText("Pause");
            HourPicker.setVisibility(View.INVISIBLE);
            MinutePicker.setVisibility(View.INVISIBLE);
            bButtonReset.setVisibility(View.INVISIBLE);
            mCountDownText.setVisibility(View.VISIBLE);
            bButtonSetTime.setVisibility(View.INVISIBLE);
        } else {
            bButtonReset.setVisibility(View.INVISIBLE);
            HourPicker.setVisibility(View.VISIBLE);
            MinutePicker.setVisibility(View.VISIBLE);
            mCountDownText.setVisibility(View.INVISIBLE);
            bButtonStartPause.setText("Start");
            bButtonStartPause.setVisibility(View.VISIBLE);
            bButtonSetTime.setVisibility(View.INVISIBLE);
        }
    }

    private void updateWatchInterfacePause() {
        bButtonStartPause.setText("Start");
        bButtonReset.setVisibility(View.VISIBLE);
    }

    private void updateWatchInterfaceFinish() {
        mCountDownText.setText("Done!☺");
        mCountDownText.setVisibility(View.VISIBLE);
        bButtonSetTime.setText("Set Timer");
        bButtonSetTime.setVisibility(View.VISIBLE);
        bButtonStartPause.setVisibility(View.INVISIBLE);
        bButtonReset.setVisibility(View.INVISIBLE);
        HourPicker.setVisibility(View.INVISIBLE);
        MinutePicker.setVisibility(View.INVISIBLE);
        //mProgressBar.setVisibility(View.INVISIBLE);
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

        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }

    /**
     * this method is called when the app is opened; it retrieves the saved state of the timer,
     * if the imer was running when the app was last used it calculates the remaining time and
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