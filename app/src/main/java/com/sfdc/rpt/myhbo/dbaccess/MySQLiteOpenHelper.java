package com.sfdc.rpt.myhbo.dbaccess;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.sfdc.rpt.myhbo.application.MyHBOApplication;
import com.sfdc.rpt.myhbo.provider.DataEntryColumns;


/**
 * Base DB access class for making actual DB layer calls for storing user movies
 */
public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_FILE_NAME = "favmovies.db";
    private static final int DATABASE_VERSION = 2;
    private static MySQLiteOpenHelper sInstance;
    private final Context mCxt;


    public static final String SQL_CREATE_TABLE_DATA_ENTRY = "CREATE TABLE IF NOT EXISTS "
            + DataEntryColumns.TABLE_NAME + " ( "
            + DataEntryColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DataEntryColumns.NAME + " TEXT NOT NULL, "
            + DataEntryColumns.IMDB_ID + " TEXT NOT NULL, "
            + DataEntryColumns.YEAR + " TEXT, "
            + DataEntryColumns.POSTER + " TEXT, "
            + DataEntryColumns.IS_FAVORITE + " BOOLEAN );";



    public MySQLiteOpenHelper(Context context) {
        super(context, DATABASE_FILE_NAME, null, DATABASE_VERSION);
        this.mCxt = context;
    }


    public static MySQLiteOpenHelper getInstance(Context ctx) {
        if (sInstance == null) {
            sInstance = new MySQLiteOpenHelper(ctx.getApplicationContext());
        }
        return sInstance;
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(MyHBOApplication.LOG_TAG, SQL_CREATE_TABLE_DATA_ENTRY);
        db.execSQL(SQL_CREATE_TABLE_DATA_ENTRY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
