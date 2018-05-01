package com.leveling.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.leveling.RegisterActivity;
import com.leveling.chat.app.ChatApplication;
import com.leveling.entity.Url;
import com.leveling.entity.UserInfo;
import com.leveling.new_chat.IMSession;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.data.RegisterEntity;

public class HttpPostUtils {
    static String UserId = null;
    static int gender;
    static String phone = null;
    static int role;
    static String Nickname = null;
    static String picture = null;
    static String Invitation = null;
    static int Vip;
    static String Cardtype = null;
    static String Cardnumber = null;
    static int age;
    static String Ticket = null;
    private UserInfo userInfo;
    private static final String USER_NAME = "user_name";
    private static final String TICKET = "ticket";

    public static void setTicket(String ticket) {
        Ticket = ticket;
        if(Ticket == null){
            IMSession.getInstance().stopQuery();
            XGPushManager.unregisterPush(ChatApplication.getInstance(), new XGIOperateCallback() {
                @Override
                public void onSuccess(Object o, int i) {
                    System.out.println("XGPushManager unregist success");
                }

                @Override
                public void onFail(Object o, int i, String s) {
                    System.out.println("XGPushManager unregist fail");
                }
            });
        }else{
            final UserInfo userInfo = new UserInfo(ChatApplication.getInstance());
            IMSession.getInstance().setLoginResultListener(new IMSession.OnLoginResultListener() {
                @Override
                public void onLoginResult(String token) {
                    if(token == null)
                        IMSession.getInstance().login(userInfo.getStringInfo(RegisterActivity.USER_NAME), userInfo.getStringInfo(RegisterActivity.PASSWORD));
                }
            });
            IMSession.getInstance().login(userInfo.getStringInfo(RegisterActivity.USER_NAME), userInfo.getStringInfo(RegisterActivity.PASSWORD));
            XGPushConfig.enableDebug(ChatApplication.getInstance(), false);
            XGPushManager.registerPush(ChatApplication.getInstance(), HttpPostUtils.getUserId(), new XGIOperateCallback() {
                @Override
                public void onSuccess(Object o, int i) {
                    System.out.println("XGPushManager regist success, token is " + o.toString());
                }

                @Override
                public void onFail(Object o, int i, String s) {
                    System.out.println("XGPushManager regist fail");
                }
            });
        }
    }

    static String number;

    public static void setNumber(String number) {
        HttpPostUtils.number = number;
    }

    public static String getNumber() {
        return number;
    }

    public static String getTicket() {
        return Ticket;
    }

    public static String getUserId() {
        return UserId;
    }

    public static void setUserId(String UserID) {
        UserId = UserID;
    }

    public static void setGender(int Gender) {
        gender = Gender;
    }

    public static int getGender() {
        return gender;
    }

    public static void setPhone(String Phone) {
        phone = Phone;
    }

    public static String getPhone() {
        return phone;
    }

    public static void setRole(int Role) {
        role = Role;
    }

    public static int getRole() {
        return role;
    }

    public static void setNickname(String NickName) {
        Nickname = NickName;
    }

    public static String getNickname() {
        return Nickname;
    }

    public static String getPicture() {
        return picture;
    }

    public static void setPicture(String Picture) {
        picture = Picture;
    }

    public static String getInvitation() {
        return Invitation;
    }

    public static void setInvitation(String InvitationCode) {
        Invitation = InvitationCode;
    }

    public static int getVip() {
        return Vip;
    }

    public static String getCardtype() {
        return Cardtype;
    }

    public static void setCardtype(String CardType) {
        Cardtype = CardType;
    }

    public static String getCardnumber() {
        return Cardnumber;
    }

    public static void setCardnumber(String CardNumber) {
        Cardnumber = CardNumber;
    }

    public static int getAge() {
        return age;
    }

    public static void setAge(int Age) {
        age = Age;
    }

    public static void setVip(int VIP) {
        Vip = VIP;
    }


    interface IRunner {
        Object run(int actionID, String url, String postData, Handler handler) throws Exception;
    }

    static class HttpPostRunner implements HttpPostUtils.IRunner {

        @Override
        public Object run(int actionID, String url, String postData, Handler handler)
                throws Exception {
            String res = null;
            try {
                HttpParams params = new BasicHttpParams();
                // �������ӳ�ʱʱ��
                HttpConnectionParams.setConnectionTimeout(params, 30000);
                HttpClient client = new DefaultHttpClient(params);
                String fullURL = url;
                if (!url.startsWith("http://") && !url.startsWith("https://"))
                    fullURL = Url.urlShort + url;
                HttpPost request = new HttpPost(fullURL);
                if (Ticket != null)
                    request.addHeader("Authorization", "BasicAuth" + " " + Ticket);
                //System.out.println(userInfo.getStringInfo(TICKET));
                request.addHeader("Cookie", CookieManager.getCookie());
                StringEntity se = new StringEntity(postData, "UTF-8");
                se.setContentEncoding("UTF-8");
                se.setContentType("application/json");
                request.setEntity(se);
                request.setParams(params);
                HttpResponse response = client.execute(request);
                CookieManager.updateCookie(response);
                // ��ȡ���������ص�����
                String a = response.getStatusLine().getReasonPhrase();
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == HttpStatus.SC_OK || statusCode == HttpStatus.SC_BAD_REQUEST) {
                    res = EntityUtils.toString(response.getEntity(), "UTF-8");
                }else{
                    throw new Exception("Invalid status code " + statusCode);
                }
            } catch (Exception e) {
                throw e;
            }
            return res;
        }
    }

    static void Execute(final int actionID, final String url, final String postData, final Handler handler, final HttpPostUtils.IRunner runner) {
        new Thread() {
            @Override
            public void run() {
                // TODO Auto-generated method stu
                int actID = actionID;
                Object res = null;
                try {
                    res = runner.run(actionID, url, postData, handler);
                } catch (Exception e) {
                    actID = -actionID;
                }
                Message msg = new Message();
                msg.what = actID;
                msg.obj = res;
                handler.sendMessage(msg);
            }
        }.start();
    }

    public static void httpPostFile(int actionID, String url, JSONObject json, Handler handler) {
        IRunner runner = new HttpPostRunner();
        Execute(actionID, url, json.toString(), handler, runner);
    }

    public static void httpPostFile(int actionID, String url, String postData, Handler handler) {
        IRunner runner = new HttpPostRunner();
        Execute(actionID, url, postData, handler, runner);
    }

}
