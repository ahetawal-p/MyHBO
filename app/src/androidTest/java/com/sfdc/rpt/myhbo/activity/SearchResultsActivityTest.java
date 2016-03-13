package com.sfdc.rpt.myhbo.activity;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;

import com.sfdc.rpt.myhbo.R;
import com.sfdc.rpt.myhbo.util.MockDataUtil;

import org.json.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isSelected;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.not;

/**
 * SearchResults Activity Intrumentation tests
 * This activity is used for displaying search results from OMDB api in a recyclerview
 *
 * Created by ahetawal on 3/12/16.
 */
@RunWith(AndroidJUnit4.class)
public class SearchResultsActivityTest extends ActivityInstrumentationTestCase2<SearchResultsActivity> {

    SearchResultsActivity mSearchResultsActivity;

    public SearchResultsActivityTest() {
        super(SearchResultsActivity.class);
    }

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mSearchResultsActivity = getActivity();
    }

    @After
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test for verifying the search activity layout when a SUCCESSFUL response is received from api.<br/>
     * 1. Mock a Success response from omdb search api <br/>
     * 2. Verify the "No Results Found" text is NOT displayed<br/>
     * 3. Verify recyclerview is present with a row with movie name "Test Pilot" {@link com.sfdc.rpt.myhbo.util.MockDataUtil}<br/>
     * 4. Verify Title textview in the at row matches movie title<br/>
     * 5. Verify ReleaseYear textview in the at row matches movie title<br/>
     * 6. Verify Favorite imageView in the at row is displayed<br/>
     * 7. Verify Poster imageView in the at row is displayed
     */
    @Test
    public void testSearchRecyclerViewLayout() throws JSONException {
        mSearchResultsActivity.runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            mSearchResultsActivity.parseApiResponse(MockDataUtil.getMockSuccessReponse(), new HashMap<String, Boolean>());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );

        onView(withId(R.id.noresults)).check(matches(not(isDisplayed())));
        onView(withId(R.id.movielist)).perform(RecyclerViewActions.scrollTo(hasDescendant(withText("Test Pilot"))))
                .perform(RecyclerViewActions.actionOnItem(hasDescendant(withText("Test Pilot")), click()));
        onView(allOf(withId(R.id.title), withText("Test Pilot"))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.releaseYear), withParent(hasDescendant(withText("Test Pilot"))))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.tag), withParent(hasDescendant(withText("Test Pilot"))))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.file_type), withParent(withParent(hasDescendant(withText("Test Pilot")))))).check(matches(isDisplayed()));
    }

    /**
     * Test for verifying the search activity layout when a FAILURE response is received from api.<br/>
     * 1. Mock a FAILED response from omdb search api<br/>
     * 2. Verify the "No Results Found" text is displayed<br/>
     * 3. Verify recyclerview is NOT displayed
     */
    @Test
    public void testLayoutWithNoSearchResults() throws JSONException {
        mSearchResultsActivity.runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            mSearchResultsActivity.parseApiResponse(MockDataUtil.getMockNoResultsResponse(), new HashMap<String, Boolean>());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
        onView(withId(R.id.noresults)).check(matches(isDisplayed()));
        onView(withId(R.id.movielist)).check(matches(not(isDisplayed())));
    }


    /**
     * Test for verifying the RecyclerView row when an already Favorited movie is searched again, shows fav status correctly<br/>
     * 1. Mock a SUCCESS response from omdb search api<br/>
     * 2. Prepare Fav Map with an preexisting favorited movie<br/>
     * 3. Verify recycler view row exists with the favorited movie
     * 4. Verify the favorite icon is SELECTED since it shows already favorited movie status<br/>
     * 5. Verify another non favorited movie whose icon is NOT SELECTED, as it doesnt exists in the Fav map
     */
    @Test
    public void testSearchRecyclerViewLayoutWithAFavoritedMovie() throws JSONException {
        final Map<String, Boolean> favMap =  new HashMap<>();
        favMap.put("tt2184095", true);
        mSearchResultsActivity.runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            mSearchResultsActivity.parseApiResponse(MockDataUtil.getMockSuccessReponse(), favMap);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
        onView(withId(R.id.movielist)).perform(RecyclerViewActions.scrollTo(hasDescendant(withText("3 Day Test"))))
                .perform(RecyclerViewActions.actionOnItem(hasDescendant(withText("3 Day Test")), click()));
        onView(allOf(withId(R.id.tag), withParent(hasDescendant(withText("3 Day Test"))))).check(matches(isSelected()));

        onView(allOf(withId(R.id.tag), withParent(hasDescendant(withText("Test Pilot"))))).check(matches(not(isSelected())));

    }

    /**
     * Test for verifying favoriting action on movie title<br/>
     * 1. Mock a SUCCESS response from omdb search api<br/>
     * 2. Verify the movie row under test with title = "Test Pilot" is NOT favorited yet.<br/>
     * 3. Perform click operation on the Favorite icon of the movie under test<br/>
     * 4. Verify the Favorite icon is SELECTED. The movie is successfully favorited.<br/>
     */
    @Test
    public void testFavoriteClickAction() throws JSONException {
        mSearchResultsActivity.runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            mSearchResultsActivity.parseApiResponse(MockDataUtil.getMockSuccessReponse(), new HashMap<String, Boolean>());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );

        onView(allOf(withId(R.id.tag), withParent(hasDescendant(withText("Test Pilot"))))).check(matches(not(isSelected())));
        onView(allOf(withId(R.id.tag), withParent(hasDescendant(withText("Test Pilot"))))).perform(click());
        onView(allOf(withId(R.id.tag), withParent(hasDescendant(withText("Test Pilot"))))).check(matches(isSelected()));

    }

}
