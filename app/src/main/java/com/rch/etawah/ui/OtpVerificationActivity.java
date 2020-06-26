package com.rch.etawah.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.rch.etawah.R;
import com.rch.etawah.app.ApiClient;
import com.rch.etawah.app.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class OtpVerificationActivity extends BaseActivity {

    @Override
    protected int getResourcesID() {

        return R.layout.activity_otp_verification;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView tvMobile = findViewById(R.id.txtMobile);
        EditText et_otp = findViewById(R.id.et_otp);

        Button verify = findViewById(R.id.btnVerify);

        String  MobileNO = getIntent().getStringExtra("MobNo");
        tvMobile.setText(MobileNO);

        verify.setOnClickListener(v -> {

            if (et_otp.getText().toString().trim().length() > 3) {
                HashMap<String, String> param = new HashMap<>();
                param.put("otp", et_otp.getText().toString().trim());
                param.put("user_phone", MobileNO);
                ApiClient.getInstance(getActivity(), response -> {
                    try {
                        JSONObject jsonObject = new JSONObject((String) response);
                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("1")) {
                            showToast(jsonObject.getString("message"));
                            startActivity(LoginActivity.class, null);
                            finish();
                        } else {
                            showToast(jsonObject.getString("message"));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }).getApiResponse(VERIFY_OTP, param);
            }
            //
            else {
                et_otp.setError("Invalid");
            }

        });

    }
}
