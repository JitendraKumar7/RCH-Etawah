package com.rch.etawah.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.rch.etawah.activity.ViewAll_TopDeals;
import com.rch.etawah.Adapters.Deal_Adapter;
import com.rch.etawah.ModelClass.CartModel;
import com.rch.etawah.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.rch.etawah.Config.BaseURL.HomeDeal;

public class Deals_Fragment extends Fragment {

    ProgressDialog progressDialog;
    Deal_Adapter DealAdapter;
    private List<CartModel> dealList = new ArrayList<>();
    ShimmerRecyclerView shimmerRecyclerView;
    String catId,catName;
    TextView viewall;
    public Deals_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_deals, container, false);
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        viewall =view.findViewById(R.id.viewall);

        viewall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ViewAll_TopDeals.class));
            }
        });

        shimmerRecyclerView= view.findViewById(R.id.recyclerDealsDay);
        if(isOnline()){
            progressDialog.show();
            DealUrl();}
    /*    shimmerRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), shimmerRecyclerView, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                catId = dealList.get(position).getpId();
                catName = dealList.get(position).getpNAme();
                Intent intent=new Intent(getActivity(), ProductDetails.class);
                intent.putExtra("sId",catId);
                intent.putExtra("sName",catName);
                startActivity(intent);

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));*/
        return view;
    }

    private void DealUrl() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, HomeDeal, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("HomeDeal",response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("1")){
                        dealList.clear();
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String product_id = jsonObject1.getString("product_id");
                            String varient_id = jsonObject1.getString("varient_id");
                            String product_name = jsonObject1.getString("product_name");
                            String description= jsonObject1.getString("description");
                            String pprice= jsonObject1.getString("price");
                            String quantity = jsonObject1.getString("quantity");
                            String product_image = jsonObject1.getString("varient_image");
                            String mmrp = jsonObject1.getString("mrp");
                            String unit = jsonObject1.getString("unit");
                            Long count = jsonObject1.getLong("timediff");
                            String totalOff= String.valueOf(Integer.parseInt(String.valueOf(mmrp))-Integer.parseInt(String.valueOf(pprice)));

                            CartModel recentData= new CartModel(product_id,product_name,description,pprice,quantity+" "+unit,product_image,getResources().getString(R.string.currency)+totalOff+" "+"Off",mmrp," ",unit);
                            recentData.setVarient_id(varient_id);
                            recentData.setHoursmin(count);
                            dealList.add(recentData);

                        }
                        DealAdapter = new Deal_Adapter(dealList,getActivity());
                        shimmerRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        shimmerRecyclerView.setAdapter(DealAdapter);
                        DealAdapter.notifyDataSetChanged();

                    }else {
                        JSONObject resultObj = jsonObject.getJSONObject("results");
                        String msg = resultObj.getString("message");
                        Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
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
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);

    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

}
