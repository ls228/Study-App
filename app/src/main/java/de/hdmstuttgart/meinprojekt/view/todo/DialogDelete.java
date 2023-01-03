package de.hdmstuttgart.meinprojekt.view.todo;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import java.util.List;

import de.hdmstuttgart.meinprojekt.R;
import de.hdmstuttgart.meinprojekt.model.todo.ToDoItem;

public class DialogDelete {
    private View v;
    private AlertDialog.Builder dialogBuilder;
    private ToDoViewModel viewModel;
    private AlertDialog dialogEdit;
    private ToDoAdapter toDoAdapter;
    private List<ToDoItem> list;
    int position;

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

        View dialogView = LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.edittodo_dialog, null);

        dialogBuilder.setView(dialogView);

        dialogBuilder.setCancelable(false);

        AlertDialog dialogEdit = dialogBuilder.show();

        Log.d(TAG, "onClick: opening Edit dialog success");

        Button btnDelete = dialogView.findViewById(R.id.btndelete);
        Button btnNo = dialogView.findViewById(R.id.btnNo);

        btnDelete.setOnClickListener(v -> {

            viewModel.removeToDo(list.get(position));
            toDoAdapter.notifyItemRemoved(position);
            toDoAdapter.notifyItemRangeChanged(position, 1);
            dialogEdit.dismiss();

        });

        btnNo.setOnClickListener(v -> {
            dialogEdit.dismiss();
        });

    }
}
