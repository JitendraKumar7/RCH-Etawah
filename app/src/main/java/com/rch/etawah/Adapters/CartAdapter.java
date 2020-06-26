package com.rch.etawah.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rch.etawah.activity.ProductDetails;
import com.rch.etawah.ModelClass.CartModel;
import com.rch.etawah.ModelClass.varient_product;
import com.rch.etawah.R;
import com.rch.etawah.util.DatabaseHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.rch.etawah.Config.BaseURL.IMG_URL;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {
    SharedPreferences preferences;
    private DatabaseHandler dbcart;
    private List<CartModel> cartList;
    private final int limit = 4;

    Context context;

    private List<varient_product> varientProducts = new ArrayList<>();

    RecyclerView recyler_popup;
    LinearLayout cancl;
    String varient_id, product_id;

    public CartAdapter(Context context, List<CartModel> cartList) {
        this.cartList = cartList;
        dbcart = new DatabaseHandler(context);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView prodNAme, pDescrptn, pQuan, pPrice, pdiscountOff, pMrp, minus, plus, txtQuan;
        ImageView image;
        LinearLayout btn_Add, ll_addQuan;
        int minteger = 0;
        RelativeLayout rlQuan;
        String catId, catName;

        public MyViewHolder(View view) {
            super(view);
            prodNAme = (TextView) view.findViewById(R.id.txt_pName);
            pDescrptn = (TextView) view.findViewById(R.id.txt_pInfo);
            pQuan = (TextView) view.findViewById(R.id.txt_unit);
            pPrice = (TextView) view.findViewById(R.id.txt_Pprice);
            image = (ImageView) view.findViewById(R.id.prodImage);
            pdiscountOff = (TextView) view.findViewById(R.id.txt_discountOff);
            pMrp = (TextView) view.findViewById(R.id.txt_Mrp);
            rlQuan = view.findViewById(R.id.rlQuan);
            btn_Add = view.findViewById(R.id.btn_Add);
            ll_addQuan = view.findViewById(R.id.ll_addQuan);
            txtQuan = view.findViewById(R.id.txtQuan);
            minus = view.findViewById(R.id.minus);
            plus = view.findViewById(R.id.plus);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    catId = cartList.get(position).getpId();
                    catName = cartList.get(position).getpNAme();
                    Intent intent = new Intent(context, ProductDetails.class);
                    intent.putExtra("sId", catId);
                    intent.putExtra("sName", catName);
                    context.startActivity(intent);

                }
            });
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
//            map.put("varient_id",cartList.get(position).getpId());
            map.put("varient_id", cartList.get(position).getVarient_id());
            map.put("product_name", cartList.get(position).getpNAme());
            map.put("category_id", cartList.get(position).getpId());
            map.put("title", cartList.get(position).getpDes());
            map.put("price", cartList.get(position).getpPrice());
            map.put("mrp", cartList.get(position).getpMrp());
            Log.d("fd", cartList.get(position).getpImage());
            map.put("product_image", cartList.get(position).getpImage());
            map.put("status", cartList.get(position).getStatus());
            map.put("in_stock", cartList.get(position).getIn_stock());
            map.put("unit_value", cartList.get(position).getpQuan());
            map.put("unit", cartList.get(position).getUnit());
            map.put("increament", "0");
            map.put("rewards", "0");
            map.put("stock", "0");
            map.put("product_description", "0");

            Log.d("fgh", cartList.get(position).getUnit() + cartList.get(position).getpQuan());
            Log.d("fghfgh", cartList.get(position).getpPrice());
             /*   map.put("start_date", cartList.get(position).getStart_date());
                map.put("start_time", cartList.get(position).getStart_time());
                map.put("end_date", cartList.get(position).getEnd_date());
                map.put("end_time", cartList.get(position).getEnd_time());*/
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
                pDescrptn.setText("" + cartList.get(position).getpDes());
                pPrice.setText("" + price * items);
                txtQuan.setText("" + items);
                pMrp.setText("" + mrp * items);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    //  ((MainActivity) context).setCartCounter(""  + dbcart.getCartCount());
                }
            } catch (IndexOutOfBoundsException e) {
                e.toString();
                Log.d("qwer", e.toString());
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


        @Override
        public void onClick(View view) {

        }
    }

    public CartAdapter(List<CartModel> cartList, Context context) {
        this.cartList = cartList;
        dbcart = new DatabaseHandler(context);
    }

    @Override
    public CartAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_product_add, parent, false);
        context = parent.getContext();
        return new CartAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CartAdapter.MyViewHolder holder, int position) {

        dbcart = new DatabaseHandler(context);
        CartModel cc = cartList.get(position);
        holder.prodNAme.setText(cc.getpNAme());
        holder.pDescrptn.setText(cc.getpDes());
        holder.pQuan.setText(cc.getpQuan());
        holder.pPrice.setText(cc.getpPrice());
        holder.pdiscountOff.setText(cc.getDiscountOff());
        holder.pMrp.setText(cc.getpMrp());
        holder.pMrp.setPaintFlags(holder.pMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        Glide.with(context)
                .load(IMG_URL + cc.getpImage())
                .centerCrop()
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.image);


        Double items = Double.parseDouble(dbcart.getInCartItemQty(cartList.get(position).getpId()));
        Double price = Double.parseDouble(cartList.get(position).getpPrice());

    }


    @Override
    public int getItemCount() {

        if (cartList.size() > limit) {
            return limit;
        } else {
            return cartList.size();
        }
    }


}
