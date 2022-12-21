package de.hdmstuttgart.meinprojekt.database;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import de.hdmstuttgart.meinprojekt.model.todo.ToDoItem;

@Dao
public interface ToDoDao {

        @Query("SELECT * FROM toDoItem ORDER BY date DESC")
        LiveData<List<ToDoItem>> getAll();

        @Insert
        void insert(ToDoItem toDoItem);

        @Delete
        void delete(ToDoItem toDoItem);
    }

