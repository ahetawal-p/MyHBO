package com.sfdc.rpt.myhbo.activity;

import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.sfdc.rpt.myhbo.R;
import com.sfdc.rpt.myhbo.adapter.MovieSearchViewAdapter;
import com.sfdc.rpt.myhbo.application.MyHBOApplication;
import com.sfdc.rpt.myhbo.dbaccess.MyAsyncQueryHandler;
import com.sfdc.rpt.myhbo.model.OMDBApiResponse;
import com.sfdc.rpt.myhbo.provider.DataEntryColumns;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Search Display Activity, used for displaying the search list recycler view from the
 * api results.
 * User can also favorite the movies on the list, and which can be accessed later as well. i.e after
 * app restart.
 *
 */
public class SearchResultsActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView noResultsText;
    private RecyclerView movieSearchView;
    private MovieSearchViewAdapter searchViewAdapter;

    private String tempQuery;
    private int totalResponseLength;
    private int initalPageNo = 1;

    private boolean shouldLoadMore = true;

    private MyAsyncQueryHandler asyncQuery;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(MyHBOApplication.LOG_TAG, "SearchResult");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        noResultsText = (TextView) findViewById(R.id.noresults);
        movieSearchView = (RecyclerView) findViewById(R.id.movielist);
        final LinearLayoutManager llm = new LinearLayoutManager(this);
        movieSearchView.setLayoutManager(llm);

        asyncQuery = new MyAsyncQueryHandler(getContentResolver());

        searchViewAdapter = new MovieSearchViewAdapter(asyncQuery, this);
        movieSearchView.setAdapter(searchViewAdapter);

        /**
         * Fix for adding an endless scrolling for the recyclerview, since the api
         * results are paginated, and for a seamless user experience while they are scrolling,
         * this fix provides a dynamic fetching of data
         *
         */
        movieSearchView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int pastVisiblesItems, visibleItemCount, totalItemCount;
                if (dy > 0) {
                    visibleItemCount = llm.getChildCount();
                    totalItemCount = llm.getItemCount();
                    pastVisiblesItems = llm.findFirstVisibleItemPosition();

                    if (searchViewAdapter.getItemCount() < totalResponseLength && shouldLoadMore) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            Log.v("...", "Last Item Wow !");
                            makeApiCall(++initalPageNo);
                        }
                    }
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        handleIntent(getIntent());

    }

    @Override
    protected void onNewIntent(Intent intent) {

        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.i(MyHBOApplication.LOG_TAG, "Searched string is : " + query);
            try {
                tempQuery = URLEncoder.encode(query.trim(), "utf-8");
            } catch (UnsupportedEncodingException e) {
                Log.e(MyHBOApplication.LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            makeApiCall(initalPageNo);
        }
    }


    private void makeApiCall(int pageNo) {

        progressBar.setVisibility(View.VISIBLE);
        shouldLoadMore = false;
        final Map<String, Boolean> favMap = loadCurrentFavMovieMap();
        // Post params to be sent to the server
        String omdb_url = getResources().getString(R.string.omdbapi_url, tempQuery, pageNo);
        Log.i(MyHBOApplication.LOG_TAG, "Search url is : " + omdb_url);

        JsonObjectRequest req = new JsonObjectRequest(omdb_url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        parseApiResponse(response, favMap);
                    }
                }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Error: ", error.toString());
                            noResultsText.setText("Something went wrong. Please try again.");
                            noResultsText.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);
                            movieSearchView.setVisibility(View.INVISIBLE);
                            shouldLoadMore = true;
                        }
                }
        );

        // add the request object to the queue to be executed
        MyHBOApplication.getInstance().addToRequestQueue(req);
    }

    /**
     * Parsing Api response, and setting up earlier favorited movie statuses
     *
     * @param response
     * @param favMap
     */

    public void parseApiResponse(JSONObject response, Map<String, Boolean> favMap) {
        try {
            List<OMDBApiResponse> searchResultsTemp = new ArrayList<>();
            Log.d("Response:%n %s", response.toString(4));
            Boolean responseStatus = response.getBoolean("Response");
            if(responseStatus) {
                totalResponseLength = Integer.parseInt(response.getString("totalResults"));
                JSONArray results = response.getJSONArray("Search");
                for (int i = 0; i < results.length(); ++i) {
                    JSONObject rec = results.getJSONObject(i);
                    OMDBApiResponse parsedObj = new OMDBApiResponse();
                    parsedObj.setImdbId(rec.getString("imdbID"));
                    parsedObj.setTitle(rec.getString("Title"));
                    parsedObj.setPosterUrl(rec.getString("Poster"));
                    parsedObj.setYear(rec.getString("Year"));
                    if(favMap.containsKey(parsedObj.getImdbId())){
                        parsedObj.setIsFavorite(true);
                    }
                    searchResultsTemp.add(parsedObj);
                }
                movieSearchView.setVisibility(View.VISIBLE);
                searchViewAdapter.updateSearchList(searchResultsTemp);
            } else {
                if(searchViewAdapter.getItemCount() < 1) {
                    noResultsText.setVisibility(View.VISIBLE);
                    movieSearchView.setVisibility(View.INVISIBLE);
                    noResultsText.setText(response.getString("Error"));
                }
            }

        } catch (JSONException e) {
            Log.e("Error: ", e.getMessage(), e);
        } finally {
            progressBar.setVisibility(View.INVISIBLE);
            shouldLoadMore = true;
        }
     }

    /**
     * Uitility method for generated a map of earlier favorited movies,
     * for optimal lookup of already favrited movies
     * @return
     */
    private Map<String, Boolean> loadCurrentFavMovieMap() {
        Map<String, Boolean> favMap = new HashMap<>();
        Cursor mCursor = getContentResolver().query(DataEntryColumns.CONTENT_URI, DataEntryColumns.ALL_COLUMNS, null, null, null);
        if (mCursor != null) {
            while (mCursor.moveToNext()) {
                String imdb_id = mCursor.getString(2);
                favMap.put(imdb_id, true);
            }
            mCursor.close();
        }
        return favMap;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(MyHBOApplication.LOG_TAG, "Clicked back");
        onBackPressed();
        return true;
    }


}
