package com.example.k.chat20.datastruct;

public class Contacter {
    private String contactId;				//联系人账号
    private String contactName;				//联系人昵称
    private String accid;					//联系人Accid

    public Contacter(String contactId, String contactName, String accid) {
        super();
        this.contactId = contactId;
        this.contactName = contactName;
        this.accid = accid;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getAccid() {
        return accid;
    }

    public void setAccid(String userId) {
        this.accid = userId;
    }

    @Override
    public String toString() {
        return "Contact [contactId=" + contactId + ", contactName=" + contactName + ", accid=" + accid + "]";
    }

}
