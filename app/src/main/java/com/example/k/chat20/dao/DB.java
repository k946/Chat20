package com.example.k.chat20.dao;

import android.os.Handler;
import android.os.Message;

import com.example.k.chat20.CHAT;
import com.example.k.chat20.datastruct.Contacter;
import com.example.k.chat20.datastruct.UserInfo;
import com.example.k.chat20.service.ServiceContacter;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;

import oracle.sql.BLOB;

import static com.example.k.chat20.CHAT.serviceMessage;

public class DB {

    //序列化后保存对象到数据库
    public static void exportMessage(ArrayList<IMMessage> msgList, Handler handler){

        try{

            System.out.println("---------------上传size：" + msgList.size());
            //将对象存入blob字段
            ByteArrayOutputStream byteOut=new ByteArrayOutputStream();
            ObjectOutputStream outObj=new ObjectOutputStream(byteOut);
            outObj.writeObject(msgList) ;
            final byte[] objbytes=byteOut.toByteArray();

            Connection con = JdbcCommon.getConnection();
            con.setAutoCommit(false);

            Statement st = con.createStatement();

            try{
                st.executeUpdate("insert into CHAT_RECORD(\"userid\", \"record\") values ('" + CHAT.user.getUserId() + "', empty_blob())");
            }catch (SQLIntegrityConstraintViolationException ex){

            }

            st.execute("update CHAT_RECORD set \"record\"=empty_blob() where \"userid\"= '" + CHAT.user.getUserId() + "'");

            ResultSet rs = st.executeQuery("select \"record\" from CHAT_RECORD where \"userid\"= '" + CHAT.user.getUserId() + "' for update");

            if (rs.next()) {
                BLOB blob = (BLOB) rs.getBlob("record");
                OutputStream outStream = blob.getBinaryOutputStream();
                outStream.write(objbytes, 0, objbytes.length);
                outStream.flush();
                outStream.close();

            }

            byteOut.close();
            outObj.close();
            con.commit();
            st.close();
            con.close();

        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }

        Message msg = new Message();
        msg.what = serviceMessage.UP_FINISH;
        handler.sendMessage(msg);
    }


    public static void importMessage(Handler handler){
        try{

            Connection con = JdbcCommon.getConnection();
            con.setAutoCommit(false);

            Statement st = con.createStatement();
            con.commit();
            //取出blob字段中的对象，并恢复
            ResultSet rs = st.executeQuery("select \"record\" from CHAT_RECORD where \"userid\"= '"  + CHAT.user.getUserId() + "'" );

            BLOB inblob = null;
            if (rs.next()) {
                inblob = (BLOB) rs.getBlob("record");
            }else{
                return ;
            }

            InputStream is = inblob.getBinaryStream();
            BufferedInputStream input = new BufferedInputStream(is);

            //inblob.getBufferSize() 需要重新设置

            byte[] buff = new byte[102400];
            while(-1 != (input.read(buff, 0, buff.length)));

            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(buff));

            ArrayList<IMMessage> w3 = (ArrayList<IMMessage>)in.readObject();

            int j = w3.size();
            System.out.println("--------------聊天记录导入 " + "size:  " + j);
            for (int i = 0; i < j; i++) {
                Message msg = new Message();
                msg.what = serviceMessage.DOWN;
                msg.obj = w3.get(i);
                handler.sendMessage(msg);
            }

            st.close();
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }

