package com.rch.etawah.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.cardview.widget.CardView;

import com.rch.etawah.R;
import com.rch.etawah.activity.Forget_otp_verify;
import com.rch.etawah.app.ApiClient;
import com.rch.etawah.app.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ForgotPassOtpActivity extends BaseActivity {

    EditText et_req_mobile;
    CardView btnVerify;

    @Override
    protected int getResourcesID() {

        return R.layout.activity_forgot_pass_otp;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        et_req_mobile = findViewById(R.id.et_req_mobile);
        btnVerify = findViewById(R.id.btnVerify);

        btnVerify.setOnClickListener(v -> {
            if (getValue(et_req_mobile).length() != 10 || getValue(et_req_mobile).contains("+")) {
                et_req_mobile.setError("Enter valid mobile number");
            }
            //
            else {

                HashMap<String, String> params = new HashMap<>();
                params.put("user_phone", getValue(et_req_mobile));

                ApiClient.getInstance(getActivity(), response -> {
                    try {
                        JSONObject jsonObject = new JSONObject((String) response);

                        if (jsonObject.getString("status").contains("1")) {

                            showToast(jsonObject.getString("message"));

                            Intent intent = new Intent(getActivity(), Forget_otp_verify.class);
                            intent.putExtra("user_phone", getValue(et_req_mobile));
                            startActivity(intent);

                        }
                        // Error
                        else {
                            showToast(jsonObject.getString("message"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }).getApiResponse(FORGOT_OTP, params);

            }
        });
    }
}