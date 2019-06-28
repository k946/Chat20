package com.example.k.chat20.service;

import android.os.Handler;

import com.example.k.chat20.dao.DB;
import com.example.k.chat20.workerinterface.*;

public class ServiceContacter implements WorkerContacter {

    public static final int CONTACTERITEM = 0;
    public static final int ALLDOEN = 1;

    public void openList(String userID, String listName, Handler handler){

        new Thread(){
            @Override
            public void run() {
                super.run();
                DB.getFriendListByAcount(userID,handler);
            }
        }.start();

    }
}
