package de.hdmstuttgart.meinprojekt.ui.todo;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static androidx.core.content.ContentProviderCompat.requireContext;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import java.util.Calendar;
import java.util.Date;

import de.hdmstuttgart.meinprojekt.R;
import de.hdmstuttgart.meinprojekt.database.Converter;
import de.hdmstuttgart.meinprojekt.model.todo.ToDoItem;

public class DialogAdd {

    private String inputTitle = "";
    private String inputTopic = "";
    private String currentTime;
    private View v;
    private ToDoItem toDoItem;
    private AlertDialog.Builder dialogBuilder;
    private ToDoViewModel viewModel;

    public DialogAdd(View v, AlertDialog.Builder dialogBuilder, ToDoViewModel viewModel) {
        this.v = v;
        this.dialogBuilder = dialogBuilder;
        this.viewModel = viewModel;
    }

    public void dialog() {

        View dialogView = LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.addtodo_dialog, null);

        TextView titleHeading;
        TextView topicHeading;


        Button btnCancel = dialogView.findViewById(R.id.btncancel);
        Button btnAdd = dialogView.findViewById(R.id.btnadd);

        EditText titleInput = dialogView.findViewById(R.id.titleinput);
        EditText topicInput = dialogView.findViewById(R.id.textInputEditText);

        titleHeading = dialogView.findViewById(R.id.titleheading);
        topicHeading = dialogView.findViewById(R.id.topicheading);

        titleHeading.setText("Title of your new To Do");
        topicHeading.setText("Description: ");

        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(true);

        AlertDialog dialog = dialogBuilder.show();

        btnCancel.setOnClickListener(
                a -> {
                    Log.d(TAG, "onClick: closing dialog");
                    dialog.dismiss();
                });

        btnAdd.setOnClickListener(
                a -> {
                    Date time = Calendar.getInstance().getTime();
                    currentTime = Converter.dateToTimestamp(time);

                    Log.d(TAG, "onClick: capturing input");

                    inputTitle = titleInput.getText().toString();
                    inputTopic = topicInput.getText().toString();

                    this.toDoItem = new ToDoItem(inputTitle, currentTime, inputTopic, 0);

                    if(toDoItem.getTitle().equals("")) {
                        Toast toastMessage = Toast.makeText(v.getContext(), "Please enter a valid to do!", Toast.LENGTH_LONG);
                        toastMessage.show();
                    }else {
                        viewModel.saveToDo(toDoItem);
                        dialog.dismiss();
                    }

                });

    }



}

