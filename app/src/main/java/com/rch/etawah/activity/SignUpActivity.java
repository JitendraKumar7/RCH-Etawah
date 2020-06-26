package com.rch.etawah.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.rch.etawah.R;
import com.rch.etawah.ui.LoginActivity;
import com.rch.etawah.ui.OtpVerificationActivity;
import com.rch.etawah.util.LocaleHelper;
import com.rch.etawah.util.Session_management;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.rch.etawah.Config.BaseURL.SignUp;

public class SignUpActivity extends AppCompatActivity {

    EditText etName, etPhone, etEmail, etPAss;
    Button btnSignUP;
    TextView btnLogin;
    ProgressDialog progressDialog;
    String emailPattern, token;

    private void init() {

        emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        FirebaseApp.initializeApp(this);
        token = FirebaseInstanceId.getInstance().getToken();

        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        etPAss = findViewById(R.id.etPass);

        btnSignUP = findViewById(R.id.btnSignUP);
        btnLogin = findViewById(R.id.btn_Login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnSignUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etName.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Full name required!", Toast.LENGTH_SHORT).show();
                } else if (etEmail.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Email id required!", Toast.LENGTH_SHORT).show();
                } else if (!etEmail.getText().toString().trim().matches(emailPattern)) {
                    Toast.makeText(getApplicationContext(), "Valid Email id required!", Toast.LENGTH_SHORT).show();
                } else if (etPhone.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Mobile Number required!", Toast.LENGTH_SHORT).show();
                } else if (etPhone.getText().toString().trim().length() < 9) {
                    Toast.makeText(getApplicationContext(), "Valid Mobile Number required!", Toast.LENGTH_SHORT).show();
                } else if (etPAss.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Password required!", Toast.LENGTH_SHORT).show();
                } else if (!isOnline()) {
                    Toast.makeText(getApplicationContext(), "Please check your Internet Connection!", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.show();
                    signUpUrl();

                    progressDialog.dismiss();
                }
            }
        });

    }

    private void signUpUrl() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SignUp, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("SignUP", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        String msg = jsonObject.getString("message");
                        JSONObject resultObj = jsonObject.getJSONObject("data");

                        String user_name = resultObj.getString("user_name");
                        String id = resultObj.getString("user_id");
                        String user_email = resultObj.getString("user_email");
                        String user_phone = resultObj.getString("user_phone");
                        String password = resultObj.getString("user_password");


                        Session_management session_management = new Session_management(SignUpActivity.this);
                        session_management.createLoginSession(id, user_email, user_name, user_phone, password);

                        //  Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                        // Intent intent = new Intent(getApplicationContext(), OtpVerification.class);
                        Intent intent = new Intent(getApplicationContext(), OtpVerificationActivity.class);
                        intent.putExtra("MobNo", etPhone.getText().toString());
                        startActivity(intent);
                        finish();
                    } else {
                        // JSONObject resultObj = jsonObject.getJSONObject("results");
                        String msg = "User already registered!";
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

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

                param.put("user_name", etName.getText().toString());
                param.put("user_phone", etPhone.getText().toString());
                param.put("user_email", etEmail.getText().toString());
                param.put("user_password", etPAss.getText().toString());
                param.put("device_id", token);

              /*  param.put("user_name","Sonal Jain");
                param.put("user_phone","9179985780");
                param.put("user_email","sonal@gmail.com");
                param.put("user_password","12345");
                param.put("device_id","f1bca86f19636b42");*/
                return param;

            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);

    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        newBase = LocaleHelper.onAttach(newBase);
        super.attachBaseContext(newBase);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        init();

    }
}
