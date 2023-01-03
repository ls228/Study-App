package de.hdmstuttgart.meinprojekt.view.home;

import android.app.Application;
import android.view.View;
import android.widget.Button;

import de.hdmstuttgart.meinprojekt.R;

public class HomeUI extends Application {

    Button bButtonStartPause;
    Button bButtonReset;
    Button bButtonSetTime;

    StudyTimer studyTimer;
    HomeFragment homeFragment;


    public HomeUI(View view) {

        studyTimer = new StudyTimer(homeFragment, view);

        bButtonStartPause = view.findViewById(R.id.button_start_pause);
        bButtonReset = view.findViewById(R.id.button_reset);
        bButtonSetTime = view.findViewById(R.id.button_set_time);
    }


}
