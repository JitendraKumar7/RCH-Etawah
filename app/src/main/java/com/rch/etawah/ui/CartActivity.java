package com.rch.etawah.ui;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rch.etawah.R;
import com.rch.etawah.app.BaseActivity;
import com.rch.etawah.ui.modal.CartHome;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CartActivity extends BaseActivity {

    @BindView(R.id.txtTotalAmount)
    TextView tvTotalAmount;

    @BindView(R.id.txtTotalQuantity)
    TextView tvTotalQuantity;

    @BindView(R.id.btnCheckout)
    LinearLayout btnCheckout;

    @BindView(R.id.recyclerCart)
    RecyclerView mRecyclerView;

    private CartAdapter mAdapter;
    private ArrayList<CartHome> mDataList;

    private void setAmountValue() {

        double totalAmount = 0.0;
        for (CartHome cart : mDataList) {

            totalAmount += cart.getCartPrice();
        }


        tvTotalAmount.setText(String.format("%s %s", getResources().getString(R.string.currency), totalAmount));
        tvTotalQuantity.setText(String.format("%s  Total Items:", mDataList.size()));
    }

    @Override
    protected int getResourcesID() {

        return R.layout.activity_cart;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);
        mDataList = getIntent().getParcelableArrayListExtra(SHARE);

        setAmountValue();
        mAdapter = new CartAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        btnCheckout.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(SHARE, mDataList);
            startActivity(ActivitySelectAddress.class, bundle);
        });

    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_title, tv_contetiy, iv_plus, iv_minus, pDescrptn, pQuan, pPrice, pdiscountOff, pMrp;
        LinearLayout tv_add, ll_addQuan;
        public ImageView iv_logo, btnClose;

        public CustomViewHolder(View view) {
            super(view);

            tv_title = (TextView) view.findViewById(R.id.txt_pName);
            iv_logo = (ImageView) view.findViewById(R.id.prodImage);

            pQuan = (TextView) view.findViewById(R.id.txtQuan);

            tv_add = view.findViewById(R.id.btn_Add);
            ll_addQuan = view.findViewById(R.id.ll_addQuan);
            iv_plus = view.findViewById(R.id.plus);
            iv_minus = view.findViewById(R.id.minus);

            pDescrptn = (TextView) view.findViewById(R.id.txt_pInfo);
            tv_contetiy = (TextView) view.findViewById(R.id.txt_unit);
            pPrice = (TextView) view.findViewById(R.id.txt_Pprice);
            pdiscountOff = (TextView) view.findViewById(R.id.txt_discountOff);
            pMrp = (TextView) view.findViewById(R.id.txt_Mrp);

            btnClose = view.findViewById(R.id.btnClose);

            //  tv_add.setText(R.string.tv_pro_update);

        }
    }

    public class CartAdapter extends RecyclerView.Adapter<CustomViewHolder> {

        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = null;
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_cart, parent, false);
            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final CustomViewHolder holder, final int position) {

            final CartHome bean = mDataList.get(position);
            Glide.with(getActivity()).load(BASE_URL + bean.getProductImage()).centerCrop()
                    .crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).dontAnimate().into(holder.iv_logo);

            holder.tv_title.setText(bean.getProductName());
            holder.pDescrptn.setText(bean.getDescription());

            holder.pPrice.setText(String.valueOf(bean.getCartPrice()));
            holder.pQuan.setText(String.valueOf(bean.getCartValue()));


            holder.tv_contetiy.setText(String.format("%s %s", bean.getUnit(), bean.getQuantity()));

            holder.pMrp.setText(bean.getMrp());
            holder.pMrp.setPaintFlags(holder.pMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            holder.btnClose.setOnClickListener(view -> {

                if (mDataList.size() == 1) {
                    finish();
                }
                // not empty
                else {
                    mDataList.remove(position);
                    notifyDataSetChanged();
                    setAmountValue();
                }
            });

            holder.iv_plus.setOnClickListener(v -> {

                final int value = bean.getCartValue() + 1;
                holder.pQuan.setText(String.valueOf(bean.getCartValue()));

                mDataList.set(position, bean.setCartValue(value));
                mAdapter.notifyItemChanged(position, bean.setCartValue(value));

                setAmountValue();

            });

            holder.iv_minus.setOnClickListener(v1 -> {
                if (bean.getCartValue() > 1) {
                    final int value = bean.getCartValue() - 1;
                    holder.pQuan.setText(String.valueOf(bean.getCartValue()));

                    mDataList.set(position, bean.setCartValue(value));
                    mAdapter.notifyItemChanged(position, bean.setCartValue(value));

                    setAmountValue();
                }
                // Remove
                else {

                    mDataList.set(position, bean.setAddInCart(false));
                    mAdapter.notifyItemChanged(position, bean.setAddInCart(false));

                    setAmountValue();
                }
            });


        }

        @Override
        public int getItemCount() {
            if (mDataList == null) {
                finish();
                return 0;
            }
            return mDataList.size();
        }

    }

}
