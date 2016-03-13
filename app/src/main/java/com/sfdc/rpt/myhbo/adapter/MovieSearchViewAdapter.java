package com.sfdc.rpt.myhbo.adapter;

import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sfdc.rpt.myhbo.R;
import com.sfdc.rpt.myhbo.activity.MovieDetailActivity;
import com.sfdc.rpt.myhbo.application.MyHBOApplication;
import com.sfdc.rpt.myhbo.model.OMDBApiResponse;
import com.sfdc.rpt.myhbo.provider.DataEntryColumns;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahetawal on 3/7/16.
 */
public class MovieSearchViewAdapter extends RecyclerView.Adapter<MovieSearchViewAdapter.SearchViewHolder>{

    private List<OMDBApiResponse> resultlist = new ArrayList<>();
    private AsyncQueryHandler asyncQuery;
    private Context activity;

    public MovieSearchViewAdapter(AsyncQueryHandler asyncQuery, Context parentActivity) {
        this.asyncQuery = asyncQuery;
        this.activity = parentActivity;
    }


    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_result_item, viewGroup, false);
        SearchViewHolder viewholder = new SearchViewHolder(v, new SearchViewHolder.SetupOnClicks() {
            @Override
            public void doFavClick(SearchViewHolder holder) {
                final SearchViewHolder tempHolder = holder;
                final int position = tempHolder.getAdapterPosition();
                final ImageView favIcon = tempHolder.favoriteIcon;
                OMDBApiResponse clickedObj = resultlist.get(position);
                toggleFavIcon(clickedObj, tempHolder);
            }

            @Override
            public void doDetailView(SearchViewHolder holder) {

                Intent intent = new Intent(activity, MovieDetailActivity.class);
                intent.setAction(MyHBOApplication.VIEW_DETAIL_ACTION);
                intent.putExtra("IMDB_ID", resultlist.get(holder.getAdapterPosition()).getImdbId());
                activity.startActivity(intent);
            }

        });
        return viewholder;
    }

    /***
     * Core method for storing the favorite movies to local storage.
     * I have tried using the asyncHandler, to keep the UI thread free from my
     * making any Database operations.
     *
     * @param clickedObj
     * @param viewHolder
     */
    private void toggleFavIcon(OMDBApiResponse clickedObj, SearchViewHolder viewHolder) {
        if(clickedObj.isFavorite()){
            clickedObj.setIsFavorite(false);
            viewHolder.favoriteIcon.setSelected(false);

            String delSelectionClause = DataEntryColumns.IMDB_ID + " = ?";
            String[] delSelectionArgs = {clickedObj.getImdbId()};
            asyncQuery.startDelete(MyHBOApplication.ASYNC_DELETE_TOKEN, null, DataEntryColumns.CONTENT_URI, delSelectionClause, delSelectionArgs);
        } else {
            clickedObj.setIsFavorite(true);
            viewHolder.favoriteIcon.setSelected(true);
            ContentValues mNewValues = new ContentValues();
            mNewValues.put(DataEntryColumns.NAME, clickedObj.getTitle());
            mNewValues.put(DataEntryColumns.YEAR, clickedObj.getYear());
            mNewValues.put(DataEntryColumns.POSTER, clickedObj.getPosterUrl());
            mNewValues.put(DataEntryColumns.IS_FAVORITE, clickedObj.isFavorite());
            mNewValues.put(DataEntryColumns.IMDB_ID, clickedObj.getImdbId());
            asyncQuery.startInsert(MyHBOApplication.ASYNC_INSERT_TOKEN, null, DataEntryColumns.CONTENT_URI, mNewValues);

        }



    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position) {
        OMDBApiResponse currentObj = resultlist.get(position);
        holder.title.setText(currentObj.getTitle());
        holder.year.setText(currentObj.getYear());
        if(currentObj.getPosterUrl().length() < 1 || "N/A".equals(currentObj.getPosterUrl())){
            holder.poster.setImageResource(R.drawable.unknow_user);
        }else {
            Picasso.with(MyHBOApplication.getInstance().getApplicationContext()).load(currentObj.getPosterUrl())
                    .fit()
                    .centerCrop()
                    .into(holder.poster);
        }
        if(currentObj.isFavorite()){
            holder.favoriteIcon.setSelected(true);
        }else {
            holder.favoriteIcon.setSelected(false);
        }
    }

    @Override
    public int getItemCount() {
        return resultlist.size();
    }


    public void updateSearchList(List<OMDBApiResponse> list){
        resultlist.addAll(list);
        notifyDataSetChanged();
    }



    public static class SearchViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{

        ImageView poster;
        TextView title;
        TextView year;
        SetupOnClicks onClicks;
        ImageView favoriteIcon;

        public SearchViewHolder(View itemView, SetupOnClicks listener) {
            super(itemView);
            poster = (ImageView)itemView.findViewById(R.id.file_type);
            poster.setTag("poster");
            title = (TextView)itemView.findViewById(R.id.title);
            year = (TextView)itemView.findViewById(R.id.releaseYear);
            favoriteIcon = (ImageView)itemView.findViewById(R.id.tag);
            favoriteIcon.setTag("favIcon");
            onClicks = listener;
            favoriteIcon.setOnClickListener(this);
            poster.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if("poster".equals(v.getTag())){
                onClicks.doDetailView(this);
            }else if("favIcon".equals(v.getTag())){
                onClicks.doFavClick(this);
            }

        }

        public interface SetupOnClicks {
            void doFavClick(SearchViewHolder holder);
            void doDetailView(SearchViewHolder holder);
        }

    }
}
