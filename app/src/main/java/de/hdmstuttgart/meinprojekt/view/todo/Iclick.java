package de.hdmstuttgart.meinprojekt.view.todo;

import de.hdmstuttgart.meinprojekt.model.ToDoItem;

public interface Iclick {
    void onClickDelete(ToDoItem toDoItem, int position);
    void onChecked(int id, boolean isChecked);
}
