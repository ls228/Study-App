package de.hdmstuttgart.meinprojekt.view.todo;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import de.hdmstuttgart.meinprojekt.R;
import de.hdmstuttgart.meinprojekt.model.ToDoItem;
import de.hdmstuttgart.meinprojekt.view.dialog.DialogAdd;
import de.hdmstuttgart.meinprojekt.view.dialog.DialogDone;
import de.hdmstuttgart.meinprojekt.view.interfaces.IOnClick;
import de.hdmstuttgart.meinprojekt.viewmodel.ViewModel;

public class ToDoFragment extends Fragment {

    public RecyclerView recyclerView;
    public ToDoAdapter toDoAdapter;
    private ViewModel viewModel;
    private TextView todoCount;
    private DialogAdd dialogAdd;
    private AlertDialog.Builder dialogBuilder;
    private boolean justSwappedToFragment = true;

    public boolean allChecked = false;
    public int countAll;
    private int countChecked;

    private static final String tag = "ToDoFragment";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_todo, container, false);


        // showing todos
        recyclerView = view.findViewById(R.id.view_todolist);
        recyclerView.setBackground(new ColorDrawable(Color.TRANSPARENT));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        todoCount = view.findViewById(R.id.todocount);

        //enabling add and to do dialog
        dialogBuilder = new AlertDialog.Builder(getContext());

        try {
            viewModel = new ViewModelProvider(this).get(ViewModel.class);

            IOnClick iOnClick = new IOnClick() {
                @Override
                public void onClickDelete(ToDoItem toDoItem, int position) {
                    viewModel.removeToDo(toDoItem);
                    allChecked = false;
                }

                @Override
                public void onChecked(int id, boolean isChecked) {
                    Log.d(tag, "ID: " + id + " | checked: " + isChecked);
                    viewModel.updateStatus(isChecked, id);
                    allChecked = true;
                }

            };

            toDoAdapter = new ToDoAdapter(new ToDoAdapter.TodoDiff(), iOnClick);
            toDoAdapter.submitList(new ArrayList<>());
            recyclerView.setAdapter(toDoAdapter);
            recyclerView.setItemAnimator(null);

            setCount();

            LiveData<List<ToDoItem>> toDoItems = viewModel.getSavedToDos();

            toDoItems.observe(getViewLifecycleOwner(), list -> {

                if (list == null) throw new NullPointerException();
                Log.d(tag, "Count: " + list.size());

                toDoAdapter.submitList(list);

                if (justSwappedToFragment) {
                    justSwappedToFragment = false;
                    final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(recyclerView.getContext(), R.anim.layout_animation);
                    recyclerView.setLayoutAnimation(controller);
                    recyclerView.scheduleLayoutAnimation();
                } else {
                    if (checkAllDone(list)) {
                        doneAnimation();
                        allChecked = false;
                    }
                }
            });


        } catch (Exception e) {
            Log.e(tag, "Thrown exception: " + e.getMessage());
        }

        //fab button
        FloatingActionButton fab = view.findViewById(R.id.fab);

        //Floating button is opening add dialog on click
        fab.setOnClickListener(v -> {
                    dialogAdd = new DialogAdd(v, dialogBuilder, addTodoItem);
                    dialogAdd.dialog();
                }
        );

        //To check all todos at once
        Button checkAll = view.findViewById(R.id.checkAllButton);

        checkAll.setOnClickListener(v -> {
            viewModel.statusOne();
            if (countAll == countChecked) {
                Toast message = Toast.makeText(v.getContext(), "All To-Dos are already checked", Toast.LENGTH_LONG);
                message.show();
                Log.d(tag, allChecked + " check all clicked, all checked");
            } else {
                doneAnimation();
            }

        });

        return view;
    }

    //this Method is used to update the current count on the top
    private void setCount() {
        try {
            viewModel.getSavedToDos().observe(getViewLifecycleOwner(), list -> {
                if (list == null) throw new NullPointerException();
                this.countAll = list.size();
                Log.d(tag, "all todos: " + countAll);

                this.countChecked = (int) list.stream().filter(ToDoItem::getStatus).count();
                Log.d(tag, "checked: " + countChecked);

                todoCount.setText("Your To-Dos: " + countChecked + " / " + countAll);

            });
        } catch (Exception e) {
            Log.d(tag, "Error observing LiveData: " + e.getMessage());
        }

    }

    //this Animation shows a done icon
    public void doneAnimation() {
        dialogBuilder = new AlertDialog.Builder(getContext());
        DialogDone dialogDone = new DialogDone(getView(), dialogBuilder);
        dialogDone.done();
    }

    private final DialogAdd.IAddTodoItem addTodoItem = new DialogAdd.IAddTodoItem() {
        @Override
        public void addTodoItem(ToDoItem toDoItem) {
            viewModel.saveToDo(toDoItem);
        }
    };

    public boolean checkAllDone(List<ToDoItem> list) {
        countAll = list.size();
        countChecked = (int) list.stream().filter(ToDoItem::getStatus).count();
        return countAll == countChecked && countAll != 0 && allChecked;
    }


}