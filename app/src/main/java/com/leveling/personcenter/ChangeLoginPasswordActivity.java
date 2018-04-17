package com.leveling.personcenter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.leveling.LoginActivity;
import com.leveling.R;
import com.leveling.entity.UserInfo;
import com.leveling.utils.HttpPostUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * *******************************************************
 * Description:
 * Autour: 3W攻城狮
 * Date: 2017/11/25 14:37
 * Update:2017/11/25
 * Version:
 * *******************************************************
 */
public class ChangeLoginPasswordActivity extends AppCompatActivity implements OnClickListener {
    private EditText old_password;
    private EditText new_password;
    private EditText conform_new_password;
    private Button update_password_button;
    private JSONObject json = new JSONObject();
    private LinearLayout img_pwd_back;
    private UserInfo userInfo;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seting_new_password_layout);
        UserInfo userInfo = new UserInfo();
        img_pwd_back = (LinearLayout) findViewById(R.id.img_pwd_back);
        img_pwd_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ChangeLoginPasswordActivity.this).setTitle("提示")//设置对话框标题
                        .setMessage("确认退出修改?")//设置显示的内容
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                            @Override
                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                // TODO Auto-generated method stub
                                finish();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//响应事件
                        // TODO Auto-generated method stub
                    }
                }).show();//在按键响应事件中显示此对话框
            }
        });
        old_password = (EditText) findViewById(R.id.old_password);
        new_password = (EditText) findViewById(R.id.new_password);
        conform_new_password = (EditText) findViewById(R.id.conform_new_password);
        update_password_button = (Button) findViewById(R.id.update_password_button);
        update_password_button.setOnClickListener(this);
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(ChangeLoginPasswordActivity.this).setTitle("提示")//设置对话框标题
                .setMessage("确认退出修改?")//设置显示的内容
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                        // TODO Auto-generated method stub
                        finish();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {//响应事件
                // TODO Auto-generated method stub
            }
        }).show();//在按键响应事件中显示此对话框
    }

    protected void getDate(JSONObject json) {
        // TODO Auto-generated method stub
        try {
            json.put("oldpassword", old_password.getText().toString().trim());
            json.put("newpassword", conform_new_password.getText().toString().trim());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    private Handler  handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch(msg.what){
                case 2:
                    try {
                        String res = (String) msg.obj;
                        if (res==null){
                            return;
                        }
                        JSONObject obj = new JSONObject(res);
                        String success = obj.getString("Success");
                        String err = obj.getString("ErrMsg");
                        if(success == "true"){
                            Toast.makeText(ChangeLoginPasswordActivity.this,"密码修改成功，请重新登录",Toast.LENGTH_SHORT).show();
                            HttpPostUtils.setTicket(null);
                            Intent intent = new Intent(ChangeLoginPasswordActivity.this,LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }else if(success == "false"){
                            Toast.makeText(ChangeLoginPasswordActivity.this,err,Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
            }
        }
    };
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.update_password_button:
                if (old_password.getText().toString().trim().equals("")){
                    Toast.makeText(ChangeLoginPasswordActivity.this,"请输入旧密码",Toast.LENGTH_SHORT).show();
                }else if (conform_new_password.getText().toString().trim().equals("")||new_password.getText().toString().equals("")){
                    Toast.makeText(ChangeLoginPasswordActivity.this,"请输入新密码",Toast.LENGTH_SHORT).show();
                }else if (conform_new_password.getText().toString().length()<6||conform_new_password.getText().toString().length()>20){
                    Toast.makeText(ChangeLoginPasswordActivity.this,"请输入6-20位密码",Toast.LENGTH_SHORT).show();
                }else if (conform_new_password.getText().toString().equals(new_password.getText().toString())==false){
                    Toast.makeText(ChangeLoginPasswordActivity.this,"两次输入的密码不一致，请重新输入！",Toast.LENGTH_SHORT).show();
                    conform_new_password.setText("");
                    new_password.setText("");
                }else {
                    new AlertDialog.Builder(ChangeLoginPasswordActivity.this).setTitle("提示")//设置对话框标题
                            .setMessage("确认修登录密码？")//设置显示的内容
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                                @Override
                                public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                    // TODO Auto-generated method stub
                                    getDate(json);
                                    String url = "/api/Users/PostUpdatePassword";
                                    HttpPostUtils.httpPostFile(2, url, json, handler);
                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮
                        @Override
                        public void onClick(DialogInterface dialog, int which) {//响应事件
                            // TODO Auto-generated method stub
                        }
                    }).show();//在按键响应事件中显示此对话框
                }
                break;

            default:break;
        }
    }
}
