package com.sfdc.rpt.myhbo.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.sfdc.rpt.myhbo.application.MyHBOApplication;
import com.sfdc.rpt.myhbo.dbaccess.MySQLiteOpenHelper;

/**
 * The Favorited movies content provider class, which provides an abstraction
 * for all the storage calls being made for storing or retrieving fav movies data.
 * It works well with the Cursor Loaders for automatic updates to the recyclerview of
 * Favorited movie lists.
 *
 */
public class FavMoviesContentProvider extends ContentProvider {

    private static final String TYPE_CURSOR_ITEM = "vnd.android.cursor.item/";
    private static final String TYPE_CURSOR_DIR = "vnd.android.cursor.dir/";

    public static final String AUTHORITY = "com.sfdc.rpt.myhbo.provider";
    public static final String CONTENT_URI_BASE = "content://" + AUTHORITY;


    private MySQLiteOpenHelper mOpenHelper;


    @Override
    public boolean onCreate() {
        mOpenHelper = MySQLiteOpenHelper.getInstance(getContext());
        return true;
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String table = uri.getLastPathSegment();

        int rowNums = mOpenHelper.getWritableDatabase().delete(table, selection, selectionArgs);

        getContext().getContentResolver().notifyChange(uri, null);
        return rowNums;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        Log.i(MyHBOApplication.LOG_TAG, "URI Passed in is " + uri.toString());

        String table = uri.getLastPathSegment();
        long rowId = mOpenHelper.getWritableDatabase().insertOrThrow(table, null, values);
        if (rowId == -1) return null;

        getContext().getContentResolver().notifyChange(uri, null);
        return uri.buildUpon().appendEncodedPath(String.valueOf(rowId)).build();

    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        String table = uri.getLastPathSegment();
        Cursor res = mOpenHelper.getReadableDatabase().query(table, projection, selection, selectionArgs, null, null, sortOrder);

        // to receive data change notifications
        res.setNotificationUri(getContext().getContentResolver(), uri);

        return res;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        String table = uri.getLastPathSegment();
        int rowNums = mOpenHelper.getWritableDatabase().update(table, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);

        return rowNums;


    }

}
