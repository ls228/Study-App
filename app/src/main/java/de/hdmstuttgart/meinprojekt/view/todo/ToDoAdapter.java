package de.hdmstuttgart.meinprojekt.view.todo;

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
import de.hdmstuttgart.meinprojekt.model.ToDoItem;
import de.hdmstuttgart.meinprojekt.viewmodel.ToDoViewModel;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder> {

    private final List<ToDoItem> todoitem;
    private final String checked = "onCheckedChanged: checked";
    private final String unchecked = "onCheckedChanged: unchecked";
    OnItemClickListener listener;
    ToDoItem toDoItemPos;
    private final ToDoViewModel viewModel;

    //item
    public ToDoAdapter(ToDoViewModel viewModel, List<ToDoItem> todoitem, OnItemClickListener listener) {
        this.viewModel = viewModel;
        this.todoitem = todoitem;
        this.listener = listener;
    }

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

    @NonNull
    @Override
    public ToDoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_item_layout, parent, false);

        return new ToDoViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoViewHolder holder, int position) {
        toDoItemPos = todoitem.get(position);
        int id = toDoItemPos.getId();

        holder.titleTextView.setText(toDoItemPos.getTitle());
        holder.dateTextView.setText(toDoItemPos.getDate());
        holder.topicTextView.setText(toDoItemPos.getTopic());

        holder.itemView.setOnClickListener(v -> listener.onToDoCLickListener(toDoItemPos, position));

        if (toDoItemPos.getStatus() == 1) {
            holder.checkBox.setChecked(true);
            holder.itemView.setBackgroundColor(Color.rgb(252, 236, 207));
        } else {
            holder.checkBox.setChecked(false);
        }

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {

                // The toggle is enabled
                Log.d("ToDoAdapter", checked);
                //updating status to checked in viewmodel
                this.getToDoViewModel().updateStatus(1, id);

            } else {
                // The toggle is disabled
                Log.d("ToDoAdapter", unchecked);
                //updating status to unchecked in viewmodel
                this.getToDoViewModel().updateStatus(0, id);

            }
        });

    }

    @Override
    public int getItemCount() {
        return todoitem.size();
    }

    //If item is clicked, item and position will be given to onToDoClickListener
    public interface OnItemClickListener {
        void onToDoCLickListener(ToDoItem toDoItem, int position);
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


