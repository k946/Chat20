package com.example.k.chat20.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.k.chat20.CHAT;
import com.example.k.chat20.adapter.LoadListView;
import com.example.k.chat20.service.ServiceMessage;
import com.example.k.chat20.adapter.AdapterChatItem;
import com.example.k.chat20.R;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatActivity extends AppCompatActivity implements LoadListView.ILoadListener2 {
    String myAccid = "";
    String friendNick;
    String friendAccid = "";

    private LoadListView lv;
    EditText edtTexSend;
    String text;
    AdapterChatItem mAdapter;
    ArrayList< HashMap<String, Object> > listItem;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat);

        lv = findViewById(R.id.chat_lv_recever);
        edtTexSend = findViewById(R.id.chat_edTex_send);

        lv.setDividerHeight(0);

        listItem = new ArrayList<>();

        Intent i = getIntent();
        friendAccid = i.getStringExtra("contacterAccid");
        friendNick = i.getStringExtra("nickName");

        myAccid = CHAT.user.getAccid();
        System.out.println("----------ACCId" + CHAT.user);

        //填充lv
        mAdapter = new AdapterChatItem(this, listItem);//得到一个MyAdapter对象

        //从本地获取消息填充listItem
        CHAT.serviceMessage.getMessageListFromLocal(myAccid,friendAccid,handler,5);

        //实时接收消息
        CHAT.serviceMessage.registerObserverForInstant(friendAccid, handler);

        lv.setAdapter(mAdapter);//为ListView绑定Adapter
        lv.setInterface(this);
        //CHAT.serviceMessage.importMessageFromDatabase(handler);
    }


    @Override
    protected void onPause() {
        super.onPause();

        //注销消息监听者
        CHAT.serviceMessage.cancelOberserver();
        //取消锚点
        CHAT.serviceMessage.cancelAnchor();

        CHAT.serviceMessage.clearUnread(friendAccid);
    }

    public void sendMessage(View view){
        text = edtTexSend.getText().toString();

        if( !text.equals("") && text != null) {
            CHAT.serviceMessage.sendMessage(friendAccid, text, "text", handler);
            edtTexSend.setText("");
        }
    }

    Handler handler = new Handler(){

        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                //即时发送消息
                case ServiceMessage.SEND_NOW:
                    Toast.makeText(getApplicationContext(), "发送成功", Toast.LENGTH_SHORT).show();
                    //tvReceiver.setText(tvReceiver.getText() + "\n" + "me" + " > " +  receiverAccid + " : " + text);
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("Content", text);
                    map.put("Sender", "me");
                    listItem.add(map);
                    mAdapter.notifyDataSetChanged();
                    lv.setSelection(listItem.size() - 1);
                    break;

                //即时接收消息
                case ServiceMessage.RECEIVER_NOW:
                    IMMessage imMessage = (IMMessage) msg.obj;
                    //tvReceiver.setText("\n" + imMessage.getFromAccount()  + " > 我 :" + imMessage.getContent() + tvReceiver.getText()  );
                    HashMap<String, Object> map1 = new HashMap<>();
                    map1.put("Content",imMessage.getContent() );
                    map1.put("Sender",  friendAccid);
                    listItem.add(map1);
                    mAdapter.notifyDataSetChanged();
                    lv.setSelection(listItem.size() - 1);
                    break;

                //自己发送的历史消息
                case ServiceMessage.SEND_OLD:
                    IMMessage imMessage1 = (IMMessage) msg.obj;
                    //tvReceiver.setText("\n" +  "我 > " + imMessage1.getSessionId() + " :" + imMessage1.getContent() + tvReceiver.getText() );
                    HashMap<String, Object> map2 = new HashMap<>();
                    map2.put("Content",imMessage1.getContent() );
                    map2.put("Sender",  "me");
                    listItem.add(0,map2);
                    lv.setSelection( listItem.size() > 5 ? 5 : listItem.size());
                    break;

                case ServiceMessage.RECEIVER_OLD:
                    IMMessage imMessage2 = (IMMessage) msg.obj;
                    //tvReceiver.setText("\n" +  "我 > " + imMessage1.getSessionId() + " :" + imMessage1.getContent() + tvReceiver.getText() );
                    HashMap<String, Object> map3 = new HashMap<>();
                    map3.put("Content",imMessage2.getContent());
                    map3.put("Sender",  friendAccid);
                    listItem.add(0,map3);
                    lv.setSelection(listItem.size() > 5 ? 5 : listItem.size());
                    break;

                case ServiceMessage.LOAD_NUMBER:
                    Toast.makeText(getApplicationContext(), "加载消息：" + msg.obj + " 条 " , Toast.LENGTH_SHORT).show();
                    break;


                default:
                    Toast.makeText(getApplicationContext(), "发送失败 > " + msg.obj, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    @Override
    public void onLoad() {

        CHAT.serviceMessage.getMessageListFromLocal(myAccid,friendAccid,handler,5);
        //lv.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        //lv.setStackFromBottom(true);
        //通知listView加载完毕，底部布局消失
        lv.loadComplete();
    }
}
