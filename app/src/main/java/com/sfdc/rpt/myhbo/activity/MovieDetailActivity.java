package com.sfdc.rpt.myhbo.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.sfdc.rpt.myhbo.R;
import com.sfdc.rpt.myhbo.application.MyHBOApplication;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Movie Detail Activity class, when passed in an imdb_id for a movie,
 * this activity shows the detail view of the movie, using a detail view
 * omdb api call.
 */
public class MovieDetailActivity extends AppCompatActivity {

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageView moviePoster;
    private TextView plot;
    private TextView genre;
    private TextView director;
    private TextView actor;
    private TextView writer;
    private ProgressBar progressBar;

    private CardView genreView;
    private CardView plotView;
    private CardView castView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handleIntent(getIntent());
        setContentView(R.layout.activity_movie_detail);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
       // collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        genreView = (CardView) findViewById(R.id.genreContainer);
        plotView = (CardView) findViewById(R.id.plotContainer);
        castView = (CardView) findViewById(R.id.castContainer);


        moviePoster = (ImageView) findViewById((R.id.moviePoster));
        plot = (TextView) findViewById((R.id.plotText));
        genre = (TextView) findViewById((R.id.genre));
        director = (TextView) findViewById((R.id.director));
        actor = (TextView) findViewById((R.id.actor));
        writer = (TextView) findViewById((R.id.writer));
        progressBar = (ProgressBar)findViewById(R.id.detailProgressBar);

    }

    private void handleIntent(Intent intent) {
        if (MyHBOApplication.VIEW_DETAIL_ACTION.equals(intent.getAction())) {
            String imdbId = getIntent().getStringExtra("IMDB_ID");
            // Make the detail omdb api call to get more info about the movie clicked
            makeDetailCall(imdbId);
        }
    }

    /**
     * Main method making the api call to fetch the details of the movie
     * @param imdbId
     */
    private void makeDetailCall(String imdbId) {

        String omdb_detail_url = getResources().getString(R.string.omdbapi_detail_url, imdbId);
        Log.i(MyHBOApplication.LOG_TAG, "Detail movie api url is : " + omdb_detail_url);

        JsonObjectRequest req = new JsonObjectRequest(omdb_detail_url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        parseApiResponseAndPopulateView(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error: ", error.toString());
                progressBar.setVisibility(View.INVISIBLE);
                setupErrorView();
            }
        }
        );
        // add the request object to the queue to be executed
        MyHBOApplication.getInstance().addToRequestQueue(req);

    }

    private void setupErrorView(){
        collapsingToolbarLayout.setTitle(getResources().getString(R.string.errorMsg));
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.white));
        genreView.setVisibility(View.INVISIBLE);
        plotView.setVisibility(View.INVISIBLE);
        castView.setVisibility(View.INVISIBLE);
    }
    /**
     * Parsing the JSON response and popluating the view with the data
     * @param response
     */
    public void parseApiResponseAndPopulateView(JSONObject response) {
        Log.i(MyHBOApplication.LOG_TAG, response.toString());
        try {
            Boolean responseStatus = response.getBoolean("Response");
            if(responseStatus){
                String title = response.getString("Title");
                String genreText = response.getString("Genre");
                String poster = response.getString("Poster");
                String plotText = response.getString("Plot");
                String directorName = response.getString("Director");
                String actorName = response.getString("Actors");
                String writerName = response.getString("Writer");

                collapsingToolbarLayout.setTitle(title);
                if(poster.length() < 1 || "N/A".equals(poster)){
                    moviePoster.setImageResource(R.drawable.unknow_user);
                }else {
                    Picasso.with(MyHBOApplication.getInstance().getApplicationContext()).load(poster)
                            .fit()
                            .centerInside()
                            .into(moviePoster);
                }
                plot.setText(plotText);
                genre.setText(genreText);

                // Spannable string to decorate Label different then actual values
                SpannableString actorSpan = new SpannableString("Actors: " + actorName);
                actorSpan.setSpan(new StyleSpan(Typeface.ITALIC), 0, 6, 0);
                actor.setText(actorSpan);

                SpannableString writerSpan = new SpannableString("Writers: " + writerName);
                writerSpan.setSpan(new StyleSpan(Typeface.ITALIC), 0, 7, 0);
                writer.setText(writerSpan);

                SpannableString directorSpan = new SpannableString("Director: " + directorName);
                directorSpan.setSpan(new StyleSpan(Typeface.ITALIC), 0, 8, 0);
                director.setText(directorSpan);

                moviePoster.setVisibility(View.VISIBLE);
            } else {
                setupErrorView();
            }
        } catch (JSONException e) {
            Log.e("Error: ", e.getMessage(), e);
        } finally {
            progressBar.setVisibility(View.INVISIBLE);
        }

    }
}
