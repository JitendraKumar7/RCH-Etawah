package com.rch.etawah.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.rch.etawah.R;
import com.rch.etawah.ui.ForgotPassOtpActivity;

public class ForgotPAssword extends AppCompatActivity {

    EditText et_MObile;
    Button btnNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_p_assword);
        et_MObile=findViewById(R.id.et_mobile);
        btnNext=findViewById(R.id.btnNext);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_MObile.getText().toString().trim().equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(),"Mobile Number required!",Toast.LENGTH_SHORT).show(); }

               else if(et_MObile.getText().toString().trim().length()<9){
                    Toast.makeText(getApplicationContext(),"Mobile Number required!",Toast.LENGTH_SHORT).show(); }

                else if(!isOnline()){
                    Toast.makeText(getApplicationContext(),"Please check your Internet Connection!",Toast.LENGTH_SHORT).show();
                }else{
                    Intent in=new Intent(getApplicationContext(), ForgotPassOtpActivity.class);
                    in.putExtra("MobileNo",et_MObile.getText().toString());
                    startActivity(in);
                    finish();
                }
            }
        });

    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}
