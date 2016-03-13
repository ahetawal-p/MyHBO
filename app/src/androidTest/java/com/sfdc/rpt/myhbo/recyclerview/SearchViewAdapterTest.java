package com.sfdc.rpt.myhbo.recyclerview;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.InstrumentationTestCase;
import android.test.UiThreadTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.sfdc.rpt.myhbo.adapter.MovieSearchViewAdapter;
import com.sfdc.rpt.myhbo.model.OMDBApiResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

/**
 * SearchViewAdapterTest Test used for serving data to Search movie resultsrecyclerview
 * Created by ahetawal on 3/12/16.
 */
@RunWith(AndroidJUnit4.class)
public class SearchViewAdapterTest extends InstrumentationTestCase {

    private MovieSearchViewAdapter mSearchViewAdapter;

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        final Context context = getInstrumentation().getTargetContext();
        mSearchViewAdapter = new MovieSearchViewAdapter(null, context);
    }

    /**
     * Verify the correct count is present for the viewadapter records
     */
    @Test
    @SmallTest
    public void testGetItemCount() {
        mSearchViewAdapter.updateSearchList(getMockMovieResponse());
        assertEquals(1, mSearchViewAdapter.getItemCount());
    }


    /**
     * Verify the correct viewholder is returned from the onCreateViewHolder method
     */
    @Test
    @SmallTest
    @UiThreadTest
    public void testOnCreateViewHolder() {
        ViewGroup viewGroup = new RelativeLayout(getInstrumentation().getTargetContext());
        assertTrue(mSearchViewAdapter.onCreateViewHolder(viewGroup, 1) instanceof MovieSearchViewAdapter.SearchViewHolder);
    }

    /**
     * Utility for mock data for the adapter store
     */
    private List<OMDBApiResponse> getMockMovieResponse() {
        final List<OMDBApiResponse> listViewObjects = new ArrayList<>();
        OMDBApiResponse testObj1 = new OMDBApiResponse();
        testObj1.setTitle("Test1");
        testObj1.setYear("2016");
        testObj1.setPosterUrl("http://ia.media-imdb.com/images/M/MV5BYWFlMTE2MGUtNDBlYy00YWI3LWJiZWMtOGY2OWFjZTVmNjU1XkEyXkFqcGdeQXVyMTEwNTQxMjQ@._V1_SX300.jpg");
        testObj1.setIsFavorite(false);
        testObj1.setImdbId("123_1");
        listViewObjects.add(testObj1);
        return listViewObjects;
    }


}