        Message msg = new Message();
        msg.what = serviceMessage.DOWN_FINISH;
        handler.sendMessage(msg);
    }



    /*
     * 通过id查询好友列表
     */
    public static boolean isFriend(String userId1 ,String userId2){

        //System.out.println("请输入账号：");
        //acount = input.next();
        Connection conn = JdbcCommon.getConnection();

        try {

            String sql = "select * from contacts where (user_id = ? and contact_id = ?) or (user_id = ? and contact_id = ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, userId1);
            pst.setString(2, userId2);
            pst.setString(4, userId1);
            pst.setString(3, userId2);
            ResultSet rs = pst.executeQuery();

            if(null != rs && rs.next()) {
                return true;
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        JdbcCommon.closeConnection(conn);

        return false;

    }

    /*
     * 通过用户accid查找userID
     */
    public static String getUserIdByAccid(String accid){
        String userId = null;
        Connection conn = JdbcCommon.getConnection();
        try{
            String sql = "select user_id from users where accid=?";
            PreparedStatement pst =conn.prepareStatement(sql);
            pst.setString(1, accid);
            ResultSet rs = pst.executeQuery();
            if(null != rs && rs.next()) {
                userId = rs.getString("user_id");
                //System.out.println(userName);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        JdbcCommon.closeConnection(conn);
        return userId;
    }
    /*
     * 通过用户id查找用户名
     */
    public static String getUserNameByAccount(String userid){
        String userName = null;
        Connection conn = JdbcCommon.getConnection();
        try{
            String sql = "select accid from users where user_id=?";
            PreparedStatement pst =conn.prepareStatement(sql);
            pst.setString(1, userid);
            ResultSet rs = pst.executeQuery();
            if(null != rs && rs.next()) {
                userName = rs.getString("accid");
                //System.out.println(userName);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        JdbcCommon.closeConnection(conn);
        return userName;
    }
    /*
     * 通过用户id查找accid
     */
    public static String getAccidByAcount(String userid){
        String accid = null;
        Connection conn = JdbcCommon.getConnection();
        try{
            String sql = "select accid from users where user_id=?";
            PreparedStatement pst =conn.prepareStatement(sql);
            pst.setString(1, userid);
            ResultSet rs = pst.executeQuery();
            if(null != rs && rs.next()) {
                accid = rs.getString("accid");
                System.out.println(accid);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        JdbcCommon.closeConnection(conn);
        return accid;
    }

    /*
     * 通过用户id查找token
     */
    public static String getTokenByAcount(String userid){
        String token = null;
        Connection conn = JdbcCommon.getConnection();
        try{
            String sql = "select token from users where user_id=?";
            PreparedStatement pst =conn.prepareStatement(sql);
            pst.setString(1, userid);
            ResultSet rs = pst.executeQuery();
            if(null != rs && rs.next()) {
                token = rs.getString("token");
                System.out.println(token);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        JdbcCommon.closeConnection(conn);
        return token;
    }

    /*
     * 通过账号密码查询密码是否正确
     */
    public static boolean checkPasswd(final String userid,final String pwd){

        Connection conn = JdbcCommon.getConnection();
        try {

            String sql = "select * from users where user_id = ? and user_pwd = ? ";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, userid);
            pst.setString(2, pwd);
            ResultSet rs = pst.executeQuery();
            if(null != rs && rs.next()) {
                System.out.println("密码正确！！");
                return true;
            }else{
                System.out.println("密码错误！！");
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        JdbcCommon.closeConnection(conn);
        return false;
    }

    /*
     * 查询是否存在该用户
     */
    public static boolean checkAcount(String userid){
        //System.out.println("请输入账号：");
        //userid = input.next();

        Connection conn = JdbcCommon.getConnection();
        try {
            String sql = "select * from users where user_id = ? ";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, userid);
            ResultSet rs = pst.executeQuery();
            if(null != rs && rs.next()) {
                userid = rs.getString("user_id");
                System.out.println("用户已存在！！");
                return true;
            }else{
                System.out.println("该用户不存在！！");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JdbcCommon.closeConnection(conn);
        return false;
    }

    /*
     * 通过id查询用户信息
     */
    public static UserInfo getUserInfoByAcount(String acount){
        UserInfo user = null;
        //System.out.println("请输入账号：");
        //acount = input.next();
        Connection conn = JdbcCommon.getConnection();
        try {
            String sql = "select * from users where user_id = ? ";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, acount);
            ResultSet rs = pst.executeQuery();
            if(null != rs && rs.next()) {
                acount = rs.getString("user_id");
                String name = rs.getString("user_name");
                String pwd = rs.getString("user_pwd");
                String accid = rs.getString("accid");
                String token = rs.getString("token");
                int sex = rs.getInt("sex");
                String phone = rs.getString("phone");
                user = new UserInfo( acount, pwd, accid, token, name, sex, phone);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JdbcCommon.closeConnection(conn);


        return user;
    }

    /*
     * 通过id查询好友列表
     */
    public static ArrayList<Contacter> getFriendListByAcount(String acount ,Handler handler){
        ArrayList<Contacter> friendList = new ArrayList<>();
        //System.out.println("请输入账号：");
        //acount = input.next();
        Connection conn = JdbcCommon.getConnection();
        try {

            String sql = "select contact_id,contact_name from contacts where user_id = ? ";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, acount);
            ResultSet rs = pst.executeQuery();

            while(null != rs && rs.next()) {
                String friendId = rs.getString("contact_id");
                String friendName = rs.getString("contact_name");
                Contacter friend = new Contacter(friendId, friendName, friendId );
                System.out.println( "+++" + friend);
                Message msg = new Message();
                msg.obj = friend;
                msg.what = ServiceContacter.CONTACTERITEM;
                handler.sendMessage(msg);
            }

            sql = "select user_id from contacts where contact_id = ? ";
            pst = conn.prepareStatement(sql);
            pst.setString(1, acount);
            rs = pst.executeQuery();

            while(null != rs && rs.next()) {
                String friendId = rs.getString("user_id");
                Contacter friend = new Contacter(friendId, DB.getUserNameByAccount(friendId), friendId );
                System.out.println( "---" + friend);

                Message msg = new Message();
                msg.obj = friend;
                msg.what = ServiceContacter.CONTACTERITEM;
                handler.sendMessage(msg);

                //friendList.add(friend);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        JdbcCommon.closeConnection(conn);

        Message msg1 = new Message();
        msg1.what = ServiceContacter.ALLDOEN;
        handler.sendMessage(msg1);

        return friendList;

    }

    /*
     * 添加好友
     */
    public static void addFriend(String acount,String contactId){
        UserInfo user = null;
        boolean result = checkAcount(contactId);
        Connection conn = JdbcCommon.getConnection();
        if(result == true){
            try {
                String sql1 = "select user_name from Users where user_id = ? ";
                PreparedStatement pst1 = conn.prepareStatement(sql1);
                pst1.setString(1, contactId);
                ResultSet rs = pst1.executeQuery();
                if(null != rs && rs.next()) {
                    String userName = rs.getString("user_name");
                    String sql2 = "insert into Contacts(contact_id,contact_name,user_id) values(?,?,?)";
                    PreparedStatement pst2 = conn.prepareStatement(sql2);
                    pst2.setString(1, contactId);
                    pst2.setString(2, userName);
                    pst2.setString(3, acount);
                    pst2.executeUpdate();
                    System.out.println("添加成功！！");
                }
            }catch (SQLException e) {
                e.printStackTrace();
            }
            JdbcCommon.closeConnection(conn);
        }
    }

    /**
     * 注册：新增用户
     */
    public static void register(String userid, String pwd, String accid, String token, String username, int sex, String phone) {

        Connection conn = JdbcCommon.getConnection();
        try {
            Statement stmt = conn.createStatement();
            StringBuffer sb = new StringBuffer("insert into users(user_id,user_name,user_pwd,accid,token,sex,phone) values(");
            sb.append("'").append(userid).append("',");
            sb.append("'").append(username).append("',");
            sb.append("'").append(pwd).append("',");
            sb.append("'").append(accid).append("',");
            sb.append("'").append(token).append("',");
            sb.append(sex).append(",");
            sb.append("'").append(phone).append("'");
            sb.append(")");
            System.out.println(sb.toString());
            stmt.executeUpdate(sb.toString());

        } catch (SQLException e) {
            e.printStackTrace();
        }
        JdbcCommon.closeConnection(conn);
    }
}
