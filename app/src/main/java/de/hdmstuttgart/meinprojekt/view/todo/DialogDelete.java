package de.hdmstuttgart.meinprojekt.view.todo;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import java.util.List;

import de.hdmstuttgart.meinprojekt.R;
import de.hdmstuttgart.meinprojekt.model.ToDoItem;
import de.hdmstuttgart.meinprojekt.viewmodel.ToDoViewModel;

public class DialogDelete {

    /**
     * This class is used when an item is clicked
     * The user can decide whether he wants to delete or cancel
     */

    private View v;
    private View dialogView;
    private ToDoViewModel viewModel;
    private ToDoAdapter toDoAdapter;

    private List<ToDoItem> list;
    private int position;

    private Button btnDelete;
    private Button btnNo;

    private AlertDialog dialogEdit;
    private AlertDialog.Builder dialogBuilder;
    private String tag = "DialogDelete";

    private final String dialogOpen = "onClick: opening Edit dialog success";
    private final String delete = "onClick: selected item has been successfully deleted";
    private final String dialogClose= "Closing dialog";

    public DialogDelete(View v,
                        AlertDialog.Builder dialogBuilder,
                        ToDoViewModel viewModel,
                        ToDoAdapter toDoAdapter,
                        List<ToDoItem> list,
                        int position) {
        this.v = v;
        this.dialogBuilder = dialogBuilder;
        this.viewModel = viewModel;
        this.toDoAdapter = toDoAdapter;
        this.list = list;
        this.position = position;
    }

    public void delete() {

        dialogView = LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.edittodo_dialog, null);

        dialogBuilder.setView(dialogView);

        dialogBuilder.setCancelable(false);

        dialogEdit = dialogBuilder.show();

        Log.d(tag, dialogOpen);

        btnDelete = dialogView.findViewById(R.id.btndelete);
        btnNo = dialogView.findViewById(R.id.btnNo);

        btnDelete.setOnClickListener(v -> {
            Log.d(tag, delete);
            viewModel.removeToDo(list.get(position));
            toDoAdapter.notifyItemRemoved(position);
            toDoAdapter.notifyItemRangeChanged(position, 1);
            dialogEdit.dismiss();
            Log.d(tag, dialogClose);

        });

        btnNo.setOnClickListener(v -> {
            dialogEdit.dismiss();
        });

    }
}
