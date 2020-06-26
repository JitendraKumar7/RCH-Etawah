package com.rch.etawah.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.rch.etawah.Adapters.Coupun_Listing_Adapter;
import com.rch.etawah.Config.BaseURL;
import com.rch.etawah.ModelClass.CoupunModel;
import com.rch.etawah.R;
import com.rch.etawah.app.AppController;
import com.rch.etawah.ui.ActivityOrderSummary;
import com.rch.etawah.util.CustomVolleyJsonRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Coupen extends AppCompatActivity {

    List<CoupunModel> coupunModelList=new ArrayList<>();
    RecyclerView coupen_recycler;
    String cart_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupen);

        cart_id = ActivityOrderSummary.cart_id;

        coupen_recycler=findViewById(R.id.recycleer_coupen);
        coupen_recycler.setLayoutManager(new GridLayoutManager(getApplicationContext(),1));


//        coupen_recycler.addOnItemTouchListener(new RecyclerTouchListener(getContext(), coupen_recycler, new RecyclerTouchListener.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
////                AppCategory_Model catagoryModel = appCategoryModels.get(position);
////
//
//                Intent intent = new Intent(getApplicationContext(), PaymentDetails.class);
//
//                intent.putExtra("code",coupunModelList.get(position).getCoupon_code());
//
//
//                startActivity(intent);
//
////
//
//            }
//
//            @Override
//            public void onLongItemClick(View view, int position) {
//
//            }
//        }));

        Coupon_code();
    }

    private void Coupon_code() {
        String tag_json_obj = "json_order_detail_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("cart_id",cart_id);
//        Log.d("ssd",cart_id);




        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.COUPON_CODE, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", response.toString());

                try {

                    String status = response.getString("status");
                    String message = response.getString("message");

                    if (status.contains("1")) {

                        JSONArray jsonArray = response.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String name = jsonObject.getString("coupon_name");
                            String discrip = jsonObject.getString("coupon_description");
                            String code = jsonObject.getString("coupon_code");

                            CoupunModel coupunModel12 = new CoupunModel();

                            coupunModel12.setCoupon_name(name);
                            coupunModel12.setCoupon_description(discrip);
                            coupunModel12.setCoupon_code(code);

                            coupunModelList.add(coupunModel12);



                        }

                        Coupun_Listing_Adapter complainAdapter = new Coupun_Listing_Adapter(coupunModelList);

                        coupen_recycler.setAdapter(complainAdapter);
                        complainAdapter.notifyDataSetChanged();

                    }

                    else{

                        Toast.makeText(Coupen.this, ""+message, Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);


    }
}
