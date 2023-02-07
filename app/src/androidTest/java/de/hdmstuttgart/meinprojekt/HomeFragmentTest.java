package de.hdmstuttgart.meinprojekt;

import static org.junit.Assert.assertEquals;
import static de.hdmstuttgart.meinprojekt.view.home.StudyTimer.mTimeLeftInMillis;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import de.hdmstuttgart.meinprojekt.view.home.HomeFragment;

@RunWith(AndroidJUnit4.class)
public class HomeFragmentTest {

    private final HomeFragment homeFragment = new HomeFragment();

    @Test
    public void testCalculateTime() {
        // Test case 1: minutes = 30, hours = 1
        long expectedTime = 5400000;
        long actualTime = homeFragment.calculateTime(30, 1);
        assertEquals(expectedTime, actualTime);

        // Test case 2: minutes = 0, hours = 2
        expectedTime = 7200000;
        actualTime = homeFragment.calculateTime(0, 2);
        assertEquals(expectedTime, actualTime);

        // Test case 3: minutes = 45, hours = 0
        expectedTime = 2700000;
        actualTime = homeFragment.calculateTime(45, 0);
        assertEquals(expectedTime, actualTime);
    }

    @Test
    public void testSaveTimerProgressBar() {
        // Test case 1: timeSet = 30, mTimeLeftInMillis = 2000
        int timeSet = 1800000;
        mTimeLeftInMillis = 2000;
        int expectedProgress = 1798000;
        int actualProgress = homeFragment.saveTimerProgressBar(timeSet);
        assertEquals(expectedProgress, actualProgress);

        // Test case 2: timeSet = 60, mTimeLeftInMillis = 0
        timeSet = 60000;
        mTimeLeftInMillis = 3000;
        expectedProgress = 57000;
        actualProgress = homeFragment.saveTimerProgressBar(timeSet);
        assertEquals(expectedProgress, actualProgress);

        // Test case 3: timeSet = 10, mTimeLeftInMillis = 1000
        timeSet = 900000;
        mTimeLeftInMillis = 300000;
        expectedProgress = 600000;
        actualProgress = homeFragment.saveTimerProgressBar(timeSet);
        assertEquals(expectedProgress, actualProgress);
    }
}


