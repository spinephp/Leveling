package com.youyudj.leveling;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.youyudj.leveling.entity.UserInfo;
import com.youyudj.leveling.new_chat.IMSession;
import com.youyudj.leveling.utils.HttpPostUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements OnClickListener {
    private EditText username;
    private EditText password;
    private Button btn_login;
    private TextView btn_register;
    private TextView find_password;
    private UserInfo userInfo;
    private String name;
    private String pwd;
    private TextView btn_back_to_main;
    private static final String USER_NAME = "user_name";
    private static final String LOGIN = "login";
    private static final String PASSWORD = "password";
    private static final String USERID = "userid";
    private static final String TICKET = "ticket";
    private static final String NICKNAME = "nickname";
    private static final String ISSAVEPASS = "savePassWord";
    private static final String AUTOLOGIN = "autoLogin";
    private static final String FIRSTAUTOLOGIN = "firstautoLogin";
    private JSONObject json = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        userInfo = new UserInfo(this);
        userInfo.clear();
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_register = (TextView) findViewById(R.id.btn_register);
        find_password = (TextView) findViewById(R.id.find_password);
        btn_back_to_main = (TextView) findViewById(R.id.btn_back_to_main);
        btn_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        find_password.setOnClickListener(this);
        btn_back_to_main.setOnClickListener(this);
    }

    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            //登陆按钮点击事件
            case R.id.btn_login:
                getDate(json);
                String url = "/api/Users/PostLoginUsers";
                name = username.getText().toString();
                pwd = password.getText().toString();
                userInfo.setUserInfo(USER_NAME, name);
                userInfo.setUserInfo(PASSWORD, pwd);
                HttpPostUtils.httpPostFile(6, url, json, handler);
                break;
            //注册按钮点击事件
            case R.id.btn_register:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.find_password:
                Intent tent = new Intent(LoginActivity.this, FindBackPasswordActivity.class);
                startActivity(tent);
                break;
            case R.id.btn_back_to_main:
                Intent intent1 = new Intent(LoginActivity.this, MainActivity.class);
                intent1.putExtra("id", 10);
                startActivity(intent1);
                break;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 6:
                    try {
                        String res = (String) msg.obj;
                        if (res == null) {
                            return;
                        }
                        JSONObject result = new JSONObject(res);
                        String success = result.getString("Success");
                        String err = result.getString("ErrMsg");

                        if (success == "true") {
                            String date = result.getString("Data");
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
                            userInfo.setUserInfo(TICKET, Ticket);
                            userInfo.setUserInfo(USERID, UserId);
                            userInfo.setUserInfo(NICKNAME, nickname);
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
                            //Toast.makeText(LoginActivity.this, success,Toast.LENGTH_SHORT ).show();
                            userInfo.setUserInfo(ISSAVEPASS, true);
                            userInfo.setUserInfo(AUTOLOGIN, true);
                            userInfo.setUserInfo(FIRSTAUTOLOGIN, true);
                            userInfo.setUserInfo(LOGIN, true);
                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(i);
                        } else if (success == "false") {
                            Toast.makeText(LoginActivity.this, err, Toast.LENGTH_SHORT).show();
                            //Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                            userInfo.setUserInfo(ISSAVEPASS, false);
                            userInfo.setUserInfo(AUTOLOGIN, false);
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private void getDate(JSONObject json) {
        try {
            json.put("Phone", username.getText().toString());
            json.put("PassWord", pwd = password.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
