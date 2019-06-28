package com.example.k.chat20.service;

import android.icu.util.Measure;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

import com.example.k.chat20.dao.DB;
import com.example.k.chat20.workerinterface.WorkerRecentContact;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.NIMSDK;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.RecentContact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceRecentContact implements WorkerRecentContact {
    private static Observer<List<IMMessage>> observer;  //只会注册一个消息接收者

    //获取最近会话
    public void queryRecentContacts(Handler handler) {
/*
        //保存新接收的会话未读数
        HashMap<String, Integer> map = new HashMap<>();
        observer = (Observer<List<IMMessage>>) imMessages -> {
            //获取消息的数量
            int j = imMessages.size();

            for(int i = 0; i < j; i++) {
                IMMessage msg = imMessages.get(i);
                String sender = msg.getFromAccount();
                if( map.containsKey(sender) ){
                    int unread = map.get(sender);
                    map.remove(sender);
                    map.put(sender, unread+1);
                }else{
                    map.put(sender, 1);
                }
            }

        };

        //注册消息监听者
        //NIMSDK.getMsgServiceObserve().observeReceiveMessage(observer,true);
*/
        //等待系统消息监听者完成作业
        SystemClock.sleep(800);

        NIMClient.getService(MsgService.class).queryRecentContacts()
                .setCallback(new RequestCallbackWrapper<List<RecentContact>>() {

                    @Override
                    public void onResult(int code, List<RecentContact> recents, Throwable e) {
                        // recents参数即为最近联系人列表（最近会话列表）

                        int j = recents.size();
                        for(int i = 0; i < j; i++) {
                            RecentContact r = recents.get(i);
                            String sender = r.getContactId();
                            com.example.k.chat20.datastruct.RecentContact recent = new com.example.k.chat20.datastruct.RecentContact();
                            recent.setSender( sender );
                            recent.setText( r.getContent() );
                            int unRead = r.getUnreadCount();
/*
                            //过去未读会话加上当前新接收的会话
                            if( map.containsKey(sender) ){
                                unRead += map.get(sender);
                                map.remove(sender);
                            }*/
                            recent.setUnreadCount( String.valueOf(unRead) );
                            recent.setSenderAccid( r.getContactId() );
                            Message msg = new Message();
                            msg.obj = recent;
                            handler.sendMessage(msg);
                            System.out.println("++++++++++最近会话列表:" + recent.getSender() + "  " + unRead );
                        }

                        /*
                        //完全为新接收的会话
                        if( !map.isEmpty() ){
                            Object[]  sl = map.keySet().toArray();
                            j = sl.length;
                            for(int i = 0; i < j; i++){
                                com.example.k.chat20.datastruct.RecentContact recent = new com.example.k.chat20.datastruct.RecentContact();
                                recent.setSender( sl[i].toString() );
                                recent.setUnreadCount( String.valueOf( map.get(sl[i]) ));
                                Message msg = new Message();
                                msg.obj = recent;
                                handler.sendMessage(msg);
                            }

                        }
                        */
                    }
                });
    }

}
