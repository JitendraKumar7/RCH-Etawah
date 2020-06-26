//package com.tecmanic.etawah.Activity;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.RelativeLayout;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.android.volley.NoConnectionError;
//import com.android.volley.Request;
//import com.android.volley.Response;
//import com.android.volley.TimeoutError;
//import com.android.volley.VolleyError;
//import com.android.volley.VolleyLog;
//import com.tecmanic.etawah.Config.BaseURL;
//import com.tecmanic.etawah.R;
//import com.tecmanic.etawah.util.AppController;
//import com.tecmanic.etawah.util.ConnectivityReceiver;
//import com.tecmanic.etawah.util.CustomVolleyJsonRequest;
//import com.tecmanic.etawah.util.DatabaseHandler;
//import com.tecmanic.etawah.util.Session_management;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//
//
//import static com.android.volley.VolleyLog.TAG;
//
//public class PayPal extends AppCompatActivity {
//    private Session_management sessionManagement;
//    RelativeLayout confirm;
//    private DatabaseHandler db_cart;
//    private String getlocation_id = "";
//    private String getstore_id = "";
//    private String getvalue = "";
//    private String get_wallet_ammount = "";
//    private String gettime = "";
//    private String getdate = "";
//    private String getuser_id = "";
//    String text;
//    String total_rs;
//    private String user_id = "";
//    //The views
//    //Payment Amount
//    private String paymentAmount , deliver_charged;
//    int price= 0,chager =0;
//    String Used_Wallet_amount;
//
//    //Paypal intent request code to track onActivityResult method
//    public static final int PAYPAL_REQUEST_CODE = 123;
//
//
//    //Paypal Configuration Object
//    private static PayPalConfiguration config = new PayPalConfiguration()
//            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
//            // or live (ENVIRONMENT_PRODUCTION)
//            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
//            .clientId(PayPalConfig.PAYPAL_CLIENT_ID);
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_pay_pal);
//        sessionManagement = new Session_management(PayPal.this);
//        String email = sessionManagement.getUserDetails().get(BaseURL.KEY_EMAIL);
//        String mobile = sessionManagement.getUserDetails().get(BaseURL.KEY_MOBILE);
//        String name = sessionManagement.getUserDetails().get(BaseURL.KEY_NAME);
//        user_id = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);
//        total_rs = getIntent().getStringExtra("total");
//        Log.d("ss",total_rs);
//
//        Used_Wallet_amount = getIntent().getStringExtra("usedwallet");
//        getlocation_id = getIntent().getStringExtra("getlocationid");
//        getstore_id = getIntent().getStringExtra("getstoreid");
//        gettime = getIntent().getStringExtra("gettime");
//        getvalue = getIntent().getStringExtra("getpaymentmethod");
//        getdate = getIntent().getStringExtra("getdate");
//        get_wallet_ammount = getIntent().getStringExtra("wallet_ammount");
////        deliver_charged = getIntent().getStringExtra("deliver_charged");
//
////        chager= Integer.parseInt(deliver_charged);
//
////        Log.d("sdfsd",deliver_charged);
//
//
//        sessionManagement = new Session_management(PayPal.this);
//        db_cart = new DatabaseHandler(PayPal.this);
////        startPayment(name,total_rs,email,mobile);
//
//
//        Intent intent = new Intent(this, PayPalService.class);
//
//        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
//
//        startService(intent);
//
//
//        getPayment();
//
//    }
//
//    //    public void startPayment(String name,String amount,String email,String phone) {
////        /*
////          You need to pass current activity in order to let Razorpay create CheckoutActivity
////         */
////
////
////        final Activity activity = this;
////
////        final Checkout co = new Checkout();
////
////        try {
////
////            JSONObject options = new JSONObject();
////
////            options.put("name", name);
////            options.put("description", "Demoing Charges");
////            //You can omit the image option to fetch the image from dashboard
////            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
////            options.put("currency", "INR");
////
////            options.put("amount", Double.valueOf(amount)*100);
////
////            JSONObject preFill = new JSONObject();
////
////            preFill.put("email", email);
////
////            preFill.put("contact", phone);
////
////            options.put("prefill", preFill);
////
////            co.open(activity, options);
////
////        } catch (Exception e) {
////            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
////                    .show();
////            e.printStackTrace();
////        }
////    }
//    @Override
//    public void onDestroy() {
//        stopService(new Intent(this, PayPalService.class));
//        super.onDestroy();
//    }
//
//    private void getPayment() {
//        //Getting the amount from editText
////        paymentAmount = getIntent().getStringExtra("total_amount");
//
////        price= Integer.parseInt(paymentAmount);
//////
////        int total = price+chager;
//
////        Log.d("asdf", String.valueOf(paymentAmount));
//
//
//        //Creating a paypalpayment
//        PayPalPayment payment = new PayPalPayment(new BigDecimal(total_rs), "USD", "Coding Fee",
//                PayPalPayment.PAYMENT_INTENT_SALE);
//
//        //Creating Paypal Payment activity intent
//        Intent intent = new Intent(this, PaymentActivity.class);
//
//        //putting the paypal configuration to the intent
//        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
//
//        //Puting paypal payment to the intent
//        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
//
//        //Starting the intent activity for result
//        //the request code will be used on the method onActivityResult
//        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        //If the result is from paypal
//        if (requestCode == PAYPAL_REQUEST_CODE) {
//
//            //If the result is OK i.e. user has not canceled the payment
//            if (resultCode == Activity.RESULT_OK) {
//                //Getting the payment confirmation
//                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
//
//                //if confirmation is not null
//                if (confirm != null) {
//                    try {
//                        //Getting the payment details
//                        String paymentDetails = confirm.toJSONObject().toString(5);
//                        Log.i("paymentExample", paymentDetails);
//
//                        attemptOrder();
//                        //Starting a new activity for the payment details and also putting the payment details with intent
////                        startActivity(new Intent(this, ConfirmationActivity.class)
////                                .putExtra("PaymentDetails", paymentDetails)
//////                                .putExtra("deliver_charged",deliver_charged)
////                                .putExtra("getdate",getIntent().getStringExtra("getdate"))
////                                .putExtra("gettime",getIntent().getStringExtra("gettime"))
////                                .putExtra("usedwalllet",getIntent().getStringExtra("usedwalllet"))
////                                .putExtra("usedamount",getIntent().getStringExtra("usedamount"))
////                                .putExtra("previousamount",getIntent().getStringExtra("previousamount"))
////                                .putExtra("couponcode",getIntent().getStringExtra("couponcode"))
////                                .putExtra("getlocationid",getIntent().getStringExtra("getlocationid"))
////                                .putExtra("getstoreid",getIntent().getStringExtra("getstoreid"))
////                                .putExtra("getpaymentmethod",getIntent().getStringExtra("getpaymentmethod"))
////                                .putExtra("PaymentAmount", paymentAmount));
//
//                    } catch (JSONException e) {
//                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
//                    }
//                }
//            } else if (resultCode == Activity.RESULT_CANCELED) {
//                Log.i("paymentExample", "The user canceled.");
//            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
//                Log.i("paymentExample", PaymentActivity.RESULT_EXTRAS_INVALID+"An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
//            }
//        }
//    }
//    private void attemptOrder() {
//        ArrayList<HashMap<String, String>> items = db_cart.getCartAll();
//        if (items.size() > 0) {
//            JSONArray passArray = new JSONArray();
//            for (int i = 0; i < items.size(); i++) {
//                HashMap<String, String> map = items.get(i);
//                JSONObject jObjP = new JSONObject();
//                try {
//                    jObjP.put("product_id", map.get("product_id"));
//                    jObjP.put("qty", map.get("qty"));
//                    jObjP.put("unit_value", map.get("unit_value"));
//                    jObjP.put("unit", map.get("unit"));
//                    jObjP.put("price", map.get("price"));
//                    jObjP.put("rewards", map.get("rewards"));
//                    passArray.put(jObjP);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            getuser_id = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);
//
//            if (ConnectivityReceiver.isConnected()) {
//
//                Log.e(TAG, "from:" + gettime + "\ndate:" + getdate +
//                        "\n" + "\nuser_id:" + getuser_id + "\n" + getlocation_id + getstore_id + "\ndata:" + passArray.toString());
//
//                makeAddOrderRequest(getdate, gettime, getuser_id, getlocation_id, getstore_id, passArray);
//            }
//        }
//    }
//
//    private void makeAddOrderRequest(String date, String gettime, String userid, String location, String store_id, JSONArray passArray) {
//        String tag_json_obj = "json_add_order_req";
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("date", date);
//        params.put("time", gettime);
//        params.put("user_id", userid);
//        params.put("location", location);
//        params.put("store_id", store_id);
//        params.put("payment_method", getvalue);
//        params.put("data", passArray.toString());
//        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
//                BaseURL.ADD_ORDER_URL, params, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                Log.d(TAG, response.toString());
//
//                try {
//                    Boolean status = response.getBoolean("responce");
//                    if (status) {
//                        db_cart.clearCart();
//                        Intent intent = new Intent(PayPal.this, ThanksOrder.class);
//                        startActivity(intent);
//
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
//                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                    Toast.makeText(PayPal.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
//    }
//
//
//}
