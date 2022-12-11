package de.hdmstuttgart.meinprojekt.ui.todo;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
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

public class ToDoFragment extends Fragment {

    private FragmentTodoBinding binding;
    private RecyclerView recyclerView;
    List<ToDoItem> list = new ArrayList<>();

    private ToDoAdapter toDoAdapter;
    private TextView title;
    private TextView topic;
    private Date currentTime = Calendar.getInstance().getTime();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo, container, false);
        //View view = inflater.inflate(R.layout.fragment_todo, container, false);
        addToDo();
        // showing todos
        recyclerView = view.findViewById(R.id.view_todolist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        toDoAdapter = new ToDoAdapter(getContext(),list,list.size());
        recyclerView.setAdapter(toDoAdapter);

        //fab button
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
                View dialogView= LayoutInflater.from(view.getRootView().getContext()).inflate(R.layout.addtodo_dialog,null);

                TextView titleheading;
                TextInputLayout titleinput;
                TextView topicheading;
                TextInputLayout textInputLayout;
                Button button;

                titleheading= dialogView.findViewById(R.id.titleheading);
                titleinput = dialogView.findViewById(R.id.titleinput);

            }
        });

        return view;

    }

    private void addToDo(){
        list.add(new ToDoItem("It-Security",currentTime,"Chapter 1, Chapter 2"));
        list.add(new ToDoItem("Math",currentTime,"Chapter 1, new Assignment"));
        list.add(new ToDoItem("Mobile Application Development",currentTime,"Assignment 1, Chapter 2"));
        list.add(new ToDoItem("User interface design",currentTime,"Presentation wireframes"));
        list.add(new ToDoItem("It-Security",currentTime,"Chapter 1, Chapter 2"));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}