//package com.tecmanic.etawah.Fragments;
//
//import android.os.Bundle;
//import android.text.Html;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.fragment.app.Fragment;
//
//import com.android.volley.AuthFailureError;
//import com.android.volley.NoConnectionError;
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.TimeoutError;
//import com.android.volley.VolleyError;
//import com.android.volley.VolleyLog;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.android.volley.toolbox.Volley;
//import com.google.gson.Gson;
//import com.google.gson.JsonObject;
//import com.google.gson.reflect.TypeToken;
//import com.tecmanic.etawah.Activity.MainActivity;
//import com.tecmanic.etawah.Config.BaseURL;
//import com.tecmanic.etawah.ModelClass.Support_info_model;
//import com.tecmanic.etawah.R;
//import com.tecmanic.etawah.util.AppController;
//import com.tecmanic.etawah.util.ConnectivityReceiver;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.lang.reflect.Type;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//
//public class About_us_fragment extends Fragment {
//
//    private static String TAG = About_us_fragment.class.getSimpleName();
//
//    private TextView tv_info;
//    String description;
//
//    public About_us_fragment() {
//        // Required empty public constructor
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_about_us, container, false);
//
//        tv_info = (TextView) view.findViewById(R.id.tv_info);
//
//        tv_info.setText(description);
//        String geturl = getArguments().getString("url");
//        //   String title = getArguments().getString("title");
//
////        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.nav_about));
//
//        // check internet connection
//        if (ConnectivityReceiver.isConnected()) {
//            makeGetInfoRequest(geturl);
//        } else {
//           // ((MainActivity) getActivity()).onNetworkConnectionChanged(false);
//        }
//
//        return view;
//    }
//
//    /**
//     * Method to make json object request where json response starts wtih
//     */
//    private void makeGetInfoRequest(String url) {
//
//        // Tag used to cancel the request
//        String tag_json_obj = "json_info_req";
//
//        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, BaseURL.AboutUrl, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                Log.d(TAG, response.toString());
//
//                try {
//                    // Parsing json array response
//                    // loop through each json object
//
//                    String status = response.getString("status");
//                    String message = response.getString("message");
//                    if (status.contains("1")) {
//
//                        JSONObject jsonObject = response.getJSONObject("data");
//
//                         description = jsonObject.getString("description");
//
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(getActivity(),
//                            "Error: " + e.getMessage(),
//                            Toast.LENGTH_LONG).show();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                HashMap<String,String> param = new HashMap<>();
//                return param;
//            }
//        };
//        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
//        requestQueue.getCache().clear();
//        requestQueue.add(jsonObjReq);
//    }
//
//}
//
