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
import de.hdmstuttgart.meinprojekt.view.Dialog.DialogAdd;
import de.hdmstuttgart.meinprojekt.view.Dialog.DialogDelete;
import de.hdmstuttgart.meinprojekt.view.Dialog.DialogDone;
import de.hdmstuttgart.meinprojekt.viewmodel.ViewModel;

public class ToDoFragment extends Fragment {

    public RecyclerView recyclerView;

    public ToDoAdapter toDoAdapter;
    private ViewModel viewModel;
    private TextView todoCount;


    private DialogAdd dialogAdd;
    private DialogDelete dialogDelete;
    private DialogDone dialogDone;

    private AlertDialog.Builder dialogBuilder;
    private Button checkAll;
    private int countAll;
    private long countChecked;
    boolean allChecked = false;

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
                    toDoAdapter.removeItem(position);
                    setCount();
                    Toast.makeText(getContext(), "Deleted successfully: " + toDoItem.getTitle(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onChecked(int id, boolean isChecked) {
                    Log.d(tag, "LOOOOOL ME ID: " + id + " | checked: " + isChecked);
                    viewModel.updateStatus(isChecked, id);
                    setCount();



                    if (countAll -1  == countChecked  && countAll != 0 && isChecked) {
                        doneAnimation();
                        allChecked = true;
                    }

                }
            };

            toDoAdapter = new ToDoAdapter(new ArrayList<>(), iOnClick);

            recyclerView.setAdapter(toDoAdapter);

            setCount();

            LiveData<List<ToDoItem>> toDoItems = viewModel.getSavedToDos();

            toDoItems.observe(getViewLifecycleOwner(), list -> {

                if (list == null) throw new NullPointerException();
                Log.d(tag, "Count: " + list.size());

                toDoAdapter.addList(list);

                final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(recyclerView.getContext(), R.anim.layout_animation);
                recyclerView.setLayoutAnimation(controller);
                recyclerView.scheduleLayoutAnimation();

                toDoItems.removeObservers(getViewLifecycleOwner());
            });

            //fab button
            FloatingActionButton fab = view.findViewById(R.id.fab);

            //Floating button is opening add dialog on click
            fab.setOnClickListener(v -> {
                        dialogAdd = new DialogAdd(v, dialogBuilder, addTodoItem);
                        dialogAdd.dialog();
                    }
            );



            checkAll = view.findViewById(R.id.checkAllButton);

            checkAll.setOnClickListener(v -> {
                setCount();
                toDoAdapter.checkAll();
                viewModel.statusOne();
                doneAnimation();
            });




        } catch (Exception e) {
            Log.e(tag, "onAttach: Exception: "
                    + e.getMessage());
        }

        return view;
    }


    private void setCount(){
        this.countAll = toDoAdapter.getList().size();
        this.countChecked = toDoAdapter.getList().stream().filter(ToDoItem::getStatus).count();
        long count = countChecked+1;
        if(allChecked){
            count = countChecked-1;
        }
        todoCount.setText("Your Todos: " + count + " / " + countAll);
    }

    public void doneAnimation() {
        dialogBuilder = new AlertDialog.Builder(getContext());
        dialogDone = new DialogDone(getView(), dialogBuilder);
        dialogDone.done();
    }

    private final DialogAdd.IAddTodoItem addTodoItem = new DialogAdd.IAddTodoItem() {
        @Override
        public void addTodoItem(ToDoItem toDoItem) {
            todoCount.setText("Your Todos: " + countChecked + " / " + countAll);
            toDoAdapter.addListItem(toDoItem);
            viewModel.saveToDo(toDoItem);
            recyclerView.smoothScrollToPosition(0);
        }
    };

}