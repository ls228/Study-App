package de.hdmstuttgart.meinprojekt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import de.hdmstuttgart.meinprojekt.view.todo.ToDoFragment;

@RunWith(AndroidJUnit4.class)
public class ToDoFragmentTest {

    private ToDoFragment toDoFragment = new ToDoFragment();


    //View view = toDoFragment.getView();
    //RecyclerView recyclerView = toDoFragment.recyclerView;

    @Test
    public void countItems() {
        int expectedCount = 2;
        int actualCount = toDoFragment.toDoAdapter.getItemCount();

        assertThat(actualCount, is(expectedCount));
    }

}