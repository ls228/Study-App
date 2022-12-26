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

        //get all entries with status 0
        @Query("SELECT COUNT(*) FROM todoitem WHERE status = 0")
        LiveData<Integer> getStatusUnchecked();

        //Counting all entries with status 1
        @Query("SELECT COUNT(*) FROM todoitem WHERE status = 1")
        LiveData<Integer> getCountStatus();

        @Insert
        void insert(ToDoItem toDoItem);

        @Query("UPDATE todoitem SET status=:status WHERE uid=:id")
        void updateStatus(Integer status, Integer id);

        @Delete
        void delete(ToDoItem toDoItem);

    }
