package com.example.k.chat20.service;

import android.os.Handler;
import android.os.Message;

import com.example.k.chat20.CHAT;
import com.example.k.chat20.datastruct.UserInfo;
import com.example.k.chat20.dao.DB;
import com.example.k.chat20.workerinterface.WorkerLogin;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;

public class ServiceLogin implements WorkerLogin {

    public static final int INITUSER = 0;
    public static final int LOGIN_SUC = 200;

    private static boolean loginAppServer(String acount, String passwd){

        return DB.checkPasswd(acount, passwd);
    }

    public void login(final String acount, final String passwd, final Handler handler) {
        new Thread(){
            @Override
            public void run() {
                super.run();
                String accid = "";
                String token = "";
                Message msg = new Message();

                if( loginAppServer(acount,passwd) ){

                    accid = DB.getAccidByAcount(acount);
                    token = DB.getTokenByAcount(acount);
                    CHAT.user.setAccid(accid);
                    LoginInfo info = new LoginInfo(accid, token); // config...
                    RequestCallback<LoginInfo> callback = new RequestCallback<LoginInfo>() {

                        @Override
                        public void onSuccess(LoginInfo param) {
                            // 可以在此保存LoginInfo到本地，下次启动APP做自动登录用
                            msg.what = LOGIN_SUC;
                            handler.sendMessage(msg);

                            new Thread(){
                                @Override
                                public void run() {
                                    super.run();
                                    CHAT.user = DB.getUserInfoByAcount(acount);
                                }
                            }.start();

                        }

                        @Override
                        public void onFailed(int code) {
                            msg.what = code;
                            handler.sendMessage(msg);
                        }

                        @Override
                        public void onException(Throwable exception) {
                        }
                    };

                    NIMClient.getService(AuthService.class).login(info).setCallback(callback);
                }else{
                    msg.what = 0;
                    handler.sendMessage(msg);
                }
            }
        }.start();

     }


}
