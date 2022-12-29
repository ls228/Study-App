package de.hdmstuttgart.meinprojekt.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import de.hdmstuttgart.meinprojekt.database.ToDoRepository;

public class HomeViewModel extends AndroidViewModel {

    private final LiveData<Integer> countStatusLD;
    private final LiveData<Integer> statusUnchecked;
    private final ToDoRepository repository;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        this.repository = new ToDoRepository(application);
        this.statusUnchecked = repository.getCountStatusUnchecked();
        this.countStatusLD = repository.getCountStatusLD();
    }

    public LiveData<Integer> getCountStatusLD() {
        return countStatusLD;
    }

    public LiveData<Integer> getCountStatusUnchecked() {
        return statusUnchecked;
    }
}