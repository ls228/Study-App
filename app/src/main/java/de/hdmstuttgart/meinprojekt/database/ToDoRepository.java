package de.hdmstuttgart.meinprojekt.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import de.hdmstuttgart.meinprojekt.model.todo.ToDoItem;

public class ToDoRepository {
    private final ToDoDao toDoDao;

    private final List<ToDoItem> list = new ArrayList<>();
    private final LiveData<List<ToDoItem>> toDoLiveData;

    //erstellt Instanz von der Datenbank
    public ToDoRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        toDoDao = db.toDoDao();
        toDoLiveData = toDoDao.getAll();

    }

    public LiveData<List<ToDoItem>> getSavedToDos() {
        return toDoLiveData;
    }

    public void insert(ToDoItem toDoItem){
        AppDatabase.databaseWriteExecutor.execute(() -> toDoDao.insert(toDoItem));
    }

    public void delete(ToDoItem toDoItem){
        AppDatabase.databaseWriteExecutor.execute(() -> toDoDao.delete(toDoItem));
    }

}

