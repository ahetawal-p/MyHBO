package com.sfdc.rpt.myhbo.provider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ProviderTestCase2;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumentation test for FavMovies ContentProvider.
 * Verifies the access methods query, delete and insert for the fav movies.
 * Created by ahetawal on 3/12/16.
 */
@RunWith(AndroidJUnit4.class)
public class FavMoviesContentProviderTest extends ProviderTestCase2<FavMoviesContentProvider> {

    private ContentResolver mockResolver;

    public FavMoviesContentProviderTest() {
        super(FavMoviesContentProvider.class, FavMoviesContentProvider.AUTHORITY);
    }

    @Override
    @Before
    public void setUp() throws Exception {
        setContext(InstrumentationRegistry.getTargetContext());
        super.setUp();
        mockResolver = getMockContentResolver();
        // Cleaning up contentprovider before every test run.
        getProvider().delete(DataEntryColumns.CONTENT_URI, "1", null);
    }

    @Override
    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }


    /**
     * Test for verifying the Favorite Movie insert into the contentProvider.<br/>
     * 1. Insert a Fav movie,<br/>
     * 2. Assert URI is not null<br/>
     * 3. Query/Select * from ContentProvider<br/>
     * 4. Verify the record count is 1<br/>
     */
    @Test
    public void testFavoriteMovieInsert(){
        Uri uri = mockResolver.insert(DataEntryColumns.CONTENT_URI, createFavoriteRecord("1"));
        assertNotNull(uri);
        Cursor cursor = mockResolver.query(DataEntryColumns.CONTENT_URI, DataEntryColumns.ALL_COLUMNS, null, null, null);
        assertNotNull(cursor);
        assertEquals(1, cursor.getCount());
        cursor.close();
    }

    /**
     * Test for verifying the Favorite Movie delete aka unfav from contentProvider.<br/>
     * 1. Insert 2 Fav movie<br/>
     * 2. Delete movie with imdb_id="123_2" from ContentProvider<br/>
     * 3. Verify deleted row count is 1<br/>
     * 4. Verify the remaining record count is 1<br/>
     * 5. Verify the remaining record is imdb_id=123_1
     */
    @Test
    public void testFavoriteMovieDelete(){
        Uri uri1 = mockResolver.insert(DataEntryColumns.CONTENT_URI, createFavoriteRecord("1"));
        Uri uri2 = mockResolver.insert(DataEntryColumns.CONTENT_URI, createFavoriteRecord("2"));

        String delSelectionClause = DataEntryColumns.IMDB_ID + " = ?";
        String[] delSelectionArgs = {"123_" + "2"};

        int deletedRow = mockResolver.delete(DataEntryColumns.CONTENT_URI, delSelectionClause, delSelectionArgs);
        assertEquals(1, deletedRow);

        Cursor cursor = mockResolver.query(DataEntryColumns.CONTENT_URI, DataEntryColumns.ALL_COLUMNS, null, null, null);
        assertNotNull(cursor);
        assertEquals(1, cursor.getCount());
        cursor.moveToFirst();
        assertEquals("123_1", cursor.getString(cursor.getColumnIndex(DataEntryColumns.IMDB_ID)));
        cursor.close();
    }

    /**
     * Test for query/retrieving Favorite Movies from contentProvider.<br/>
     * 1. Insert 3 Fav movie<br/>
     * 2. Query/Select * from ContentProvider<br/>
     * 3. Verify the record count is 3<br/>
     */
    @Test
    public void testFavoriteMovieQuery(){
        Uri uri1 = mockResolver.insert(DataEntryColumns.CONTENT_URI, createFavoriteRecord("1"));
        Uri uri2 = mockResolver.insert(DataEntryColumns.CONTENT_URI, createFavoriteRecord("2"));
        Uri uri3 = mockResolver.insert(DataEntryColumns.CONTENT_URI, createFavoriteRecord("3"));

        Cursor cursor = mockResolver.query(DataEntryColumns.CONTENT_URI, DataEntryColumns.ALL_COLUMNS, null, null, null);
        assertNotNull(cursor);
        assertEquals(3, cursor.getCount());
        cursor.close();

    }


    /**
     * Helper method to generate fav movie record contentvalues
     */
    private ContentValues createFavoriteRecord(String suffix) {
        ContentValues mNewValues = new ContentValues();
        mNewValues.put(DataEntryColumns.NAME, "Test Movie_" + suffix);
        mNewValues.put(DataEntryColumns.YEAR, "2016");
        mNewValues.put(DataEntryColumns.POSTER, "http://www.nodomain.com/" + suffix);
        mNewValues.put(DataEntryColumns.IS_FAVORITE, true);
        mNewValues.put(DataEntryColumns.IMDB_ID, "123_" + suffix);
        return mNewValues;

    }
}
