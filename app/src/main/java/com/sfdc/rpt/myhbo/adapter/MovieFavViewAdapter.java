package com.sfdc.rpt.myhbo.adapter;

import android.database.Cursor;
import android.database.DataSetObserver;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sfdc.rpt.myhbo.R;
import com.sfdc.rpt.myhbo.application.MyHBOApplication;
import com.squareup.picasso.Picasso;

import static com.sfdc.rpt.myhbo.adapter.MovieSearchViewAdapter.SearchViewHolder;

/**
 * Created by ahetawal on 3/8/16.
 */
public class MovieFavViewAdapter extends RecyclerView.Adapter<SearchViewHolder>{


    private Cursor mCursor;
    private boolean mDatasetValid;
    private DataViewSetObserver mObserver;
    private ClickActions clickActions;

    public MovieFavViewAdapter(ClickActions actioner){
        mObserver = new DataViewSetObserver();
        clickActions = actioner;
    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_result_item, viewGroup, false);
        SearchViewHolder viewholder = new SearchViewHolder(v, new SearchViewHolder.SetupOnClicks() {
            @Override
            public void doFavClick(SearchViewHolder holder) {
                if(mCursor.moveToPosition(holder.getAdapterPosition())) {
                    String imdb_id = mCursor.getString(2);
                    clickActions.doUnFav(imdb_id);
                }
            }

            @Override
            public void doDetailView(SearchViewHolder holder) {
                if(mCursor.moveToPosition(holder.getAdapterPosition())){
                    String imdb_id = mCursor.getString(2);
                    clickActions.showDetailView(imdb_id);
                }
            }
        });
        return viewholder;
    }

    @Override
    public void onBindViewHolder(MovieSearchViewAdapter.SearchViewHolder holder, int position) {
        if(mDatasetValid){
            if(mCursor.moveToPosition(position)){
                holder.title.setText(mCursor.getString(1));
                holder.year.setText(mCursor.getString(3));
                holder.favoriteIcon.setSelected(true);
                if(mCursor.getString(4).length() < 1 || "N/A".equals(mCursor.getString(4))){
                    holder.poster.setImageResource(R.drawable.unknow_user);
                }else {
                    Picasso.with(MyHBOApplication.getInstance().getApplicationContext()).load(mCursor.getString(4))
                            .fit()
                            .centerCrop()
                            .into(holder.poster);
                }
            }else {
                Log.e(MyHBOApplication.LOG_TAG, "Can't bind view; Cursor is not valid.");
            }
        }else {
            Log.e(MyHBOApplication.LOG_TAG, "Dataset is not valid.");
        }
    }

    @Override
    public int getItemCount() {
        if (mDatasetValid && mCursor != null) {
            return mCursor.getCount();
        }
        Log.e(MyHBOApplication.LOG_TAG, "Dataset is not valid.");
        return 0;
    }


    public void swapCursor(Cursor cursor) {
        // Sanity check.
        if (cursor == mCursor) {
            return;
        }

        // Before getting rid of the old cursor, disassociate it from the Observer.
        final Cursor oldCursor = mCursor;
        if (oldCursor != null && mObserver != null) {
            oldCursor.unregisterDataSetObserver(mObserver);
        }

        mCursor = cursor;
        if (mCursor != null) {
            // Attempt to associate the new Cursor with the Observer.
            if (mObserver != null) {
                mCursor.registerDataSetObserver(mObserver);
            }
            mDatasetValid = true;
        } else {
            mDatasetValid = false;
        }
        Log.i(MyHBOApplication.LOG_TAG, "SwapCursor Notifier");
        notifyDataSetChanged();
    }


    private final class DataViewSetObserver extends DataSetObserver {

        @Override
        public void onChanged() {
            super.onChanged();

            mDatasetValid = true;
            Log.i(MyHBOApplication.LOG_TAG, "DataViewSetObserver CHANGED");

            notifyDataSetChanged();
        }

        /**
         * //getLoaderManager().getLoader(1).reset(); triggers this method
         */
        @Override
        public void onInvalidated() {
            super.onInvalidated();

            mDatasetValid = false;
            Log.i(MyHBOApplication.LOG_TAG, "DataViewSetObserver INVALIDATED");
            notifyDataSetChanged();
        }
    }

    /**
     * Bridge to pass back control to activty class instead of calling
     * start activity from adapter code.
     */
    public interface ClickActions {
        void showDetailView(String imdb_id);
        void doUnFav(String imdb_id);
    }
}
