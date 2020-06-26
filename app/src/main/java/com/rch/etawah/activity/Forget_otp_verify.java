package com.rch.etawah.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.rch.etawah.Config.BaseURL;
import com.rch.etawah.R;
import com.rch.etawah.app.AppController;
import com.rch.etawah.util.CustomVolleyJsonRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Forget_otp_verify extends AppCompatActivity {
    CardView submit;

    EditText edtotp;
    TextView number;
    String getuserphone;
    public static String TAG = "Otp";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_otp_verify);


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Login");

        edtotp = findViewById(R.id.et_otp);


        number = findViewById(R.id.txnm);
        submit = findViewById(R.id.cvLogin);
        getuserphone = getIntent().getStringExtra("user_phone");

        number.setText(getuserphone);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtotp.getText().toString().length() == 0) {
                    Toast.makeText(Forget_otp_verify.this, "Enter Otp", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.show();
                    setotpverify();
                }
            }
        });
    }

    public void setotpverify() {


        String tag_json_obj = "json_register_req";

        Map<String, String> params = new HashMap<String, String>();

        params.put("user_phone", getuserphone);

        params.put("otp", edtotp.getText().toString());


        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,

                BaseURL.verify_otp, params, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {

                Log.d(TAG, response.toString());


                try {

                    String status = response.getString("status");
                    String message = response.getString("message");

                    if (status.contains("1")) {


                        progressDialog.dismiss();


                        Intent intent = new Intent(Forget_otp_verify.this, NewPassword.class);
                        intent.putExtra("user_phone", getuserphone);
                        startActivity(intent);

//

                    } else {

                        progressDialog.dismiss();
                        Toast.makeText(Forget_otp_verify.this, message, Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    progressDialog.dismiss();
//                    Toast.makeText(Forget_otp_verify.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }
}