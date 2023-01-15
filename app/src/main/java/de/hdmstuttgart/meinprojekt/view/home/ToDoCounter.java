package de.hdmstuttgart.meinprojekt.view.home;

import android.view.View;
import android.widget.ProgressBar;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import de.hdmstuttgart.meinprojekt.R;
import de.hdmstuttgart.meinprojekt.viewmodel.HomeViewModel;

public class ToDoCounter {


    private final ProgressBar mProgressBarToDo;

    LiveData<Integer> countStatus;
    LiveData<Integer> countStatusUnchecked;
    LiveData<Integer> countStatusAll;
    private int countChecked;
    private int countAll;
    public boolean todosDone = false;

    View view;
    HomeFragment homeFragment;

    private final HomeViewModel viewModel;

    public ToDoCounter(HomeFragment homeFragment, View view) {
        this.homeFragment = homeFragment;
        this.view = view;

        mProgressBarToDo = view.findViewById(R.id.progress_bar_count_todo);

        viewModel = new ViewModelProvider(homeFragment).get(HomeViewModel.class);

        countStatus = this.getHomeViewModel().getCountStatusLD();
        countStatusUnchecked = this.getHomeViewModel().getCountStatusUnchecked();
        countStatusAll = this.getHomeViewModel().getCountAll();
    }

    private HomeViewModel getHomeViewModel()
    {
        if(viewModel==null)
        {
            throw new IllegalArgumentException();
        }
        return viewModel;
    }

    public void progressToDos() {
        countStatusAll.observe((LifecycleOwner) homeFragment.getContext(), list -> {
            countAll = countStatusAll.getValue();
            System.out.println("count all To Do's:" + countAll);
            mProgressBarToDo.setMax(countAll);
        });

        countStatus.observe((LifecycleOwner) homeFragment.getContext(), list -> {
            countChecked = countStatus.getValue();
            System.out.println("checked:" + countChecked);
            mProgressBarToDo.setProgress(countChecked);

            if(countAll==countChecked){
                homeFragment.allTodosChecked=true;
                System.out.println("all checked");
                todosDone = true;

            }else{
                homeFragment.allTodosChecked=false;
                todosDone = false;
                System.out.println("not all checked");
            }
            System.out.println("todos: "+todosDone);
        });


    }
}
