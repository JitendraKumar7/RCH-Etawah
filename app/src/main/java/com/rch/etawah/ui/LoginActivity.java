package com.rch.etawah.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.rch.etawah.Config.BaseURL;
import com.rch.etawah.R;
import com.rch.etawah.activity.SignUpActivity;
import com.rch.etawah.app.ApiClient;
import com.rch.etawah.app.BaseActivity;
import com.rch.etawah.app.SharedPreference;
import com.rch.etawah.util.Session_management;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends BaseActivity {

    Button btnSignIn;
    EditText etMob, etPass;
    TextView forgotPass, btnSignUp;

    @Override
    protected int getResourcesID() {

        return R.layout.activity_login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        etMob = findViewById(R.id.etMob);
        etPass = findViewById(R.id.etPass);
        btnSignIn = findViewById(R.id.btn_Login);
        btnSignUp = findViewById(R.id.btn_Signup);
        forgotPass = findViewById(R.id.btn_ForgotPass);

        forgotPass.setOnClickListener(v -> {
            // Start Activity
            startActivity(ForgotPassOtpActivity.class, null);
        });
        btnSignUp.setOnClickListener(v -> {
            // Start Activity
            startActivity(SignUpActivity.class, null);
            finish();
        });
        btnSignIn.setOnClickListener(v -> {
            if (getValue(etMob).length() < 9) {
                showToast("Valid Mobile Number required!");
            }
            //
            else if (getValue(etPass).length() < 4) {
                showToast("Password required!");
            }
            //
            else {

                HashMap<String, String> param = new HashMap<>();

                param.put("user_phone", getValue(etMob));
                param.put("user_password", getValue(etPass));
                param.put("device_id", FirebaseInstanceId.getInstance().getToken());

                ApiClient.getInstance(getActivity(), response -> {

                    try {
                        JSONObject jsonObject = new JSONObject((String) response);
                        String status = jsonObject.getString("status");
                        String msg = jsonObject.getString("message");

                        if (status.equals("1")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject obj = jsonArray.getJSONObject(i);

                                User user = new Gson().fromJson(obj.toString(), User.class);

                                Session_management sessionManagement = new Session_management(LoginActivity.this);
                                SharedPreferences.Editor editor = getSharedPreferences(BaseURL.MyPrefreance, MODE_PRIVATE).edit();
                                editor.putString(BaseURL.KEY_MOBILE, user.getUserPhone());
                                editor.putString(BaseURL.KEY_PASSWORD, user.getPassword());
                                editor.apply();

                                sessionManagement.createLoginSession(user.getUserId(),
                                        user.getUserEmail(), user.getUserName(),
                                        user.getUserPhone(), user.getPassword()
                                );

                                SharedPreference.putString(USER_DATA, obj.toString());
                                startActivity(DashboardActivity.class, null);
                                SharedPreference.putBoolean(IS_LOGIN, true);
                                showToast(msg);
                                finish();

                            }
                        } else if (status.equals("2")) {
                            Intent intent = new Intent(getActivity(), OtpVerificationActivity.class);
                            intent.putExtra("MobNo", etMob.getText().toString());
                            startActivity(intent);
                            showToast(msg);
                        } else {
                            showToast(msg);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }).getApiResponse(LOGIN, param);
            }
        });

    }

}
