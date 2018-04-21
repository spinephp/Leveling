package com.youyudj.leveling;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.youyudj.leveling.chat.db.ChatMessageBean;
import com.youyudj.leveling.entity.BannerInfo;
import com.youyudj.leveling.entity.Url;
import com.youyudj.leveling.entity.UserInfo;
import com.youyudj.leveling.new_chat.IMSession;
import com.youyudj.leveling.utils.HttpGetUtils;
import com.youyudj.leveling.utils.HttpPostUtils;
import com.youyudj.leveling.utils.NetWorkUtils;
import com.zsoft.SignalA.Hubs.HubConnection;
import com.zsoft.SignalA.Hubs.HubInvokeCallback;
import com.zsoft.SignalA.Hubs.HubOnDataCallback;
import com.zsoft.SignalA.Hubs.IHubProxy;
import com.zsoft.SignalA.Transport.Longpolling.LongPollingTransport;
import com.zsoft.SignalA.Transport.StateBase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WelcomActivity extends AppCompatActivity {

    private UserInfo userInfo;
    EditText pre_login_name, pre_login_pwd;
    private static final String USER_NAME = "user_name";
    private static final String PASSWORD = "password";
    private static final String FIRSTAUTOLOGIN = "firstautoLogin";
    private JSONObject json = new JSONObject();
    private String name0,name1,name2,name3;
    private static final String ISSAVEPASS = "savePassWord";
    private static final String AUTOLOGIN = "autoLogin";
    private Handler handlerwel;
    public HubConnection con = null;
    public IHubProxy hub = null;
    private TextView djs;
    private int ticks = 3;
    private boolean jumpTag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatService.start(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcom);
        handlerwel = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(jumpTag)
                    return;
                djs.setText("" + ticks);
                if(ticks == 0) {
                    getHome();
                }else{
                    handlerwel.sendEmptyMessageDelayed(0,1000);
                }
                ticks--;
            }
        };
        //new MyCount().execute();
        userInfo = new UserInfo(this);
        djs = (TextView) findViewById(R.id.djs);
        pre_login_name = (EditText) findViewById(R.id.pre_login_name);
        pre_login_pwd = (EditText) findViewById(R.id.pre_login_pwd);
        pre_login_name.setText(userInfo.getStringInfo(USER_NAME));
        pre_login_name.setText(userInfo.getStringInfo(PASSWORD));
        handlerwel.sendEmptyMessageDelayed(0,1000);
        findViewById(R.id.linearLayout2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpTag = true;
                getHome();
            }
        });

