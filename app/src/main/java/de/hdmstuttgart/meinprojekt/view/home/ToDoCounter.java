package de.hdmstuttgart.meinprojekt.view.home;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import org.junit.runner.manipulation.Ordering;

import java.security.acl.Owner;
import java.util.List;

import de.hdmstuttgart.meinprojekt.R;
import de.hdmstuttgart.meinprojekt.model.ToDoItem;
import de.hdmstuttgart.meinprojekt.viewmodel.HomeViewModel;

public class ToDoCounter {


    private final ProgressBar mProgressBarToDo;
    private final HomeViewModel viewModel;
    private final Context homeFragmentContext;


    public ToDoCounter(HomeFragment homeFragment, View view, Context context) {
        this.homeFragmentContext = context;
        mProgressBarToDo = view.findViewById(R.id.progress_bar_count_todo);
        viewModel = new ViewModelProvider(homeFragment).get(HomeViewModel.class);

    }

    private HomeViewModel getHomeViewModel() {
        if (viewModel == null) {
            throw new NullPointerException();
        }
        return viewModel;
    }

    public void progressToDos() {
        try {

            getHomeViewModel().getSavedToDos().observe((LifecycleOwner) homeFragmentContext, list -> {
                if (list == null) throw new NullPointerException();
                int countAll = list.size();
                Log.d("ToDoCounter", "all todos: " + countAll);
                mProgressBarToDo.setMax(countAll * 5);

                int countChecked = (int) list.stream().filter(toDoItem -> toDoItem.getStatus() == 1).count();

                Log.d("ToDoCounter", "checked: " + countChecked + "int: " + (int) countChecked);
                mProgressBarToDo.setProgress(countChecked);

                ObjectAnimator animation = ObjectAnimator.ofInt(mProgressBarToDo, "progress", 0, countChecked * 5);
                animation.setDuration(1500);
                animation.start();

            });
        } catch (Exception e) {
            Log.d("ToDoCounter", "Thrown exception: " + e.getMessage());
        }

        /*
        countStatusAll.observe((LifecycleOwner) homeFragment.getContext(), count -> {
            countAll = count;
            System.out.println("count all To Do's:" + countAll);

            mProgressBarToDo.setMax(countAll * 5);
            ObjectAnimator animation = ObjectAnimator.ofInt(mProgressBarToDo, "progress", 0, countChecked * 5);
            animation.setDuration(1500);
            animation.start();


        });

        countStatus.observe((LifecycleOwner) homeFragment.getContext(), list -> {
            countChecked = countStatus.getValue();
            System.out.println("checked:" + countChecked);
            mProgressBarToDo.setProgress(countChecked);

            if (countAll == countChecked) {

                HomeFragment.allTodosChecked = true;
                System.out.println("all checked");
                todosDone = true;

            } else {
                HomeFragment.allTodosChecked = false;
                todosDone = false;
                System.out.println("not all checked");
            }
            System.out.println("todos: " + todosDone);
        });
*/

    }
}
