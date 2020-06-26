package com.rch.etawah.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rch.etawah.activity.ProductDetails;
import com.rch.etawah.Constans.RecyclerTouchListener;
import com.rch.etawah.ModelClass.CategoryGrid;
import com.rch.etawah.ModelClass.varient_product;
import com.rch.etawah.R;
import com.rch.etawah.util.DatabaseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.rch.etawah.Config.BaseURL.IMG_URL;
import static com.rch.etawah.Config.BaseURL.ProductVarient;

public class CategoryGridAdapter  extends RecyclerView.Adapter<CategoryGridAdapter.MyViewHolder> {

        private List<CategoryGrid> CategoryGridList;
        Context context;
    RecyclerView recyler_popup;
    LinearLayout cancl;
    private DatabaseHandler dbcart;
    private List<varient_product> varientProducts = new ArrayList<>();

    public CategoryGridAdapter( Context context,List<CategoryGrid> categoryGridList) {
        CategoryGridList = categoryGridList;
        dbcart = new DatabaseHandler(context);

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView prodNAme,pDescrptn,pQuan,pPrice,pdiscountOff,pMrp,minus,plus,txtQuan,txt_unitvalue;
        ImageView image;
        LinearLayout btn_Add,ll_addQuan;
        int minteger = 0;
        RelativeLayout rlQuan;
        String catId,catName;

            public MyViewHolder(View view) {
                super(view);
                prodNAme = (TextView) view.findViewById(R.id.txt_pName);
                pDescrptn = (TextView) view.findViewById(R.id.txt_pInfo);
                pQuan = (TextView) view.findViewById(R.id.txt_unit);
                pPrice = (TextView) view.findViewById(R.id.txt_Pprice);
                image = (ImageView) view.findViewById(R.id.prodImage);
                pdiscountOff = (TextView) view.findViewById(R.id.txt_discountOff);
                pMrp = (TextView) view.findViewById(R.id.txt_Mrp);
                rlQuan =  view.findViewById(R.id.rlQuan);
                btn_Add =  view.findViewById(R.id.btn_Add);
                ll_addQuan =  view.findViewById(R.id.ll_addQuan);
                txtQuan =  view.findViewById(R.id.txtQuan);
                minus =  view.findViewById(R.id.minus);
                plus =  view.findViewById(R.id.plus);
                txt_unitvalue =  view.findViewById(R.id.txt_unitvalue);
//                ll_addQuan =  view.findViewById(R.id.ll_addQuan);
                btn_Add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        btn_Add.setVisibility(View.GONE);
                        ll_addQuan.setVisibility(View.VISIBLE);
                        txtQuan.setText("1");
                        updateMultiply();
                    }
                });
                plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        increaseInteger();
                        updateMultiply();

                        if (Float.parseFloat(txtQuan.getText().toString()) == 1) {
                       /* minus.setClickable(false);
                        minus.setFocusable(false);*/
                        } else if (Float.parseFloat(txtQuan.getText().toString()) > 1) {
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
                //  minus.setOnClickListener(this);
                //   plus.setOnClickListener(this);


            }
        private void updateMultiply() {
            int position = getAdapterPosition();
            HashMap<String, String> map = new HashMap<>();
//            map.put("varient_id",CategoryGridList.get(position).getpId());
            map.put("varient_id",CategoryGridList.get(position).getVarient_id());
            Log.d("dfass",CategoryGridList.get(position).getVarient_id());
            map.put("product_name",CategoryGridList.get(position).getName());
//            map.put("category_id",CategoryGridList.get(position).get());
            map.put("title",CategoryGridList.get(position).getDescription());
            map.put("price",CategoryGridList.get(position).getPrice());
            Log.d("dsfa",CategoryGridList.get(position).getPrice());
            map.put("mrp",CategoryGridList.get(position).getMrp());
            Log.d("fd",CategoryGridList.get(position).getImage());
            map.put("product_image",CategoryGridList.get(position).getImage());
//            map.put("status",CategoryGridList.get(position).get());
//            map.put("in_stock",CategoryGridList.get(position).getIn_stock());
            map.put("unit_value",CategoryGridList.get(position).getQuantity());
            map.put("unit",CategoryGridList.get(position).getUnit());
            map.put("increament","0");
            map.put("rewards","0");
            map.put("stock","0");
            map.put("product_description","0");

            Log.d("fgh",txtQuan.getText().toString());
//            Log.d("fghfgh",CategoryGridList.get(position).getpPrice());
             /*   map.put("start_date", CategoryGridList.get(position).getStart_date());
                map.put("start_time", CategoryGridList.get(position).getStart_time());
                map.put("end_date", CategoryGridList.get(position).getEnd_date());
                map.put("end_time", CategoryGridList.get(position).getEnd_time());*/
            if (!txtQuan.getText().toString().equalsIgnoreCase("0")) {
                if (dbcart.isInCart(map.get("varient_id"))) {
                    dbcart.setCart(map, Integer.valueOf(txtQuan.getText().toString()));
                    Log.d("sdf", "update");
                    //  Toast.makeText(context, "Product quantity is updated in your cart", Toast.LENGTH_SHORT).show();

                } else {
                    dbcart.setCart(map, Integer.valueOf(txtQuan.getText().toString()));
                    //   Toast.makeText(context, "Product quantity is added in your cart", Toast.LENGTH_SHORT).show();

                }
            } else {
                dbcart.removeItemFromCart(map.get("varient_id"));
            }
            try {
                int items = (int) Double.parseDouble(dbcart.getInCartItemQty(map.get("varient_id")));
                Double price = Double.parseDouble(map.get("price").trim());
                Double mrp = Double.parseDouble(map.get("mrp").trim());
                //  Double reward = Double.parseDouble(map.get("rewards"));
                // tv_reward.setText("" + reward * items);
//                pDescrptn.setText(""+CategoryGridList.get(position).getpDes());
                pPrice.setText("" +price* items);
                txtQuan.setText("" + items);
                pMrp.setText("" + mrp* items );
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    //  ((MainActivity) context).setCartCounter(""  + dbcart.getCartCount());
                }
            }catch (IndexOutOfBoundsException e){
                e.toString();
                Log.d("qwer",e.toString());
            }
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



        }


    public CategoryGridAdapter(List<CategoryGrid> categoryGridList, Context context) {
        CategoryGridList = categoryGridList;
        dbcart = new DatabaseHandler(context);

    }

    @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_category_list, parent, false);
        context = parent.getContext();

            return new MyViewHolder(itemView);

    }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            dbcart=new DatabaseHandler(context);
            CategoryGrid cc = CategoryGridList.get(position);
            holder.prodNAme.setText(cc.getName());

            holder.pPrice.setText(cc.getPrice());
            holder.txt_unitvalue.setText(cc.getUnit());
            holder.pQuan.setText(cc.getQuantity());
            holder.pMrp.setText(cc.getMrp());

            Glide.with(context)
                    .load(IMG_URL+ cc.getImage())
                    .centerCrop()
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate()
                    .into(holder.image);
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent=new Intent(context, ProductDetails.class);
                    intent.putExtra("sId",cc.getId());
                    intent.putExtra("sName",cc.getName());
                    intent.putExtra("descrip",cc.getDescription());
                    intent.putExtra("price",cc.getPrice());
                    intent.putExtra("mrp",cc.getMrp());
                    intent.putExtra("unit",cc.getUnit());
                    intent.putExtra("qty",cc.getQuantity());
                    intent.putExtra("image",cc.getVarient_image());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    context.startActivity(intent);
                }
            });

            Glide.with(context)
                    .load(IMG_URL+ cc.getVarient_image())
                    .centerCrop()
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate()
                    .into(holder.image);


            holder.rlQuan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.layout_dialog_varient);
                    dialog.setCanceledOnTouchOutside(false);
                    TextView txt = dialog.findViewById(R.id.txt);
                    txt.setText(cc.getName());
                    cancl = dialog.findViewById(R.id.cancl);
                    recyler_popup = dialog.findViewById(R.id.recyclerVarient);
                    recyler_popup.setLayoutManager(new GridLayoutManager(context.getApplicationContext() , 1));

                    Varient_product(cc.getId());
                    cancl.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            dialog.dismiss();
                        }
                    });
                    recyler_popup.addOnItemTouchListener(new RecyclerTouchListener(context, recyler_popup, new RecyclerTouchListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {

                            String mrp= varientProducts.get(position).getVariant_mrp();
                            String price = varientProducts.get(position).getVariant_price();
                            String vQuan = varientProducts.get(position).getVariant_unit_value();
                            Log.d("asdff",cc.getPrice());

                            cc.setPrice(price);

                            Log.d("asdff",cc.getPrice());
                            holder.pMrp.setText(" " +mrp);
                            holder.pPrice.setText(" " +price);
                            holder.pQuan.setText(varientProducts.get(position).getVariant_unit());
//                            holder.txtQuan.setText(varientProducts.get(position).getVariant_unit());
                             holder.txt_unitvalue.setText(varientProducts.get(position).getVariant_unit_value());

                             cc.setVarient_image(varientProducts.get(position).getVarient_imqge());
                            Glide.with(context)
                                    .load(IMG_URL+ varientProducts.get(position).getVarient_imqge())
                                    .centerCrop()
                                    .crossFade()
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .dontAnimate()
                                    .into(holder.image);
                            dialog.dismiss();

                        }

                        @Override
                        public void onLongItemClick(View view, int position) {

                        }
                    }));


                    dialog.show();

                }
            });


        }

    private void Varient_product(String pId) {
        varientProducts.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ProductVarient, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Prod detail", response);
                try {
                    varientProducts.clear();
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String product_id = jsonObject1.getString("product_id");
                            String varient_id = jsonObject1.getString("varient_id");
                            String price = jsonObject1.getString("price");
                            String quantity = jsonObject1.getString("quantity");
                            String varient_image = jsonObject1.getString("varient_image");
                            String mrp = jsonObject1.getString("mrp");
                            String unit = jsonObject1.getString("unit");
                            String description = jsonObject1.getString("description");



                            // Picasso.get().load(IMG_URL+varient_image).into(pImage);
                            //prodMrp.setText(mrp);

                            varient_product selectCityModel = new varient_product();
                             selectCityModel.setVarient_imqge(IMG_URL+varient_image);
                            selectCityModel.setVariant_unit_value(unit);
                            selectCityModel.setVariant_price(price);
                            selectCityModel.setVariant_mrp(mrp);
                            selectCityModel.setVariant_unit(quantity);
                            selectCityModel.setVariant_id(varient_id);


                            varientProducts.add(selectCityModel);

                            Adapter_popup selectCityAdapter = new Adapter_popup(varientProducts);
                            recyler_popup.setAdapter(selectCityAdapter);



                        }

                    } else {
                        varientProducts.clear();
                        //JSONObject resultObj = jsonObject.getJSONObject("results");

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("product_id",pId);
                Log.d("kj",pId);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }

@Override
        public int getItemCount() {
            return CategoryGridList.size();
        }
}
