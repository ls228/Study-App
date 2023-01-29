package de.hdmstuttgart.meinprojekt;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

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

        //Zeit einstellen

            onView(withId(R.id.button_start))
                    .perform(click());

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

           /* onView(withId(R.id.button_pause))
                    .perform(click());

            onView(withId(R.id.button_reset))
                    .perform(click());*/
        }
    }
