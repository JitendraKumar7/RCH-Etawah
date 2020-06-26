package com.rch.etawah.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.rch.etawah.R;
import com.rch.etawah.ui.DashboardActivity;

public class OrderSuccessful extends AppCompatActivity {

    Button btn_ShopMore;

    @Override
    public void onBackPressed() {

        DashboardActivity.startActivity(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_successful);
        btn_ShopMore = findViewById(R.id.btn_ShopMore);
        btn_ShopMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DashboardActivity.startActivity(OrderSuccessful.this);
            }
        });
    }

}
