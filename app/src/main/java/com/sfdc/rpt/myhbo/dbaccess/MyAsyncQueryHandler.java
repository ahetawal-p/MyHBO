package com.sfdc.rpt.myhbo.dbaccess;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.sfdc.rpt.myhbo.application.MyHBOApplication;

/**
 * Created by ahetawal on 3/8/16.
 * All the below overriden methods are called on UI Thread once the query is complete
 *
 * Optimization class : For freeing up the UI thread from making direct DB calls,
 * instead using this class which uses a background thread for performing these operations.
 *
 */
public class MyAsyncQueryHandler extends AsyncQueryHandler {

    public MyAsyncQueryHandler(ContentResolver cr) {
        super(cr);
    }

    @Override
    protected void onQueryComplete(int token, Object cookie, Cursor mCursor) {
        Log.i(MyHBOApplication.LOG_TAG, "## OnQueryComplete for token ## : " + token);
        if (mCursor != null) {
            while (mCursor.moveToNext()) {
                String id = mCursor.getString(0);
                String name = mCursor.getString(1);
                Log.i(MyHBOApplication.LOG_TAG, "Entry is " + id + ": " + name);
            }
            mCursor.close();
        }
    }

    @Override
    protected void onInsertComplete(int token, Object cookie, Uri uri) {
        Log.i(MyHBOApplication.LOG_TAG, "## onInsertComplete for token ## : " + token);
        Log.i(MyHBOApplication.LOG_TAG, "Inserted URI: " + uri.toString());

    }

    @Override
    protected void onUpdateComplete(int token, Object cookie, int result) {
        Log.i(MyHBOApplication.LOG_TAG, "## onUpdateComplete for token ## : " + token);
        Log.i(MyHBOApplication.LOG_TAG, "Data Updated rows ..." + result);
    }

    @Override
    protected void onDeleteComplete(int token, Object cookie, int result) {
        Log.i(MyHBOApplication.LOG_TAG, "## onDeleteComplete for token ## : " + token);
        Log.i(MyHBOApplication.LOG_TAG, "Data Deleted number of rows ..." + result);

    }

}
