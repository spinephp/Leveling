package com.leveling;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
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

import com.leveling.utils.HttpGetUtils;
import com.leveling.utils.HttpPostUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class FindBackPasswordActivity extends AppCompatActivity {

    private JSONObject json = new JSONObject();
    private EditText find_pwd_code;
    private EditText find_new_password;
    private Button btnSend;
    private Button find_pwd_submit;
    private EditText find_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_back_password);
        LinearLayout img_fd_back = (LinearLayout) findViewById(R.id.img_fd_back);
        img_fd_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        find_phone = (EditText) findViewById(R.id.find_phone);
        find_pwd_code = (EditText) findViewById(R.id.find_pwd_code_new);
        find_new_password = (EditText) findViewById(R.id.find_new_password);
        btnSend = (Button) findViewById(R.id.find_pwd_button);
        find_pwd_submit = (Button) findViewById(R.id.find_pwd_submit);
        // 获取验证码
        btnSend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                String Phone_number = find_phone.getText().toString();
                if (isMobile(Phone_number) == false) {
                    Toast.makeText(FindBackPasswordActivity.this, "请输入正确的手机号", Toast.LENGTH_LONG).show();
                } else {
                    String url = "/api/SMS/GetPhoneSendInfor?phone=" + find_phone.getText().toString() + "&codetype=" + 2;
                    HttpGetUtils.httpGetFile(21, url, handler);
                    btnSend.setClickable(false);
                }
            }
        });
        // 提交按钮
        find_pwd_submit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                String Phone_number = find_phone.getText().toString();
                if (isMobile(Phone_number) == false) {
                    Toast.makeText(FindBackPasswordActivity.this, "请输入正确的手机号", Toast.LENGTH_LONG).show();
                } else if (!haveSent) {
                    Toast.makeText(FindBackPasswordActivity.this, "请先发送验证码", Toast.LENGTH_LONG).show();
                } else if (find_pwd_code.getText().toString().trim().equals("")) {
                    Toast.makeText(FindBackPasswordActivity.this, "请输入验证码", Toast.LENGTH_LONG).show();
                } else if (find_pwd_code.getText().toString().trim().length() != 6) {
                    Toast.makeText(FindBackPasswordActivity.this, "验证码为6位数字", Toast.LENGTH_LONG).show();
                } else if (find_new_password.getText().toString().trim().length() < 6 || find_new_password.getText().toString().trim().length() > 20) {
                    Toast.makeText(FindBackPasswordActivity.this, "请输入正确的密码", Toast.LENGTH_LONG).show();
                } else {
                    new AlertDialog.Builder(FindBackPasswordActivity.this).setTitle("提示")//设置对话框标题
                            .setMessage("确定找回密码？")//设置显示的内容
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                                @Override
                                public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                    // TODO Auto-generated method stub
                                    getDate(json);
                                    String url = "/api/Users/PostRetrievePW";
                                    HttpPostUtils.httpPostFile(5, url, json, handler);
                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮
                        @Override
                        public void onClick(DialogInterface dialog, int which) {//响应事件
                            // TODO Auto-generated method stub
                        }
                    }).show();//在按键响应事件中显示此对话框
                }

            }

            //  }
        });

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 21:
                    try {
                        btnSend.setClickable(true);
                        String res = (String) msg.obj;
                        if (res == null) {
                            return;
                        }
                        JSONObject result;
                        result = new JSONObject(res);
                        String success = result.getString("Success");
                        String err = result.getString("ErrMsg");
                        if (success == "true") {
                            Toast.makeText(FindBackPasswordActivity.this, "验证码发送成功", Toast.LENGTH_LONG).show();
                            haveSent = true;
                            beginTimer();
                        } else {
                            Toast.makeText(FindBackPasswordActivity.this, err, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                case 5:
                    try {
                        String res = (String) msg.obj;
                        JSONObject result;
                        result = new JSONObject(res);
                        String success = result.getString("Success");
                        String err = result.getString("ErrMsg");
                        if (success == "true") {
                            Toast.makeText(FindBackPasswordActivity.this, "密码修改成功", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(FindBackPasswordActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(FindBackPasswordActivity.this, err, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
            }
        }

    };

    private void getDate(JSONObject json) {
        try {
            json.put("phone", find_phone.getText().toString().trim());
            json.put("Code", find_pwd_code.getText().toString().trim());
            json.put("newpassword", find_new_password.getText().toString().trim());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

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
    /*
    移动：134、135、136、137、138、139、150、151、152、157(TD)、158、159、178(新)、182、184、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、170、173、177、180、181、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        String num = "[1][345678]\\d{9}";//"[1]"代表第1位为数字1，"[34578]"代表第二位可以为3、4、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(Phone_number)) {
            return false;
        } else {
            //matches():字符串是否在给定的正则表达式匹配
            return Phone_number.matches(num);
        }
    }


}
