package com.sfdc.rpt.myhbo.provider;

import android.net.Uri;

/**
 * Contract Class for defining the storage schema of favorited movies.
 */
public class DataEntryColumns {

    // SQLLite storage DB table for favorited movies
    public static final String TABLE_NAME = "favmovies";

    public static final Uri CONTENT_URI = Uri.parse(FavMoviesContentProvider.CONTENT_URI_BASE + "/" + TABLE_NAME);

    /**
     * Primary key.
     */
    public static final String _ID = "_ID";


    public static final String NAME = "name";

    public static final String IMDB_ID = "imdb_id";

    public static final String YEAR = "year";

    public static final String POSTER = "poster_url";

    public static final String IS_FAVORITE = "favorited";

    public static final String[] ALL_COLUMNS = new String[] {
            _ID,
            NAME,
            IMDB_ID,
            YEAR,
            POSTER,
            IS_FAVORITE

    };



}
