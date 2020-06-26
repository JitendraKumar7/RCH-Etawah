package com.rch.etawah.ui.fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.google.gson.Gson;
import com.rch.etawah.R;
import com.rch.etawah.app.ApiClient;
import com.rch.etawah.app.AppConstants;
import com.rch.etawah.ui.CartActivity;
import com.rch.etawah.ui.DashboardActivity;
import com.rch.etawah.ui.modal.CartHome;
import com.rch.etawah.util.CustomSlider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class HomeFragment extends Fragment implements AppConstants {

    private HomeAdapter mAdapter;
    private List<CartHome> mDataList;
    private SliderLayout ivBannerSlider;
    private ShimmerRecyclerView mRecyclerView;


    private void setIvBannerSlider() {

        ApiClient.getInstance(getActivity(), response -> {

            try {
                ArrayList<HashMap<String, String>> listArray = new ArrayList<>();

                JSONObject jsonObject = new JSONObject((String) response);
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {

                    HashMap<String, String> hashMap = new HashMap<>();
                    JSONObject object = jsonArray.getJSONObject(i);

                    hashMap.put("banner_id", object.getString("banner_id"));
                    hashMap.put("banner_name", object.getString("banner_name"));
                    hashMap.put("banner_image", BASE_URL + object.getString("banner_image"));
                    listArray.add(hashMap);

                    ivBannerSlider.setVisibility(View.VISIBLE);
                }
                for (HashMap<String, String> hashMap : listArray) {
                    CustomSlider textSliderView = new CustomSlider(getActivity());
                    textSliderView.image(hashMap.get("banner_image")).setScaleType(BaseSliderView.ScaleType.Fit);
                    textSliderView.getBundle().putString("extra", hashMap.get("banner_name"));
                    textSliderView.getBundle().putString("extra", hashMap.get("banner_id"));
                    textSliderView.description(hashMap.get(""));
                    textSliderView.bundle(new Bundle());

                    ivBannerSlider.addSlider(textSliderView);
                    textSliderView.setOnSliderClickListener(slider -> {

                    });
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }).getApiResponse(BANNER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_home, container, false);

        mAdapter = new HomeAdapter();
        mDataList = new ArrayList<>();

        ivBannerSlider = view.findViewById(R.id.ivBannerSlider);
        mRecyclerView = view.findViewById(R.id.shimmerRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ApiClient.getInstance(getActivity(), response -> {
            setIvBannerSlider();
            try {
                JSONObject jsonObject = new JSONObject((String) response);

                if (jsonObject.getString("status").equals("1")) {

                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    CartHome[] data = new Gson().fromJson(jsonArray.toString(), CartHome[].class);

                    mDataList.addAll(Arrays.asList(data));
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();

                } else {
                    ((DashboardActivity) getActivity()).
                            showToast(jsonObject.getString("message"));

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }).getApiResponse(HOME);

        view.findViewById(R.id.btnContinue).setOnClickListener(v -> {

            ArrayList<CartHome> mCartItems = new ArrayList<>();

            for (CartHome home : mDataList) {
                if (home.isAddInCart()) {
                    mCartItems.add(home);
                }
            }

            if (mCartItems.size() > 0) {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(SHARE, mCartItems);

                Intent intent = new Intent(getActivity(), CartActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
            // Empty cart
            else {
                ((DashboardActivity) getActivity()).showToast("No Item in Cart");
            }
        });

        return view;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        public TextView prodNAme, pDescrptn, pQuan, pPrice, pdiscountOff, pMrp, minus, plus, txtQuan;
        ImageView image;
        LinearLayout btn_Add, ll_addQuan;
        RelativeLayout rlQuan;

        public CustomViewHolder(View view) {
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

        }

        public void bindViewData(int position) {

            CartHome bean = mDataList.get(position);
            prodNAme.setText(bean.getProductName());
            pDescrptn.setText(bean.getDescription());
            pQuan.setText(bean.getQuantity());
            pPrice.setText(bean.getPrice());
            pdiscountOff.setText(bean.getDiscountOff());
            pMrp.setText(bean.getMrp());
            pMrp.setPaintFlags(pMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            Glide.with(getActivity()).load(BASE_URL + bean.getProductImage()).centerCrop().crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL).dontAnimate().into(image);

            txtQuan.setText(String.valueOf(bean.getCartValue()));

            btn_Add.setOnClickListener(v -> {
                btn_Add.setVisibility(View.GONE);
                ll_addQuan.setVisibility(View.VISIBLE);


                final int value = 1;

                txtQuan.setText(String.valueOf(value));
                mDataList.set(position, bean.setCartValue(value));
                mAdapter.notifyItemChanged(position, bean.setCartValue(value));

                mDataList.set(position, bean.setAddInCart(true));
                mAdapter.notifyItemChanged(position, bean.setAddInCart(true));
            });

            if (bean.isAddInCart()) {

                btn_Add.setVisibility(View.GONE);
                ll_addQuan.setVisibility(View.VISIBLE);
            }
            // Removed
            else {
                btn_Add.setVisibility(View.VISIBLE);
                ll_addQuan.setVisibility(View.GONE);
            }
            plus.setOnClickListener(v -> {

                final int value = bean.getCartValue() + 1;

                txtQuan.setText(String.valueOf(value));
                mDataList.set(position, bean.setCartValue(value));
                mAdapter.notifyItemChanged(position, bean.setCartValue(value));

            });

            minus.setOnClickListener(v1 -> {
                if (bean.getCartValue() > 1) {
                    final int value = bean.getCartValue() - 1;

                    txtQuan.setText(String.valueOf(value));
                    mDataList.set(position, bean.setCartValue(value));
                    mAdapter.notifyItemChanged(position, bean.setCartValue(value));
                }
                // Remove
                else {
                    btn_Add.setVisibility(View.VISIBLE);
                    ll_addQuan.setVisibility(View.GONE);

                    mDataList.set(position, bean.setAddInCart(false));
                    mAdapter.notifyItemChanged(position, bean.setAddInCart(false));
                }
            });

        }
    }

    public class HomeAdapter extends RecyclerView.Adapter<CustomViewHolder> {

        @SuppressWarnings("NullableProblems")
        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_product_add, parent, false);
            return new CustomViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(CustomViewHolder holder, int position) {

            holder.bindViewData(position);
        }

        @Override
        public int getItemCount() {


            return mDataList.size();
        }

    }
}
