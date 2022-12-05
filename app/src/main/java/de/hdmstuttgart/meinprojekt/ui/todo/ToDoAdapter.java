package de.hdmstuttgart.meinprojekt.ui.todo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import de.hdmstuttgart.meinprojekt.R;
import de.hdmstuttgart.meinprojekt.model.ToDoItem;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder> {

    private final List<ToDoItem> todoitem;
    private Context context;
    //item
    public ToDoAdapter(List<ToDoItem> todoitem, Context context) {

        this.todoitem=todoitem;
        this.context = context;
    }

    public ToDoAdapter(){
        this.todoitem = new ArrayList<>();
    }

    @NonNull
    @Override
    public ToDoAdapter.ToDoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoAdapter.ToDoViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return todoitem.size();
    }


    static class ToDoViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView dateTextView;
        TextView actorTextView;


        public ToDoViewHolder(View itemView) {

            super(itemView);

            titleTextView = itemView.findViewById(R.id.title);
            dateTextView = itemView.findViewById(R.id.date);
            actorTextView = itemView.findViewById(R.id.topic);
        }
    }
}
