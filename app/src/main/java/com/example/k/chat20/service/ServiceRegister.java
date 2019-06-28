package com.example.k.chat20.service;

import android.os.Handler;
import android.os.Message;

import com.example.k.chat20.dao.DB;
import com.example.k.chat20.workerinterface.WorkerRegister;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.security.MessageDigest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ServiceRegister implements WorkerRegister {
    public static final int REGISTER_FAIL = 0;
    public static final int REGISTER_SUC = 1;

    public void register(String acount, String passwd, Handler handler){
        new Thread(){
            @Override
            public void run() {
                super.run();
                String token = String.valueOf( passwd.hashCode() );
                if( !DB.checkAcount(acount) && registerNIM( acount, token ) == true  ){
                    DB.register(acount, passwd, acount, token, acount, 1, "");
                    Message msg = new Message();
                    msg.what = REGISTER_SUC;
                    handler.sendMessage(msg);
                }else{
                    Message msg = new Message();
                    msg.what = REGISTER_FAIL;
                    handler.sendMessage(msg);
                }
            }
        }.start();


    }


    //注册网易云信账号
    private boolean registerNIM(String accid, String token){
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            //String url = "https://api.netease.im/nimserver/user/create.action";
            String url = "https://api.netease.im/nimserver/user/create.action";
            HttpPost httpPost = new HttpPost(url);

            String appKey = "e8afdab3905c4d4102dee0bac9b032b0";
            String appSecret = "19ef4ce59c52";
            String nonce = "12345";
            String curTime = String.valueOf((new Date()).getTime() / 1000L);
            String checkSum = CheckSumBuilder.getCheckSum(appSecret, nonce, curTime);//参考 计算CheckSum的java代码

            // 设置请求的header
            httpPost.addHeader("Nonce", nonce);
            httpPost.addHeader("AppKey", appKey);
            httpPost.addHeader("Nonce", nonce);
            httpPost.addHeader("CurTime", curTime);
            httpPost.addHeader("CheckSum", checkSum);
            httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

            // 设置请求的参数
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("accid", accid));
            nvps.add(new BasicNameValuePair("token", token));

            httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

            // 执行请求
            HttpResponse response = null;

            response = httpClient.execute(httpPost);

            Message msg = new Message();
            // 打印执行结果
            if (EntityUtils.toString(response.getEntity(), "utf-8").contains("\"code\":200") ){
                return true;
            }else{
                return false;
            }

        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessError e){
            e.printStackTrace();
        }


        return false;
    }

}


class CheckSumBuilder {
    // 计算并获取CheckSum
    public static String getCheckSum(String appSecret, String nonce, String curTime) {
        return encode("sha1", appSecret + nonce + curTime);
    }

    // 计算并获取md5值
    public static String getMD5(String requestBody) {
        return encode("md5", requestBody);
    }

    private static String encode(String algorithm, String value) {
        if (value == null) {
            return null;
        }
        try {
            MessageDigest messageDigest
                    = MessageDigest.getInstance(algorithm);
            messageDigest.update(value.getBytes());
            return getFormattedText(messageDigest.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String getFormattedText(byte[] bytes) {
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        for (int j = 0; j < len; j++) {
            buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
            buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
        }
        return buf.toString();
    }

    private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
}