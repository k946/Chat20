package com.example.k.chat20.service;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.widget.Toast;

import com.example.k.chat20.CHAT;
import com.example.k.chat20.dao.DB;
import com.example.k.chat20.datastruct.Contacter;
import com.example.k.chat20.workerinterface.WorkerMessage;
import com.netease.nimlib.sdk.NIMSDK;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.QueryDirectionEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.k.chat20.CHAT.serviceMessage;

public class ServiceMessage implements WorkerMessage {

    static Observer<List<IMMessage>> observer;  //只会注册一个消息接收者
    static IMMessage anchor = null;
    static String fromAccount = "";
    public static final int SEND_NOW = 0;
    public static final int RECEIVER_NOW = 1;
    public static final int SEND_OLD = 2;
    public static final int RECEIVER_OLD = 3;

    public static final int LOAD_NUMBER = 4;

    public static final int UP = 6;
    public static final int DOWN = 7;
    public static final int UP_FINISH = 8;
    public static final int DOWN_FINISH = 9;




    //发送文本消息
    public void sendMessage(String accid, String text, String type, Handler handler){
        Message handlerMSG = new Message();


        //创建消息
        final IMMessage imMessage = MessageBuilder.createTextMessage(accid,SessionTypeEnum.P2P,text );
        //回调
        RequestCallback<Void> callback =

                new RequestCallback<Void>() {

                    @Override
                    public void onSuccess(final Void param) {
                        handlerMSG.what = SEND_NOW;
                        handler.sendMessage(handlerMSG);
                        //NIMSDK.getMsgService().saveMessageToLocalEx(imMessage, false, imMessage.getTime() );
                    }

                    @Override
                    public void onFailed(final int code) {
                        handlerMSG.what = code;
                        handler.sendMessage(handlerMSG);
                    }

                    @Override
                    public void onException(final Throwable exception) {

                    }

                };

        NIMSDK.getMsgService().sendMessage(imMessage,true).setCallback(callback);
    }

    //聊天窗口接收消息
    public void registerObserverForInstant(String fromAccid, Handler handler){

        observer = (Observer<List<IMMessage>>) imMessages -> {
            //获取消息的数量
            int j = imMessages.size();
            System.out.println("\n--------------" + j);
            for(int i = 0; i < j; i++) {

                //消息链中取出消息对象IMMessage
                IMMessage msg = imMessages.get(i);

                System.out.println( "------------------------实时接收聊天: " + msg.getContent());

                if( msg.getFromAccount().equals(fromAccid) ){
                    Message handlerMSG = new Message();
                    handlerMSG.what = RECEIVER_NOW;
                    handlerMSG.obj = msg;
                    handler.sendMessage(handlerMSG);
                }
            }
        };

        //注册消息监听者
        NIMSDK.getMsgServiceObserve().observeReceiveMessage(observer,true);
    }
/*
    //接收消息保存至本地
    public void registerObserverForSave(){
        Message handlerMSG = new Message();

        observer = (Observer<List<IMMessage>>) imMessages -> {
            //获取消息的数量
            int j = imMessages.size();
            System.out.println("-----------保存聊天记录到本地数据库: size : " + j);
            for(int i = 0; i < j; i++) {

                //消息链中取出消息对象IMMessage
                IMMessage msg = imMessages.get(i);
                //保存消息
                //NIMSDK.getMsgService().saveMessageToLocalEx(msg, false, msg.getTime() );

                System.out.println( "--------------------------保存聊天记录到本地数据库: " + msg.getContent());
            }
        }

        //注册消息监听者
//        NIMSDK.getMsgServiceObserve().observeReceiveMessage(observer,true);
    }
*/


    //查重
    public void duplicate(IMMessage msg){

        RequestCallbackWrapper<List<IMMessage>> callbackWrapper = new RequestCallbackWrapper<List<IMMessage>>() {
            @Override
            public void onResult(final int code, final List<IMMessage> result, final Throwable exception) {
                boolean flag = false;
                if(result != null){
                   //获取消息的数量
                    int j = result.size();
                    System.out.println("-------------------从远程数据库获取聊天记录： " + msg.getContent() + " time:" + msg.getTime() + "  uuid:" + msg.getUuid());
                    System.out.println("-------------------从本地数据库获取聊天记录：size: " +j);

                    for(int i = 0; i < j ; i++) {
                        //消息链中取出消息对象IMMessage
                        IMMessage msg2 = result.get(i);
                        System.out.println("-------------------从本地数据库获取聊天记录： " + msg2.getContent() + " time:" + msg2.getTime() + "  uuid:" + msg2.getUuid());
                        //查重
                        if( msg2.getUuid().equals( msg.getUuid() ) )
                               flag = true;
                   }

                }

                if( !flag ) {
                    NIMSDK.getMsgService().saveMessageToLocalEx(msg, false, msg.getTime());
                    System.out.println("-----------保存");
                }
            }
        };

        List<String> accid = new ArrayList<>();
        accid.add(msg.getFromAccount());

        // 查询anchor往前，,limit, 降序排序,
        NIMSDK.getMsgService().searchAllMessageHistory("关键字", accid, msg.getTime() + 1, 1)
                .setCallback(callbackWrapper);
    }

