package de.hdmstuttgart.meinprojekt.view.todo;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import de.hdmstuttgart.meinprojekt.R;
import de.hdmstuttgart.meinprojekt.model.ToDoItem;
import de.hdmstuttgart.meinprojekt.view.interfaces.IOnClick;

public class ToDoAdapter extends ListAdapter<ToDoItem, ToDoAdapter.ToDoViewHolder> {

    /**
     * This class holds and displays the data of the recyclerview items
     */

    IOnClick iclick;

    //DiffUtil for better performance
    public ToDoAdapter(@NonNull DiffUtil.ItemCallback<ToDoItem> diffCallback, IOnClick iclick) {
        super(diffCallback);
        this.iclick = iclick;
    }

    @NonNull
    @Override
    public ToDoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_item_layout, parent, false);
        return new ToDoAdapter.ToDoViewHolder(rootView);
    }

    //allows access to one item in the recyclerView
    @Override
    public void onBindViewHolder(ToDoViewHolder holder, int position) {
        ToDoItem current = getItem(position);
        holder.bind(current);

        holder.deleteButton.setOnClickListener(v -> iclick.onClickDelete(current, position));

        //setting a different color if an items are checked
        int checkedColor = Color.rgb(252, 236, 207);

        Log.d("ToDoAdapter", "Title/ Status: " + current.getTitle() + " | " + current.getStatus());

        holder.itemView.setBackgroundColor(current.getStatus() ? checkedColor : Color.WHITE);

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            iclick.onChecked(current.getId(), isChecked);
            if (isChecked) {
                holder.itemView.setBackgroundColor(checkedColor);
            } else {
                holder.itemView.setBackgroundColor(Color.WHITE);
            }
        });

    }


    /**
     * ViewHolder class describes an item view and metadata about its place within the RecyclerView
     */
    static class ToDoViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView dateTextView;
        TextView topicTextView;
        CheckBox checkBox;
        Button deleteButton;

        //getting access to the views by their id
        public ToDoViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.title);
            dateTextView = itemView.findViewById(R.id.date);
            topicTextView = itemView.findViewById(R.id.topic);
            checkBox = itemView.findViewById(R.id.checkbox);
            deleteButton = itemView.findViewById(R.id.btndelete);
        }

        public void bind(ToDoItem current) {
            titleTextView.setText(current.getTitle());
            dateTextView.setText(current.getDate());
            topicTextView.setText(current.getTopic());
            checkBox.setOnCheckedChangeListener(null);
            checkBox.setChecked(current.getStatus());
        }
    }

    /**
     * DiffUtil Callback
     */
    public static class TodoDiff extends DiffUtil.ItemCallback<ToDoItem> {

        //Called to check whether two objects represent the same item.
        @Override
        public boolean areItemsTheSame(@NonNull ToDoItem oldItem, @NonNull ToDoItem newItem) {
            return oldItem == newItem;
        }

        //Called to check whether two items have the same data and to detect if the contents of an item have changed
        @Override
        public boolean areContentsTheSame(@NonNull ToDoItem oldItem, @NonNull ToDoItem newItem) {
            return oldItem.getId() == newItem.getId() && oldItem.getTitle().equals(newItem.getTitle());
        }
    }
}


