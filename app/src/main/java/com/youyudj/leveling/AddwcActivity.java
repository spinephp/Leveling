package com.youyudj.leveling;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.youyudj.leveling.utils.HttpGetUtils;
import com.youyudj.leveling.utils.HttpPostUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class AddwcActivity extends AppCompatActivity {

    private Button addwxh;
    private EditText add_wx,add_wx_name;
    private LinearLayout img_title_back;
    private TextView wxnc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addwc);
        add_wx = (EditText) findViewById(R.id.add_wx);
        wxnc = (TextView) findViewById(R.id.wxnc);
        wxnc.setText(HttpPostUtils.getNickname());
        add_wx_name = (EditText) findViewById(R.id.add_wx_name);
        addwxh = (Button) findViewById(R.id.addwxh);
        img_title_back = (LinearLayout) findViewById(R.id.img_title_back);
        img_title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HttpPostUtils.getRole()==0){
                    Intent intent = new Intent(AddwcActivity.this,TixianActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    if (MyWalletActivity.message=="1"){
                        Intent intent = new Intent(AddwcActivity.this,TixianPackageActivity.class);
                        startActivity(intent);
                        finish();
                    }else if (MyWalletActivity.message=="2"){
                        Intent intent = new Intent(AddwcActivity.this,TixianDailianActivity.class);
                        startActivity(intent);
                        finish();
                    }else if (MyWalletActivity.message=="3"){
                        Intent intent = new Intent(AddwcActivity.this,TixianPeilianActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }

            }
        });
        addwxh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (add_wx.getText().toString().equals("")){
                    Toast.makeText(AddwcActivity.this,"请输入微信账户",Toast.LENGTH_LONG).show();
                }else if (add_wx_name.getText().toString().equals("")){
                    Toast.makeText(AddwcActivity.this,"请输入微信昵称",Toast.LENGTH_LONG).show();
                }else{
                    new AlertDialog.Builder(AddwcActivity.this).setTitle("提示")//设置对话框标题
                            .setMessage("确认进行该操作？")//设置显示的内容
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                                @Override
                                public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                    // TODO Auto-generated method stub
                                    String url = "/api/Users/GetUpdatePayInfor?type="+2+"&Name="+add_wx_name.getText().toString()+"&Number="+add_wx.getText().toString()+"&CardType="+"";
                                    HttpGetUtils.httpGetFile(1,url,handler);
                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮
                        @Override
                        public void onClick(DialogInterface dialog, int which) {//响应事件
                            // TODO Auto-generated method stub
                        }
                    }).show();//在按键响应事件中显示此对话框

                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (HttpPostUtils.getRole()==0){
            Intent intent = new Intent(AddwcActivity.this,TixianActivity.class);
            startActivity(intent);
            finish();
        }else {
            if (MyWalletActivity.message=="1"){
                Intent intent = new Intent(AddwcActivity.this,TixianPackageActivity.class);
                startActivity(intent);
                finish();
            }else if (MyWalletActivity.message=="2"){
                Intent intent = new Intent(AddwcActivity.this,TixianDailianActivity.class);
                startActivity(intent);
                finish();
            }else if (MyWalletActivity.message=="3"){
                Intent intent = new Intent(AddwcActivity.this,TixianPeilianActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    try {
                        String res = (String) msg.obj;
                        if (res==null){
                            return;
                        }
                        JSONObject obj = new JSONObject(res);
                        String success = obj.getString("Success");
                        String err = obj.getString("ErrMsg");
                        if(success == "true"){
                            Toast.makeText(AddwcActivity.this,"操作成功",Toast.LENGTH_LONG).show();
                            if (HttpPostUtils.getRole()==0){
                                Intent intent = new Intent(AddwcActivity.this,TixianActivity.class);
                                startActivity(intent);
                                finish();
                            }else{
                                if (MyWalletActivity.message=="1"){
                                    Intent intent = new Intent(AddwcActivity.this,TixianPackageActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else if (MyWalletActivity.message=="2"){
                                    Intent intent = new Intent(AddwcActivity.this,TixianDailianActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else if (MyWalletActivity.message=="3"){
                                    Intent intent = new Intent(AddwcActivity.this,TixianPeilianActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }else if(success == "false"){
                            Toast.makeText(AddwcActivity.this,err,Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
}
