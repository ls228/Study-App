package de.hdmstuttgart.meinprojekt;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
    @RunWith(AndroidJUnit4.class)
    public class ToDoFragmentEspressoTest {

        @Rule
        public ActivityScenarioRule<MainActivity> activityScenarioRule
                = new ActivityScenarioRule<>(MainActivity.class);

        @Test
        public void assignment5Test() {

            onView(withId(R.id.navigation_todo))
                    .perform(click());

            onView(withId(R.id.fab))
                    .perform(click());

            onView(withId(R.id.titleinput)).perform(replaceText("study"));

            onView(withId(R.id.textInputEditText)).perform(replaceText("encryption"));


            onView(withId(R.id.btnadd))
                    .perform(click());


            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            onView(withId(R.id.checkAllButton))
                    .perform(click());


            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
}