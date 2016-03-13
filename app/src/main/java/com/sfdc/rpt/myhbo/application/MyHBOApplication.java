package com.sfdc.rpt.myhbo.application;

import android.app.Application;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Android application class for holding the volley request queue and
 * other constants which are shared across this application.
 *
 *
 * Created by ahetawal on 3/7/16.
 */
public class MyHBOApplication extends Application {

    public static final String LOG_TAG = "myhbo";
    public static final String NETWORK_TAG = "OMDB_API";

    public static final String VIEW_DETAIL_ACTION = "com.sfdc.rpt.myhbo.DETAILVIEW";


    //These tokens are used by AsyncHandler for making request, can be used with cancelOperation(tokenId)
    public static final int ASYNC_QUERY_TOKEN = 1;
    public static final int ASYNC_INSERT_TOKEN = 2;
    public static final int ASYNC_UPDATE_TOKEN = 3;
    public static final int ASYNC_DELETE_TOKEN = 4;

    private static MyHBOApplication mInstance;

    private RequestQueue mRequestQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized MyHBOApplication getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(NETWORK_TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

}
