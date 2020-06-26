package com.rch.etawah.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.rch.etawah.ModelClass.DeliveryModel;
import com.rch.etawah.R;
import com.rch.etawah.activity.AddAddress;
import com.rch.etawah.app.ApiClient;
import com.rch.etawah.app.BaseActivity;
import com.rch.etawah.app.SharedPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivitySelectAddress extends BaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private DeliveryAdapter mAdapter;
    private List<DeliveryModel> mDataList;

    @Override
    protected int getResourcesID() {

        return R.layout.activity_select_address;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);
        mDataList = new ArrayList<>();

        findViewById(R.id.btnBack).setOnClickListener(v -> onBackPressed());
        findViewById(R.id.btnAddAddress).setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                startActivity(AddAddressActivity.class, null);
                return;
            }
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    100);
        });

        String obj = SharedPreference.getString(USER_DATA);
        User user = new Gson().fromJson(obj, User.class);
        HashMap<String, String> param = new HashMap<>();
        param.put("user_id", user.getUserId());

        ApiClient.getInstance(getActivity(), response -> {
            try {
                JSONObject jsonObject = new JSONObject((String) response);
                String status = jsonObject.getString("status");
                String msg = jsonObject.getString("message");
                if (status.equals("1")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String address_id = jsonObject1.getString("address_id");
                        String user_id = jsonObject1.getString("user_id");
                        String receiver_name = jsonObject1.getString("receiver_name");
                        String receiver_phone = jsonObject1.getString("receiver_phone");
                        String cityyyy = jsonObject1.getString("city");
                        String society = jsonObject1.getString("society");
                        String house_no = jsonObject1.getString("house_no");
                        String pincode = jsonObject1.getString("pincode");
                        String stateeee = jsonObject1.getString("state");
                        String landmark2 = jsonObject1.getString("landmark");

                        DeliveryModel ss = new DeliveryModel(address_id, receiver_name, receiver_phone, house_no + ", " + society
                                + "," + cityyyy + ", " + stateeee + ", " + pincode);
                        ss.setCity_name(cityyyy);
                        ss.setHouse_no(house_no);
                        ss.setLandmark(landmark2);
                        ss.setPincode(pincode);
                        ss.setState(stateeee);
                        ss.setReceiver_phone(receiver_phone);
                        ss.setReceiver_name(receiver_name);

                        ss.setSociety(society);
                        mDataList.add(ss);
                    }
                    mAdapter = new DeliveryAdapter();
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    mRecyclerView.setAdapter(mAdapter);

                } else {
                    showToast(msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }).getApiResponse(SHOW_ADDRESS, param);


    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        public TextView name, phone, address;
        ImageView edit_address;
        RadioButton radioButton;
        LinearLayout edit, layout;

        public CustomViewHolder(View view) {
            super(view);
            radioButton = view.findViewById(R.id.radioButton);
            layout = view.findViewById(R.id.layout);
            edit_address = view.findViewById(R.id.edit_address);
            name = (TextView) view.findViewById(R.id.txt_name_myhistoryaddrss_item);
            phone = (TextView) view.findViewById(R.id.txt_mobileno_myaddrss_history);
            address = (TextView) view.findViewById(R.id.txt_address_myaddrss_history);
            edit = view.findViewById(R.id.edit);
        }
    }

    public class DeliveryAdapter extends RecyclerView.Adapter<CustomViewHolder> {

        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_deliveryaddress, parent, false);
            return new CustomViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(CustomViewHolder holder, int position) {
            DeliveryModel dd = mDataList.get(position);
            holder.name.setText(dd.getName());
            holder.address.setText(dd.getAddress());
            holder.phone.setText(dd.getAddress());

            holder.layout.setOnClickListener(v -> {

                String obj = SharedPreference.getString(USER_DATA);
                User user = new Gson().fromJson(obj, User.class);
                HashMap<String, String> param = new HashMap<>();
                param.put("user_id", user.getUserId());
                param.put("address_id", dd.getId());
                ApiClient.getInstance(getActivity(), response -> {
                    try {
                        JSONObject jsonObject = new JSONObject((String) response);
                        if (jsonObject.getString("status").equals("1")) {
                            Bundle bundle = getIntent().getExtras();
                            bundle.putSerializable(SHARE_ADDRESS, dd);
                            startActivity(ActivityOrderSummary.class, bundle);
                        } else {
                            showToast(jsonObject.getString("message"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }).getApiResponse(SELECT_ADDRESS, param);
            });

            holder.edit.setOnClickListener(v -> {
                Intent i = new Intent(ActivitySelectAddress.this, AddAddress.class);
                i.putExtra("update", "UPDATE");
                i.putExtra("addId", dd.getId());
                i.putExtra("city_name", dd.getCity_name());
                i.putExtra("society", dd.getSociety());
                i.putExtra("receiver_name", dd.getReceiver_name());
                i.putExtra("receiver_phone", dd.getReceiver_phone());
                i.putExtra("house_no", dd.getHouse_no());
                i.putExtra("landmark", dd.getLandmark());
                i.putExtra("state", dd.getState());
                i.putExtra("pincode", dd.getPincode());
                Log.d("ff", dd.getId());
                startActivity(i);
            });

        }

        @Override
        public int getItemCount() {
            return mDataList.size();
        }
    }

}
