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
import de.hdmstuttgart.meinprojekt.model.todo.ToDoItem;

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
/*
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.CustomLayout, null);
        }

        //Handle TextView and display string from your list
        TextView tvContact= (TextView)view.findViewById(R.id.title);
        tvContact.setText(todoitem.get(position));

        //Handle buttons and add onClickListeners
    Button callbtn= (Button)view.findViewById(R.id.fab);

    callbtn.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            //do something

        }
    });

    addBtn.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            //do something
            notifyDataSetChanged();
            .
        }
    });

    return view;
    }*/
}


