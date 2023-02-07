package de.hdmstuttgart.meinprojekt.view.interfaces;

import de.hdmstuttgart.meinprojekt.model.ToDoItem;

public interface IOnClick {
    void onClickDelete(ToDoItem toDoItem, int position);

    void onChecked(int id, boolean isChecked);
}
