package de.hdmstuttgart.meinprojekt.view.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


import de.hdmstuttgart.meinprojekt.database.ToDoRepository;

public class HomeViewModel extends AndroidViewModel {

    private final LiveData<Integer> countStatusLD;
    private final LiveData<Integer> statusUnchecked;
    private final LiveData<Integer> countAll;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        ToDoRepository repository = new ToDoRepository(application);
        this.statusUnchecked = repository.getCountStatusUnchecked();
        this.countStatusLD = repository.getCountStatusLD();
        this.countAll = repository.getCountAll();
    }

    public LiveData<Integer> getCountStatusLD() {
        return countStatusLD;
    }

    public LiveData<Integer> getCountStatusUnchecked() {
        return statusUnchecked;
    }

    public LiveData<Integer> getCountAll() {return countAll; }


}