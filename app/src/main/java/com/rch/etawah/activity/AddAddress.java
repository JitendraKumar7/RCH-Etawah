package com.rch.etawah.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;
import com.rch.etawah.Adapters.SearchAdapter;
import com.rch.etawah.Config.BaseURL;
import com.rch.etawah.Constans.RecyclerTouchListener;
import com.rch.etawah.ModelClass.SearchModel;
import com.rch.etawah.R;
import com.rch.etawah.util.Session_management;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.rch.etawah.Config.BaseURL.Add_address;
import static com.rch.etawah.Config.BaseURL.CityListUrl;
import static com.rch.etawah.Config.BaseURL.EditAddress;
import static com.rch.etawah.Config.BaseURL.ShowAddress;
import static com.rch.etawah.Config.BaseURL.SocietyListUrl;

public class AddAddress extends AppCompatActivity {
    Session_management session_management;
    LinearLayout back;
    Button Save, EditBtn;
    EditText pinCode, houseNo, Area, city, state, landmaark, name, mobNo, alterMob;
    TextInputLayout tpinCode, thouseNo, tArea, tcity, tstate, tlandmaark, tname, tmobNo, talterMob;

    RadioGroup radioGroup;
    RadioButton rHome, rWork;
    CardView currentLoc;
    String user_id;
    RecyclerView recyclerViewCity, recyclerViewSociety;
    String cityId, cityName, socetyId, SocetyName, landmaarkkkk, updtae, addressId, receiver_name, receiver_phone, house_no, landmark, state_st, pincode;
    ProgressDialog progressDialog;
    SearchAdapter cityAdapter, societyAdapter;
    List<SearchModel> citylist = new ArrayList<>();
    List<SearchModel> societylist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        init();
    }

    private void init() {
        session_management = new Session_management(AddAddress.this);

        cityName = getIntent().getStringExtra("city_name");
        receiver_name = getIntent().getStringExtra("receiver_name");
        receiver_phone = getIntent().getStringExtra("receiver_phone");
        house_no = getIntent().getStringExtra("house_no");
        landmark = getIntent().getStringExtra("landmark");
        state_st = getIntent().getStringExtra("state");
        pincode = getIntent().getStringExtra("pincode");
        SocetyName = getIntent().getStringExtra("society");


        back = findViewById(R.id.back);
        Save = findViewById(R.id.SaveBtn);
        EditBtn = findViewById(R.id.EditBtn);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        if (updtae != "") {
            updtae = getIntent().getStringExtra("update");
            addressId = getIntent().getStringExtra("addId");
            //     Log.d("fgh",addressId);
            Save.setVisibility(View.GONE);
            EditBtn.setVisibility(View.VISIBLE);
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        Session_management sessionManagement = new Session_management(getApplicationContext());
        user_id = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);

        EditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isOnline()) {
                    if (landmaarkkkk == "") {
                        landmaarkkkk = "NA";
                    } else {
                        landmaarkkkk = landmaark.getText().toString();

                    }
                    editAdressURl(addressId, cityName, SocetyName, landmaarkkkk);
                }
            }
        });
        currentLoc = findViewById(R.id.currentLoc);

        recyclerViewCity = findViewById(R.id.recyclerCity);
        recyclerViewSociety = findViewById(R.id.recyclerSociety);

        tpinCode = findViewById(R.id.input_layout_pinCode);
        thouseNo = findViewById(R.id.input_layout_HOuseNo);
        tArea = findViewById(R.id.input_layout_area);
        tcity = findViewById(R.id.input_layout_CIty);
        tstate = findViewById(R.id.input_layout_state);
        tlandmaark = findViewById(R.id.input_layout_landmark);
        tname = findViewById(R.id.input_layout_NAme);
        tmobNo = findViewById(R.id.input_layout_mobNo);
        talterMob = findViewById(R.id.input_layout_AltermobileNO);
        pinCode = (EditText) findViewById(R.id.input_pinCode);
        houseNo = (EditText) findViewById(R.id.input_HouseNO);
        Area = (EditText) findViewById(R.id.input_area);
        city = (EditText) findViewById(R.id.input_city);
        state = (EditText) findViewById(R.id.input_state);
        landmaark = (EditText) findViewById(R.id.input_landmark);
        name = (EditText) findViewById(R.id.input_NAme);
        mobNo = (EditText) findViewById(R.id.input_mobNO);
        alterMob = (EditText) findViewById(R.id.input_AltermobileNO);

        pinCode.setText(pincode);
        houseNo.setText(house_no);
        city.setText(cityName);
        state.setText(state_st);
        Area.setText(SocetyName);
        landmaark.setText(landmark);
        name.setText(receiver_name);
        mobNo.setText(receiver_phone);


        if (isOnline()) {

//            showAdreesUrl();
            Area.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressDialog.show();
                    societyURl(cityId);
                    progressDialog.dismiss();

                }
            });

            city.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressDialog.show();
                    cityUrl();
                    progressDialog.dismiss();
                }
            });
            recyclerViewCity.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerViewCity, new RecyclerTouchListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    cityId = citylist.get(position).getId();
                    cityName = citylist.get(position).getpNAme();
                    city.setText(cityName);
                    recyclerViewCity.setVisibility(View.GONE);
                }

                @Override
                public void onLongItemClick(View view, int position) {

                }
            }));
            recyclerViewSociety.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerViewSociety, new RecyclerTouchListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    socetyId = societylist.get(position).getId();
                    SocetyName = societylist.get(position).getpNAme();
                    Area.setText(SocetyName);
                    recyclerViewSociety.setVisibility(View.GONE);
                }

                @Override
                public void onLongItemClick(View view, int position) {

                }
            }));
        }
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (pinCode.getText().toString().trim() == "") {
                    Toast.makeText(getApplicationContext(), "Enter Pincode", Toast.LENGTH_SHORT).show();
                } else if (houseNo.getText().toString().trim() == "") {
                    Toast.makeText(getApplicationContext(), "Enter House No., Building Name", Toast.LENGTH_SHORT).show();
                } else if (Area.getText().toString().trim() == "") {
                    Toast.makeText(getApplicationContext(), "Enter Area, Colony", Toast.LENGTH_SHORT).show();
                } else if (city.getText().toString().trim() == "") {
                    Toast.makeText(getApplicationContext(), "Enter City", Toast.LENGTH_SHORT).show();
                } else if (state.getText().toString().trim() == "") {
                    Toast.makeText(getApplicationContext(), "Enter State", Toast.LENGTH_SHORT).show();
                } else if (name.getText().toString().trim() == "") {
                    Toast.makeText(getApplicationContext(), "Enter your Name", Toast.LENGTH_SHORT).show();
                } else if (mobNo.getText().toString().trim() == "") {
                    Toast.makeText(getApplicationContext(), "Enter Mobile No.", Toast.LENGTH_SHORT).show();
                } else if (!isOnline()) {
                    Toast.makeText(getApplicationContext(), "Check your Internet Connection!", Toast.LENGTH_SHORT).show();

                } else {
                    if (landmaarkkkk == "") {
                        landmaarkkkk = "NA";
                    } else {
                        landmaarkkkk = landmaark.getText().toString();

                    }
                    saveAddress(cityName, SocetyName, landmaarkkkk);
                }
                /* */
            }
        });
       /* currentLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               *//* Intent In=new Intent(getApplicationContext(),OrderSummary.class);
                startActivity(In);
                finish();*//*
            }
        });*/
    }

    private void editAdressURl(String addressId, String cityName, String soctyName, String landmaarkkkk) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EditAddress, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("editttadrss", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        String msg = jsonObject.getString("message");
//                        JSONObject resultObj = jsonObject.getJSONObject("data");

                        Intent intent = new Intent(getApplicationContext(), SelectAddress.class);
                        startActivity(intent);
                        finish();

                    } else {
                        JSONObject resultObj = jsonObject.getJSONObject("results");
                        String msg = resultObj.getString("msg");
                        Toast.makeText(getApplicationContext(), msg + "", Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();

                param.put("address_id", addressId);
                param.put("user_id", session_management.getUserDetails().get(BaseURL.KEY_ID));
                param.put("receiver_name", name.getText().toString());
                param.put("receiver_phone", mobNo.getText().toString());
                param.put("city_name", cityName);
                param.put("society_name", SocetyName);
                param.put("house_no", houseNo.getText().toString());
                param.put("landmark", landmaarkkkk);
                param.put("state", state.getText().toString());
                param.put("pin", pinCode.getText().toString());

                //  Log.d("fgh",session_management.getUserDetails().get(BaseURL.KEY_ID));
//                Log.d("cityId",cityId);
//                Log.d("socetyId",soctyName);
//                Log.d("fgh",name.getText().toString());
//                Log.d("cityId",mobNo.getText().toString());
//                Log.d("socetyId",houseNo.getText().toString());

                return param;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(AddAddress.this);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);

    }

    private void showAdreesUrl() {
        String tag_json_obj = "json_socity_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", user_id);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ShowAddress, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("adresssHoww", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("message");
                    if (status.equals("1")) {
                        // citylist.clear();
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
                            String landmark = jsonObject1.getString("landmark");
                            String pincode = jsonObject1.getString("pincode");
                            String stateeee = jsonObject1.getString("state");
                            String landmark2 = jsonObject1.getString("landmark");

                            pinCode.setText(pincode);
                            houseNo.setText(house_no);
                            city.setText(cityyyy);
                            state.setText(stateeee);
                            Area.setText(society);
                            landmaark.setText(landmark2);
                            name.setText(receiver_name);
                            mobNo.setText(receiver_phone);
                            //  alterMob.setText("");

                        }
                    } else {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                        Save.setVisibility(View.VISIBLE);
                        EditBtn.setVisibility(View.GONE);
                        saveAddress(cityId, socetyId, landmaarkkkk);
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
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                param.put("user_id", session_management.getUserDetails().get(BaseURL.KEY_ID));
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(AddAddress.this);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);

    }


    private void societyURl(String cityId) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SocietyListUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("socirrty", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("message");
                    if (status.equals("1")) {
                        societylist.clear();
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String society_id = jsonObject1.getString("society_id");
                            String society_name = jsonObject1.getString("society_name");
                            recyclerViewSociety.setVisibility(View.VISIBLE);
                            SearchModel ss = new SearchModel(society_id, society_name);
                            societylist.add(ss);
                        }
                        societyAdapter = new SearchAdapter(societylist);
                        recyclerViewSociety.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        recyclerViewSociety.setAdapter(societyAdapter);
                        societyAdapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
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
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                param.put("city_id", cityId);
                return param;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(AddAddress.this);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);

    }

    private void cityUrl() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, CityListUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("cityyyyyyyy", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("message");
                    if (status.equals("1")) {
                        citylist.clear();
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String city_id = jsonObject1.getString("city_id");
                            String city_name = jsonObject1.getString("city_name");

                            recyclerViewCity.setVisibility(View.VISIBLE);
                            SearchModel cs = new SearchModel(city_id, city_name);
                            citylist.add(cs);
                        }
                        cityAdapter = new SearchAdapter(citylist);
                        recyclerViewCity.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        recyclerViewCity.setAdapter(cityAdapter);
                        cityAdapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
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
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(AddAddress.this);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);

    }


    private void saveAddress(String cityName, String soctyName, String landmaarkkkk) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Add_address, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("addadrss", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("message");
                    if (status.equalsIgnoreCase("1")) {


                        Toast.makeText(getApplicationContext(), msg + "", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), SelectAddress.class);
                        startActivity(intent);
                        finish();

                    } else {

                        Toast.makeText(getApplicationContext(), msg + "", Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                param.put("user_id", session_management.getUserDetails().get(BaseURL.KEY_ID));
                param.put("receiver_name", name.getText().toString());
                param.put("receiver_phone", mobNo.getText().toString());
                param.put("city_name", cityName);
                param.put("society_name", soctyName);
                param.put("house_no", houseNo.getText().toString());
                param.put("landmark", "NA");
                param.put("state", state.getText().toString());
                param.put("pin", pinCode.getText().toString());

                Log.d("fgh", session_management.getUserDetails().get(BaseURL.KEY_ID));
                Log.d("cityId", cityName);
                Log.d("socetyId", soctyName);
                Log.d("fgh", name.getText().toString());
                Log.d("cityId", mobNo.getText().toString());
                Log.d("socetyId", houseNo.getText().toString());

                return param;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(AddAddress.this);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);

    }


    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}
