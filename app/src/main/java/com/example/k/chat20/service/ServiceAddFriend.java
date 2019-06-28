package com.example.k.chat20.service;

import android.os.Handler;
import android.os.Message;

import com.example.k.chat20.CHAT;
import com.example.k.chat20.datastruct.MessageofSystem;
import com.example.k.chat20.dao.DB;
import com.example.k.chat20.workerinterface.WorkerAddFriend;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.NIMSDK;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.friend.constant.VerifyType;
import com.netease.nimlib.sdk.friend.model.AddFriendData;
import com.netease.nimlib.sdk.msg.SystemMessageService;
import com.netease.nimlib.sdk.msg.constant.SystemMessageStatus;
import com.netease.nimlib.sdk.msg.constant.SystemMessageType;
import com.netease.nimlib.sdk.msg.model.SystemMessage;

import java.util.ArrayList;
import java.util.List;


public class ServiceAddFriend implements WorkerAddFriend {

    public final static int NEWFRIEND = 1;
    public final static int ADDFRIEND = 0;
    public final static int ISFRIEND = 2;

    public void addFriend(String contact, String text, Handler handler) {
        new Thread(){
            @Override
            public void run() {
                super.run();
                //判断是否已经为好友以及是否存在该用户
                if( DB.isFriend(contact, CHAT.user.getUserId()) && DB.checkAcount(contact) ) {
                    Message msg = new Message();
                    msg.what = ISFRIEND;
                    handler.sendMessage(msg);
                }else{
                    final VerifyType verifyType = VerifyType.VERIFY_REQUEST; // 发起好友验证请求
                    String msg = "好友请求附言";
                    NIMSDK.getFriendService().addFriend(new AddFriendData(contact, verifyType, msg))
                            .setCallback(new RequestCallback<Void>() {
                                             @Override
                                             public void onSuccess(final Void param) {
                                                 Message msg = new Message();
                                                 msg.what = ADDFRIEND;
                                                 handler.sendMessage(msg);
                                             }

                                             @Override
                                             public void onFailed(final int code) {
                                                 Message msg = new Message();
                                                 msg.what = code;
                                                 handler.sendMessage(msg);
                                             }

                                             @Override
                                             public void onException(final Throwable exception) {
                                             }
                                         }
                            );
                }

            }
        }.start();
    }


    public void getMessageFromFriendSystem( Handler handler) {

        new Thread(){
            @Override
            public void run() {
                super.run();
                List<SystemMessageType> types = new ArrayList<>();
                types.add(SystemMessageType.AddFriend);

                int unread = NIMClient.getService(SystemMessageService.class)
                        .querySystemMessageUnreadCountByType(types);

                // 只查询“添加好友”类型的系统通知, 从头开始查询
                NIMClient.getService(SystemMessageService.class).querySystemMessageByType(types, 0, unread)
                        .setCallback(new RequestCallback<List<SystemMessage>>() {
                            @Override
                            public void onSuccess(List<SystemMessage> param) {
                                int j = param.size();
                                String fromAcount = "";

                                for(int i = 0 ; i < j; i++){
                                    SystemMessage NIMsm = param.get(i);

                                    //判断消息是否以及被处理
                                    if( fromAcount.equals(NIMsm.getFromAccount()) || NIMsm.getStatus() == SystemMessageStatus.declined ||
                                            NIMsm.getStatus() == SystemMessageStatus.passed)
                                        continue;

                                    fromAcount = NIMsm.getFromAccount();

                                    Message msg = new Message();
                                    msg.what = NEWFRIEND;
                                    MessageofSystem mos = new MessageofSystem( NIMsm.getMessageId(), NIMsm.getFromAccount() );
                                    msg.obj = mos;
                                    handler.sendMessage(msg);

                                }
                            }

                            @Override
                            public void onFailed(int code) {
                                Message msg = new Message();
                                msg.what = code;
                                handler.sendMessage(msg);
                            }

                            @Override
                            public void onException(Throwable exception) {
                                // error
                            }

                        });
            }
        }.start();
    }


    public void refuseAddFriend(String contactId, long messageId){

        new Thread(){
            @Override
            public void run() {
                super.run();
                NIMClient.getService(SystemMessageService.class).setSystemMessageStatus(messageId, SystemMessageStatus.declined );
            }
        }.start();

    }

    public void agreeAddFriend(String contactId, long messageId){

        new Thread(){
            @Override
            public void run() {
                super.run();
                DB.addFriend( CHAT.user.getUserId(), contactId);
                NIMClient.getService(SystemMessageService.class).setSystemMessageStatus(messageId, SystemMessageStatus.passed );
            }
        }.start();

    }

}
