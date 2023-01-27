package de.hdmstuttgart.meinprojekt.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import de.hdmstuttgart.meinprojekt.model.ToDoItem;

@Dao
public interface ToDoDao {

    @Query("SELECT * FROM toDoItem ORDER BY uid DESC")
    LiveData<List<ToDoItem>> getAll();

    @Insert
    void insert(ToDoItem toDoItem);

    @Query("UPDATE todoitem SET status=:status WHERE uid=:id")
    void updateStatus(Integer status, Integer id);

    @Query("UPDATE todoitem SET status=1")
    void statusOne();

    @Delete
    void delete(ToDoItem toDoItem);

}