    //从远程数据库下载消息
    public void importMessageFromDatabase(Handler handler){

        Handler handler1 = new Handler(){
            int progress = 0;
            @Override
            public void handleMessage(final Message msg1) {
                super.handleMessage(msg1);
                switch (msg1.what){
                    case DOWN_FINISH:
                        Message msg = new Message();
                        msg.what = serviceMessage.DOWN_FINISH;
                        handler.sendMessage(msg);
                        break;

                    case DOWN:
                        IMMessage imMessage = (IMMessage)msg1.obj;
                        System.out.println( "--------------聊天记录导入: " + imMessage.getContent());
                        duplicate(imMessage);
                        break;
                }

                //回传进度
                progress++;
                if( progress % 10 == 0){
                    Message msg = new Message();
                    msg.what = DOWN;
                    msg.obj = progress;
                    handler.sendMessage(msg);
                }
            }
        };

        new Thread(){
            @Override
            public void run() {
                super.run();
                DB.importMessage(handler1);
            }
        }.start();
    }


    //上传消息到远程数据库
    public void exportMessageToDatabase(Handler handler){
        ArrayList<IMMessage> msgList = new ArrayList<>();

        //历史消息打包
        Handler handler1 = new Handler(){
            @Override
            public void handleMessage(final Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case ServiceMessage.SEND_OLD:
                        msgList.add( (IMMessage) msg.obj );
                        break;

                    case ServiceMessage.RECEIVER_OLD:
                        msgList.add( (IMMessage) msg.obj );
                        break;

                    default:
                        break;
                }
            }
        };


        //通过联系人获取历史记录
        Handler handler2 = new Handler(){
            int progress = 0;
            @Override
            public void handleMessage(final Message msg1) {
                super.handleMessage(msg1);

                switch(msg1.what){
                    case ServiceContacter.CONTACTERITEM:
                        //仅上传与不同好友之间聊天的前10条记录
                        getMessageListFromLocal(CHAT.user.getAccid(), ( (Contacter) msg1.obj ).getContactId(), handler1, 10);

                        //回传进度
                        progress++;
                        if( progress % 10 == 0){
                            Message msg = new Message();
                            msg.what = UP;
                            msg.obj = progress;
                            handler.sendMessage(msg);
                        }
                        break;

                    //上传到远程数据库
                    case ServiceContacter.ALLDOEN:
                        SystemClock.sleep(200);
                        new Thread(){
                            @Override
                            public void run() {
                                super.run();
                                DB.exportMessage(msgList, handler);
                            }
                        }.start();
                        break;

                }
            }
        };

        new Thread() {
            @Override
            public void run() {
                super.run();
                //获取联系人
                CHAT.serviceContacter.openList( CHAT.user.getUserId()
                        ,"", handler2);
            }
        }.start();
    }



    public void cancelOberserver(){
        //注销消息监听者
        NIMSDK.getMsgServiceObserve().observeReceiveMessage(observer,false);
    }

    public void clearUnread(String fromAccid){
        NIMSDK.getMsgService().clearUnreadCount(fromAccid, SessionTypeEnum.P2P);
    }

    //从本地获取消息
    public void getMessageListFromLocal(String myAccid , String fromAccid, Handler handler, int limit){

        // 检测聊天窗口是否切换，切换则定义新的锚点
        if( anchor == null || !fromAccid.equals(fromAccount) ) {
            fromAccount = fromAccid;
            anchor = MessageBuilder.createTextMessage(fromAccid, SessionTypeEnum.P2P, "");
        }

        //处理查询的消息列表
        RequestCallbackWrapper<List<IMMessage>> callbackWrapper = new RequestCallbackWrapper<List<IMMessage>>() {
            @Override
            public void onResult(final int code, final List<IMMessage> result, final Throwable exception) {

                //获取消息的数量
                int j = result.size();
                String uuid = "";
                long time = 0;
                System.out.println("-------------------从本地数据库获取聊天记录：size: " +j);

                if(j > 0 ){
                    Message msg1 = new Message();
                    msg1.what = LOAD_NUMBER;
                    msg1.obj = j;
                    handler.sendMessage(msg1);
                }

                for(int i = 0; i < j ; i++) {

                    //消息链中取出消息对象IMMessage
                    IMMessage msg = result.get(i);
                    System.out.println("-------------------从本地数据库获取聊天记录： " + msg.getContent() + " time:" + msg.getTime() + "  uuid:" + msg.getUuid());

                    if( !uuid.equals(msg.getUuid()) && time != msg.getTime() ){
                        uuid = msg.getUuid();
                        time = msg.getTime();
                        Message msgHandler = new Message();
                        msgHandler.what = msg.getFromAccount().equals(myAccid) ? SEND_OLD : RECEIVER_OLD;
                        msgHandler.obj = msg;
                        anchor = msg;
                        handler.sendMessage(msgHandler);
                    }
                }

            }
        };

        List<String> accid = new ArrayList<>();
        accid.add(fromAccid);
        accid.add(myAccid);

        // 查询anchor往前，,limit, 降序排序,
        NIMSDK.getMsgService().searchMessageHistory("关键字", accid, anchor, limit)
                .setCallback(callbackWrapper);

    }

    public void cancelAnchor(){
        //清空瞄点
        anchor = null;
        fromAccount = "";
    }



}
