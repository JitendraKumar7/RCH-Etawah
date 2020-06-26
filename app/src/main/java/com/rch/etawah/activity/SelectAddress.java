package com.rch.etawah.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rch.etawah.Config.BaseURL;
import com.rch.etawah.ModelClass.DeliveryModel;
import com.rch.etawah.R;
import com.rch.etawah.ui.AddAddressActivity;
import com.rch.etawah.ui.ActivityOrderSummary;
import com.rch.etawah.util.Session_management;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static com.rch.etawah.Config.BaseURL.SelectAddressURL;
import static com.rch.etawah.Config.BaseURL.ShowAddress;

public class SelectAddress extends AppCompatActivity {
    Session_management session_management;
    LinearLayout back,addAddress,delieverHere;
    RecyclerView recycleraddressList;
    ProgressDialog progressDialog;
     List<DeliveryModel> dlist;
    DeliveryAdapter deliveryAdapter;
    String dName,dId ,addId;
    String user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_address);
        dlist = new ArrayList<>();
        init();
    }

    private void init() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

       /* back=findViewById(R.id.back);
        addAddress=findViewById(R.id.addAdreess);
        delieverHere=findViewById(R.id.Btn_DeliverHere);
        recycleraddressList=findViewById(R.id.recycleraddressList);*/

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        Session_management sessionManagement = new Session_management(getApplicationContext());
        user_id = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);

        addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent In=new Intent(getApplicationContext(), AddAddressActivity.class);
                startActivity(In);
            }
        });
        delieverHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               finish();
            }
        });
        if(isOnline()){
            showAdreesUrl();


//            recycleraddressList.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recycleraddressList, new RecyclerTouchListener.OnItemClickListener() {
//                @Override
//                public void onItemClick(View view, int position) {
//                    dId = dlist.get(position).getId();
//                    dName = dlist.get(position).getName();
//                    Intent intent=new Intent(getApplicationContext(), OrderSummary.class);
//                    intent.putExtra("dId",dId);
//                    intent.putExtra("dName",dName);
//                    startActivity(intent);
//                }
//                @Override
//                public void onLongItemClick(View view, int position) {
//                }
//            }));
        }
    }
    private void showAdreesUrl() {



        StringRequest stringRequest = new StringRequest(Request.Method.POST, ShowAddress, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("adresssHoww",response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("message");
                    if (status.equals("1")){
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

                            DeliveryModel ss=new DeliveryModel(address_id,receiver_name,receiver_phone,house_no+", "+society
                            +","+cityyyy+", "+stateeee+", "+pincode);
                            ss.setCity_name(cityyyy);
                            ss.setHouse_no(house_no);
                            ss.setLandmark(landmark2);
                            ss.setPincode(pincode);
                            ss.setState(stateeee);
                            ss.setReceiver_phone(receiver_phone);
                            ss.setReceiver_name(receiver_name);

                            ss.setSociety(society);
                            dlist.add(ss);
                        }
                        deliveryAdapter = new DeliveryAdapter(dlist);
                        recycleraddressList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        recycleraddressList.setAdapter(deliveryAdapter);
                        deliveryAdapter.notifyDataSetChanged();

                    }
                    else {
                        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
                        /*Save.setVisibility(View.VISIBLE);
                        EditBtn.setVisibility(View.GONE);
                        saveAddress(cityId,socetyId,landmaarkkkk);*/
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
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> param = new HashMap<>();
                param.put("user_id",user_id);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(SelectAddress.this);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);

    }

    private void selectAddrsUrl() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SelectAddressURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("selectAdres",response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("message");
                    if (status.equals("1")){

                        Intent intent=new Intent(getApplicationContext(), ActivityOrderSummary.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
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
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> param = new HashMap<>();
                param.put("address_id","1");
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);

    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public class DeliveryAdapter extends RecyclerView.Adapter<DeliveryAdapter.MyViewHolder> {
        private List<DeliveryModel> dlist;
        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView name, phone, address;
            ImageView edit_address ;
            RadioButton radioButton;
            LinearLayout edit,layout;

            public MyViewHolder(View view) {
                super(view);
                radioButton=view.findViewById(R.id.radioButton);
                layout = view.findViewById(R.id.layout);
                edit_address = view.findViewById(R.id.edit_address);
                name = (TextView) view.findViewById(R.id.txt_name_myhistoryaddrss_item);
                phone = (TextView) view.findViewById(R.id.txt_mobileno_myaddrss_history);
                address = (TextView) view.findViewById(R.id.txt_address_myaddrss_history);
                edit=view.findViewById(R.id.edit);
            }}

        public DeliveryAdapter(List<DeliveryModel> dlist) {
            this.dlist = dlist;
        }
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_deliveryaddress, parent, false);
            return new MyViewHolder(itemView);
        }
        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            DeliveryModel dd = dlist.get(position);
            holder.name.setText(dd.getName());
            holder.address.setText(dd.getAddress());
            holder.phone.setText(dd.getAddress());

            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent=new Intent(getApplicationContext(), ActivityOrderSummary.class);
                    intent.putExtra("dId",dd.getId());
                    intent.putExtra("dName",dd.getName());
                    startActivity(intent);
                }
            });
            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(SelectAddress.this, AddAddress.class);
                    i.putExtra("update","UPDATE");
                    i.putExtra("addId",dd.getId());
                    i.putExtra("city_name",dd.getCity_name());
                    i.putExtra("society",dd.getSociety());
                    i.putExtra("receiver_name",dd.getReceiver_name());
                    i.putExtra("receiver_phone",dd.getReceiver_phone());
                    i.putExtra("house_no",dd.getHouse_no());
                    i.putExtra("landmark",dd.getLandmark());
                    i.putExtra("state",dd.getState());
                    i.putExtra("pincode",dd.getPincode());
                    Log.d("ff",dd.getId());
                    startActivity(i);
                }});
            holder.radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectAddrsUrl();
                }
            });
        }
        @Override
        public int getItemCount() {
            return dlist.size();
        }
    }

}
