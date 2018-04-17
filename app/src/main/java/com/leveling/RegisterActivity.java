package com.leveling;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.leveling.entity.UserInfo;
import com.leveling.new_chat.IMSession;
import com.leveling.utils.HttpGetUtils;
import com.leveling.utils.HttpPostUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class RegisterActivity extends AppCompatActivity implements OnClickListener {
    Calendar calendar = Calendar.getInstance();
    @InjectView(R.id.et_phone)
    EditText et_phone;
    @InjectView(R.id.et_pass)
    EditText et_pass; // 密码
    @InjectView(R.id.et_code)
    EditText et_code;
    @InjectView(R.id.et_payw)
    EditText et_payw;
    @InjectView(R.id.btn_submit)
    Button btn_submit; // 提交
    @InjectView(R.id.news_code_get)
    Button btnSend; // 获取验证码
    @InjectView(R.id.reg_reggg_back)
    LinearLayout reg_title_back;
    private JSONObject json = new JSONObject();
    private UserInfo userInfo;
    public static final String USER_NAME = "user_name";
    public static final String PASSWORD = "password";
    private static final String ISSAVEPASS = "savePassWord";
    private static final String AUTOLOGIN = "autoLogin";
    private static final String FIRSTAUTOLOGIN = "firstautoLogin";
    private static final String LOGIN = "login";
    private String name, pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reg_second_layout);
        ButterKnife.inject(this);
        userInfo = new UserInfo(this);
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initViews() {
        name = et_phone.getText().toString();
        pwd = et_pass.getText().toString();
        et_payw.setOnClickListener(this);
        et_phone.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        btnSend.setOnClickListener(this);
        reg_title_back.setOnClickListener(this);
        findViewById(R.id.btn_regist_contract).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(RegisterActivity.this, ServiceContractActivity.class);
                startActivity(it);
            }
        });
    }

    private void getDate(JSONObject json) {
        try {
            json.put("Phone", et_phone.getText().toString());
            json.put("PassWord", et_pass.getText().toString());
            json.put("Code", et_code.getText().toString());
            json.put("PayPW", et_payw.getText().toString());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void getDate1(JSONObject json) {
        try {
            json.put("Phone", et_phone.getText().toString());
            json.put("PassWord", et_pass.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        int id = v.getId();
        switch (id) {
            case R.id.reg_reggg_back:
                //Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                //startActivity(intent);
                finish();
                break;
            case R.id.et_payw:
                et_payw.setFocusable(true);
                et_payw.setFocusableInTouchMode(true);
                et_payw.requestFocus();
                et_payw.requestFocusFromTouch();
                break;
            case R.id.et_phone:
                et_phone.setFocusable(true);
                et_phone.setFocusableInTouchMode(true);
                et_phone.requestFocus();
                et_phone.requestFocusFromTouch();
                break;
            case R.id.news_code_get:
                String Phone_number = et_phone.getText().toString();
                if (isMobile(Phone_number) == false) {
                    Toast.makeText(RegisterActivity.this, "请输入正确的手机号", Toast.LENGTH_LONG).show();
                } else {
                    String url = "/api/SMS/GetPhoneSendInfor?phone=" + et_phone.getText().toString().trim() + "&codetype=" + 1;
                    HttpGetUtils.httpGetFile(12, url, handler);
                    btnSend.setClickable(false);
                }
                break;
            case R.id.btn_submit:
                if (!haveSent) {
                    Toast.makeText(RegisterActivity.this, "请先发送验证码", Toast.LENGTH_LONG).show();
                } else if (et_code.getText().toString().trim().equals("")) {
                    Toast.makeText(RegisterActivity.this, "请输入验证码", Toast.LENGTH_LONG).show();
                } else if (et_pass.getText().toString().length() < 6 || et_pass.getText().toString().length() > 20) {
                    Toast.makeText(RegisterActivity.this, "请设置6-20位登录密码", Toast.LENGTH_LONG).show();
                } else if (et_payw.getText().toString().length() != 6) {
                    Toast.makeText(RegisterActivity.this, "请设置六位支付密码", Toast.LENGTH_LONG).show();
                } else {
                    new AlertDialog.Builder(RegisterActivity.this).setTitle("提示")//设置对话框标题
                            .setMessage("确认提交注册信息?")//设置显示的内容
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                                @Override
                                public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                    // TODO Auto-generated method stub
                                    getDate(json);
                                    String url = "/api/Users/PostUsers";
                                    HttpPostUtils.httpPostFile(7, url, json, handler);
                                    System.out.println(json);
                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮
                        @Override
                        public void onClick(DialogInterface dialog, int which) {//响应事件
                            // TODO Auto-generated method stub
                        }
                    }).show();//在按键响应事件中显示此对话框
                }
                break;
            default:
                break;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 12:
                    btnSend.setClickable(true);
                    try {
                        String res = (String) msg.obj;
                        if (res == null) {
                            return;
                        }
                        JSONObject result = new JSONObject(res);
                        String success = result.getString("Success");
                        String err = result.getString("ErrMsg");
                        String date = result.getString("Data");
                        if (success == "true") {
                            Toast.makeText(RegisterActivity.this, "验证码发送成功", Toast.LENGTH_LONG).show();
                            haveSent = true;
                            beginTimer();
                        } else {
                            Toast.makeText(RegisterActivity.this, err, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                case 7:
                    try {
                        String res = (String) msg.obj;
                        JSONObject result = new JSONObject(res);
                        String success = result.getString("Success");
                        String err = result.getString("ErrMsg");
                        String date = result.getString("Data");
                        if (success == "true") {
                            Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_LONG).show();
                            getDate1(json);
                            String url = "/api/Users/PostLoginUsers";
                            name = et_phone.getText().toString();
                            pwd = et_pass.getText().toString();
                            userInfo.setUserInfo(USER_NAME, name);
                            userInfo.setUserInfo(PASSWORD, pwd);
                            HttpPostUtils.httpPostFile(6, url, json, handler);
                        } else {
                            Toast.makeText(RegisterActivity.this, err, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                case 6:
                    try {
                        String res = (String) msg.obj;
                        JSONObject result = new JSONObject(res);
                        String date = result.getString("Data");
                        JSONObject inresult = new JSONObject(date);
                        String UserId = inresult.getString("UserID");
                        String UserPhone = inresult.getString("Phone");
                        int role = inresult.getInt("Role");
                        String nickname = inresult.getString("NickName");
                        String picture = inresult.getString("Picture");
                        String Ticket = inresult.getString("ticket");
                        HttpPostUtils.setUserId(UserId);
                        HttpPostUtils.setPhone(UserPhone);
                        HttpPostUtils.setRole(role);
                        HttpPostUtils.setNickname(nickname);
                        HttpPostUtils.setPicture(picture);
                        HttpPostUtils.setTicket(Ticket);
                        HttpPostUtils.setNickname(nickname);
                        userInfo.setUserInfo(ISSAVEPASS, true);
                        userInfo.setUserInfo(FIRSTAUTOLOGIN, true);
                        userInfo.setUserInfo(AUTOLOGIN, true);
                        userInfo.setUserInfo(LOGIN, true);
                        Intent i = new Intent(RegisterActivity.this, SetingActivity.class);
                        startActivity(i);
                        finish();
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

    private boolean haveSent = false;
    private int timerTicks = 0;
    private Handler handlerTimer = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            timerTicks--;
            if (timerTicks <= 0) {
                btnSend.setClickable(true);
                btnSend.setText("重新获取");
            } else {
                btnSend.setText("剩余" + timerTicks + "秒");
                handlerTimer.sendEmptyMessageDelayed(0, 1000);
            }
        }
    };

    private void beginTimer() {
        timerTicks = 60;
        btnSend.setClickable(false);
        handlerTimer.sendEmptyMessageDelayed(0, 1000);
    }

    public static boolean isMobile(String Phone_number) {
        String num = "[1][345678]\\d{9}";
        if (TextUtils.isEmpty(Phone_number)) {
            return false;
        } else {
            //matches():字符串是否在给定的正则表达式匹配
            return Phone_number.matches(num);
        }
    }

}