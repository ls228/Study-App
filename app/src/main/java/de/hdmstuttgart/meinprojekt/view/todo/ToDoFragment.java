package de.hdmstuttgart.meinprojekt.view.todo;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import de.hdmstuttgart.meinprojekt.R;
import de.hdmstuttgart.meinprojekt.view.Dialog.DialogAdd;
import de.hdmstuttgart.meinprojekt.view.Dialog.DialogDelete;
import de.hdmstuttgart.meinprojekt.view.Dialog.DialogDone;
import de.hdmstuttgart.meinprojekt.viewmodel.ViewModel;

public class ToDoFragment extends Fragment {

    private RecyclerView recyclerView;

    private ToDoAdapter toDoAdapter;
    private ViewModel viewModel;

    private DialogAdd dialogAdd;
    private DialogDelete dialogDelete;
    private DialogDone dialogDone;

    private AlertDialog.Builder dialogBuilder;


    private static final String tag = "ToDoFragment";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_todo, container, false);

        // showing todos
        recyclerView = view.findViewById(R.id.view_todolist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        //enabling add and to do dialog
        dialogBuilder = new AlertDialog.Builder(getContext());

        try {
            viewModel = new ViewModelProvider(this).get(ViewModel.class);

            //On tap opening new dialog that allows to delete the to do
            viewModel.getSavedToDos().observe(getViewLifecycleOwner(), list -> {

                if (list == null) throw new NullPointerException();
                Log.d(tag, "Count: " + list.size());

                toDoAdapter = new ToDoAdapter(viewModel,
                        list,
                        (toDoItemPos, position) -> {
                            dialogDelete = new DialogDelete(view, dialogBuilder, viewModel, toDoAdapter, list, position);
                            dialogDelete.delete();
                        });
                recyclerView.setAdapter(toDoAdapter);

                int countAll = list.size();
                long countChecked = list.stream().filter(toDoItem -> toDoItem.getStatus() == 1).count();

                if (countAll == countChecked&&countAll!=0) {
                    dialogBuilder = new AlertDialog.Builder(getContext());
                    dialogDone = new DialogDone(getView(), dialogBuilder);
                    dialogDone.done();
                }
            });

            //fab button
            FloatingActionButton fab = view.findViewById(R.id.fab);

            //Floating button is opening add dialog on click
            fab.setOnClickListener(v -> {
                        dialogAdd = new DialogAdd(v, dialogBuilder, viewModel);
                        dialogAdd.dialog();
                    }
            );
        } catch (Exception e) {
            Log.e(tag, "onAttach: Exception: "
                    + e.getMessage());
        }

        return view;
    }

}