package com.sfdc.rpt.myhbo.activity;

import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

import com.sfdc.rpt.myhbo.R;
import com.sfdc.rpt.myhbo.adapter.MovieFavViewAdapter;
import com.sfdc.rpt.myhbo.application.MyHBOApplication;
import com.sfdc.rpt.myhbo.dbaccess.MyAsyncQueryHandler;
import com.sfdc.rpt.myhbo.provider.DataEntryColumns;

/**
 * Launcher Activity class. It hosts the search view to search the OMDB for the movies
 * Also displays the list of user favorited movies in the recyclerview.
 * Implements Cursor Loader for automatic loading of new favorited movies from search screen to the
 * fav list.
 */
public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private RecyclerView movieFavList;
    private MovieFavViewAdapter favViewAdapter;
    public static final int DATA_LOADER_ID = 1;
    private MyAsyncQueryHandler asyncQuery;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i(MyHBOApplication.LOG_TAG, "MainActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        asyncQuery = new MyAsyncQueryHandler(getContentResolver());

        movieFavList = (RecyclerView) findViewById(R.id.favlist);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        movieFavList.setLayoutManager(llm);
        favViewAdapter = new MovieFavViewAdapter(new MovieFavViewAdapter.ClickActions() {
            @Override
            public void showDetailView(String imdb_id) {
                Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);
                intent.setAction(MyHBOApplication.VIEW_DETAIL_ACTION);
                intent.putExtra("IMDB_ID", imdb_id);
                startActivity(intent);
            }

            @Override
            public void doUnFav(String imdb_id) {
                String delSelectionClause = DataEntryColumns.IMDB_ID + " = ?";
                String[] delSelectionArgs = {imdb_id};
                asyncQuery.startDelete(MyHBOApplication.ASYNC_DELETE_TOKEN, null, DataEntryColumns.CONTENT_URI, delSelectionClause, delSelectionArgs);

            }
        });
        movieFavList.setAdapter(favViewAdapter);

        // Setup the loader
        getLoaderManager().initLoader(DATA_LOADER_ID, null, this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.i(MyHBOApplication.LOG_TAG, "Creating loader with id: " + id);
        switch (id){
            case DATA_LOADER_ID :
                return new CursorLoader(this, DataEntryColumns.CONTENT_URI, DataEntryColumns.ALL_COLUMNS,null,null,null);

            default :
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.i(MyHBOApplication.LOG_TAG, "Loader load finished...");
        if (favViewAdapter != null) {
            favViewAdapter.swapCursor(data);
        }


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.i(MyHBOApplication.LOG_TAG, "Loader Reset...");
        if (favViewAdapter != null) {
            favViewAdapter.swapCursor(null);
        }
    }
}
