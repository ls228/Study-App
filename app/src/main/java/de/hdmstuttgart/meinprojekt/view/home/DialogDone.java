package de.hdmstuttgart.meinprojekt.view.home;

import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import de.hdmstuttgart.meinprojekt.R;

public class DialogDone {

    private final String dialogOpen = "onClick: opening Edit dialog success";
    /**
     * This class is used when an item is clicked
     * The user can decide whether he wants to delete or cancel
     */

    private final View v;
    private View dialogView;
    private AlertDialog dialogDone;
    private final AlertDialog.Builder dialogBuilder;
    private CountDownTimer timer;

    public DialogDone(View v,
                      AlertDialog.Builder dialogBuilder) {
        this.v = v;
        this.dialogBuilder = dialogBuilder;

    }

    public void done() {

        dialogView = LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.done_dialog, null);

        dialogBuilder.setView(dialogView);

        dialogBuilder.setCancelable(true);

        dialogDone = dialogBuilder.show();

        Log.d("DialogDone", dialogOpen);

        dialogDone.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        //20000 milli seconds total time, 500 milli seconds is time interval
        timer = new CountDownTimer(1000, 500) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                dialogDone.dismiss();
                stop();
            }
        }.start();

    }

    private void stop() {
        timer.cancel();
    }
}
