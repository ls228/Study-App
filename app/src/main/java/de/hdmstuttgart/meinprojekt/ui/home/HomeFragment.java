package de.hdmstuttgart.meinprojekt.ui.home;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import de.hdmstuttgart.meinprojekt.R;

public class HomeFragment extends Fragment {

    private CountDownTimer mCountDownTimer;

    private TextView mTextViewCountDown;
    private Button mButtonStartPause;
    private Button mButtonReset;
    private CountDownTimer timer;



    private boolean mTimerRunning;

    private long mStartTimeInMillis;
    private long mTimeLeftInMillis;
    private long mEndTime;

    NumberPicker hoursPicker;
    NumberPicker minutesPicker;



    long selectedTimeInSeconds = 0;
    long currentMinutesPicker = 0;
    long currentHourPicker = 0;



    private long millisInput;
    boolean timerRanOnce = false;


    //private FragmentHomeBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mTextViewCountDown = view.findViewById(R.id.text_view_countdown);

        mButtonStartPause = view.findViewById(R.id.button_start_pause);
        mButtonReset = view.findViewById(R.id.button_reset);

        hoursPicker = view.findViewById(R.id.number_picker_h);
        minutesPicker = view.findViewById(R.id.number_picker_min);

        hoursPicker.setMinValue(0);
        hoursPicker.setMaxValue(12);
        hoursPicker.setValue(0);

        minutesPicker.setMinValue(0);
        minutesPicker.setMaxValue(60);
        minutesPicker.setValue(0);




        // Create a new CountDownTimer instance
        // Create a new CountDownTimer instance






        //on changes
        hoursPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                // Update the selected time when the user changes the number of hours
                currentHourPicker = newVal * 1000 * 60 * 60;

                calculateTotalTime();
                System.out.println("set time");
                System.out.println(selectedTimeInSeconds);
            }

            private void calculateTotalTime() {
                selectedTimeInSeconds = currentHourPicker + currentMinutesPicker;
            }
        });

        minutesPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                // Update the selected time when the user changes the number of minutes
                currentMinutesPicker = newVal * 1000 * 60;

                calculateTotalTime();
                System.out.println("set time");
                System.out.println(selectedTimeInSeconds);

            }

            private void calculateTotalTime() {
                selectedTimeInSeconds = currentHourPicker + currentMinutesPicker;
            }


        });




        //onclick listener
        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("start timer");
                System.out.println(selectedTimeInSeconds);

                if(timerRanOnce) timer.cancel();

                timer = setupCountDown(selectedTimeInSeconds);
                timer.start();
                timerRanOnce = true;
            }

            private CountDownTimer setupCountDown(long selectedTimeInSeconds) {
                CountDownTimer timer =  new CountDownTimer(selectedTimeInSeconds, 1000) {

                    public void onTick(long millisUntilFinished) {
                        System.out.println("tick");
                        mTextViewCountDown.setVisibility(View.VISIBLE);
                        mTextViewCountDown.setText("seconds remaining: " + millisUntilFinished / 1000);
                    }

                    public void onFinish() {
                        mTextViewCountDown.setText("done!");
                    }
                };

                return timer;
            }
        });




        return view;
    }


}