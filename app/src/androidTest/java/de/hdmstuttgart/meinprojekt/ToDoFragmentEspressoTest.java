package de.hdmstuttgart.meinprojekt;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.core.internal.deps.guava.base.Preconditions.checkNotNull;

import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
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
        public void addToDoTest() {

            onView(withId(R.id.navigation_todo))
                    .perform(click());

            onView(withId(R.id.fab))
                    .perform(click());

            onView(withId(R.id.titleinput)).perform(replaceText("study"));

            onView(withId(R.id.textInputEditText)).perform(replaceText("encryption"));

            onView(withId(R.id.btnadd))
                    .perform(click());


            //second input

            onView(withId(R.id.fab))
                    .perform(click());

            onView(withId(R.id.titleinput)).perform(replaceText("Finish group project"));

            onView(withId(R.id.textInputEditText)).perform(replaceText("Assignment 2 and 3"));

            onView(withId(R.id.btnadd))
                    .perform(click());

            onView(withId(R.id.view_todolist))
                    .perform(RecyclerViewActions.scrollToPosition(2));


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


    public static Matcher<View> hasChildCount(final int count) {
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("with child count: " + count);
            }

            @Override
            protected boolean matchesSafely(RecyclerView recyclerView) {
                RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(0);
                assert viewHolder != null;
                View view = viewHolder.itemView;
                if (!(view instanceof ViewGroup)) {
                    return false;
                }

                ViewGroup viewGroup = (ViewGroup) view;
                return viewGroup.getChildCount() == count;
            }
        };
    }



    public static Matcher<View> atPosition(final int position, @NonNull final Matcher<View> itemMatcher) {
        checkNotNull(itemMatcher);
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has item at position " + position + ": ");
                itemMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(final RecyclerView view) {
                RecyclerView.ViewHolder viewHolder = view.findViewHolderForAdapterPosition(position);
                if (viewHolder == null) {
                    // has no item on such position
                    return false;
                }
                return itemMatcher.matches(viewHolder.itemView);
            }
        };
    }


}