package com.example.k.chat20.datastruct;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.SystemMessageService;
import com.netease.nimlib.sdk.msg.constant.SystemMessageStatus;

public class MessageofSystem {
    private long messageID;
    private String sender;
    final static int DECLINED = 0;
    final static int PASSED = 1;
    final static int INIT = 2;



    public MessageofSystem(long messageID, String sender){
        this.messageID = messageID;
        this.sender = sender;

    }

    public void setMessageID(final long messageID) {
        this.messageID = messageID;
    }

    public void setSender(final String sender) {
        this.sender = sender;
    }

    public long getMessageID() {
        return messageID;
    }

    public String getSender() {
        return sender;
    }

    public void setMessageStatu(long messageID, int statu){
        switch(statu){
            case DECLINED:
                NIMClient.getService(SystemMessageService.class)
                        .setSystemMessageStatus(messageID, SystemMessageStatus.declined);
                break;
            case PASSED:
                NIMClient.getService(SystemMessageService.class)
                        .setSystemMessageStatus(messageID, SystemMessageStatus.passed);
                break;
            case INIT:
                NIMClient.getService(SystemMessageService.class)
                        .setSystemMessageStatus(messageID, SystemMessageStatus.init);
                break;
            default:
                break;
        }
    }
}
