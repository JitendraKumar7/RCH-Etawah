package com.rch.etawah.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.rch.etawah.Config.BaseURL;
import com.rch.etawah.R;
import com.rch.etawah.util.ConnectivityReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class About_us extends AppCompatActivity {
    private static String TAG = About_us.class.getSimpleName();

    private TextView tv_info;
    String description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        tv_info = (TextView) findViewById(R.id.tv_info);

//        String geturl = getArguments().getString("url");
        //   String title = getArguments().getString("title");

//        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.nav_about));

        // check internet connection
        if (ConnectivityReceiver.isConnected()) {
            makeGetInfoRequest();
        } else {
            // ((MainActivity) getActivity()).onNetworkConnectionChanged(false);
        }
    }

        private void makeGetInfoRequest() {

            // Tag used to cancel the request
            String tag_json_obj = "json_info_req";

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, BaseURL.AboutUrl, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d(TAG, response.toString());

                    try {
                        // Parsing json array response
                        // loop through each json object

                        String status = response.getString("status");
                        String message = response.getString("message");
                        if (status.contains("1")) {

                            JSONObject jsonObject = response.getJSONObject("data");

                            description = jsonObject.getString("description");

                            tv_info.setText(description);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),
                                "Error: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String,String> param = new HashMap<>();
                    return param;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.getCache().clear();
            requestQueue.add(jsonObjReq);
        }
    }

