package com.example.k.chat20.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.k.chat20.CHAT;
import com.example.k.chat20.service.ServiceAddFriend;
import com.example.k.chat20.adapter.AdapterAddFriendItem;
import com.example.k.chat20.datastruct.MessageofSystem;
import com.example.k.chat20.R;

import java.util.ArrayList;
import java.util.HashMap;


public class AddFriendActivity extends AppCompatActivity {

    EditText edtTexid;
    private ListView lv;
    ArrayList< HashMap<String, Object> > listItem;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfriend);

        edtTexid = findViewById(R.id.addfriend_edtTex__id);
        lv = findViewById(R.id.addfrien_lv_addquest);
        listItem = new ArrayList<>();/*添加好友请求列表*/

        //填充listItem
        CHAT.serviceAddFriend.getMessageFromFriendSystem(handler);

        //填充lv
        AdapterAddFriendItem mAdapter = new AdapterAddFriendItem(this, listItem);//得到一个MyAdapter对象

        lv.setAdapter(mAdapter);//为ListView绑定Adapter

        //CHAT.serviceMessage.exportMessageToDatabase(handler);

    }


    Handler handler = new Handler(){
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case ServiceAddFriend.ADDFRIEND:
                    Toast.makeText(getApplicationContext(), "添加好友发送成功", Toast.LENGTH_SHORT).show();
                    break;

                case ServiceAddFriend.NEWFRIEND:
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("MessageId", ((MessageofSystem)msg.obj).getMessageID() );
                    map.put("Sender", ((MessageofSystem)msg.obj).getSender());
                    map.put("Content", "请求添加为好友");
                    listItem.add(map);
                    break;
                case ServiceAddFriend.ISFRIEND:
                    Toast.makeText(getApplicationContext(), "对方已经是好友", Toast.LENGTH_SHORT).show();
                    break;

                default:
                    Toast.makeText(getApplicationContext(), "不存在用户", Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    };

    public void AFbtnAdd(View view) {
        String id = edtTexid.getText().toString();
        CHAT.serviceAddFriend.addFriend(id,"", handler);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }



}
