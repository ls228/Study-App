package de.hdmstuttgart.meinprojekt.view.todo.ui;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import de.hdmstuttgart.meinprojekt.R;
import de.hdmstuttgart.meinprojekt.model.todo.ToDoItem;
import de.hdmstuttgart.meinprojekt.view.todo.Converter;
import de.hdmstuttgart.meinprojekt.view.todo.ToDoViewModel;

public class DialogAdd {

    /**
     * This class is used when the users wants to add a new To do to the list
     * You can add title, a description and add or close dialog
     */

    private String inputTitle = "";
    private String inputTopic = "";
    private String currentTime;

    private View v;
    private View dialogView;

    private ToDoItem toDoItem;
    private AlertDialog.Builder dialogBuilder;
    private ToDoViewModel viewModel;

    private EditText titleInput;
    private EditText topicInput;


    private Button btnCancel;
    private Button btnAdd;

    private TextView titleHeading;
    private TextView topicHeading;

    private String errorMessage = "Please enter a valid To Do!";

    public DialogAdd(View v, AlertDialog.Builder dialogBuilder, ToDoViewModel viewModel) {
        this.v = v;
        this.dialogBuilder = dialogBuilder;
        this.viewModel = viewModel;
    }

    public void dialog() {

        dialogView = LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.addtodo_dialog, null);


        btnCancel = dialogView.findViewById(R.id.btncancel);
        btnAdd = dialogView.findViewById(R.id.btnadd);

        titleInput = dialogView.findViewById(R.id.titleinput);
        topicInput = dialogView.findViewById(R.id.textInputEditText);

        titleHeading = dialogView.findViewById(R.id.titleheading);
        topicHeading = dialogView.findViewById(R.id.topicheading);

        titleHeading.setText("Title of your new To Do");
        topicHeading.setText("Description: ");

        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);

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

                    if (toDoItem.getTitle().equals("")) {
                        //if input has no title toast message will pop up
                        Toast toastMessage = Toast.makeText(v.getContext(), errorMessage, Toast.LENGTH_LONG);
                        toastMessage.show();
                    } else {
                        //saving input in viewmodel
                        viewModel.saveToDo(toDoItem);
                        dialog.dismiss();
                    }

                });

    }

}

