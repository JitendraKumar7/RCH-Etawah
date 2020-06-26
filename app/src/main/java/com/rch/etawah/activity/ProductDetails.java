package com.rch.etawah.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rch.etawah.R;
import com.rch.etawah.util.DatabaseHandler;

import java.util.HashMap;

public class ProductDetails extends AppCompatActivity {
    private DatabaseHandler dbcart;

    int minteger = 0;

    LinearLayout back,btn_Add,ll_addQuan,ll_details;
    ImageView pImage;
    TextView prodName,prodDesc,ProdPrice,prodMrp,nodata , txt_description;
    TextView txtQuan,minus,plus,txt_unit,txt_qty;
    String varientId,varientName,proImage ,discription12 , price12 , mrp12 , unit12 , qty , varientimage;
    ProgressDialog progressDialog;
    String product_id,varient_id, price, quantity, varient_image,mrp, unit, description ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        dbcart = new DatabaseHandler(getApplicationContext());

        init();

    }

    private void init() {
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        varientId=getIntent().getStringExtra("sId");
        varientName=getIntent().getStringExtra("sName");
        proImage=getIntent().getStringExtra("sImge");

        discription12=getIntent().getStringExtra("descrip");
        price12=getIntent().getStringExtra("price");
        mrp12=getIntent().getStringExtra("mrp");
        unit12=getIntent().getStringExtra("unit");
        qty=getIntent().getStringExtra("qty");
        varientimage=getIntent().getStringExtra("image");

        ll_details=findViewById(R.id.ll3);
        back=findViewById(R.id.back);
        pImage=findViewById(R.id.pImage);
        nodata=findViewById(R.id.nodata);
        prodName=findViewById(R.id.txt_pName);
        prodDesc=findViewById(R.id.txt_pInfo);
        ProdPrice=findViewById(R.id.txt_pPrice);
        prodMrp=findViewById(R.id.txt_pMrp);
        btn_Add=findViewById(R.id.btn_Add);
        ll_addQuan=findViewById(R.id.ll_addQuan);
        txtQuan=findViewById(R.id.txtQuan);
        minus=findViewById(R.id.minus);
        plus=findViewById(R.id.plus);
        txt_unit =  findViewById(R.id.txt_unit);
        txt_qty = findViewById(R.id.txt_qty);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_Add.setVisibility(View.GONE);
                ll_addQuan.setVisibility(View.VISIBLE);
                txtQuan.setText("1");

            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseInteger();
                updateMultiply();

                if (Integer.parseInt(txtQuan.getText().toString()) == 1) {
                       /* minus.setClickable(false);
                        minus.setFocusable(false);*/
                } else if (Integer.parseInt(txtQuan.getText().toString()) > 1) {
                    minus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            decreaseInteger();
                            updateMultiply();
                        }
                    });
                }
            }
        });

        if(isOnline()){
            prodName.setText(varientName);
            ProdPrice.setText(getApplication().getResources().getString(R.string.currency) + price12);
            prodMrp.setPaintFlags(prodMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            prodMrp.setText(mrp12);
            txt_unit.setText(unit12);
            txt_qty.setText(qty);
//            varientUrl(varientId);

            Glide.with(getApplicationContext())
                    .load(varientimage)
                    .centerCrop()
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate()
                    .into(pImage);
        }
    }

