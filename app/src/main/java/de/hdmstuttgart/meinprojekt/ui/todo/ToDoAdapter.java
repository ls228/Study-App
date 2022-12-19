package de.hdmstuttgart.meinprojekt.ui.todo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
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

    }

    @Override
    public int getItemCount() {
        return todoitem.size();
    }

    public int OnCheckboxClicked(View view, int count, int position){

        boolean checked = ((CheckBox) view).isChecked();

        return count++;
    }
/*
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {

            viewHolder = new ViewHolder();

            viewHolder.checkedTextView = convertView.findViewById(R.id.checkbox);
            viewHolder.checkedTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewHolder.checkedTextView.setChecked(!viewHolder.checkedTextView.isChecked());
                }
            });
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //viewHolder.checkedTextView.set(todoitem.get(position));

        return convertView;
}
*/
    static class ToDoViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView dateTextView;
        TextView topicTextView;


        public ToDoViewHolder(View itemView) {

            super(itemView);

            titleTextView = itemView.findViewById(R.id.title);
            dateTextView = itemView.findViewById(R.id.date);
            topicTextView = itemView.findViewById(R.id.topic);
        }
    }

    private static class ViewHolder {
        CheckedTextView checkedTextView;
    }


}


