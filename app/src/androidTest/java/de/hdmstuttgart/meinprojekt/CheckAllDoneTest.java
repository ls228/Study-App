
package de.hdmstuttgart.meinprojekt;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import de.hdmstuttgart.meinprojekt.model.ToDoItem;
import de.hdmstuttgart.meinprojekt.view.todo.ToDoFragment;

public class CheckAllDoneTest {

    ToDoFragment toDoFragment = new ToDoFragment();

    private final ToDoItem item1 = new ToDoItem("Learn IT-Security","02.02.2023","All summaries",true);
    private final ToDoItem item2 = new ToDoItem("Learn Web Development","02.03.2023","Chapter 1 and 2",false);
    private final ToDoItem item3 = new ToDoItem("Learn IT-Security","05.02.2023","Chapter 1 and 5",true);

    @Test
    public void testCheckAllDone_AllChecked() {
        toDoFragment.allChecked=true;
        List<ToDoItem> list = new ArrayList<>();
        list.add(item1);
        list.add(item3);

        boolean result = toDoFragment.checkAllDone(list);
        assertTrue(result);
    }

    @Test
    public void testCheckAllDone_NotAllChecked() {
        toDoFragment.allChecked=false;
        List<ToDoItem> list = new ArrayList<>();
        list.add(item1);
        list.add(item2);
        list.add(item3);

        boolean result = toDoFragment.checkAllDone(list);
        assertFalse(result);
    }

    @Test
    public void testCheckAllDone_EmptyList() {
        toDoFragment.allChecked=false;
        List<ToDoItem> list = new ArrayList<>();

        boolean result = toDoFragment.checkAllDone(list);
        assertFalse(result);
    }
}

