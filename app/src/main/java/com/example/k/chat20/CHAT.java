package com.example.k.chat20;

import com.example.k.chat20.datastruct.UserInfo;
import com.example.k.chat20.service.ServiceAddFriend;
import com.example.k.chat20.service.ServiceContacter;
import com.example.k.chat20.service.ServiceLogin;
import com.example.k.chat20.service.ServiceMessage;
import com.example.k.chat20.service.ServiceRecentContact;
import com.example.k.chat20.service.ServiceRegister;

public class CHAT {
    public static UserInfo user = new UserInfo();
    public static final ServiceAddFriend serviceAddFriend = new ServiceAddFriend();
    public static final ServiceContacter serviceContacter = new ServiceContacter();
    public static final ServiceLogin serviceLogin = new ServiceLogin();
    public static final ServiceMessage serviceMessage = new ServiceMessage();
    public static final ServiceRegister serviceRegister = new ServiceRegister();
    public static final ServiceRecentContact serviceRecentContact = new ServiceRecentContact();
}
