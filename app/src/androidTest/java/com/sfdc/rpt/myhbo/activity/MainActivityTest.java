package com.sfdc.rpt.myhbo.activity;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;
import android.widget.EditText;

import com.sfdc.rpt.myhbo.R;
import com.sfdc.rpt.myhbo.util.VolleyIdlingResource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.registerIdlingResources;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.not;

/**
 * Tests for MainActivity
 *
 * Created by ahetawal on 3/12/16.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    MainActivity mMainActivity;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mMainActivity = getActivity();
        VolleyIdlingResource volleyResources;
        try {
            volleyResources = new VolleyIdlingResource("VolleyCalls");
            registerIdlingResources(volleyResources);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @After
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Integration test with OMDB api, when an invalid search is performed from the searchview.<br/>
     * 1. Click the search icon on search view<br/>
     * 2. Type incorrect search text ex. "ssdfs" and press search button on keyboard<br/>
     * 3. Verify "NO Results found" text is NOT displayed<br/>
     * 4. Verify Search List recyclerview IS displayed.
     */
    @Test
    @LargeTest
    public void testItemNotFound() {
        // Click on the search icon
        onView(withId(R.id.search)).perform(click());
        // Type the text in the search field and submit the query
        onView(isAssignableFrom(EditText.class)).perform(typeText("ssdfs"), pressImeActionButton());
        // Check the empty view is displayed
        onView(withId(R.id.noresults)).check(matches(isDisplayed()));
        onView(withId(R.id.movielist)).check(matches(not(isDisplayed())));
    }

    /**
     * Integration test with OMDB api, when a valid search is performed from the searchview.<br/>
     * 1. Click the search icon on search view<br/>
     * 2. Type incorrect search text ex. "test" and press search button on keyboard<br/>
     * 3. Verify "NO Results found" text is NOT displayed<br/>
     * 4. Verify Search List recyclerview IS displayed.
     */
    @Test
    @LargeTest
    public void testSearchResultsFound() {
        // Click on the search icon
        onView(withId(R.id.search)).perform(click());

        // Type the text in the search field and submit the query
        onView(isAssignableFrom(EditText.class)).perform(typeText("test"), pressImeActionButton());
        // Check the empty view is displayed
        onView(withId(R.id.movielist)).check(matches(isDisplayed()));
        onView(withId(R.id.noresults)).check(matches(not(isDisplayed())));
    }
}
