package de.hdmstuttgart.meinprojekt.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import de.hdmstuttgart.meinprojekt.database.ToDoRepository;
import de.hdmstuttgart.meinprojekt.model.ToDoItem;

public class HomeViewModel extends AndroidViewModel {

    private final LiveData<List<ToDoItem>> toDoLiveData;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        ToDoRepository repository = new ToDoRepository(application);
        this.toDoLiveData = repository.getSavedToDos();

    }

    public LiveData<List<ToDoItem>> getSavedToDos() {
        return toDoLiveData;
    }



}