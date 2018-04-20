package com.youyudj.leveling;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.youyudj.leveling.utils.HttpGetUtils;

import org.json.JSONArray;
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
public class SimpleContactActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvCompTel, tvFrontTel, tvCompAddr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_contact_service);
        tvCompTel = (TextView)findViewById(R.id.tv_comp_tel);
        tvFrontTel = (TextView)findViewById(R.id.tv_front_tel);
        tvCompAddr = (TextView)findViewById(R.id.tv_comp_address);
        tvCompTel.setOnClickListener(this);
        tvFrontTel.setOnClickListener(this);
        tvCompAddr.setOnClickListener(this);
        findViewById(R.id.image11).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cm1 = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                cm1.setText("djzj666888");
                Toast.makeText(SimpleContactActivity.this, "微信号已复制", Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.img_tel_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String uu = "/api/SystemInfor/GetContactInfor";
        HttpGetUtils.httpGetFile(11, uu, handler);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String res = (String) msg.obj;
            if (res == null) {
                return;
            }
            JSONObject result = null;
            try {
                result = new JSONObject(res);
                String success = result.getString("Success");
                String err = result.getString("ErrMsg");
                if (success == "true") {
                    String da = result.getString("Data");
                    JSONArray date = result.getJSONArray("Data");
                    if (date.getJSONObject(0) != null) {
                        JSONObject js = date.getJSONObject(0);
                        tvCompTel.setText((String) js.get("systemKey"));
                        tvFrontTel.setText((String) js.get("systemValue"));
                        tvCompAddr.setText((String) js.get("remark"));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_comp_tel:
            case R.id.tv_front_tel:{
                Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + ((TextView)v).getText().toString().trim()));
                startActivity(dialIntent);
                break;
            }
        }
    }
}
