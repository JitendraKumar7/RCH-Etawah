package com.rch.etawah.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.rch.etawah.util.ConnectivityReceiver;

public class AppController extends Application {

    private RequestQueue mRequestQueue;
    private static AppController mInstance;
    private static SharedPreferences mPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(AppConstants.TAG);
        getRequestQueue().add(req);
    }

    public synchronized SharedPreferences getPreference() {
        if (mPreferences == null) {
            mPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        }
        return mPreferences;
    }

    public static synchronized AppController getInstance() {

        return mInstance;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? AppConstants.TAG : tag);
        getRequestQueue().add(req);
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

}
