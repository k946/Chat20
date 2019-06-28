package com.example.k.chat20.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import com.example.k.chat20.CHAT;
import com.example.k.chat20.R;
import com.example.k.chat20.service.ServiceMessage;

public class UploadActivity extends AppCompatActivity {

    ProgressBar pb;
    private int progress = 0;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        pb = findViewById(R.id.upload_pb);
    }


    public void ULbtnup(View view){
        pb.setVisibility(View.VISIBLE);
        CHAT.serviceMessage.exportMessageToDatabase(handler);
    }

    public void ULbtndown(View view){
        pb.setVisibility(View.VISIBLE);
        CHAT.serviceMessage.importMessageFromDatabase(handler);
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case ServiceMessage.DOWN_FINISH:
                    pb.setVisibility(View.INVISIBLE);
                    break;

                case ServiceMessage.UP_FINISH:
                    pb.setVisibility(View.INVISIBLE);
                    break;
            }
        }
    };
}
