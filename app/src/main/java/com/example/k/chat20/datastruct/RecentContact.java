package com.example.k.chat20.datastruct;

public class RecentContact {
    private String sender;
    private String text;
    private String unreadCount;
    private String senderAccid;

    public RecentContact(String sender, String text, String unreadCount){
        this.sender = sender;
        this.text = text;
        this.unreadCount = unreadCount;
    }


    public void setSenderAccid(final String senderAccid) {
        this.senderAccid = senderAccid;
    }

    public String getSenderAccid() {
        return senderAccid;
    }

    public RecentContact(){};

    public String getSender() {
        return sender;
    }

    public String getText() {
        return text;
    }

    public String getUnreadCount() {
        return unreadCount;
    }

    public void setSender(final String sender) {
        this.sender = sender;
    }

    public void setText(final String text) {
        this.text = text;
    }

    public void setUnreadCount(final String unreadCount) {
        this.unreadCount = unreadCount;
    }
}