//        NetWorkUtils.isNetWorkAvailableOfGet("http://www.baidu.com", new Comparable<Boolean>() {
//            @Override
//            public int compareTo(Boolean available) {
//                    if (available) {
//
//                    } else {
//                        Toast.makeText(WelcomActivity.this, "请检查网络连接",Toast.LENGTH_LONG).show();
//                    }
//                    return 0;
//                }
//
//        });

  }
    class MyCount extends AsyncTask<Integer,Integer,Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if(values[0]!=0){
                djs.setText(values[0]+"");
            }
        }
        //倒计时
        @Override
        protected Integer doInBackground(Integer... params) {
            for (int i=3;i>=0;i--){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                publishProgress(i);
            }
            return null;
        }
    }
    private void getDate(JSONObject json){
        try {
            json.put("Phone", userInfo.getStringInfo(USER_NAME));
            json.put("PassWord", userInfo.getStringInfo(PASSWORD));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private Handler   handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            if(msg.what < 0){
                Toast.makeText(WelcomActivity.this, "无法连接服务器", Toast.LENGTH_SHORT).show();
                String url = "/api/SystemInfor/GetBanner";
                HttpGetUtils.httpGetFile(19,url,handler);
                Intent i = new Intent();
                i.setClass(WelcomActivity.this, MainActivity.class);
                startActivity(i);
                return;
            }
            switch (msg.what) {
                case 12:
                    try {
                        String res = (String) msg.obj;
                        if (res==null){
                            return;
                        }
                        JSONObject result = new JSONObject(res);
                        String success = result.getString("Success");
                        String err = result.getString("ErrMsg");
                        String date = result.getString("Data");
                        Log.d("TTTTTT",result.toString());
                        if (success == "true") {
//                            Uri address = Uri.parse(Url.urlShort);
//                            ConnectionRequested(address);
                            JSONObject inresult = new JSONObject(date);
                            String UserId = inresult.getString("UserID");
                            int Gender = inresult.getInt("Gender");
                            String UserPhone = inresult.getString("Phone");
                            int role = inresult.getInt("Role");
                            String nickname = inresult.getString("NickName");
                            String picture = inresult.getString("Picture");
                            String InvitationCode = inresult.getString("InvitationCode");
                            int vip = inresult.getInt("VIP");
                            String cardtype = inresult.getString("CardType");
                            String cardnumber = inresult.getString("CardNumber");
                            int age = inresult.getInt("Age");
                            String Ticket = inresult.getString("ticket");
                            HttpPostUtils.setUserId(UserId);
                            HttpPostUtils.setGender(Gender);
                            HttpPostUtils.setPhone(UserPhone);
                            HttpPostUtils.setRole(role);
                            HttpPostUtils.setNickname(nickname);
                            HttpPostUtils.setPicture(picture);
                            HttpPostUtils.setInvitation(InvitationCode);
                            HttpPostUtils.setVip(vip);
                            HttpPostUtils.setCardtype(cardtype);
                            HttpPostUtils.setCardnumber(cardnumber);
                            HttpPostUtils.setAge(age);
                            HttpPostUtils.setTicket(Ticket);
                            HttpPostUtils.setNickname(nickname);
                            HttpPostUtils.setNumber(inresult.getString("Number"));
                            String url = "/api/SystemInfor/GetBanner";
                            HttpGetUtils.httpGetFile(19,url,handler);
                        }
                        else if (success=="false"){
                            if(err != null)
                                Toast.makeText(WelcomActivity.this, err, Toast.LENGTH_SHORT).show();
                            userInfo.clear();
                            userInfo.setUserInfo(ISSAVEPASS, false);
                            userInfo.setUserInfo(AUTOLOGIN, false);
                            userInfo.setUserInfo("login", false);
                            String url = "/api/SystemInfor/GetBanner";
                            HttpGetUtils.httpGetFile(19,url,handler);
                            Intent i = new Intent();
                            i.setClass(WelcomActivity.this, MainActivity.class);
                            startActivity(i);
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                case 19:
                    try {
                        String res = (String) msg.obj;
                        if (res == null){
                            return;
                        }
                        JSONObject result = new JSONObject(res);
                        String success = result.getString("Success");
                        String err = result.getString("ErrMsg");
                        JSONArray data = result.getJSONArray("Data");
                        int length = data.length();
                        if(length > 0) {
                            JSONObject oj0 = data.getJSONObject(0);
                            name0 = (String) oj0.get("systemValue");
                            BannerInfo.name0 = name0;
                            BannerInfo.remark0 = (String) oj0.get("remark");
                            Log.d("TAG", name0);
                            JSONObject oj1 = data.getJSONObject(1);
                            name1 = (String) oj1.get("systemValue");
                            BannerInfo.name1 = name1;
                            BannerInfo.remark1 = (String) oj1.get("remark");
                            Log.d("TAG", name1);
                            JSONObject oj2 = data.getJSONObject(2);
                            name2 = (String) oj2.get("systemValue");
                            BannerInfo.remark2 = (String) oj2.get("remark");
                            BannerInfo.name2 = name2;
                            Log.d("TAG", name2);
                            JSONObject oj3 = data.getJSONObject(3);
                            name3 = (String) oj3.get("systemValue");
                            BannerInfo.remark3 = (String) oj3.get("remark");
                            BannerInfo.name3 = name3;
                            Log.d("TAG", name3);
                            if (success == "true") {
//                            Uri address = Uri.parse(Url.urlShort);
//                            ConnectionRequested(address);
//                            Toast.makeText(WelcomActivity.this, success,
//                                    Toast.LENGTH_LONG).show();
                            } else if (success == "false") {
                                Toast.makeText(WelcomActivity.this, err, Toast.LENGTH_LONG)
                                        .show();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Intent i = new Intent(WelcomActivity.this, MainActivity.class);
                    startActivity(i);
                    break;
                default:
                    break;
            }
        }
    };

    private void getHome() {
        if (userInfo.getBooleanInfo(FIRSTAUTOLOGIN) == true){
            getDate(json);
            String url = "/api/Users/PostLoginUsers";
            HttpPostUtils.httpPostFile(12,url, json, handler);
        }else{
            String url = "/api/SystemInfor/GetBanner";
            HttpGetUtils.httpGetFile(19,url,handler);
        }
    }
}
