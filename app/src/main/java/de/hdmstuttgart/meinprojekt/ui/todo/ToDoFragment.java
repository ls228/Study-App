package de.hdmstuttgart.meinprojekt.ui.todo;

import static androidx.constraintlayout.helper.widget.MotionEffect.SOUTH;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdmstuttgart.meinprojekt.R;
import de.hdmstuttgart.meinprojekt.database.Converter;
import de.hdmstuttgart.meinprojekt.model.todo.ToDoItem;

public class ToDoFragment extends Fragment{

    private RecyclerView recyclerView;
    List<ToDoItem> list = new ArrayList<>();

    private ToDoAdapter toDoAdapter;
    private String currentTime;
    private ToDoViewModel viewModel;

    private String inputTitle ="";
    private String inputTopic ="";


    private DialogAdd dialogAdd;
    private DialogDelete dialogDelete;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.fragment_todo, container, false);

            // showing todos
            recyclerView = view.findViewById(R.id.view_todolist);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());

            try {

                viewModel = new ViewModelProvider(this).get(ToDoViewModel.class);

                toDoAdapter = new ToDoAdapter(viewModel, list, (toDoItemPos, position) -> {
                });


                viewModel.getSavedToDos().observe((LifecycleOwner) getContext(), list -> {

                    if (list == null) return;
                    toDoAdapter = new ToDoAdapter(viewModel,
                            list,
                            (toDoItemPos, position) -> {
                                ToDoAdapter adapter = (ToDoAdapter) recyclerView.getAdapter();

                                dialogDelete = new DialogDelete(view,dialogBuilder,viewModel,toDoAdapter,list,position);
                                dialogDelete.delete();


                            });
                    recyclerView.setAdapter(toDoAdapter);
                });


                recyclerView.setAdapter(toDoAdapter);

                //fab button
                FloatingActionButton fab = view.findViewById(R.id.fab);

                fab.setOnClickListener(v -> {

                        dialogAdd = new DialogAdd(v,dialogBuilder, viewModel);
                        dialogAdd.dialog();


                        /*
                        if(toDoItem.getTitle().equals("")) {
                            Toast toastMessage = Toast.makeText(requireContext(), "Please enter a valid to do!", Toast.LENGTH_LONG);
                            toastMessage.show();
                        }else {
                            viewModel.saveToDo(toDoItem);
                            //attach(inputTitle, currentTime, inputTopic, 0);
                        }*/

                        }
                );
            }catch (NullPointerException e) {
            Log.e(TAG, "onAttach: NullPointerException: "
                    + e.getMessage());
        }

        return view;
    }


/*
 public void onResume(){
            super.onResume();
            toDoAdapter.notifyDataSetChanged();
        }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }*/
}