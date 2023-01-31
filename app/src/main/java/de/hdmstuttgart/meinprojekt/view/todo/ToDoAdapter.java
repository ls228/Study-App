package de.hdmstuttgart.meinprojekt.view.todo;


import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.hdmstuttgart.meinprojekt.R;
import de.hdmstuttgart.meinprojekt.model.ToDoItem;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder> {

    private final List<ToDoItem> toDoItems;
    IOnClick listener;
    private static final String tag = "ToDoAdapter";
    private boolean allChecked = false;

    //item
    public ToDoAdapter(List<ToDoItem> todoitem, IOnClick listener) {
        this.toDoItems = todoitem;
        this.listener = listener;
    }

    public void addList(List<ToDoItem> toDoItems) {
        this.toDoItems.addAll(toDoItems);
        notifyItemRangeInserted(0, toDoItems.size() - 1);
        notifyItemRangeChanged(0, toDoItems.size());
    }

    public void checkAll() {

        for (ToDoItem item : toDoItems) {
            item.setStatus(true);
        }
        allChecked = true;
        notifyItemRangeChanged(0, getItemCount());
        notifyItemRangeChanged(0, getItemCount());
    }

    public void removeItem(int position) {

        toDoItems.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }

    public void addListItem(ToDoItem toDoItem) {
        toDoItems.add(0, toDoItem);
        notifyItemInserted(0);
        notifyItemRangeChanged(0, getItemCount());
    }

    public List<ToDoItem> getList() {
        return toDoItems;
    }


    /**
     * This class holds and displays the data of the recyclerview items
     */

    @NonNull
    @Override
    public ToDoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_item_layout, parent, false);

        return new ToDoViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoViewHolder holder, int position) {
        ToDoItem toDoItem = toDoItems.get(position);
        int id = toDoItem.getId();

        holder.titleTextView.setText(toDoItem.getTitle());
        holder.dateTextView.setText(toDoItem.getDate());
        holder.topicTextView.setText(toDoItem.getTopic());
        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(toDoItem.getStatus());
        Log.d(tag, "Test: " + toDoItem.getTitle() + " | " + toDoItem.getStatus());
        holder.deleteButton.setOnClickListener(v -> listener.onClickDelete(toDoItem, position));
        int checkedColor = Color.rgb(252, 236, 207);

        holder.itemView.setBackgroundColor(toDoItem.getStatus() ? checkedColor : Color.WHITE);

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                listener.onChecked(id, isChecked);
                toDoItem.setStatus(isChecked);
                if (isChecked) {
                    holder.itemView.setBackgroundColor(checkedColor);
                } else {
                    holder.itemView.setBackgroundColor(Color.WHITE);
                    allChecked = false;
                }
            }
        });

        if(allChecked){
            listener.onChecked(id, true);
            toDoItem.setStatus(true);
        }
    }

    @Override
    public int getItemCount() {
        return toDoItems.size();
    }

    //getting the ids of views and checkbox
    static class ToDoViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView dateTextView;
        TextView topicTextView;
        CheckBox checkBox;
        Button deleteButton;


        public ToDoViewHolder(View itemView) {

            super(itemView);

            titleTextView = itemView.findViewById(R.id.title);
            dateTextView = itemView.findViewById(R.id.date);
            topicTextView = itemView.findViewById(R.id.topic);
            checkBox = itemView.findViewById(R.id.checkbox);
            deleteButton = itemView.findViewById(R.id.btndelete);

        }
    }


}


