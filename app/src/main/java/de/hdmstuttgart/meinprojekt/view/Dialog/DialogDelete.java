package de.hdmstuttgart.meinprojekt.view.Dialog;

import static de.hdmstuttgart.meinprojekt.view.Dialog.DialogAdd.dialogOpen;
import static de.hdmstuttgart.meinprojekt.view.Dialog.DialogAdd.dialogClosed;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import java.util.List;

import de.hdmstuttgart.meinprojekt.R;
import de.hdmstuttgart.meinprojekt.model.ToDoItem;
import de.hdmstuttgart.meinprojekt.view.todo.ToDoAdapter;
import de.hdmstuttgart.meinprojekt.viewmodel.ViewModel;

public class DialogDelete {


    private final String delete = "onClick: selected item has been successfully deleted";
    /**
     * This class is used when an item is clicked
     * The user can decide whether he wants to delete or cancel
     */

    private final View v;
    private final ViewModel viewModel;
    private final ToDoAdapter toDoAdapter;
    private final List<ToDoItem> toDoItem;
    private final int position;
    private AlertDialog dialogEdit;
    private final AlertDialog.Builder dialogBuilder;
    private final String tag = "DialogDelete";

    public DialogDelete(View v,
                        AlertDialog.Builder dialogBuilder,
                        ViewModel viewModel,
                        ToDoAdapter toDoAdapter,
                        List<ToDoItem> toDoItem,
                        int position) {
        this.v = v;
        this.dialogBuilder = dialogBuilder;
        this.viewModel = viewModel;
        this.toDoAdapter = toDoAdapter;
        this.toDoItem = toDoItem;
        this.position = position;
    }

    public void delete() {

        View dialogView = LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.edittodo_dialog, null);

        dialogBuilder.setView(dialogView);

        dialogBuilder.setCancelable(false);

        dialogEdit = dialogBuilder.show();
        dialogEdit.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        Log.d(tag, dialogOpen);

        Button btnDelete = dialogView.findViewById(R.id.btndelete);
        Button btnNo = dialogView.findViewById(R.id.btnNo);

        btnDelete.setOnClickListener(v -> {
            Log.d(tag, delete);
            viewModel.removeToDo(toDoItem.get(position));
            toDoAdapter.notifyItemRemoved(position);
            toDoAdapter.notifyItemRangeChanged(position, 1);
            dialogEdit.dismiss();
            Log.d(tag, dialogClosed);

        });

        btnNo.setOnClickListener(v -> dialogEdit.dismiss());

    }
}
