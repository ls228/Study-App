package de.hdmstuttgart.meinprojekt.view.todo;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import de.hdmstuttgart.meinprojekt.R;
import de.hdmstuttgart.meinprojekt.model.ToDoItem;
import de.hdmstuttgart.meinprojekt.view.home.DialogDone;
import de.hdmstuttgart.meinprojekt.view.home.HomeFragment;
import de.hdmstuttgart.meinprojekt.viewmodel.ToDoViewModel;

public class ToDoFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<ToDoItem> list = new ArrayList<>();

    private ToDoAdapter toDoAdapter;
    private ToDoViewModel viewModel;

    private DialogAdd dialogAdd;
    private DialogDelete dialogDelete;
    private DialogDone dialogDone;

    private AlertDialog.Builder dialogBuilder;

    //LiveData<Integer> countStatus;
    //LiveData<Integer> countStatusAll;
    //private int countChecked;
    //private int countAll;


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
            viewModel = new ViewModelProvider(this).get(ToDoViewModel.class);

            toDoAdapter = new ToDoAdapter(viewModel, list, (toDoItemPos, position) -> {
            });

            //countStatusAll = viewModel.getCountAll();


            //On tap opening new dialog that allows to delete the to do
            viewModel.getSavedToDos().observe(getViewLifecycleOwner(), list -> {

                if (list == null) return;
                Log.d("TESCHD", "Count: " + list.size());
                toDoAdapter = new ToDoAdapter(viewModel,
                        list,
                        (toDoItemPos, position) -> {
                            dialogDelete = new DialogDelete(view, dialogBuilder, viewModel, toDoAdapter, list, position);
                            dialogDelete.delete();
                        });
                recyclerView.setAdapter(toDoAdapter);

                int countAll = list.size();
                long countChecked = list.stream().filter(toDoItem -> toDoItem.getStatus()==1).count();
                System.out.println("tap on todo countchecked: "+ countChecked + " countall: "+countAll);
                if(countAll==countChecked){
                    dialogBuilder = new AlertDialog.Builder(getContext());
                    dialogDone = new DialogDone(getView(),dialogBuilder);
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
        } catch (NullPointerException e) {
            Log.e(TAG, "onAttach: NullPointerException: "
                    + e.getMessage());
        }

        return view;
    }

}