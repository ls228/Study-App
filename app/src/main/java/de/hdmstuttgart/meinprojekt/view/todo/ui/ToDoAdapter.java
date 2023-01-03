package de.hdmstuttgart.meinprojekt.view.todo.ui;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.hdmstuttgart.meinprojekt.R;
import de.hdmstuttgart.meinprojekt.model.todo.ToDoItem;
import de.hdmstuttgart.meinprojekt.view.todo.ToDoViewModel;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder> {

    private final List<ToDoItem> todoitem;
    private ToDoViewModel viewModel;
    OnItemClickListener listener;

    /**
     * This class holds and displays the data of the recyclerview items
     */

    //exception handling
    private ToDoViewModel getToDoViewModel() {
        if (viewModel == null) {
            throw new NullPointerException();
        }
        return viewModel;
    }

    //If item is clicked, item and position will be given to onToDoClickListener
    public interface OnItemClickListener {
        void onToDoCLickListener(ToDoItem toDoItem, int position);
    }

    //item
    public ToDoAdapter(ToDoViewModel viewModel, List<ToDoItem> todoitem, OnItemClickListener listener) {
        this.viewModel = viewModel;
        this.todoitem = todoitem;
        this.listener = listener;
    }


    @NonNull
    @Override
    public ToDoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_item_layout, parent, false);

        return new ToDoViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoViewHolder holder, int position) {
        ToDoItem toDoItemPos = todoitem.get(position);
        int id = toDoItemPos.getId();

        holder.titleTextView.setText(toDoItemPos.getTitle());
        holder.dateTextView.setText(toDoItemPos.getDate());
        holder.topicTextView.setText(toDoItemPos.getTopic());

        holder.itemView.setOnClickListener(v -> listener.onToDoCLickListener(toDoItemPos, position));

        //if status 1 item has been clicked before, checkbox will be set to checked
        if (toDoItemPos.getStatus() == 1) {
            holder.checkBox.setChecked(true);
            holder.itemView.setBackgroundColor(Color.rgb(252, 236, 207));
        } else {
            holder.checkBox.setChecked(false);
        }

        //if checkbox is clicked
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {
                // The toggle is enabled
                Log.d(TAG, "onCheckedChanged: checked");
                //updating status to checked in viewmodel
                this.getToDoViewModel().updateStatus(1, id);

            } else {
                // The toggle is disabled
                Log.d(TAG, "onCheckedChanged: unchecked");
                //updating status to unchecked in viewmodel
                this.getToDoViewModel().updateStatus(0, id);

            }
        });

    }

    @Override
    public int getItemCount() {
        return todoitem.size();
    }

    //getting the ids of views and checkbox
    static class ToDoViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView dateTextView;
        TextView topicTextView;
        CheckBox checkBox;


        public ToDoViewHolder(View itemView) {

            super(itemView);

            titleTextView = itemView.findViewById(R.id.title);
            dateTextView = itemView.findViewById(R.id.date);
            topicTextView = itemView.findViewById(R.id.topic);
            checkBox = itemView.findViewById(R.id.checkbox);
        }
    }


}


