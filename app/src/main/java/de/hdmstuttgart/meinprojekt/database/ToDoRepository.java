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
    //private String currentTime = Calendar.getInstance().getTime().toString();

    //erstellt Instanz von der Datenbank
    public ToDoRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        toDoDao = db.toDoDao();
        toDoLiveData = toDoDao.getAll();

        /*
        list.add(new ToDoItem("It-Security",currentTime,"Chapter 1, Chapter 2"));
        list.add(new ToDoItem("Math",currentTime,"Chapter 1, new Assignment"));
        list.add(new ToDoItem("Mobile Application Development",currentTime,"Assignment 1, Chapter 2"));
        list.add(new ToDoItem("User interface design",currentTime,"Presentation wireframes"));
        list.add(new ToDoItem("It-Security",currentTime,"Chapter 1, Chapter 2"));
        */


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

