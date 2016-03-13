package com.sfdc.rpt.myhbo.recyclerview;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.InstrumentationTestCase;
import android.test.UiThreadTest;
import android.test.mock.MockCursor;
import android.test.suitebuilder.annotation.SmallTest;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.sfdc.rpt.myhbo.adapter.MovieFavViewAdapter;
import com.sfdc.rpt.myhbo.adapter.MovieSearchViewAdapter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * FavViewAdapter Test used for serving data to Favorite movie recyclerview
 * Created by ahetawal on 3/12/16.
 */
@RunWith(AndroidJUnit4.class)
public class FavViewAdapterTest extends InstrumentationTestCase {

    private MovieFavViewAdapter mFavViewAdapter;

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        final Context context = getInstrumentation().getTargetContext();
        mFavViewAdapter = new MovieFavViewAdapter(null);
    }

    /**
     * Verify the correct count is present for the viewadapter records
     */
    @Test
    @SmallTest
    public void testGetItemCount() {
        mFavViewAdapter.swapCursor(new MyMockCursor());
        assertEquals(1, mFavViewAdapter.getItemCount());
    }

    /**
     * Verify the correct viewholder is returned from the onCreateViewHolder method
     */
    @Test
    @SmallTest
    @UiThreadTest
    public void testOnCreateViewHolder() {
        ViewGroup viewGroup = new RelativeLayout(getInstrumentation().getTargetContext());
        assertTrue(mFavViewAdapter.onCreateViewHolder(viewGroup, 1) instanceof MovieSearchViewAdapter.SearchViewHolder);
    }

    /**
     * Mock cursor to be used witht the fav adapter
     */
    private class MyMockCursor extends MockCursor {

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public boolean moveToNext() {
            return true;
        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer){

        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer){

        }

    }



}
