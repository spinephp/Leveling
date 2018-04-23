package com.youyudj.leveling.personcenter;

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
import android.widget.TextView;
import android.widget.Toast;

import com.youyudj.leveling.LoginActivity;
import com.youyudj.leveling.R;
import com.youyudj.leveling.entity.UserInfo;
import com.youyudj.leveling.utils.HttpGetUtils;
import com.youyudj.leveling.utils.HttpPostUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdatePhoneActivity extends AppCompatActivity implements OnClickListener {

    private JSONObject json = new JSONObject();
    private EditText old_phone_code;
    private Button get_old_phone_code;
    private EditText new_phone_number;
    private Button new_phone;
    private Boolean pk;
    private LinearLayout img_phone_back;
    private TextView up_your_phone;
    private UserInfo userInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_phone);
        UserInfo userInfo = new UserInfo();
        old_phone_code = (EditText) findViewById(R.id.tv_code);
        get_old_phone_code = (Button) findViewById(R.id.btn_send);
        up_your_phone = (TextView) findViewById(R.id.your_phone_number);
        new_phone_number = (EditText) findViewById(R.id.new_phone_number);
        img_phone_back = (LinearLayout) findViewById(R.id.btn_back);
        new_phone = (Button) findViewById(R.id.btn_confirm);
        String ph = HttpPostUtils.getPhone();
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
            up_your_phone.setText(sb.toString());
        }
        get_old_phone_code.setOnClickListener(this);
        new_phone.setOnClickListener(this);
        img_phone_back.setOnClickListener(this);
    }
    private Handler  handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);

            switch(msg.what){
                case 9:
                    try {
                        String res = (String) msg.obj;
                        if (res==null){
                            return;
                        }
                        JSONObject obj = new JSONObject(res);
                        String success = obj.getString("Success");
                        String err = obj.getString("ErrMsg");
                        if(success == "true"){
                            Toast.makeText(UpdatePhoneActivity.this,"验证码已发送",Toast.LENGTH_LONG).show();
                        }else if(success == "false"){
                            Toast.makeText(UpdatePhoneActivity.this,success,Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                case 10:
                    try {
                        String res = (String) msg.obj;
                        if (res==null){
                            return;
                        }
                        JSONObject obj = new JSONObject(res);
                        String success = obj.getString("Success");
                        String err = obj.getString("ErrMsg");
                        if(success == "true"){
                            Toast.makeText(UpdatePhoneActivity.this,"手机号修改成功,请重新登录",Toast.LENGTH_SHORT).show();
                            HttpPostUtils.setTicket(null);
                            Intent intent = new Intent(UpdatePhoneActivity.this,LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }else if(success == "false"){
                            Toast.makeText(UpdatePhoneActivity.this,err,Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                default:break;
            }
        }

    };
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btn_send:
                String url9 = "/api/SMS/GetUserSendInfor?userid="+HttpPostUtils.getUserId()+"&codetype="+1;
                HttpGetUtils.httpGetFile(9,url9, handler);
                new MyCount1().execute();
                break;
            case R.id.btn_confirm:
                String Phone_number = new_phone_number.getText().toString().trim();
                if (old_phone_code.getText().toString().equals("")){
                    Toast.makeText(UpdatePhoneActivity.this,"请输入验证码",Toast.LENGTH_SHORT).show();
                }else if (isMobile(Phone_number)==false){
                    Toast.makeText(UpdatePhoneActivity.this,"请输入正确的手机号",Toast.LENGTH_LONG).show();
                }else{
                    new AlertDialog.Builder(UpdatePhoneActivity.this).setTitle("提示")//设置对话框标题
                            .setMessage("确认修改手机号？")//设置显示的内容
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                                @Override
                                public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                    // TODO Auto-generated method stub
                                    getDate(json);
                                    String url = "/api/Users/PostUpdatePhone";
                                    HttpPostUtils.httpPostFile(10,url, json, handler);
                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮
                        @Override
                        public void onClick(DialogInterface dialog, int which) {//响应事件
                            // TODO Auto-generated method stub
                        }
                    }).show();//在按键响应事件中显示此对话框
                }
                break;
            case R.id.btn_back:
                new AlertDialog.Builder(UpdatePhoneActivity.this).setTitle("提示")//设置对话框标题
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
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(UpdatePhoneActivity.this).setTitle("提示")//设置对话框标题
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

    class MyCount1 extends AsyncTask<Integer,Integer,Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if(values[0]!=0){
                get_old_phone_code.setClickable(false);
                get_old_phone_code.setText("剩余"+values[0]+"秒");
            }else{
                get_old_phone_code.setClickable(true);
                get_old_phone_code.setText("重新获取");
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
            json.put("NewPhone", new_phone_number.getText().toString());
            json.put("Code", old_phone_code.getText().toString());
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
