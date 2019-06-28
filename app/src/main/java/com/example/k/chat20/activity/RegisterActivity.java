package com.example.k.chat20.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.k.chat20.CHAT;
import com.example.k.chat20.service.ServiceRegister;
import com.example.k.chat20.R;

public class RegisterActivity extends AppCompatActivity {

    EditText edtTxtAcount;
    EditText edtTxtPasswd;
    ProgressBar pb;
    private int flag = 0;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtTxtAcount = findViewById(R.id.register_edtTex_acount);
        edtTxtPasswd = findViewById(R.id.register_edtTex_passwd);
        pb = findViewById(R.id.register_pb);
    }

    public void registerBtnRegister(View view){
        if(flag == 0){
            flag = 1;
            pb.setVisibility(View.VISIBLE);
            String acount = edtTxtAcount.getText().toString();
            String passwd = edtTxtPasswd.getText().toString();
            CHAT.serviceRegister.register(acount,passwd,handler);
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            pb.setVisibility(View.INVISIBLE);
            switch (msg.what){
                case ServiceRegister.REGISTER_FAIL:
                    Toast.makeText(getApplicationContext(), "注册失败", Toast.LENGTH_SHORT).show();
                    flag = 0;
                    break;

                case ServiceRegister.REGISTER_SUC:
                    Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT).show();
                    flag = 0;
                    break;
            }
        }
    };
}
