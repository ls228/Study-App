package de.hdmstuttgart.meinprojekt.ui.todo;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import de.hdmstuttgart.meinprojekt.R;
import de.hdmstuttgart.meinprojekt.model.todo.ToDoItem;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder> {

    private final List<ToDoItem> todoitem;
    private Context context;
    private int size;
    OnItemClickListener listener;
    public int countToDosChecked = 0;

    public interface OnItemClickListener{
        void onToDoCLickListener(ToDoItem toDoItem, int position);
    }
    //item
    public ToDoAdapter(Context context, List<ToDoItem> todoitem, int size, OnItemClickListener listener) {
        this.context = context;
        this.todoitem = todoitem;
        this.size = size;
        this.listener = listener;
    }



    public ToDoAdapter(){
        this.todoitem = new ArrayList<>();
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

        holder.titleTextView.setText(toDoItemPos.getTitle());
        holder.dateTextView.setText(toDoItemPos.getDate());
        holder.topicTextView.setText(toDoItemPos.getTopic());

        holder.itemView.setOnClickListener(v -> listener.onToDoCLickListener(toDoItemPos, position)) ;

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // The toggle is enabled
                Log.d(TAG, "onCheckedChanged: checked");
                OnCheckboxClicked();
                countToDosChecked++;
                System.out.println("Number of To Dos: " + countToDosChecked);

            } else {
                // The toggle is disabled
                Log.d(TAG, "onCheckedChanged: unchecked");
                OnCheckboxClicked();
                countToDosChecked--;
                System.out.println("Number of To Dos: " + countToDosChecked);
            }
        });

    }

    @Override
    public int getItemCount() {
        return todoitem.size();
    }

    public void OnCheckboxClicked(){

 //       boolean checked = ((CheckBox) view).isChecked();

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


