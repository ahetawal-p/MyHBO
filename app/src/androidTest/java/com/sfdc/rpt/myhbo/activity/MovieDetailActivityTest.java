package com.sfdc.rpt.myhbo.activity;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;

import com.sfdc.rpt.myhbo.R;
import com.sfdc.rpt.myhbo.util.CustomMatchers;
import com.sfdc.rpt.myhbo.util.MockDataUtil;

import org.json.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

/**
 * MovieDetail activity instrumentation tests
 * This activity is used for displaying movie details
 *
 * Created by ahetawal on 3/12/16.
 */
public class MovieDetailActivityTest extends ActivityInstrumentationTestCase2<MovieDetailActivity> {

    MovieDetailActivity mMovieDetailActivity;

    public MovieDetailActivityTest() {
        super(MovieDetailActivity.class);
    }

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mMovieDetailActivity = getActivity();
    }

    @After
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test for verifying DETAIL UI views are displayed for a VALID response from OMDB detail api<br/>
     * 1. Prepare success mock response from OMDB detail api<br/>
     * 2. Verify CollapsingToolbarLayout title matches movie title<br/>
     * 3. Verify MoviePoster is displayed<br/>
     * 4. Verify Genre matches the "genre" response attribute value<br/>
     * 5. Verify PlotText matches the "plot" response attribute value<br/>
     * 6. Verify Director matches the "director" response attribute value<br/>
     * 7. Verify Writer matches the "writer" response attribute value<br/>
     * 8. Verify Actor matches the "actor" response attribute value
     *
     */
    @Test
    public void testMovieDetailViewLayout() throws JSONException {

        // For some reason @UIThread annotation doesnt seem to run the test on UI thread, hence
        // had to use explicit call to runOnUiThread.

        mMovieDetailActivity.runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            mMovieDetailActivity.parseApiResponseAndPopulateView(MockDataUtil.getMockDetailSuccessReponse());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );

        CharSequence title = "Rabbit Test";
        onView(isAssignableFrom(CollapsingToolbarLayout.class)).check(matches(CustomMatchers.withTitle(is(title))));
        onView(withId(R.id.moviePoster)).check(matches(isDisplayed()));
        onView(withId(R.id.genre)).check(matches(withText("Comedy")));
        onView(withId(R.id.plotText)).check(matches(withText("This is a detailed plot for the movie")));
        onView(withId(R.id.director)).check(matches(withText("Director: Joan Rivers")));
        onView(withId(R.id.writer)).check(matches(withText("Writers: Test Writer")));
        onView(withId(R.id.actor)).check(matches(withText("Actors: Actor1, Actor2")));

    }


    /**
     * Test for verifying ERROR UI views are displayed for a INVALID response from OMDB detail api<br/>
     * 1. Prepare success mock response from OMDB detail api<br/>
     * 2. Verify CollapsingToolbarLayout title matches Error message string<br/>
     * 3. Verify Genre cardview is NOT displayed<br/>
     * 4. Verify Plot cardview is NOT displayed<br/>
     * 5. Verify Movie Cast cardview is NOT displayed<br/>
     */
    @Test
    public void testFailedMovieDetailViewLayout() throws JSONException {
        mMovieDetailActivity.runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            mMovieDetailActivity.parseApiResponseAndPopulateView(MockDataUtil.getMockNoResultsResponse());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );

        CharSequence errorTitle = InstrumentationRegistry.getTargetContext().getString(R.string.errorMsg);
        onView(isAssignableFrom(CollapsingToolbarLayout.class)).check(matches(CustomMatchers.withTitle(is(errorTitle))));
        onView(withId(R.id.genreContainer)).check(matches(not(isDisplayed())));
        onView(withId(R.id.plotContainer)).check(matches(not(isDisplayed())));
        onView(withId(R.id.castContainer)).check(matches(not(isDisplayed())));

    }


}
