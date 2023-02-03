package de.hdmstuttgart.meinprojekt;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.core.internal.deps.guava.base.Preconditions.checkNotNull;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static java.util.regex.Pattern.matches;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.ViewAssertion;


import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.espresso.util.HumanReadables;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.hdmstuttgart.meinprojekt.view.todo.ToDoFragment;

@LargeTest
    @RunWith(AndroidJUnit4.class)
    public class ToDoFragmentEspressoTest {

    //private ToDoFragment toDoFragment = new ToDoFragment();

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


            //onView(withId(R.id.title)).check(matches(atPosition(0, hasDescendant(withText("study")))));

            //onView(withId(R.id.title)).check(matches(withText("study")));


            //second input

            onView(withId(R.id.fab))
                    .perform(click());

            onView(withId(R.id.titleinput)).perform(replaceText("Finish group project"));

            onView(withId(R.id.textInputEditText)).perform(replaceText("Assignment 2 and 3"));

            onView(withId(R.id.btnadd))
                    .perform(click());

            onView(withId(R.id.view_todolist))
                    .perform(RecyclerViewActions.scrollToPosition(2));

/*
            onView(withId(R.id.view_todolist))
                    .check(matches(hasChildCount(8)));

            onView(withId(R.id.view_todolist))
                    .perform(RecyclerViewActions.scrollToPosition(0))
                    .check(matches(hasDescendant(withId(R.id.title))));

            onView(withId(R.id.title)).check(matches(withText("Finish group project")));

            onView(withId(R.id.view_todolist))
                    .perform(RecyclerViewActions.scrollToPosition(0))
                    .check(matches(atPosition(0, hasDescendant(withId(R.id.topic)))));

            onView(withId(R.id.topic)).check(matches(withText("Assignment 2 and 3")));*/


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
/*
    @Test
    public void countItems() {
        int expectedCount = 2;
        int actualCount = toDoFragment.countAll;
        assertThat(actualCount, is(expectedCount));
    }*/

    public static Matcher<View> hasChildCount(final int count) {
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("with child count: " + count);
            }

            @Override
            protected boolean matchesSafely(RecyclerView recyclerView) {
                RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(0);
                View view = viewHolder.itemView;
                if (!(view instanceof ViewGroup)) {
                    return false;
                }

                ViewGroup viewGroup = (ViewGroup) view;
                return viewGroup.getChildCount() == count;
            }
        };
    }



    private ViewAssertion matches(final Matcher<View> matcher) {
        return new ViewAssertion() {
            @Override
            public void check(View view, NoMatchingViewException noViewFoundException) {
                if (!(view instanceof RecyclerView)) {
                    throw noViewFoundException;
                }

                RecyclerView recyclerView = (RecyclerView) view;
                RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(0);
                if (viewHolder == null) {
                    throw new PerformException.Builder()
                            .withActionDescription("scroll to position: 0")
                            .withViewDescription(HumanReadables.describe(view))
                            .withCause(new IllegalStateException("No view holder at position: 0"))
                            .build();
                }
                Matcher<View> holderMatcher = new TypeSafeMatcher<View>() {
                    @Override
                    protected boolean matchesSafely(View item) {
                        return matcher.matches(item);
                    }

                    @Override
                    public void describeTo(Description description) {
                        description.appendText("View holder at position: 0 ");
                        matcher.describeTo(description);
                    }
                };
                assertThat(viewHolder.itemView, holderMatcher);
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