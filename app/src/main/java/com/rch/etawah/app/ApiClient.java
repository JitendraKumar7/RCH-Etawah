package com.rch.etawah.app;

import android.app.ProgressDialog;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ApiClient {

    private Response.Listener listener;
    private ProgressDialog _dialog = null;

    private void dismissDialog() {
        try {
            if (_dialog != null) _dialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getApiResponse(String url) {
        Utils.showLog(url);
        if (_dialog != null) _dialog.show();
        AppController.getInstance().addToRequestQueue(new StringRequest(url, response -> {
            listener.onResponse((String) response);
            Utils.showLog(response);
            dismissDialog();
        }, error -> {
            Utils.showLog(error);
            dismissDialog();
        }));
    }

    private ApiClient(Context ctx, Response.Listener listener) {
        if (ctx != null) {
            this._dialog = new ProgressDialog(ctx);
            this._dialog.setMessage("Please Wait...");
        }
        this.listener = listener;
    }

    public void getApiResponse(String url, HashMap<String, String> params) {
        Utils.showLog(url);
        Utils.showLog(params);
        if (_dialog != null) _dialog.show();
        AppController.getInstance().addToRequestQueue(new StringRequest(Request.Method.POST, url, response -> {
            listener.onResponse((String) response);
            Utils.showLog(response);
            dismissDialog();
        }, error -> {
            Utils.showLog(error);
            dismissDialog();
        }) {

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        });
    }

    public static ApiClient getInstance(Context ctx, Response.Listener listener) {

        return new ApiClient(ctx, listener);
    }

}
