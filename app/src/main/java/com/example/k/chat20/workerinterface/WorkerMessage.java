package com.example.k.chat20.workerinterface;


import android.os.Handler;

import com.netease.nimlib.sdk.msg.model.IMMessage;

public interface WorkerMessage {

   void sendMessage(String account, String text, String type, Handler handler);

    void registerObserverForInstant(String fromAccid,Handler handler);

    void cancelOberserver();

    void importMessageFromDatabase(Handler handler);

    void exportMessageToDatabase(Handler handler);

    void getMessageListFromLocal(String myAccid ,String fromAccid, Handler handler, int limit);

    void cancelAnchor();
}
