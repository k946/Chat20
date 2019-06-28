package com.example.k.chat20.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.k.chat20.R;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btnGotoRegister(View view){
        startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
    }

    public void btnGotoForgetPasswd(View view){
        startActivity(new Intent(getApplicationContext(), ForgetPasswdActivity.class));
    }


    public void btnGotoLogin(View view){
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }
}
