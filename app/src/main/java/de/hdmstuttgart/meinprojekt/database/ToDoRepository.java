package de.hdmstuttgart.meinprojekt.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;


import de.hdmstuttgart.meinprojekt.model.todo.ToDoItem;

public class ToDoRepository {
    private final ToDoDao toDoDao;

    private final List<ToDoItem> list = new ArrayList<>();
    private final LiveData<List<ToDoItem>> toDoLiveData;

    private final LiveData<Integer> countStatusZeroLD;
    private final LiveData<Integer> countStatusLD;


    //erstellt Instanz von der Datenbank
    public ToDoRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        toDoDao = db.toDoDao();
        toDoLiveData = toDoDao.getAll();
        //status  unchecked 0 or checked 1
        countStatusZeroLD = toDoDao.getStatusUnchecked();
        countStatusLD = toDoDao.getCountStatus();

    }

    public LiveData<List<ToDoItem>> getSavedToDos() {
        return toDoLiveData;
    }

    public LiveData<Integer> getCountStatusLD() {
        return countStatusLD;
    }

    public LiveData<Integer> getCountStatusUnchecked() {
        return countStatusZeroLD;
    }

    public void insert(ToDoItem toDoItem){
        AppDatabase.databaseWriteExecutor.execute(() -> toDoDao.insert(toDoItem));
    }

    public void updateStatus(Integer status, Integer id){
        AppDatabase.databaseWriteExecutor.execute(() -> toDoDao.updateStatus(status, id));
    }

    public void delete(ToDoItem toDoItem){
        AppDatabase.databaseWriteExecutor.execute(() -> toDoDao.delete(toDoItem));
    }

}

