package de.hdmstuttgart.meinprojekt.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import de.hdmstuttgart.meinprojekt.database.ToDoRepository;
import de.hdmstuttgart.meinprojekt.model.ToDoItem;

public class ViewModel extends AndroidViewModel {

    private final LiveData<List<ToDoItem>> toDoLiveData;

    private final ToDoRepository repository;

    /**
     * Connection between View an Model
     * retrieving LiveData from the repository
     * gives new data to the repository or is updating
     */

    public ViewModel(@NonNull Application application) {
        super(application);
        //repository is the connection to database
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

    public void statusOne(){repository.statusOne();}
}