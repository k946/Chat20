package com.example.k.chat20;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.uinfo.UserInfoProvider;
import com.netease.nimlib.sdk.uinfo.model.UserInfo;

public class BaseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        NIMClient.init(this, loginInfo(), options());

    }


    // 如果已经存在用户登录信息，返回LoginInfo，否则返回null即可

    private LoginInfo loginInfo() {
        return null;

    }

    // 如果返回值为 null，则全部使用默认参数。

    private SDKOptions options() {

        SDKOptions options = new SDKOptions();

        // 如果将新消息通知提醒托管给 SDK 完成，需要添加以下配置。否则无需设置。

        StatusBarNotificationConfig config = new StatusBarNotificationConfig();


        config.notificationSmallIconId = R.drawable.ic_launcher_foreground;

        // 呼吸灯配置

        config.ledARGB = Color.GREEN;

        config.ledOnMs = 1000;

        config.ledOffMs = 1500;

        // 通知铃声的uri字符串

        config.notificationSound = "android.resource://com.netease.nim.demo/raw/msg";

        options.statusBarNotificationConfig = config;


        // 配置保存图片，文件，log 等数据的目录

        // 如果 options 中没有设置这个值，SDK 会使用下面代码示例中的位置作为 SDK 的数据目录。

        // 该目录目前包含 log, file, image, audio, video, thumb 这6个目录。

        // 如果第三方 APP 需要缓存清理功能， 清理这个目录下面个子目录的内容即可。

        String sdkPath = Environment.getExternalStorageDirectory() + "/" + getPackageName() + "/nim";

        options.sdkStorageRootPath = sdkPath;


        // 配置是否需要预下载附件缩略图，默认为 true

        options.preloadAttach = true;


        // 配置附件缩略图的尺寸大小。表示向服务器请求缩略图文件的大小

        // 该值一般应根据屏幕尺寸来确定， 默认值为 Screen.width / 2

        options.thumbnailSize = 50;


        // 用户资料提供者, 目前主要用于提供用户资料，用于新消息通知栏中显示消息来源的头像和昵称

        options.userInfoProvider = new UserInfoProvider() {

            @Override

            public UserInfo getUserInfo(String account) {

                return null;

            }


            @Override
            public String getDisplayNameForMessageNotifier(String account, String sessionId, SessionTypeEnum sessionType) {

                return null;

            }


            @Override
            public Bitmap getAvatarForMessageNotifier(SessionTypeEnum sessionType, String sessionId) {

                return null;

            }

        };


        return options;

    }

}