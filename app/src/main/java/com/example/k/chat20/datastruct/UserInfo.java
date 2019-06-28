package com.example.k.chat20.datastruct;

public class UserInfo {
    private String userId;				//用户账号
    private String userName;			//用户名
    private String userPwd;				//密码
    private String accid;				//网易云信账号
    private String token;				//网易云信密码
    private int sex;					//性别
    private String phone;				//联系电话

    public UserInfo(){}

    public UserInfo(String userId){
        this.userId = userId;
    }

    public UserInfo(String account , String accid){
        this.userId = account;
        this.accid = accid;
    }

    public UserInfo(String account , String accid, String userName){
        this.userId = account;
        this.accid = accid;
        this.userName = userName;
    }

    public UserInfo(String userId, String userPwd, String accId, String token, String userName, int sex, String phone) {
        super();
        this.userId = userId;
        this.userName = userName;
        this.userPwd = userPwd;
        this.accid = accId;
        this.token = token;
        this.sex = sex;
        this.phone = phone;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getAccid() {
        return accid;
    }

    public void setAccid(final String accid) {
        this.accid = accid;
    }



    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    @Override
    public String toString() {
        return "User [userId=" + userId + ", userName=" + userName + ", userPwd=" + userPwd + ", accId=" + accid
                + ", token=" + token + ", sex=" + sex + ", phone=" + phone ;
    }



}
