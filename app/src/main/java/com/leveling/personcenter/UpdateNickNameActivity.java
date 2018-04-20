package com.youyudj.leveling.personcenter;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.youyudj.leveling.R;
import com.youyudj.leveling.entity.UserInfo;
import com.youyudj.leveling.utils.HttpPostUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdateNickNameActivity extends AppCompatActivity implements OnClickListener {

    public static Boolean nick = false;
    public static  String new_name;
    private EditText update_nickname;
    private Button update_nickname_button;
    private JSONObject json = new JSONObject();
    private static final String NICKNAME = "nickname";
    private static final String CNICKNAME = "cnickname";
    private UserInfo userInfo;
    private LinearLayout img_name_back;
    public static String str;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_nick_name);
        userInfo = new UserInfo(this);
        update_nickname = (EditText) findViewById(R.id.update_nickname);
        update_nickname_button = (Button) findViewById(R.id.update_nickname_button);
        img_name_back = (LinearLayout) findViewById(R.id.img_name_back);
        img_name_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        update_nickname_button.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.update_nickname_button:
                if ( update_nickname.getText().toString().equals("")){
                    Toast.makeText(UpdateNickNameActivity.this,"请输入昵称",Toast.LENGTH_LONG).show();
                }else if ( update_nickname.getText().toString().length()>6){
                    Toast.makeText(UpdateNickNameActivity.this,"昵称长度不能超过6个字",Toast.LENGTH_LONG).show();
                }else{
                    update_nickname_button.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            str = "1";
                            getDate(json);
                            String url = "/api/Users/PostUpdateNickName";
                            HttpPostUtils.httpPostFile(9,url, json, handler);
                        }
                    });
                }
        }
    }
    private Handler  handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 9:
                    try {
                        String res = (String) msg.obj;
                        JSONObject obj = new JSONObject(res);
                        String success = obj.getString("Success");
                        String err = obj.getString("ErrMsg");
                        if(success == "true"){
                            new_name = update_nickname.getText().toString().trim();
                            Toast.makeText(UpdateNickNameActivity.this,"修改成功",Toast.LENGTH_LONG).show();
                            finish();
                        }else if(success == "false"){
                            Toast.makeText(UpdateNickNameActivity.this,err,Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
            }
        }

    };
    protected void getDate(JSONObject json) {
        // TODO Auto-generated method stub
        try {
            json.put("nickname", update_nickname.getText().toString().trim());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
