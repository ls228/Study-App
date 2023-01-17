package de.hdmstuttgart.meinprojekt.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import de.hdmstuttgart.meinprojekt.model.ToDoItem;

public class ToDoRepository {

    /**
     * This class gets data from ToDoDao or is inserting new data
     */

    private final ToDoDao toDoDao;
    private final LiveData<List<ToDoItem>> toDoLiveData;

    public ToDoRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        toDoDao = db.toDoDao();
        toDoLiveData = toDoDao.getAll();
    }

    public LiveData<List<ToDoItem>> getSavedToDos() {
        return toDoLiveData;
    }

    public void insert(ToDoItem toDoItem) {
        AppDatabase.databaseWriteExecutor.execute(() -> toDoDao.insert(toDoItem));
    }

    public void updateStatus(Integer status, Integer id) {
        AppDatabase.databaseWriteExecutor.execute(() -> toDoDao.updateStatus(status, id));
    }

    public void delete(ToDoItem toDoItem) {
        AppDatabase.databaseWriteExecutor.execute(() -> toDoDao.delete(toDoItem));
    }

}

