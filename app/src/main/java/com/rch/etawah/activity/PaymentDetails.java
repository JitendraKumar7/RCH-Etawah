package com.rch.etawah.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.rch.etawah.Config.BaseURL;
import com.rch.etawah.Config.SharedPref;
import com.rch.etawah.ModelClass.CoupunModel;
import com.rch.etawah.R;
import com.rch.etawah.app.AppController;
import com.rch.etawah.ui.ActivityOrderSummary;
import com.rch.etawah.util.ConnectivityReceiver;
import com.rch.etawah.util.CustomVolleyJsonRequest;
import com.rch.etawah.util.DatabaseHandler;
import com.rch.etawah.util.NetworkConnection;
import com.rch.etawah.util.Session_management;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;
import static com.rch.etawah.Config.BaseURL.Wallet_CHECKOUT;

public class PaymentDetails extends AppCompatActivity implements PaymentResultListener {

    LinearLayout backcart;
    int status = 0;
    String payment_method;
    Button confirm;
    String wallet_amount = "00";
    String wallet_status = "no";

    String total_amt;
    private Session_management sessionManagement;
    private String getlocation_id = "";
    private String getstore_id = "";

    private String gettime = "";
    private String getdate = "";
    private String getuser_id = "";
    RadioButton rb_Store, rb_Cod, rb_card;
    CheckBox checkBox_coupon;
    EditText et_Coupon;
    String getvalue;
    TextView linea, lineb;
    RadioGroup radioGroup;


    String Prefrence_TotalAmmount;
    String lat, lng;
    String getwallet;
    LinearLayout Promo_code_layout;
    RelativeLayout Apply_Coupon_Code;

    SharedPreferences sharedPreferences12;
    SharedPreferences.Editor editor12;
    String code, cart_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);
        backcart = findViewById(R.id.backcart);
        confirm = findViewById(R.id.confirm_order);
        cart_id = ActivityOrderSummary.cart_id;
        linea = findViewById(R.id.line1);

        total_amt = getIntent().getStringExtra("order_amt");
        backcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });


        Prefrence_TotalAmmount = SharedPref.getString(getApplicationContext(), BaseURL.TOTAL_AMOUNT);

        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                getvalue = radioButton.getText().toString();
            }
        });

        sharedPreferences12 = getSharedPreferences("loction", MODE_PRIVATE);
        editor12 = sharedPreferences12.edit();
        lat = sharedPreferences12.getString("lat", "77.1111");
        lng = sharedPreferences12.getString("lng", "22.02002");

        Log.e("LAnt", lat + "\n" + lng);


        rb_Store = findViewById(R.id.use_store_pickup);
        rb_Cod = findViewById(R.id.use_COD);
        rb_card = findViewById(R.id.use_card);
        checkBox_coupon = findViewById(R.id.use_coupon);
        checkBox_coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Coupen.class));
            }
        });
        et_Coupon = findViewById(R.id.et_coupon_code);

        Promo_code_layout = findViewById(R.id.prommocode_layout);
        Apply_Coupon_Code = findViewById(R.id.apply_coupoun_code);

        code = getIntent().getStringExtra("code");

        if (code == null) {
            code = "";
            status = 1;
            Promo_code_layout.setVisibility(View.GONE);
            checkBox_coupon.setChecked(false);

        } else {

            checkBox_coupon.setChecked(true);
            et_Coupon.setText(code);
            Promo_code_layout.setVisibility(View.VISIBLE);
        }

        Log.d("dff", code);
//        checkBox_coupon.setTypeface(font);

        Apply_Coupon_Code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Coupen.class));
                apply();
            }
        });


        sessionManagement = new Session_management(this);


        //Show  Wallet
        getwallet = SharedPref.getString(getApplicationContext(), BaseURL.KEY_WALLET_Ammount);

        getRefresrh();
        rewardliness();

        sessionManagement = new Session_management(this);
        final String email = sessionManagement.getUserDetails().get(BaseURL.KEY_EMAIL);
        final String mobile = sessionManagement.getUserDetails().get(BaseURL.KEY_MOBILE);
        final String name = sessionManagement.getUserDetails().get(BaseURL.KEY_NAME);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (status == 1) {
                    startActivity(new Intent(getApplicationContext(), OrderSuccessful.class));

                    makeAddOrderRequest(getuser_id, cart_id, payment_method, wallet_status, "success");
                    return;
                }
                if (status == 2) {
                    startActivity(new Intent(getApplicationContext(), OrderSuccessful.class));
                    makeAddOrderRequest(getuser_id, cart_id, payment_method, wallet_status, "success");

                    return;
                }
                if (status == 3) {
                    startPayment(name, total_amt, email, mobile);
                    return;
                }

                if (status == 0) {

                    Toast.makeText(PaymentDetails.this, "Select One plz", Toast.LENGTH_SHORT).show();
                }