//    private void varientUrl(String varientId) {
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, ProductVarient, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.d("Prod detail", response);
//                progressDialog.dismiss();
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    String status = jsonObject.getString("status");
//                    if (status.equals("1")) {
//                        JSONArray jsonArray = jsonObject.getJSONArray("data");
//                        for (int i = 0; i < jsonArray.length(); i++) {
//
//                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                             product_id = jsonObject1.getString("product_id");
//                             varient_id = jsonObject1.getString("varient_id");
//                             price = jsonObject1.getString("price");
//                             quantity = jsonObject1.getString("quantity");
//                             varient_image = jsonObject1.getString("varient_image");
//
//                            mrp = jsonObject1.getString("mrp");
//                             unit = jsonObject1.getString("unit");
//                             description = jsonObject1.getString("description");
//                            nodata.setVisibility(View.GONE);
//                            ll_details.setVisibility(View.VISIBLE);
//                            prodMrp.setPaintFlags(prodMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//
//                            prodMrp.setText(mrp);
//                            ProdPrice.setText(getApplication().getResources().getString(R.string.currency) + price);
//                            prodDesc.setText(description);
//                            Picasso.with(getApplicationContext()).load(IMG_URL+varient_image).into(pImage);
//                            //prodMrp.setText(mrp);
//
//                        }
//
//                    } else {
//                        //JSONObject resultObj = jsonObject.getJSONObject("results");
//                        nodata.setVisibility(View.VISIBLE);
//                        ll_details.setVisibility(View.GONE);
//                    }
//                    progressDialog.dismiss();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                progressDialog.dismiss();
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                progressDialog.dismiss();
//
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                HashMap<String, String> params = new HashMap<>();
//                params.put("product_id",varientId);
//                return params;
//            }
//        };
//
//        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
//        requestQueue.getCache().clear();
//        requestQueue.add(stringRequest);
//    }


    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }


    public void increaseInteger() {
        minteger = minteger + 1;
        display(minteger);
    }

    public void decreaseInteger() {
        if (minteger == 1) {
            minteger = 1;
            display(minteger);
            ll_addQuan.setVisibility(View.GONE);
            btn_Add.setVisibility(View.VISIBLE);
        } else {
            minteger = minteger - 1;
            display(minteger);

        }
    }

    private void display(Integer number) {

        txtQuan.setText("" + number);
    }






    private void updateMultiply() {
        HashMap<String, String> map = new HashMap<>();
        map.put("product_id",product_id);
        map.put("product_name",varientName);
        map.put("varient_id",varient_id);
        map.put("title",description);
        map.put("price",mrp);
        map.put("deal_price",price);
        map.put("product_image",varient_image);
//        map.put("status",cartList.get(position).getStatus());
//        map.put("in_stock",cartList.get(position).getIn_stock());
        map.put("unit_value",txtQuan.getText().toString());
//        map.put("unit",cartList.get(position).getUnit());
        map.put("increament","0");
        map.put("rewards","0");
        map.put("stock","0");
        map.put("product_description","0");

//        Log.d("fgh",cartList.get(position).getpDes());
//        Log.d("fghfgh",cartList.get(position).getpPrice());
             /*   map.put("start_date", cartList.get(position).getStart_date());
                map.put("start_time", cartList.get(position).getStart_time());
                map.put("end_date", cartList.get(position).getEnd_date());
                map.put("end_time", cartList.get(position).getEnd_time());*/
        if (!txtQuan.getText().toString().equalsIgnoreCase("0")) {
            if (dbcart.isInCart(map.get("product_id"))) {
                dbcart.setCart(map, Integer.valueOf(txtQuan.getText().toString()));
                Log.d("sdf", "update");
                //  Toast.makeText(context, "Product quantity is updated in your cart", Toast.LENGTH_SHORT).show();

            } else {
                dbcart.setCart(map, Integer.valueOf(txtQuan.getText().toString()));
                //   Toast.makeText(context, "Product quantity is added in your cart", Toast.LENGTH_SHORT).show();

            }
        } else {
            dbcart.removeItemFromCart(map.get("product_id"));
        }
        try {
            int items = (int) Double.parseDouble(dbcart.getInCartItemQty(map.get("product_id")));
            Double price = Double.parseDouble(map.get("price").trim());
            //  Double reward = Double.parseDouble(map.get("rewards"));
            // tv_reward.setText("" + reward * items);
//            pDescrptn.setText(""+cartList.get(position).getpDes());
//            pPrice.setText("" +price* items);
//            txtQuan.setText("" + items);
//            pMrp.setText(cartList.get(position).getpMrp());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //  ((MainActivity) context).setCartCounter("" + dbcart.getCartCount());
            }
        }catch (IndexOutOfBoundsException e){
            e.toString();
            Log.d("qwer",e.toString());
        }
    }


}
