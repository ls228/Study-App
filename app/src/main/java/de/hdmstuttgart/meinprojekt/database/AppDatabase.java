package de.hdmstuttgart.meinprojekt.database;

import android.content.Context;

import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.hdmstuttgart.meinprojekt.model.todo.ToDoItem;
/*
@Database(entities = {ToDoItem.class}, version = 2, autoMigrations = {
        @AutoMigration(from = 1, to = 2)
})*/

@Database(entities = {ToDoItem.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase{
    public abstract ToDoDao toDoDao();

    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);


    static AppDatabase getDatabase(final Context context) {
        //es wird nur eine Datenbank Instanz erzeugt

        if(INSTANCE == null){
            synchronized (AppDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class,"todoDb")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}

