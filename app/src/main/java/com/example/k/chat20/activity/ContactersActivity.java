package com.example.k.chat20.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.k.chat20.CHAT;
import com.example.k.chat20.datastruct.Contacter;
import com.example.k.chat20.adapter.AdapterContacterItem;
import com.example.k.chat20.service.ServiceContacter;
import com.example.k.chat20.R;

import java.util.ArrayList;

public class ContactersActivity extends AppCompatActivity {

    private ListView lv;
    ArrayList<Contacter> friendList = new ArrayList<>();
    AdapterContacterItem mAdapter;
    ProgressBar pbLoad;

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_contacter);
        lv = findViewById(R.id.addressbook_lv_FriendList);
        pbLoad = findViewById(R.id.contacter_pb_load);

        /*存放用户*/
        CHAT.serviceContacter.openList( CHAT.user.getUserId()
                ,"", handler);

        //填充lv
        mAdapter = new AdapterContacterItem(this, friendList );//得到一个MyAdapter对象
        lv.setAdapter(mAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), ChatActivity.class);
                intent.putExtra("contacterAccid", friendList.get(arg2).getAccid() );
                intent.putExtra("nickName", friendList.get(arg2).getContactName() );
                startActivity(intent);
            }
        });
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case ServiceContacter.CONTACTERITEM:
                    friendList.add( (Contacter) msg.obj );
                    mAdapter.notifyDataSetChanged();
                    break;
                case ServiceContacter.ALLDOEN:
                    pbLoad.setVisibility(View.INVISIBLE);
                    break;
            }
        }
    };

    public void btnMessageList(View view){
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), RecentContactActivity.class);
        startActivity(intent);
        finish();
    }

    public void ABbtnAddFriend(View view){
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), AddFriendActivity.class);
        startActivity(intent);
    }

    public void ABbtnUpLoad(View view){
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), UploadActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }



}
