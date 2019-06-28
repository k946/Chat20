package com.example.k.chat20.workerinterface;


import android.os.Handler;

public interface WorkerRegister {
    void register(String acount, String passwd, Handler handler);
}
