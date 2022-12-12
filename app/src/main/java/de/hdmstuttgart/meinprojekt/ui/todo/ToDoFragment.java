package de.hdmstuttgart.meinprojekt.ui.todo;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdmstuttgart.meinprojekt.R;
import de.hdmstuttgart.meinprojekt.databinding.FragmentTodoBinding;
import de.hdmstuttgart.meinprojekt.model.todo.ToDoItem;

public class ToDoFragment extends Fragment{

    //private FragmentTodoBinding binding;
    private RecyclerView recyclerView;
    List<ToDoItem> list = new ArrayList<>();

    private ToDoAdapter toDoAdapter;
    private Date currentTime = Calendar.getInstance().getTime();

    private String inputTitle ="";
    private String inputTopic ="";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo, container, false);
        addToDo();
        // showing todos
        recyclerView = view.findViewById(R.id.view_todolist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        toDoAdapter = new ToDoAdapter(getContext(),list,list.size());
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

            AlertDialog test = builder.show();

                    btnCancel.setOnClickListener(
                            a -> {
                                //Log.d(TAG, "onClick: closing dialog");
                                test.dismiss();
                            });

            btnAdd.setOnClickListener(
                            a -> {
                                //Log.d(TAG, "onClick: capturing input");
                                inputTitle = titleInput.getText().toString();
                                inputTopic = topicInput.getText().toString();
                                attach(inputTitle,currentTime,inputTopic);
                                test.dismiss();
                            });
        }
        );


        return view;

    }

    private void addToDo(){
        list.add(new ToDoItem("It-Security",currentTime,"Chapter 1, Chapter 2"));
        list.add(new ToDoItem("Math",currentTime,"Chapter 1, new Assignment"));
        list.add(new ToDoItem("Mobile Application Development",currentTime,"Assignment 1, Chapter 2"));
        list.add(new ToDoItem("User interface design",currentTime,"Presentation wireframes"));
        list.add(new ToDoItem("It-Security",currentTime,"Chapter 1, Chapter 2"));

    }

    private void attach(String title,Date currentTime, String studyTopic){
        try {
            list.add(new ToDoItem(title,currentTime,studyTopic)); }
        catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ClassCastException: "
                    + e.getMessage());
        }

    }
/*
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }*/
}