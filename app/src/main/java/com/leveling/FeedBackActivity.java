package com.youyudj.leveling;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.youyudj.leveling.entity.OrderType;
import com.youyudj.leveling.utils.HttpPostUtils;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * *******************************************************
 * Description:
 * Autour: 3W攻城狮
 * Date: 2017/11/7 21:47
 * Update:2017/11/7
 * Version:
 * *******************************************************
 */
public class FeedBackActivity extends AppCompatActivity implements OnClickListener {
    private JSONObject json = new JSONObject();

    private EditText remark_title;
    private LinearLayout img_feed_back;
    private EditText remark;
    private Button remark_submit;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_back_layout);
        img_feed_back = (LinearLayout) findViewById(R.id.img_fankui_back);
        img_feed_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        remark_title = (EditText) findViewById(R.id.remark_title);
        remark = (EditText) findViewById(R.id.remark);
        remark_submit = (Button) findViewById(R.id.remark_submit);
        remark_submit.setOnClickListener(this);
    }
    private Handler  handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 4:
                    try {
                        String res = (String) msg.obj;
                        JSONObject result = new JSONObject(res);
                        String success = result.getString("Success");
                        if(success == "true"){
                            finish();
                            Toast.makeText(FeedBackActivity.this,"意见反馈成功",Toast.LENGTH_LONG).show();
//                            Intent intent = new Intent(FeedBackActivity.this,AddRemarkSuccessActivity.class);
//                            startActivity(intent);
                        }else if(success == "false"){
                            Toast.makeText(FeedBackActivity.this,"出现错误",Toast.LENGTH_LONG).show();
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

    private void getDate(JSONObject json){
        try {
            json.put("Title", remark_title.getText().toString());
            json.put("RemarkContent", remark.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.remark_submit:
                if ( remark_title.getText().toString().equals("")){
                    Toast.makeText(FeedBackActivity.this,"请输入标题",Toast.LENGTH_LONG).show();
                } else if ( remark_title.getText().toString().length()>20){
                    Toast.makeText(FeedBackActivity.this,"标题不超过20个字",Toast.LENGTH_LONG).show();
                } else if ( remark.getText().toString().equals("")){
                    Toast.makeText(FeedBackActivity.this,"反馈内容不能为空",Toast.LENGTH_LONG).show();
                } else{
                    new AlertDialog.Builder(FeedBackActivity.this).setTitle("提示")//设置对话框标题
                            .setMessage("确认提交反馈?")//设置显示的内容
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                                @Override
                                public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                    // TODO Auto-generated method stub
                                    String url ="/api/Remark/PostRemark";
                                    getDate(json);
                                    HttpPostUtils.httpPostFile(4,url, json, handler);
                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮
                        @Override
                        public void onClick(DialogInterface dialog, int which) {//响应事件
                            // TODO Auto-generated method stub
                        }
                    }).show();//在按键响应事件中显示此对话框

                }
        }
    }
}
