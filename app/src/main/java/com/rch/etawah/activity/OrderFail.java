package com.rch.etawah.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.rch.etawah.R;

public class OrderFail extends AppCompatActivity {

    Button btn_ShopMore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderfail);
        btn_ShopMore=findViewById(R.id.btn_ShopMore);
        btn_ShopMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
