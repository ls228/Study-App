package de.hdmstuttgart.meinprojekt.ui.todo;

import android.app.Application;
import android.database.Cursor;


import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import de.hdmstuttgart.meinprojekt.database.ToDoDao;
import de.hdmstuttgart.meinprojekt.database.ToDoRepository;

import de.hdmstuttgart.meinprojekt.model.todo.ToDoItem;

public class ToDoViewModel extends AndroidViewModel {

    //informiert bei Veränderungen
    private final LiveData<List<ToDoItem>> toDoLiveData;

    private final ToDoRepository repository;


    public ToDoViewModel(@NonNull Application application) {
        super(application);
        //repository Schnittstelle zur Datenbank
        repository = new ToDoRepository(application);
        toDoLiveData = repository.getSavedToDos();
    }

    public LiveData<List<ToDoItem>> getSavedToDos() {
        return toDoLiveData;
    }

    public void removeToDo(ToDoItem toDoItem) {
        repository.delete(toDoItem);
    }

    public void saveToDo(ToDoItem toDoItem) {
        repository.insert(toDoItem);
    }

    public void updateStatus(Integer status, Integer id) {
        repository.updateStatus(status, id);
    }


}