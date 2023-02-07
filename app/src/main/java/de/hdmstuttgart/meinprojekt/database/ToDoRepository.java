package de.hdmstuttgart.meinprojekt.database;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import de.hdmstuttgart.meinprojekt.model.ToDoItem;

public class ToDoRepository {

    /**
     * This class gets data from ToDoDao or is inserting new data
     */

    private final ToDoDao toDoDao;


    public ToDoRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        toDoDao = db.toDoDao();

    }

    public LiveData<List<ToDoItem>> getSavedToDos() {
        return toDoDao.getAll();
    }


    public void insert(ToDoItem toDoItem) {
        AppDatabase.databaseWriteExecutor.execute(() -> toDoDao.insert(toDoItem));
    }

    public void updateStatus(boolean status, int id) {
        AppDatabase.databaseWriteExecutor.execute(() -> toDoDao.updateStatus(status, id));
    }

    public void statusOne() {
        AppDatabase.databaseWriteExecutor.execute(() -> toDoDao.statusOne());
    }

    public void delete(ToDoItem toDoItem) {
        AppDatabase.databaseWriteExecutor.execute(() -> toDoDao.delete(toDoItem));
    }

}

