package com.rch.etawah.fragments;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rch.etawah.activity.MainActivity;
import com.rch.etawah.ModelClass.Support_info_model;
import com.rch.etawah.R;
import com.rch.etawah.app.AppController;
import com.rch.etawah.util.ConnectivityReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Contact_Us_fragment extends Fragment {

    private static String TAG = Contact_Us_fragment.class.getSimpleName();

    private TextView tv_info;
    private ImageView iv_Call, iv_Whatspp, iv_reviews, iv_share_via;


    public Contact_Us_fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact_us, container, false);



        tv_info = (TextView) view.findViewById(R.id.tv_info);

        String geturl = getArguments().getString("url");
        //   String title = getArguments().getString("title");

        ((MainActivity) getActivity()).setTitle("Contact Us");

        // check internet connection
        if (ConnectivityReceiver.isConnected()) {
            makeGetInfoRequest(geturl);
        } else {
           // ((MainActivity) getActivity()).onNetworkConnectionChanged(false);
        }

        iv_Call = (ImageView) view.findViewById(R.id.iv_call);
        iv_Whatspp = (ImageView) view.findViewById(R.id.iv_whatsapp);

        iv_Call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPermissionGranted()){
                    call_action("9889887711");
                }
            }
        });
        iv_Whatspp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String smsNumber = "9889887711";
                Intent sendIntent = new Intent("android.intent.action.MAIN");
                sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
                sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(smsNumber) + "@s.whatsapp.net");//phone number without "+" prefix
                startActivity(sendIntent);

            }
        });

        return view;
    }

    public boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG", "Permission is granted");
                return true;
            } else {

                Log.v("TAG", "Permission is revoked");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG", "Permission is granted");
            return true;
        }

    }

    public void call_action(String number) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + number));

        startActivity(callIntent);
    }

    /**
     * Method to make json object request where json response starts wtih
     */
    private void makeGetInfoRequest(String url) {

        // Tag used to cancel the request
        String tag_json_obj = "json_info_req";

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    // Parsing json array response
                    // loop through each json object

                    boolean status = response.getBoolean("responce");
                    if (status) {

                        List<Support_info_model> support_info_modelList = new ArrayList<>();

                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Support_info_model>>() {
                        }.getType();

                        support_info_modelList = gson.fromJson(response.getString("data"), listType);

                        String desc = support_info_modelList.get(0).getPg_descri();
                        String title = support_info_modelList.get(0).getPg_title();

                        tv_info.setText(Html.fromHtml(desc));

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
                VolleyLog.d(TAG, "Error: " + error.getMessage());
             /*   if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }*/
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

}

