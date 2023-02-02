package de.hdmstuttgart.meinprojekt.view.todo;

import android.graphics.Color;
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

public class ToDoAdapterV2 extends ListAdapter<ToDoItem, ToDoAdapterV2.ToDoViewHolder> {

    private final static String TAG = "AddStepListAdapter";
    IOnClick iclick;

    public ToDoAdapterV2(@NonNull DiffUtil.ItemCallback<ToDoItem> diffCallback, IOnClick iclick) {
        super(diffCallback);
        this.iclick = iclick;
    }

    @NonNull
    @Override
    public ToDoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_item_layout, parent, false);
        return new ToDoAdapterV2.ToDoViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ToDoViewHolder holder, int position) {
        ToDoItem current = getItem(position);
        holder.bind(current);


        holder.deleteButton.setOnClickListener(v -> iclick.onClickDelete(current, position));
        int checkedColor = Color.rgb(252, 236, 207);

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
     * ViewHolder
     */
    static class ToDoViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView dateTextView;
        TextView topicTextView;
        CheckBox checkBox;
        Button deleteButton;

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

        @Override
        public boolean areItemsTheSame(@NonNull ToDoItem oldItem, @NonNull ToDoItem newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull ToDoItem oldItem, @NonNull ToDoItem newItem) {
            return oldItem.getId() == newItem.getId() && oldItem.getTitle().equals(newItem.getTitle());
        }
    }
}


