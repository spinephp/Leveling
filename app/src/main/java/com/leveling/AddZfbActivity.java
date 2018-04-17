package com.leveling;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.leveling.utils.HttpGetUtils;
import com.leveling.utils.HttpPostUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * *******************************************************
 * Description:
 * Autour: 3W攻城狮
 * Date: 2017/11/7 21:47
 * Update:2017/11/7
 * Version:
 * *******************************************************
 */
public class AddZfbActivity extends AppCompatActivity {

    private EditText add_zhifb, add_zfn_name;
    private Button addzfb;
    private LinearLayout img_title_back;
    private TextView zfbnc;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_zfb_layout);
        add_zhifb = (EditText) findViewById(R.id.add_zhifb);
        zfbnc = (TextView) findViewById(R.id.zfbnc);
        zfbnc.setText(HttpPostUtils.getNickname());
        add_zfn_name = (EditText) findViewById(R.id.add_zfn_name);
        addzfb = (Button) findViewById(R.id.addzfb);
        img_title_back = (LinearLayout) findViewById(R.id.img_title_back);
        img_title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HttpPostUtils.getRole() == 0) {
                    Intent intent = new Intent(AddZfbActivity.this, TixianActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    if (MyWalletActivity.message == "1") {
                        Intent intent = new Intent(AddZfbActivity.this, TixianPackageActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (MyWalletActivity.message == "2") {
                        Intent intent = new Intent(AddZfbActivity.this, TixianDailianActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (MyWalletActivity.message == "3") {
                        Intent intent = new Intent(AddZfbActivity.this, TixianPeilianActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
        addzfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (add_zhifb.getText().toString().equals("")) {
                    Toast.makeText(AddZfbActivity.this, "请输入支付宝账户", Toast.LENGTH_LONG).show();
                } else if (add_zfn_name.getText().toString().equals("")) {
                    Toast.makeText(AddZfbActivity.this, "请输入用户名", Toast.LENGTH_LONG).show();
                } else {
                    new AlertDialog.Builder(AddZfbActivity.this).setTitle("提示")//设置对话框标题
                            .setMessage("确认进行该操作？")//设置显示的内容
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                                @Override
                                public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                    // TODO Auto-generated method stub
                                    String url = "/api/Users/GetUpdatePayInfor?type=" + 3 + "&Name=" + add_zfn_name.getText().toString() + "&Number=" + add_zhifb.getText().toString() + "&CardType=" + "";
                                    HttpGetUtils.httpGetFile(1, url, handler);
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
        if (HttpPostUtils.getRole() == 0) {
            Intent intent = new Intent(AddZfbActivity.this, TixianActivity.class);
            startActivity(intent);
            finish();
        } else {
            if (MyWalletActivity.message == "1") {
                Intent intent = new Intent(AddZfbActivity.this, TixianPackageActivity.class);
                startActivity(intent);
                finish();
            } else if (MyWalletActivity.message == "2") {
                Intent intent = new Intent(AddZfbActivity.this, TixianDailianActivity.class);
                startActivity(intent);
                finish();
            } else if (MyWalletActivity.message == "3") {
                Intent intent = new Intent(AddZfbActivity.this, TixianPeilianActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    try {
                        String res = (String) msg.obj;
                        if (res == null) {
                            return;
                        }
                        JSONObject obj = new JSONObject(res);
                        String success = obj.getString("Success");
                        String err = obj.getString("ErrMsg");
                        if (success == "true") {
                            Toast.makeText(AddZfbActivity.this, "操作成功", Toast.LENGTH_LONG).show();
                            if (HttpPostUtils.getRole() == 0) {
                                Intent intent = new Intent(AddZfbActivity.this, TixianActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                if (MyWalletActivity.message == "1") {
                                    Intent intent = new Intent(AddZfbActivity.this, TixianPackageActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else if (MyWalletActivity.message == "2") {
                                    Intent intent = new Intent(AddZfbActivity.this, TixianDailianActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else if (MyWalletActivity.message == "3") {
                                    Intent intent = new Intent(AddZfbActivity.this, TixianPeilianActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }

                        } else if (success == "false") {
                            Toast.makeText(AddZfbActivity.this, err, Toast.LENGTH_LONG).show();
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
