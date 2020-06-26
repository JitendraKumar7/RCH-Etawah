package com.rch.etawah.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.rch.etawah.Adapters.ViewAll_Adapter;
import com.rch.etawah.ModelClass.CartModel;
import com.rch.etawah.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.rch.etawah.Config.BaseURL.whatsnew;

public class ViewAll_TopDeals extends AppCompatActivity {
    private ShimmerRecyclerView rv_top_selling;
    ProgressDialog progressDialog;
    ViewAll_Adapter topSellingAdapter;
    private List<CartModel> topSellList = new ArrayList<>();
    String catId, catName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all__top_deals);


        rv_top_selling = findViewById(R.id.recyclerTopSelling);
        progressDialog = new ProgressDialog(getApplicationContext());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        if (isOnline()) {
            topSellingUrl();
        }


    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private void topSellingUrl() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, whatsnew, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("HomeTopSelling", response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                        topSellList.clear();
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String product_id = jsonObject1.getString("product_id");
                            String varient_id = jsonObject1.getString("varient_id");
                            String product_name = jsonObject1.getString("product_name");
                            String description = jsonObject1.getString("description");
                            String pprice = jsonObject1.getString("price");
                            String quantity = jsonObject1.getString("quantity");
                            String product_image = jsonObject1.getString("varient_image");
                            String mmrp = jsonObject1.getString("mrp");
                            String unit = jsonObject1.getString("unit");
//                            String count = jsonObject1.getString("count");

                            String totalOff = String.valueOf(Integer.parseInt(String.valueOf(mmrp)) - Integer.parseInt(String.valueOf(pprice)));

                            CartModel recentData = new CartModel(product_id, product_name, description, pprice, quantity + " " + unit, product_image, getResources().getString(R.string.currency) + totalOff + " " + "Off", mmrp, " ", unit);
                            recentData.setVarient_id(varient_id);
                            topSellList.add(recentData);
                        }
                        topSellingAdapter = new ViewAll_Adapter(topSellList, getApplicationContext());
                        rv_top_selling.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        rv_top_selling.setAdapter(topSellingAdapter);
                        topSellingAdapter.notifyDataSetChanged();
                      /*  topSellingAdapter = new CartAdapter(topSellList,getActivity());
                        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(getActivity(), 1);
                        rv_top_selling.setLayoutManager(gridLayoutManager2);
                        rv_top_selling.setAdapter(topSellingAdapter);
                        rv_top_selling.setItemAnimator(new DefaultItemAnimator());
                        rv_top_selling.setNestedScrollingEnabled(false);
                      //  rv_top_selling.showShimmerAdapter();
                        topSellingAdapter.notifyDataSetChanged();
*/

                    } else {
                        JSONObject resultObj = jsonObject.getJSONObject("results");
                        String msg = resultObj.getString("message");
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
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
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }
}
