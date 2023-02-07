package de.hdmstuttgart.meinprojekt.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.hdmstuttgart.meinprojekt.model.ToDoItem;


@Database(entities = {ToDoItem.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    private static volatile AppDatabase INSTANCE;

    static AppDatabase getDatabase(final Context context) {

        //only one Database instance is created
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "studyAppDatabase")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract ToDoDao toDoDao();

}

