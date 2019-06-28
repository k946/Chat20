package com.example.k.chat20.workerinterface;

import android.os.Handler;

public interface WorkerAddFriend {
    void addFriend(String id, String text, Handler handler);
    void getMessageFromFriendSystem( Handler handler);
    void refuseAddFriend(String acount, long messageId);
    void agreeAddFriend(String contactId, long messageId);
}
