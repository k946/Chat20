package com.example.k.chat20.workerinterface;

import android.os.Handler;

public interface WorkerLogin {

   void login(String account, String passwd, Handler handler);

}
