package com.rch.etawah.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
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
import com.rch.etawah.util.DatabaseHandler;
import com.rch.etawah.util.Session_management;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.rch.etawah.Config.BaseURL.HomeRecent;

public class ViewAll_Recent extends AppCompatActivity {
    private ShimmerRecyclerView recycler_recent;
    private DatabaseHandler db;
    private Session_management sessionManagement;

    ProgressDialog progressDialog;
    ViewAll_Adapter recentAdapter;
    private List<CartModel> recentList = new ArrayList<>();
    String catId,catName;
    public static String product_image;
    TextView viewall;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all__recent);

        viewall =findViewById(R.id.viewall_topdeals);

        viewall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ViewAll_TopDeals.class));
            }
        });
        sessionManagement = new Session_management(getApplicationContext());
        sessionManagement.cleardatetime();
        db = new DatabaseHandler(getApplicationContext());
        recycler_recent = findViewById(R.id.recyclerRecent);

        progressDialog=new ProgressDialog(getApplicationContext());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        if(isOnline()){
            recentUrl();}

    }
    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
    private void recentUrl() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, HomeRecent, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("homeRecent",response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("1")){
                        recentList.clear();
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String product_id = jsonObject1.getString("product_id");
                            String varient_id = jsonObject1.getString("varient_id");
                            String product_name = jsonObject1.getString("product_name");
                            String description= jsonObject1.getString("description");
                            String pprice= jsonObject1.getString("price");
                            String quantity = jsonObject1.getString("quantity");
                            product_image = jsonObject1.getString("varient_image");
                            String mmrp = jsonObject1.getString("mrp");
                            String unit = jsonObject1.getString("unit");
                            String count = jsonObject1.getString("count");
                            String totalOff= String.valueOf(Integer.parseInt(String.valueOf(mmrp))-Integer.parseInt(String.valueOf(pprice)));

                            CartModel recentData= new CartModel(product_id,product_name,description,pprice,quantity+" "+unit,product_image,getResources().getString(R.string.currency)+totalOff+" "+"Off",mmrp,count,unit);
                            recentData.setVarient_id(varient_id);
                            recentList.add(recentData);

                        }
                        recentAdapter = new ViewAll_Adapter(recentList,getApplicationContext());
                        recycler_recent.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        recycler_recent.setAdapter(recentAdapter);
                        recentAdapter.notifyDataSetChanged();

                    }else {
                        JSONObject resultObj = jsonObject.getJSONObject("results");
                        String msg = resultObj.getString("message");
                        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);

    }
//    private void updateData() {
//        tv_total.setText("" + db.getTotalAmount());
//        tv_item.setText("" + db.getCartCount());
//        ((MainActivity) getApplicationContext()).setCartCounter("" + db.getCartCount());
//    }

    @Override
    public void onPause() {
        super.onPause();
        // unregister reciver
        getApplicationContext().unregisterReceiver(mCart);
    }

    @Override
    public void onResume() {
        super.onResume();
        // register reciver
        getApplicationContext().registerReceiver(mCart, new IntentFilter("Grocery_cart"));
    }

    // broadcast reciver for receive data
    private BroadcastReceiver mCart = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String type = intent.getStringExtra("type");

            if (type.contentEquals("update")) {

            }
        }
    };


}
