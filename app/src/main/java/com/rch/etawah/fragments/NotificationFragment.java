package com.rch.etawah.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.rch.etawah.activity.MainActivity;
import com.rch.etawah.Config.BaseURL;
import com.rch.etawah.ModelClass.notice;
import com.rch.etawah.R;
import com.rch.etawah.app.AppController;
import com.rch.etawah.util.CustomVolleyJsonRequest;
import com.rch.etawah.util.Session_management;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.rch.etawah.Config.BaseURL.NoticeURl;

public class NotificationFragment extends Fragment {

    ShimmerRecyclerView recyclerNotification;

    LinearLayout back;
    RelativeLayout noData;
    NoticeAdapter noticeAdapter;
    TextView dellete;
    Session_management session_management;
    String user_id;
    private List<notice> noticeList = new ArrayList<>();
    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        recyclerNotification = view.findViewById(R.id.recyclerNotification);
        back=view.findViewById(R.id.back);
        noData= view.findViewById(R.id.noData);

        dellete = view.findViewById(R.id.dellete);
        dellete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

deleteorder();

            }
        });
        hitUrl_notice();
        return view;
    }


    private void hitUrl_notice() {
         session_management = new Session_management(getContext());
         user_id = session_management.getUserDetails().get(BaseURL.KEY_ID);

        ProgressDialog progressDialog = ProgressDialog.show(getActivity(),
                "Loading..", "Please Wait", false, true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, NoticeURl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response_notice_details",response);
                progressDialog.dismiss();


                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")){

                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject3 = jsonArray.getJSONObject(i);
                            notice nn = new notice();
                            nn.content_id = jsonObject3.getString("noti_id");
                            nn.content_name = jsonObject3.getString("noti_title");  //heading
                            nn.content_date = jsonObject3.getString("noti_message");  //title data

                         /*   SharedPreferences.Editor editor = getActivity().getSharedPreferences(MyPrefreance,MODE_PRIVATE).edit();

                            editor.putString(NOTIFICATION_TITLE,nn.content_name);
                            editor.putString(NOTIFICATION_CONTENT,nn.content_date);
                            editor.apply();*/

                            noticeList.add(nn);
                            noData.setVisibility(View.GONE);
                            recyclerNotification.setVisibility(View.VISIBLE);

                        }

                        noticeAdapter = new NoticeAdapter(noticeList);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                        recyclerNotification.setLayoutManager(mLayoutManager);
                        recyclerNotification.setAdapter(noticeAdapter);


                    }

                    else {

                        JSONObject resultObj = jsonObject.getJSONObject("results");
                        String msg = resultObj.getString("msg");
                        noData.setVisibility(View.VISIBLE);
                        recyclerNotification.setVisibility(View.GONE);

                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();

                params.put("user_id", user_id);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);

    }
    public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.MyViewHolder> {

        private List<notice> noticeList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView notice_name, notice_date;
            CardView cardView;


            public MyViewHolder(View view) {
                super(view);

                notice_name = (TextView) view.findViewById(R.id.txt_notice);
                notice_date = (TextView) view.findViewById(R.id.txt_date);
                cardView= view.findViewById(R.id.cardview);


            }
        }


        public NoticeAdapter(List<notice> noticeList) {
            this.noticeList = noticeList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_notificationlist,parent,false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder,int position) {
            notice nn = noticeList.get(position);


            holder.notice_name.setText(nn.getContent_name());
            holder.notice_date.setText(nn.getContent_date());

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                   // movetoDetailScreen(nn.getContent_id());
                }
            });

        }

        @Override
        public int getItemCount() {
            return noticeList.size();
        }
    }
    private void deleteorder() {

        // Tag used to cancel the request
        String tag_json_obj = "json_order_detail_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id",user_id);


        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.delete_all_notification, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("CheckApi", response.toString());


                try {
                    String status = response.getString("status");

                    String message = response.getString("message");


                    if (status.contains("1")){

                        startActivity(new Intent(getActivity(), MainActivity.class));
                        Toast.makeText(getActivity(), ""+message, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                VolleyLog.d("", "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }
}
