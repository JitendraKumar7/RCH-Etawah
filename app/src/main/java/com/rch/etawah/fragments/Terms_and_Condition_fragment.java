package com.rch.etawah.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.rch.etawah.activity.MainActivity;
import com.rch.etawah.Config.BaseURL;
import com.rch.etawah.R;
import com.rch.etawah.util.ConnectivityReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class Terms_and_Condition_fragment extends Fragment {

    private static String TAG = Terms_and_Condition_fragment.class.getSimpleName();

    private TextView tv_info;
    String description;

    public Terms_and_Condition_fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_terms_condition, container, false);

        tv_info = (TextView) view.findViewById(R.id.tv_info);

        String geturl = getArguments().getString("url");
        //   String title = getArguments().getString("title");

        ((MainActivity) getActivity()).setTitle("Terms & Conditions");

        // check internet connection
        if (ConnectivityReceiver.isConnected()) {
            makeGetInfoRequest();
        } else {
            //((MainActivity) getActivity()).onNetworkConnectionChanged(false);
        }

        return view;
    }

    /**
     * Method to make json object request where json response starts wtih
     */
    private void makeGetInfoRequest() {

        // Tag used to cancel the request
        String tag_json_obj = "json_info_req";

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, BaseURL.TermsUrl, null, new Response.Listener<JSONObject>() {
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
                    Toast.makeText(getActivity(),
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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.getCache().clear();
        requestQueue.add(jsonObjReq);
    }

}

