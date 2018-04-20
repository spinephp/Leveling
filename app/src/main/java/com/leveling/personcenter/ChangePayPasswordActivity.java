package com.youyudj.leveling.personcenter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.youyudj.leveling.R;
import com.youyudj.leveling.utils.HttpGetUtils;
import com.youyudj.leveling.utils.HttpPostUtils;

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
public class ChangePayPasswordActivity extends AppCompatActivity implements OnClickListener {
    private EditText old_paypw;
    private EditText new_paypw,conform_code;
    private EditText conforn_new_paypw;
    private TextView your_phone;
    private Button update_paypw;
    private JSONObject json = new JSONObject();
    private Button get_conformCode_button;
    private String ph;
    private boolean pk;
    private LinearLayout img_pay_back;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_pay_pwd_layout);

        conform_code = (EditText) findViewById(R.id.conform_code);
        //old_paypw = (EditText) findViewById(R.id.old_paypw);
        new_paypw = (EditText) findViewById(R.id.new_paypw);
        your_phone = (TextView) findViewById(R.id.your_phone_number);
        conforn_new_paypw = (EditText) findViewById(R.id.conform_new_paypw);
        img_pay_back = (LinearLayout) findViewById(R.id.img_pay_back);
        get_conformCode_button = (Button)findViewById(R.id.get_conformCode_button);
        update_paypw = (Button) findViewById(R.id.update_paypw);
        img_pay_back.setOnClickListener(this);
        get_conformCode_button.setOnClickListener(this);
        update_paypw.setOnClickListener(this);
        ph = HttpPostUtils.getPhone();
        if(ph.length()==11 ){
            StringBuilder sb  =new StringBuilder();
            for (int i = 0; i < ph.length(); i++) {
                char c = ph.charAt(i);
                if (i >= 3 && i <= 6) {
                    sb.append('*');
                } else {
                    sb.append(c);
                }
            }
            your_phone.setText(sb.toString());
        }
    }
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.img_pay_back:
                new AlertDialog.Builder(ChangePayPasswordActivity.this).setTitle("提示")//设置对话框标题
                        .setMessage("确认退出修改？")//设置显示的内容
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
                break;
            case R.id.get_conformCode_button:
                String url = "/api/SMS/GetUserSendInfor?codetype="+2;
                HttpGetUtils.httpGetFile(22,url, handler);
                new MyCount().execute();
                break;
            case R.id.update_paypw:
                if(conform_code.getText().toString().equals("")){
                    Toast.makeText(ChangePayPasswordActivity.this,"请输入验证码",Toast.LENGTH_LONG).show();
                }
//                else if(old_paypw.getText().toString().trim().equals("")){
//                    Toast.makeText(ChangePayPasswordActivity.this,"请输入旧密码",Toast.LENGTH_LONG).show();
//                }
                else if(conforn_new_paypw.getText().toString().trim().equals("")||new_paypw.getText().toString().trim().equals("")){
                    Toast.makeText(ChangePayPasswordActivity.this,"请输入新密码",Toast.LENGTH_LONG).show();
                }else if(new_paypw.getText().toString().equals(conforn_new_paypw.getText().toString())==false){
                    Toast.makeText(ChangePayPasswordActivity.this,"请确保两次密码输入一样",Toast.LENGTH_LONG).show();
                } else if(conforn_new_paypw.getText().toString().length()!=6){
                    Toast.makeText(ChangePayPasswordActivity.this,"请输入6位支付密码",Toast.LENGTH_LONG).show();
                } else{
                    new AlertDialog.Builder(ChangePayPasswordActivity.this).setTitle("提示")//设置对话框标题
                            .setMessage("确认修支付密码？")//设置显示的内容
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                                @Override
                                public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                    // TODO Auto-generated method stub
                                    getDate(json);
                                    String ur = "/api/Users/PostUpdatePayPW";
                                    HttpPostUtils.httpPostFile(3,ur, json, handler);
                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮
                        @Override
                        public void onClick(DialogInterface dialog, int which) {//响应事件
                            // TODO Auto-generated method stub
                        }
                    }).show();//在按键响应事件中显示此对话框
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(ChangePayPasswordActivity.this).setTitle("提示")//设置对话框标题
                .setMessage("确认退出修改？")//设置显示的内容
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

    private Handler  handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch(msg.what){
                case 22:
                    try {
                        String res = (String) msg.obj;
                        if (res==null){
                            return;
                        }
                        JSONObject obj = new JSONObject(res);
                        String success = obj.getString("Success");
                        String err = obj.getString("ErrMsg");
                        if(success == "true"){
                            Toast.makeText(ChangePayPasswordActivity.this,"验证码已发送",Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(ChangePayPasswordActivity.this,err,Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    try {
                        String res = (String) msg.obj;
                        if (res==null){
                            return;
                        }
                        JSONObject obj = new JSONObject(res);
                        String success = obj.getString("Success");
                        String err = obj.getString("ErrMsg");
                            if(success == "true"){
                                //Toast.makeText(ChangePayPasswordActivity.this,success,Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(ChangePayPasswordActivity.this,ChangeSuccessActivity.class);
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(ChangePayPasswordActivity.this,err,Toast.LENGTH_LONG).show();
                            }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
            }
        }

    };
    class MyCount extends AsyncTask<Integer,Integer,Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if(values[0]!=0){
                get_conformCode_button.setClickable(false);
                get_conformCode_button.setText("剩余"+values[0]+"秒");
            }else{
                get_conformCode_button.setClickable(true);
                get_conformCode_button.setText("重新获取");
            }
        }

        //倒计时
        @Override
        protected Integer doInBackground(Integer... params) {
            for (int i=60;i>=0;i--){
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
    protected void getDate(JSONObject json) {
        // TODO Auto-generated method stub

        try {
            //json.put("OldPayPW", old_paypw.getText().toString().trim());
            json.put("NewPayPW", conforn_new_paypw.getText().toString().trim());
            json.put("Code",conform_code.getText().toString().trim());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

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
