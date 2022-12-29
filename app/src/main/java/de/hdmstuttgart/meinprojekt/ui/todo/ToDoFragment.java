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


    DialogAdd dialogAdd;



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


                                View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.edittodo_dialog, null);

                                dialogBuilder.setView(dialogView);

                                dialogBuilder.setCancelable(true);

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
/*

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

                                        if (inputTitle.equals("")) {
                                            Toast toastMessage = Toast.makeText(requireContext(), "Please enter a valid to do!", Toast.LENGTH_LONG);
                                            toastMessage.show();
                                        } else {
                                            ToDoItem toDoItem = new ToDoItem(inputTitle, currentTime, inputTopic, 0);
                                            viewModel.saveToDo(toDoItem);
                                            dialog.dismiss();
                                        }

                                    });*/
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