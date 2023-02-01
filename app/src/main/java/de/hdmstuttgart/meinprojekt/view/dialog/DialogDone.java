package de.hdmstuttgart.meinprojekt.view.dialog;

import static de.hdmstuttgart.meinprojekt.view.dialog.DialogAdd.dialogClosed;
import static de.hdmstuttgart.meinprojekt.view.dialog.DialogAdd.dialogOpen;

import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import de.hdmstuttgart.meinprojekt.R;

public class DialogDone {


    /**
     * This class is used when an item is clicked
     * The user can decide whether he wants to delete or cancel
     */

    private final View v;
    private AlertDialog dialogDone;
    private final AlertDialog.Builder dialogBuilder;
    private CountDownTimer timer;

    private final String tag = "DialogDone";

    public DialogDone(View v,
                      AlertDialog.Builder dialogBuilder) {
        this.v = v;
        this.dialogBuilder = dialogBuilder;

    }

    public void done() {

        View dialogView = LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.done_dialog, null);

        dialogBuilder.setView(dialogView);

        dialogBuilder.setCancelable(true);

        dialogDone = dialogBuilder.show();

        Log.d(tag, dialogOpen);

        dialogDone.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        //20000 milli seconds total time, 500 milli seconds is time interval
        timer = new CountDownTimer(1000, 500) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                dialogDone.dismiss();
                stop();
                Log.d(tag, dialogClosed);
            }
        }.start();
    }

    private void stop() {
        timer.cancel();
    }
}
