package com.rch.etawah.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rch.etawah.ModelClass.DeliveryModel;
import com.rch.etawah.R;
import com.rch.etawah.activity.PaymentDetails;
import com.rch.etawah.app.ApiClient;
import com.rch.etawah.app.BaseActivity;
import com.rch.etawah.app.SharedPreference;
import com.rch.etawah.ui.modal.CartHome;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import butterknife.BindView;

public class ActivityOrderSummary extends BaseActivity {

    @BindView(R.id.recyclerView)
    LinearLayout mRecyclerView;

    @BindView(R.id.txtDeliveryAddress)
    TextView tvDeliveryAddress;

    @BindView(R.id.txtTotalItems)
    TextView tvTotalItems;

    @BindView(R.id.txtTotalPrice)
    TextView tvTotalPrice;

    @BindView(R.id.txtTotalCharge)
    TextView tvTotalCharge;

    @BindView(R.id.txtDeliveryCharge)
    TextView tvDeliveryCharge;

    public static String cart_id;
    private double totalAmount = 0.0;

    @Override
    protected int getResourcesID() {

        return R.layout.activity_order_summary;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        findViewById(R.id.btnBack).setOnClickListener(v -> onBackPressed());

        ArrayList<CartHome> mDataList = getIntent().getParcelableArrayListExtra(SHARE);


        Bundle bundle = getIntent().getExtras();
        DeliveryModel dd = (DeliveryModel) bundle.getSerializable(SHARE_ADDRESS);
        String ADDRESS = dd.getHouse_no() + " " + dd.getSociety() + " " + dd.getCity_name()
                + " " + dd.getLandmark() + " " + dd.getState() + " " + dd.getPincode();
        tvDeliveryAddress.setText(ADDRESS);


        JSONArray array = new JSONArray();
        for (CartHome cart : mDataList) {

            totalAmount += cart.getCartPrice();

            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put("qty", cart.getCartValue());
                jsonObject.put("varient_id", cart.getVarientId());

                array.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            TextView textView = new TextView(getActivity());
            textView.setPadding(10, 10, 10, 10);
            textView.setText(cart.getProductName());
            mRecyclerView.addView(textView);
        }

        tvTotalPrice.setText(String.format("\u20B9 %s", totalAmount));

        tvTotalCharge.setText(String.format("\u20B9 %s", totalAmount));

        tvTotalItems.setText(String.format("Price ( %s  Items)", mDataList.size()));

        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);
        String obj = SharedPreference.getString(USER_DATA);
        User user = new Gson().fromJson(obj, User.class);

        String todayTime = String.valueOf(DateFormat.format("hh:mm a", startDate));
        String todayDate = String.valueOf(DateFormat.format("yyyy-MM-dd", startDate));
        findViewById(R.id.btnContinue).setOnClickListener(v -> {


            HashMap<String, String> param = new HashMap<>();
            param.put("time_slot", todayTime);
            param.put("address_id", dd.getId());
            param.put("delivery_date", todayDate);
            param.put("user_id", user.getUserId());
            param.put("order_array", array.toString());
            ApiClient.getInstance(getActivity(), response -> {
                try {
                    JSONObject jsonObject = new JSONObject((String) response);
                    if (jsonObject.getString("status").equals("1")) {

                        JSONObject object = jsonObject.getJSONObject("data");

                        cart_id = object.getString("cart_id");
                        Intent intent = new Intent(getActivity(), PaymentDetails.class);
                        intent.putExtra("order_amt", String.valueOf(totalAmount));
                        intent.putExtra("cart_id", cart_id);
                        startActivity(intent);
                    } else {
                        showToast(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }).getApiResponse(MAKE_AN_ORDER, param);

        });

        ApiClient.getInstance(getActivity(), object -> {
            try {
                JSONObject response = new JSONObject((String) object);

                if (response.getString("status").contains("1")) {

                    JSONObject jsonObject = response.getJSONObject("data");

                    String min_cart_value = jsonObject.getString("min_cart_value");

                    String del_charge = jsonObject.getString("del_charge");

                    if (Integer.parseInt(min_cart_value) >= 500) {
                        showToast("Minimum Order Amount Should be Greater then ₹500");

                        tvDeliveryCharge.setText(String.format("\u20B9 %s", 0));
                    } else {

                        tvDeliveryCharge.setText(String.format("\u20B9 %s", del_charge));

                        totalAmount += Double.parseDouble(del_charge);
                        tvTotalCharge.setText(String.format("\u20B9 %s", totalAmount));
                    }
                } else {
                    showToast(response.getString("message"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }).getApiResponse(DELIVERY_INFO);

    }

}
