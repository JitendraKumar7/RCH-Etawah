package com.rch.etawah.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.rch.etawah.app.SharedPreference;
import com.rch.etawah.ui.LoginActivity;
import com.rch.etawah.activity.MainActivity;

import java.util.HashMap;

import static com.rch.etawah.Config.BaseURL.IS_LOGIN;
import static com.rch.etawah.Config.BaseURL.KEY_DATE;
import static com.rch.etawah.Config.BaseURL.KEY_EMAIL;
import static com.rch.etawah.Config.BaseURL.KEY_HOUSE;
import static com.rch.etawah.Config.BaseURL.KEY_ID;
import static com.rch.etawah.Config.BaseURL.KEY_IMAGE;
import static com.rch.etawah.Config.BaseURL.KEY_MOBILE;
import static com.rch.etawah.Config.BaseURL.KEY_NAME;
import static com.rch.etawah.Config.BaseURL.KEY_PASSWORD;
import static com.rch.etawah.Config.BaseURL.KEY_PAYMENT_METHOD;
import static com.rch.etawah.Config.BaseURL.KEY_PINCODE;
import static com.rch.etawah.Config.BaseURL.KEY_REWARDS_POINTS;
import static com.rch.etawah.Config.BaseURL.KEY_SOCITY_ID;
import static com.rch.etawah.Config.BaseURL.KEY_SOCITY_NAME;
import static com.rch.etawah.Config.BaseURL.KEY_TIME;
import static com.rch.etawah.Config.BaseURL.KEY_WALLET_Ammount;
import static com.rch.etawah.Config.BaseURL.PREFS_NAME;
import static com.rch.etawah.Config.BaseURL.PREFS_NAME2;
import static com.rch.etawah.Config.BaseURL.TOTAL_AMOUNT;

/**
 * Created by Rajesh Dabhi on 28/6/2017.
 */

public class Session_management {

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    private Context context;

    @SuppressLint("CommitPrefEdits")
    public Session_management(Context context) {

        this.context = context;
        int PRIVATE_MODE = 0;
        prefs = context.getSharedPreferences(PREFS_NAME, PRIVATE_MODE);
        editor = prefs.edit();

    }

    public void createLoginSession(String id, String email, String name
            , String mobile, String password) {

        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_ID, id);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_MOBILE, mobile);
//        editor.putString(KEY_IMAGE, image);
//        editor.putString(KEY_WALLET_Ammount, wallet_ammount);
//        editor.putString(KEY_REWARDS_POINTS, reward_point);
//        editor.putString(KEY_PINCODE, pincode);
//        editor.putString(KEY_SOCITY_ID, socity_id);
//        editor.putString(KEY_SOCITY_NAME, socity_name);
//        editor.putString(KEY_HOUSE, house);
        editor.putString(KEY_PASSWORD, password);
//        editor.putString(KEY_PASSWORD, password);

        editor.commit();
    }


    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_ID, prefs.getString(KEY_ID, null));
        // user email id
        user.put(KEY_EMAIL, prefs.getString(KEY_EMAIL, null));
        // user name
        user.put(KEY_NAME, prefs.getString(KEY_NAME, null));
        user.put(KEY_MOBILE, prefs.getString(KEY_MOBILE, null));
        user.put(KEY_IMAGE, prefs.getString(KEY_IMAGE, null));
        user.put(KEY_WALLET_Ammount, prefs.getString(KEY_WALLET_Ammount, null));
        user.put(KEY_REWARDS_POINTS, prefs.getString(KEY_REWARDS_POINTS, null));
        user.put(KEY_PAYMENT_METHOD, prefs.getString(KEY_PAYMENT_METHOD, ""));
        user.put(TOTAL_AMOUNT, prefs.getString(TOTAL_AMOUNT, null));
        user.put(KEY_PINCODE, prefs.getString(KEY_PINCODE, null));
        user.put(KEY_SOCITY_ID, prefs.getString(KEY_SOCITY_ID, null));
        user.put(KEY_SOCITY_NAME, prefs.getString(KEY_SOCITY_NAME, null));
        user.put(KEY_HOUSE, prefs.getString(KEY_HOUSE, null));
        user.put(KEY_PASSWORD, prefs.getString(KEY_PASSWORD, null));

        // return user
        return user;
    }

    public void logoutSession() {
        editor.clear();
        editor.commit();

        cleardatetime();

        Intent logout = new Intent(context, MainActivity.class);
        // Closing all the Activities
        logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(logout);
    }

    public void cleardatetime() {
    }

    // Get Login State
    public boolean isLoggedIn() {

        return SharedPreference.getBoolean(IS_LOGIN);
    }


}
