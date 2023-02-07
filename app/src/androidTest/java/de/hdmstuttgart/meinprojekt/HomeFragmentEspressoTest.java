package de.hdmstuttgart.meinprojekt;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.view.View;
import android.widget.NumberPicker;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.matcher.ViewMatchers;
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
    public class HomeFragmentEspressoTest {

        @Rule
        public ActivityScenarioRule<MainActivity> activityScenarioRule
                = new ActivityScenarioRule<>(MainActivity.class);


        @Test
        public void timerTest() {

            onView(withId(R.id.number_picker_min)).perform(setNumber(1));
            onView(withId(R.id.number_picker_min))
                    .check(matches(withNumberPickerValue(1)));

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            onView(withId(R.id.button_start))
                    .perform(click());

            onView(withId(R.id.text_view_countdown)).check(matches(withText("00:59")));

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            onView(withId(R.id.text_view_countdown)).check(matches(withText("00:56")));

            onView(withId(R.id.button_pause))
                    .perform(click());

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            onView(withId(R.id.button_reset))
                    .perform(click());

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Test
        public void secondTimerTest() {

            onView(withId(R.id.number_picker_h)).perform(setNumber(1));
            onView(withId(R.id.number_picker_min)).perform(setNumber(10));
            onView(withId(R.id.number_picker_h))
                    .check(matches(withNumberPickerValue(1)));
            onView(withId(R.id.number_picker_min))
                    .check(matches(withNumberPickerValue(10)));


            onView(withId(R.id.button_start))
                    .perform(click());

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            onView(withId(R.id.button_pause))
                    .perform(click());

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            onView(withId(R.id.button_reset))
                    .perform(click());

            onView(withId(R.id.number_picker_h))
                    .check(matches(withNumberPickerValue(0)));

            onView(withId(R.id.number_picker_min))
                    .check(matches(withNumberPickerValue(0)));

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }



        public static Matcher<View> withNumberPickerValue(final int expectedValue) {
            return new TypeSafeMatcher<View>() {

                @Override
                public boolean matchesSafely(View view) {
                    if (!(view instanceof NumberPicker)) {
                        return false;
                    }

                    NumberPicker numberPicker = (NumberPicker) view;
                    return numberPicker.getValue() == expectedValue;
                }

                @Override
                public void describeTo(Description description) {
                    description.appendText("with number picker value: " + expectedValue);
                }
            };
        }


        public static ViewAction setNumber(final int number) {
            return new ViewAction() {
                @Override
                public void perform(UiController uiController, View view) {
                    NumberPicker numberPicker = (NumberPicker) view;
                    numberPicker.setValue(number);
                }

                @Override
                public String getDescription() {
                    return "Set the value of a NumberPicker";
                }

                @Override
                public Matcher<View> getConstraints() {
                    return ViewMatchers.isAssignableFrom(NumberPicker.class);
                }
            };
        }
    }


