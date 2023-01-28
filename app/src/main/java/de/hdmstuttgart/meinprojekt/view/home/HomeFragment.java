package de.hdmstuttgart.meinprojekt.view.home;

import static android.content.Context.MODE_PRIVATE;
import static de.hdmstuttgart.meinprojekt.view.home.StudyTimer.mEndTime;
import static de.hdmstuttgart.meinprojekt.view.home.StudyTimer.mTimeLeftInMillis;
import static de.hdmstuttgart.meinprojekt.view.home.StudyTimer.mTimerRunning;
import static de.hdmstuttgart.meinprojekt.view.home.TimerStatus.PAUSE;
import static de.hdmstuttgart.meinprojekt.view.home.TimerStatus.RESET;
import static de.hdmstuttgart.meinprojekt.view.home.TimerStatus.RUNNING;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.Locale;

import de.hdmstuttgart.meinprojekt.R;
import de.hdmstuttgart.meinprojekt.model.ToDoItem;
import de.hdmstuttgart.meinprojekt.view.Dialog.DialogDone;
import de.hdmstuttgart.meinprojekt.viewmodel.ViewModel;

public class HomeFragment extends Fragment {

    StudyTimer studyTimer;

    Button bButtonStart;
    Button bButtonPause;
    Button bButtonReset;
    private boolean pauseBeforeStart = false;

    private long mStartTimeInMillis;

    private ProgressBar mProgressBarToDo;
    private ViewModel viewModel;
    private MediaPlayer media;

    private int newTime;
    private int mhour = 0;
    private int mMinute = 0;
    private static final String tag = "HomeFragment";


    /**
     * sets the layout of the fragment
     */


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        saveTimerProgress();

        media = new MediaPlayer();
        media = MediaPlayer.create(this.getContext(), R.raw.ringtone);

        //Opens interface to show done animation and reset the timer, to call it from different classes
        IOnFinish onFinish = () -> {
            media.start();
            doneAnimation();
            HomeFragment.this.updateWatchInterface(RESET);
        };


        studyTimer = new StudyTimer(view, onFinish);

        bButtonStart = view.findViewById(R.id.button_start);
        bButtonPause = view.findViewById(R.id.button_pause);
        bButtonReset = view.findViewById(R.id.button_reset);

        mProgressBarToDo = view.findViewById(R.id.progress_bar_count_todo);
        viewModel = new ViewModelProvider(this).get(ViewModel.class);

        bButtonStart.setOnClickListener(v -> {
            if(!pauseBeforeStart){
                mhour = studyTimer.hourPicker.getValue();
                mMinute = studyTimer.minutePicker.getValue();
                Log.d(tag, "Hour: " + mhour + " Minute: " + mMinute);
                newTime = (int) calculateTime(mMinute, mhour);

                mTimeLeftInMillis = newTime;
                Log.d(tag, "calculated time: " + newTime);
                studyTimer.mProgressBar.setMax(newTime);


            Log.d(tag, "time left in millis: " + mTimeLeftInMillis);}

            if (mTimeLeftInMillis == 0) {
                Toast toastMessage = Toast.makeText(requireContext(), "Please enter a positive number!", Toast.LENGTH_LONG);
                toastMessage.show();
            } else {
                updateWatchInterface(RUNNING);
                studyTimer.startTimer(newTime);
            }
            pauseBeforeStart = false;
        });

        bButtonReset.setOnClickListener(v -> {
            pauseBeforeStart = false;
            mhour = 0;
            mMinute = 0;
            studyTimer.resetTimer();
            updateWatchInterface(RESET);
        });

        bButtonPause.setOnClickListener(v -> {
            pauseBeforeStart = true;
            studyTimer.pauseTimer();
            updateWatchInterface(PAUSE);
        });

        return view;
    }


    public int saveTimerProgressBar(int timeSet) {
        if (mTimeLeftInMillis != 0) {
            int progress = timeSet - (int) (mTimeLeftInMillis);
            Log.d(tag, "Saved progress " + progress);
            return progress;
        } else {
            return 0;
        }
    }


    private void saveTimerProgress() {

        SharedPreferences prefs = requireContext().getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong("startTimeInMillis", mStartTimeInMillis);
        editor.putLong("millisLeft", mTimeLeftInMillis);
        editor.putBoolean("timerRunning", mTimerRunning);
        editor.putLong("endTime", mEndTime);

        editor.apply();

    }

    private ViewModel getViewModel() {
        if (viewModel == null) {
            throw new NullPointerException();
        }
        return viewModel;
    }


    //this method calculates the minute and hour input in milliseconds and returns the these two summed up
    public long calculateTime(int minutes, int hours) {
        long minutesSet = (long) minutes * 60000;
        long hoursSet = (long) hours * 3600000;

       // return minutesSet + hoursSet;
        return 5000;

    }

    public void progressToDos() {

        try {
            getViewModel().getSavedToDos().observe(getViewLifecycleOwner(), list -> {
                if (list == null) throw new NullPointerException();
                int countAll = list.size();
                Log.d(tag, "all todos: " + countAll);
                mProgressBarToDo.setMax(countAll * 5);

                int countChecked = (int) list.stream().filter(toDoItem -> toDoItem.getStatus()).count();

                Log.d(tag, "checked: " + countChecked + "int: " + (int) countChecked);
                mProgressBarToDo.setProgress(countChecked);

                ObjectAnimator animation = ObjectAnimator.ofInt(mProgressBarToDo, "progress", 0, countChecked * 5);
                animation.setDuration(1500);
                animation.start();

            });
        } catch (Exception e) {
            Log.d("ToDoCounter", "Thrown exception: " + e.getMessage());
        }
    }

    //this Method opens the dialog for the check animation if the time is up or all todos checked
    public void doneAnimation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        DialogDone dialogDone = new DialogDone(getView(), builder);
        dialogDone.done();
    }

    /**
     * This method updates the interface of the timer depending on the status event
     *
     * @param status Running, Reset or Pause
     */
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

    @Override
    public void onResume() {
        super.onResume();
        progressToDos();
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

        SharedPreferences prefs = requireContext().getSharedPreferences("prefs", MODE_PRIVATE);

        mStartTimeInMillis = prefs.getLong("startTimeInMillis", 600000);
        mTimeLeftInMillis = prefs.getLong("millisLeft", mStartTimeInMillis);
        mTimerRunning = prefs.getBoolean("timerRunning", false);

        studyTimer.mProgressBar.setMax(newTime);
        int progress = saveTimerProgressBar(newTime);
        studyTimer.mProgressBar.setProgress(progress);

        progressToDos();

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

    /**
     * this method is called when the app is closed, it saves the current state of the timer
     * this allows the app to restore the timer's state  when the app is restarted, so that the
     * user can continue using the timer where they left off;
     * it cancels the CountDownTimer
     */
    @Override
    public void onStop() {
        super.onStop();

        progressToDos();
        media.release();

        if (studyTimer.mCountDownTimer != null) {
            studyTimer.mCountDownTimer.cancel();
        }

    }

    @Override
    public void onDestroy() {
        Log.d(tag, "onDestroy");
        super.onDestroy();
    }
}