//                    confirm.setEnabled(false);
//                    if (checkBox_Wallet.isChecked()){
//
//
//                        getuser_id = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);
//
//                        Usewalletfororder(getuser_id,Used_Wallet_amount);
//                        checked();
//
//                    }
//                    else {
//                        checked();
//
//                    }
//
//
//
//                } else {
//                    confirm.setEnabled(true);
//
//                    //  ((MainActivity) getActivity()).onNetworkConnectionChanged(false);
//                }

            }
        });
//


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // This will get the radiobutton that has changed in its check state
                RadioButton checkedRadioButton = (RadioButton) group.findViewById(checkedId);
                // This puts the value (true/false) into the variable
                boolean isChecked = checkedRadioButton.isChecked();
                // If the radiobutton that has changed in check state is now checked...
                if (isChecked) {
                    checkBox_coupon.setEnabled(false);
                    // Changes the textview's text to "Checked: example radiobutton text"
                    Log.d("afs", checkedRadioButton.getText().toString());

                    if (checkedRadioButton.getText().equals("COD")) {
                        status = 2;
                        payment_method = "COD";
                        wallet_status = "no";
                        return;
                    }
                    if (checkedRadioButton.getText().equals("Credit/Debit Card / Net Banking")) ;

                    {
                        status = 3;
                        payment_method = "cards";
                        wallet_status = "no";

                    }

                }
            }
        });


        checkBox_coupon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Promo_code_layout.setVisibility(View.GONE);
                } else {
                    et_Coupon.setText(code);
                    Promo_code_layout.setVisibility(View.VISIBLE);

                }
            }
        });

        rb_Cod.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                wallet_status = "no";
            }
        });


    }


    private void rewardliness() {

        String tag_json_obj = "json_order_detail_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("cart_id", cart_id);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.rewardlines, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", response.toString());

                try {

                    String status = response.getString("status");
                    String message = response.getString("message");

                    if (status.contains("1")) {


                        String line1 = response.getString("line1");
                        String line2 = response.getString("line2");

                        linea.setText(line1);
                        lineb.setText(line2);


                    } else {


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().clear();
        AppController.getInstance().getRequestQueue().add(jsonObjReq);


    }


    private void apply() {
        String tag_json_obj = "json_order_detail_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("cart_id", cart_id);
        params.put("coupon_code", code);
        Log.d("ssd", cart_id);
        Log.d("dd", code);


        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.apply_coupon, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", response.toString());

                try {

                    String status = response.getString("status");
                    String message = response.getString("message");

                    if (status.contains("1")) {

                        JSONObject jsonObject = response.getJSONObject("data");

                        Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_SHORT).show();


                    } else {

                        Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    private void makeAddOrderRequest(String userid, String cart_id, String payment_method, String wallet_status, String payment_status) {
        String tag_json_obj = "json_add_order_req";
        Map<String, String> params = new HashMap<String, String>();
//        params.put("date", date);
//        params.put("time", gettime);
        params.put("user_id", userid);
        params.put("payment_status", payment_status);
        params.put("cart_id", cart_id);
//        params.put("total_ammount",total_amount);
        params.put("payment_method", payment_method);
        params.put("wallet", wallet_status);
//        params.put("lat",lat);
//        params.put("lng",lng);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.ADD_ORDER_URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    String status = response.getString("status");
                    String message = response.getString("message");
                    if (status.contains("1")) {
                        JSONObject jsonObject = response.getJSONObject("data");

                        Intent intent = new Intent(getApplicationContext(), OrderSuccessful.class);
                        intent.putExtra("msg", message);
                        startActivity(intent);

                        Toast.makeText(PaymentDetails.this, "" + message, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
               /* if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }*/
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    public void getRefresrh() {
        String user_id = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);
        RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
        StringRequest strReq = new StringRequest(Request.Method.POST, BaseURL.WALLET_REFRESH + user_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jObj = new JSONObject(response);
                            Log.d("dfsd", jObj.toString());
//                            if (jObj.optString("success").equalsIgnoreCase("success")) {
                            wallet_amount = jObj.getString("data");

//                                payble_ammount.setText(wallet_amount);
//                                Wallet_Ammount.setText(wallet_amount);
//                                SharedPref.putString(getActivity(), BaseURL.KEY_WALLET_Ammount, wallet_amount);
//                            } else {
//                                 Toast.makeText(DashboardPage.this, "" + jObj.optString("msg"), Toast.LENGTH_LONG).show();
//                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

        };
        rq.add(strReq);
    }

    public void startPayment(String name, String amount, String email, String phone) {

/*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */


        final Activity activity = this;

        final Checkout co = new Checkout();

        try {

            JSONObject options = new JSONObject();

            options.put("name", name);
            options.put("description", "Demoing Charges");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");

            options.put("amount", Integer.parseInt(amount) * 100);

            JSONObject preFill = new JSONObject();

            preFill.put("email", email);

            preFill.put("contact", phone);

            options.put("prefill", preFill);

            co.open(activity, options);

        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }


    @Override
    public void onPaymentSuccess(String s) {

        makeAddOrderRequest(getuser_id, cart_id, payment_method, wallet_status, "success");


    }

    @Override
    public void onPaymentError(int i, String s) {

        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();

    }


}
