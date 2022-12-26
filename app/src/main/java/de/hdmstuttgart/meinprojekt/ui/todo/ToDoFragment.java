package de.hdmstuttgart.meinprojekt.ui.todo;

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

    //private FragmentTodoBinding binding;
    private RecyclerView recyclerView;
    List<ToDoItem> list = new ArrayList<>();

    private ToDoAdapter toDoAdapter;
    private String currentTime;
    private ToDoViewModel viewModel;

    private String inputTitle ="";
    private String inputTopic ="";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //try

        View view = inflater.inflate(R.layout.fragment_todo, container, false);

        // showing todos
        recyclerView = view.findViewById(R.id.view_todolist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        viewModel = new ViewModelProvider(this).get(ToDoViewModel.class);

        toDoAdapter = new ToDoAdapter(viewModel, getContext(),list,list.size(),(toDoItemPos,position) -> {});


        viewModel.getSavedToDos().observe((LifecycleOwner) getContext(), list -> {
            Log.d(TAG, "onClick: opening Edit dialog");
            if(list == null) return;
            toDoAdapter = new ToDoAdapter(viewModel, getContext(),
                    list, list.size(),
                    (toDoItemPos, position) ->{
                        ToDoAdapter adapter = (ToDoAdapter) recyclerView.getAdapter();

                        //bei Verwendung von try/catch darf die Methode
                        //zwischendrin nicht verlassen werden
                        if (adapter == null) {
                            return;
                        }

                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.edittodo_dialog, null);

                        dialogBuilder.setView(dialogView);

                        dialogBuilder.setCancelable(true);

                        AlertDialog dialogEdit = dialogBuilder.show();

                        Log.d(TAG, "onClick: opening Edit dialog success");

                        Button btnDelete = dialogView.findViewById(R.id.btndelete);

                        btnDelete.setOnClickListener(v -> {

                                    viewModel.removeToDo(list.get(position));
                                    toDoAdapter.notifyItemRemoved(position);
                                    toDoAdapter.notifyItemRangeChanged(position, 1);
                                    dialogEdit.dismiss();

                        });

                    });
            recyclerView.setAdapter(toDoAdapter);
        });


        recyclerView.setAdapter(toDoAdapter);

        //fab button
        FloatingActionButton fab = view.findViewById(R.id.fab);

        fab.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
            View dialogView = LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.addtodo_dialog,null);

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

            builder.setView(dialogView);
            builder.setCancelable(true);

            AlertDialog dialog = builder.show();

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

                                attach(inputTitle,currentTime,inputTopic,0);

                                dialog.dismiss();
                            });
        }
        );

        //catch IllegalArgumentException ..


        return view;
    }



    private void attach(String title,String currentTime, String studyTopic, Integer status){
        try {
            if(title!="") {
                ToDoItem toDoItem = new ToDoItem(title, currentTime, studyTopic, status);
                viewModel.saveToDo(toDoItem);
            }
        }
        catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ClassCastException: "
                    + e.getMessage());
        }
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