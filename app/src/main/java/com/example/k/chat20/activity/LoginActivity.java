package com.example.k.chat20.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.k.chat20.CHAT;
import com.example.k.chat20.service.ServiceLogin;
import com.example.k.chat20.R;

public class LoginActivity extends AppCompatActivity {

    private int flag = 0;

    private String acount;
    private String passwd;

    EditText edtTxtAcount;
    EditText edtTxtPasswd;
    ProgressBar pbLoad;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        pbLoad = findViewById(R.id.login_pb_load);
        edtTxtAcount = findViewById(R.id.login_et_account);
        edtTxtPasswd = findViewById(R.id.login_et_passwd);
    }




    Handler handler = new Handler(){
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            pbLoad.setVisibility(View.INVISIBLE);
            switch(msg.what) {
                case ServiceLogin.LOGIN_SUC:
                    Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent();
                    intent.putExtra("userID", acount);
                    intent.setClass(getApplicationContext(), RecentContactActivity.class);
                    startActivity(intent);
                    flag = 0;

                    break;
                default:
                    Toast.makeText(getApplicationContext(), "登录失败：" + msg.what, Toast.LENGTH_SHORT).show();
                    flag  = 0;
                    break;


            }
        }
    };


    public void btnLogin(View view){
        if(flag == 0 ){
            flag = 1;
            acount = edtTxtAcount.getText().toString();
            passwd = edtTxtPasswd.getText().toString();
            CHAT.serviceLogin.login(acount, passwd, handler);
            pbLoad.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


}
