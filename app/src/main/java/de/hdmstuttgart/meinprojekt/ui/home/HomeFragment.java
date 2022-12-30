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
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import java.util.Locale;

import de.hdmstuttgart.meinprojekt.R;

public class HomeFragment extends Fragment {


    /**
     * save time of timer when closing the fragment
     */
    private long mStartTimeInMillis;


    /**
     * select time
     */
    public NumberPicker hourPicker;
    public NumberPicker minutePicker;

    private long hoursSet;
    private long minutesSet;
    public static int timeSet;

    /**
     * UI
     */
    private TextView mCountDownText;

    private Button bButtonStartPause;
    private Button bButtonReset;
    private Button bButtonSetTime;

    public ProgressBar mProgressBar;
    private ProgressBar mProgressBarToDo;

    /**
     * count To Dos
     */
    LiveData<Integer> countStatus;
    LiveData<Integer> countStatusUnchecked;
    LiveData<Integer> countStatusAll;
    private int countChecked;
    private int countUnchecked;
    private int countAll;



    private HomeViewModel viewModel;

    StudyTimer studyTimer = new StudyTimer(this);



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);


        View view = inflater.inflate(R.layout.fragment_home, container, false);

        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        //count progress bar
        countStatus = this.getHomeViewModel().getCountStatusLD();
        countStatusUnchecked = this.getHomeViewModel().getCountStatusUnchecked();
        countStatusAll = this.getHomeViewModel().getCountAll();

        /*countStatus.observe((LifecycleOwner) getContext(), list -> {
            countChecked = countStatus.getValue();
            System.out.println("checked:" + countChecked);
            mProgressBarToDo.setProgress(countChecked);
        });*/

        /*countStatusUnchecked.observe((LifecycleOwner) getContext(), list -> {
            countUnchecked = countStatusUnchecked.getValue();
            System.out.println("unchecked:" + countUnchecked);

        });*/

        /*countStatusAll.observe((LifecycleOwner) getContext(), list -> {
            countAll = countStatusAll.getValue();
            System.out.println("count all To Do's:" + countAll);
            mProgressBarToDo.setMax(countAll);
        });*/

        mCountDownText = view.findViewById(R.id.text_view_countdown);

        bButtonStartPause = view.findViewById(R.id.button_start_pause);
        bButtonReset = view.findViewById(R.id.button_reset);
        bButtonSetTime = view.findViewById(R.id.button_set_time);

        hourPicker = view.findViewById(R.id.number_picker_h);
        minutePicker = view.findViewById(R.id.number_picker_min);

        mProgressBar = view.findViewById(R.id.progress_bar);
        mProgressBarToDo = view.findViewById(R.id.progress_bar_count_todo);

        hourPicker.setMinValue(0);
        hourPicker.setMaxValue(12);
        hourPicker.setValue(0);

        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(60);
        minutePicker.setValue(0);

        //progressToDos();

        /**
         * set hours with number picker and calculate total time
         */
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

        /**
         * set minutes with number picker and calculate total time
         */
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


        bButtonStartPause.setOnClickListener(v -> {

            if (mTimerRunning) {
                studyTimer.pauseTimer();
            } else if (mTimeLeftInMillis == 0) {
                    Toast toastMessage = Toast.makeText(requireContext(), "Please enter a positive number!", Toast.LENGTH_LONG);
                    toastMessage.show();
                }else
                studyTimer.startTimer();
        });

        bButtonReset.setOnClickListener(v -> {
            studyTimer.resetTimer();
            mProgressBar.setMax(0);
        });


        /*count.observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer value) {
                int countToDos = count.getValue();
                mProgressBarToDo.setMax(countToDos);
                mProgressBarToDo.setProgress(countToDos);
            }
        });*/


        bButtonSetTime.setOnClickListener(v ->{
            studyTimer.resetTimer();
            mProgressBar.setMax(0);
            updateWatchInterface();
        });

        return view;
    }

    /*public void progressToDos() {
        mProgressBarToDo.setMax(countAll);
        System.out.println("all" + countAll);
        mProgressBarToDo.setProgress(countChecked);
        System.out.println("checked" + countChecked);
        System.out.println("set!");
    }*/

    private HomeViewModel getHomeViewModel()
    {
        if(viewModel==null)
        {
            throw new IllegalArgumentException();
        }
        return viewModel;
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

        mCountDownText.setText(timeLeftFormatted);
    }

    /**
     * in this method the visibility of the buttons is set
     */
    public void updateWatchInterface() {
        if (mTimerRunning) {
            bButtonStartPause.setText("Pause");
            hourPicker.setVisibility(View.INVISIBLE);
            minutePicker.setVisibility(View.INVISIBLE);
            bButtonReset.setVisibility(View.INVISIBLE);
            mCountDownText.setVisibility(View.VISIBLE);
        } else {
            bButtonStartPause.setText("Start");
            hourPicker.setVisibility(View.VISIBLE);
            minutePicker.setVisibility(View.VISIBLE);
            mCountDownText.setVisibility(View.INVISIBLE);
            bButtonStartPause.setVisibility(View.VISIBLE);
            bButtonReset.setVisibility(View.INVISIBLE);
            bButtonSetTime.setVisibility(View.INVISIBLE);
        }
    }

    void updateWatchInterfacePause() {
        bButtonStartPause.setText("Start");
        bButtonReset.setVisibility(View.VISIBLE);
        mCountDownText.setVisibility(View.VISIBLE);
        minutePicker.setVisibility(View.INVISIBLE);
        hourPicker.setVisibility(View.INVISIBLE);
    }


    void updateWatchInterfaceFinish() {
        mCountDownText.setText("Done!☺");
        mCountDownText.setVisibility(View.VISIBLE);
        bButtonSetTime.setText("Set Timer");
        bButtonSetTime.setVisibility(View.VISIBLE);
        bButtonStartPause.setVisibility(View.INVISIBLE);
        bButtonReset.setVisibility(View.INVISIBLE);
        hourPicker.setVisibility(View.INVISIBLE);
        minutePicker.setVisibility(View.INVISIBLE);
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

        int progress = timeSet - (int) (mTimeLeftInMillis);
        mProgressBar.setMax(timeSet);
        mProgressBar.setProgress(progress);

        countStatusAll.observe((LifecycleOwner) getContext(), list -> {
            countAll = countStatusAll.getValue();
            System.out.println("count all To Do's:" + countAll);
            mProgressBarToDo.setMax(countAll);
        });

        countStatus.observe((LifecycleOwner) getContext(), list -> {
            countChecked = countStatus.getValue();
            System.out.println("checked:" + countChecked);
            mProgressBarToDo.setProgress(countChecked);
        });

        updateCountDownText();

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
                updateCountDownText();
                updateWatchInterface();
            } else {
                studyTimer.startTimer();
            }
        }
    }
}

