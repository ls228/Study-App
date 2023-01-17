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
    private final LiveData<Integer> countStatusZeroLD;
    private final LiveData<Integer> countStatusLD;
    private final LiveData<Integer> countAll;

    public ToDoRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        toDoDao = db.toDoDao();
        toDoLiveData = toDoDao.getAll();
        //status  unchecked 0 or checked 1
        countStatusZeroLD = toDoDao.getStatusUnchecked();
        countStatusLD = toDoDao.getCountStatus();
        countAll = toDoDao.getCount();
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

    public LiveData<Integer> getCountAll() {
        return countAll;
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

