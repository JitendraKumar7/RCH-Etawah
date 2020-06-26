package com.rch.etawah.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rch.etawah.R;
import com.rch.etawah.util.DatabaseHandler;

import java.util.ArrayList;
import java.util.HashMap;


import static com.rch.etawah.Config.BaseURL.IMG_URL;
import static com.rch.etawah.fragments.CartFragment.tv_total;

/**
 * Created by Rajesh Dabhi on 26/6/2017.
 */

public class Cart_adapter extends RecyclerView.Adapter<Cart_adapter.ProductHolder> {
    ArrayList<HashMap<String, String>> list;
    Activity activity;
    String price_tx;
    SharedPreferences preferences;
    String language;

    int lastpostion;
    DatabaseHandler dbHandler;

    public Cart_adapter(Activity activity, ArrayList<HashMap<String, String>> list) {
        this.list = list;
        this.activity = activity;

        dbHandler = new DatabaseHandler(activity);
    }

    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_cart, parent, false);
        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductHolder holder, final int position) {

        final HashMap<String, String> map = list.get(position);
        Glide.with(activity)
                .load(IMG_URL + map.get("product_image"))
                .centerCrop()
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.iv_logo);

        holder.tv_title.setText(map.get("product_name"));
        holder.pDescrptn.setText(map.get("product_description"));
        int items = (int) Double.parseDouble(dbHandler.getInCartItemQty(map.get("varient_id")));
        int sprice = (int) Double.parseDouble(map.get("price"));
        Log.d("kjs", String.valueOf(sprice));

        holder.price = sprice;
//        int items = (int) Double.parseDouble(dbcart.getInCartItemQty(map.get("varient_id")));

        holder.pPrice.setText("" + sprice * items);
        //  Log.d("fgh",map.get("title"));
        Log.d("fghfgh", map.get("price"));
        tv_total.setText(activity.getResources().getString(R.string.currency) + " " + dbHandler.getTotalAmount());


        //  Double reward = Double.parseDouble(map.get("rewards"));

        //  holder.pPrice.setText(map.get("deal_price"));
        holder.tv_contetiy.setText("" + items);
        holder.minteger = items;
        holder.pQuan.setText("" + map.get("unit_value"));
        holder.pMrp.setText("" + map.get("price"));
        holder.pMrp.setPaintFlags(holder.pMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        //  holder.tv_reward.setText("" + reward * items);
       /* holder.iv_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qty = 0;
                if (!holder.tv_contetiy.getText().toString().equalsIgnoreCase(""))
                    qty = Integer.valueOf(holder.tv_contetiy.getText().toString());

                if (qty > 0) {
                    qty = qty - 1;
                    holder.tv_contetiy.setText(String.valueOf(qty));
                }

                if (holder.tv_contetiy.getText().toString().equalsIgnoreCase("0")) {
                    dbHandler.removeItemFromCart(map.get("product_id"));
                    list.remove(position);
                    notifyDataSetChanged();

                    updateintent();
                }
            }
        });*/

      /*  holder.iv_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int qty = Integer.valueOf(holder.tv_contetiy.getText().toString());
                qty = qty + 1;

                holder.tv_contetiy.setText(String.valueOf(qty));
            }
        });*/

        holder.tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.tv_add.setVisibility(View.GONE);
                holder.ll_addQuan.setVisibility(View.GONE);

                dbHandler.setCart(map, Integer.valueOf(holder.tv_contetiy.getText().toString()));
                Double items = Double.parseDouble(dbHandler.getInCartItemQty(map.get("varient_id")));
                Double price = Double.parseDouble(map.get("deal_price"));
                Double reward = Double.parseDouble(map.get("rewards"));
                holder.pPrice.setText("" + price * items);
                //  holder.tv_reward.setText("" + reward * items);
                //   holder.tv_total.setText(activity.getResources().getString(R.string.tv_cart_total) + price * items + " " + activity.getResources().getString(R.string.currency));
                updateintent();
            }
        });

        holder.btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHandler.removeItemFromCart(map.get("varient_id"));
                list.remove(position);
                notifyDataSetChanged();
                updateintent();
            }
        });
        holder.iv_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseInteger();
                updateMultiply();

                if (Integer.parseInt(holder.tv_contetiy.getText().toString()) == 1) {
                       /* minus.setClickable(false);
                        minus.setFocusable(false);*/
                } else if (Integer.parseInt(holder.tv_contetiy.getText().toString()) > 1) {
                    holder.iv_minus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            decreaseInteger();
                            updateMultiply();
                        }
                    });
                }
            }

            private void updateMultiply() {

                dbHandler.setCart(map, Integer.valueOf(holder.tv_contetiy.getText().toString()));
                Log.d("asfd", holder.tv_contetiy.getText().toString());


                int items = (int) Double.parseDouble(dbHandler.getInCartItemQty(map.get("varient_id")));
                //  Double price = Double.parseDouble(map.get("price"));
                Double reward = Double.parseDouble(dbHandler.getInCartItemQty(map.get("price")));
                holder.tv_contetiy.setText("" + Integer.valueOf(String.valueOf(items)));
                holder.pPrice.setText("" + holder.price * items);
                tv_total.setText(activity.getResources().getString(R.string.currency) + " " + dbHandler.getTotalAmount());

                //   holder.tv_total.setText(activity.getResources().getString(R.string.tv_cart_total) + price * items + " " + activity.getResources().getString(R.string.currency));
                updateintent();
            }

            public void increaseInteger() {
                holder.minteger = holder.minteger + 1;
                display(holder.minteger);
            }

            public void decreaseInteger() {
                if (holder.minteger == 1) {
                    holder.minteger = 1;
                    display(holder.minteger);
                    holder.ll_addQuan.setVisibility(View.GONE);
                    holder.tv_add.setVisibility(View.VISIBLE);
                } else {
                    holder.minteger = holder.minteger - 1;
                    display(holder.minteger);

                }
            }

            private void display(Integer number) {

                holder.tv_contetiy.setText("" + number);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ProductHolder extends RecyclerView.ViewHolder {
        public TextView tv_title, tv_contetiy, iv_plus, iv_minus, pDescrptn, pQuan, pPrice, pdiscountOff, pMrp, tv_unit, tv_unit_value;
        LinearLayout tv_add, ll_addQuan;
        public ImageView iv_logo, btnClose;

        int minteger = 0;
        int price = 0;

        public ProductHolder(View view) {
            super(view);

            tv_title = (TextView) view.findViewById(R.id.txt_pName);
            iv_logo = (ImageView) view.findViewById(R.id.prodImage);

            tv_contetiy = (TextView) view.findViewById(R.id.txtQuan);
            tv_add = view.findViewById(R.id.btn_Add);
            ll_addQuan = view.findViewById(R.id.ll_addQuan);
            iv_plus = view.findViewById(R.id.plus);
            iv_minus = view.findViewById(R.id.minus);

            pDescrptn = (TextView) view.findViewById(R.id.txt_pInfo);
            pQuan = (TextView) view.findViewById(R.id.txt_unit);
            pPrice = (TextView) view.findViewById(R.id.txt_Pprice);
            pdiscountOff = (TextView) view.findViewById(R.id.txt_discountOff);
            pMrp = (TextView) view.findViewById(R.id.txt_Mrp);

            btnClose = view.findViewById(R.id.btnClose);

            //  tv_add.setText(R.string.tv_pro_update);

        }
    }

    private void updateintent() {
        Intent updates = new Intent("Grocery_cart");
        updates.putExtra("type", "update");
        activity.sendBroadcast(updates);
    }

}

