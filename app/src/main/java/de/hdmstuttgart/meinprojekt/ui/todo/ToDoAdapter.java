package de.hdmstuttgart.meinprojekt.ui.todo;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import de.hdmstuttgart.meinprojekt.R;
import de.hdmstuttgart.meinprojekt.model.todo.ToDoItem;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder> {

    private final List<ToDoItem> todoitem;
    private ToDoViewModel viewModel;
    OnItemClickListener listener;


    private ToDoViewModel getToDoViewModel()
    {
        if(viewModel==null)
        {
            throw new IllegalArgumentException();
        }
        return viewModel;
    }

    public interface OnItemClickListener{
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
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_item_layout,parent,false);

        return new ToDoViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoViewHolder holder, int position) {
        ToDoItem toDoItemPos = todoitem.get(position);
        int id = toDoItemPos.getId();

        holder.titleTextView.setText(toDoItemPos.getTitle());
        holder.dateTextView.setText(toDoItemPos.getDate());
        holder.topicTextView.setText(toDoItemPos.getTopic());

        holder.itemView.setOnClickListener(v -> listener.onToDoCLickListener(toDoItemPos, position)) ;

        if(toDoItemPos.getStatus()==1)
        {
            holder.checkBox.setChecked(true);
            holder.itemView.setBackgroundColor(Color.rgb(252, 236, 207));
        }
        else {
            holder.checkBox.setChecked(false);
        }

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {

            //LiveData<Integer> countStatus = this.getToDoViewModel().getCountStatusLD();
            //LiveData<Integer> countStatusUnchecked = this.getToDoViewModel().getCountStatusUnchecked();

            if (isChecked) {
                // The toggle is enabled
                Log.d(TAG, "onCheckedChanged: checked");

                this.getToDoViewModel().updateStatus(1, id);

                //System.out.println("Number of checked To Dos: " + countStatus);

            } else {
                // The toggle is disabled
                Log.d(TAG, "onCheckedChanged: unchecked");
                this.getToDoViewModel().updateStatus(0, id);
                //System.out.println("Number of unchecked To Dos: " + countStatusUnchecked);
            }
        });

    }

    @Override
    public int getItemCount() {
        return todoitem.size();
    }


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


