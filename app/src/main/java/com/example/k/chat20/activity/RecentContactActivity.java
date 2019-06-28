package com.example.k.chat20.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.example.k.chat20.CHAT;
import com.example.k.chat20.R;
import com.example.k.chat20.datastruct.RecentContact;
import com.example.k.chat20.adapter.AdapterMessageList;

import java.util.ArrayList;

public class RecentContactActivity extends AppCompatActivity {

    private ListView lv;
    ArrayList<RecentContact> listItem;
    AdapterMessageList mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recentcontact);
        lv = findViewById(R.id.messagelist_lv_Contacts);

        listItem = new ArrayList<>();/*会话列表*/

        //填充lv
        mAdapter = new AdapterMessageList(this, listItem);//得到一个MyAdapter对象
        lv.setAdapter(mAdapter);
        lv.setSelection( listItem.size() - 1 );

        lv.setOnItemClickListener( (arg0, arg1, arg2, arg3) -> {
            Intent intent = new Intent();
            listItem.get(arg2).setUnreadCount("0");
            mAdapter.notifyDataSetChanged();
            intent.setClass(getApplicationContext(), ChatActivity.class);
            intent.putExtra("contacterAccid", listItem.get(arg2).getSenderAccid() );
            startActivity(intent);
        });

        CHAT.serviceRecentContact.queryRecentContacts(handler);

    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            listItem.add((RecentContact)msg.obj);
            mAdapter.notifyDataSetChanged();
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }


    public void MLbtnAddressBook(View view){
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), ContactersActivity.class);
        startActivity(intent);
        finish();
    }

    public void MLbtnAddFriend(View view){
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), AddFriendActivity.class);
        startActivity(intent);
    }

    public void MLbtnUpLoad(View view){
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), UploadActivity.class);
        startActivity(intent);
    }


}
