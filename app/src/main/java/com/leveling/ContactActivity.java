package com.youyudj.leveling;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.youyudj.leveling.utils.HttpFileHelper;
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
public class ContactActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView Call_tel, tel, weixin1, weixin2, weixin3, weixin4, dizhi;
    private String name0, name1, name2, name3;
    private ImageView image11, image22, image33, image44;
    private LinearLayout img_tel_back;
    private String tel1, tel2, ar;
    Context context;
    private String mmssgg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_service);
        Call_tel = (TextView) findViewById(R.id.Call_tel);
        tel = (TextView) findViewById(R.id.tel11);
        dizhi = (TextView) findViewById(R.id.dizhi);
        weixin1 = (TextView) findViewById(R.id.wexin1);
        weixin2 = (TextView) findViewById(R.id.wexin2);
        weixin3 = (TextView) findViewById(R.id.wexin3);
        weixin4 = (TextView) findViewById(R.id.wexin4);
        weixin1.setOnClickListener(this);
        weixin2.setOnClickListener(this);
        weixin3.setOnClickListener(this);
        weixin4.setOnClickListener(this);
        image11 = (ImageView) findViewById(R.id.image11);
        image22 = (ImageView) findViewById(R.id.image22);
        image33 = (ImageView) findViewById(R.id.image33);
        image44 = (ImageView) findViewById(R.id.image44);
        img_tel_back = (LinearLayout) findViewById(R.id.img_tel_back);
        img_tel_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String url = "/api/SystemInfor/GetServiceInfor";
        HttpGetUtils.httpGetFile(30, url, handler);
        Call_tel.setOnClickListener(this);
        tel.setOnClickListener(this);
        findViewById(R.id.img_tel_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            if (msg.what < 0) {
                return;
            }
            switch (msg.what) {
                case 30:
                    try {
                        String res = (String) msg.obj;
                        if (res == null) {
                            return;
                        }
                        JSONObject result = new JSONObject(res);
                        String success = result.getString("Success");
                        String err = result.getString("ErrMsg");
                        if (success == "true") {
                            JSONArray data = result.getJSONArray("Data");
                            if (data.getJSONObject(0) != null) {
                                JSONObject oj0 = data.getJSONObject(0);
                                name0 = (String) oj0.get("systemValue");
                                weixin1.setText((String) oj0.get("remark"));
                                String url1 = "/api/File/GetSysteminfo?filename=" + name0;
                                HttpFileHelper.httpGetFile(10000, url1, handler);
                            }
                            if (data.getJSONObject(1) != null) {
                                JSONObject oj1 = data.getJSONObject(1);
                                name1 = (String) oj1.get("systemValue");
                                weixin2.setText((String) oj1.get("remark"));
                                String url1 = "/api/File/GetSysteminfo?filename=" + name1;
                                HttpFileHelper.httpGetFile(10001, url1, handler);
                            }
                            if (data.getJSONObject(2) != null) {
                                JSONObject oj2 = data.getJSONObject(2);
                                name2 = (String) oj2.get("systemValue");
                                weixin3.setText((String) oj2.get("remark"));
                                String url1 = "/api/File/GetSysteminfo?filename=" + name2;
                                HttpFileHelper.httpGetFile(10002, url1, handler);
                            }
                            if (data.getJSONObject(3) != null) {
                                JSONObject oj3 = data.getJSONObject(3);
                                name3 = (String) oj3.get("systemValue");
                                weixin4.setText((String) oj3.get("remark"));
                                String url1 = "/api/File/GetSysteminfo?filename=" + name3;
                                HttpFileHelper.httpGetFile(10003, url1, handler);
                            }
                            String uu = "/api/SystemInfor/GetContactInfor";
                            HttpGetUtils.httpGetFile(11, uu, handler);
                        } else if (success == "false") {
                            Toast.makeText(ContactActivity.this, err, Toast.LENGTH_LONG)
                                    .show();
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                case 10000:
                    byte[] data = (byte[]) msg.obj;
                    if (data == null) {
                        return;
                    }
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    image11.setImageBitmap(bitmap);
                    break;
                case 10001:
                    byte[] data1 = (byte[]) msg.obj;
                    if (data1 == null) {
                        return;
                    }
                    Bitmap bitmap1 = BitmapFactory.decodeByteArray(data1, 0, data1.length);
                    image22.setImageBitmap(bitmap1);
                    break;
                case 10002:
                    byte[] data2 = (byte[]) msg.obj;
                    if (data2 == null) {
                        return;
                    }
                    Bitmap bitmap2 = BitmapFactory.decodeByteArray(data2, 0, data2.length);
                    image33.setImageBitmap(bitmap2);
                    break;
                case 10003:
                    byte[] data3 = (byte[]) msg.obj;
                    if (data3 == null) {
                        return;
                    }
                    Bitmap bitmap3 = BitmapFactory.decodeByteArray(data3, 0, data3.length);
                    image44.setImageBitmap(bitmap3);
                    break;
                case 11:
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
                                Call_tel.setText((String) js.get("systemKey"));
                                tel.setText((String) js.get("systemValue"));
                                dizhi.setText((String) js.get("remark"));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.Call_tel:
                Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Call_tel.getText().toString().trim()));
                startActivity(dialIntent);
                break;
            case R.id.tel11:
                Intent dialIntent1 = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tel.getText().toString().trim()));
                startActivity(dialIntent1);
                break;
            case R.id.wexin1:
                ClipboardManager cm1 = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm1.setText(weixin1.getText().toString().trim());
                Toast.makeText(ContactActivity.this, "微信号已复制", Toast.LENGTH_SHORT).show();
                break;
            case R.id.wexin2:
                ClipboardManager cm2 = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm2.setText(weixin2.getText().toString().trim());
                Toast.makeText(ContactActivity.this, "微信号已复制", Toast.LENGTH_SHORT).show();
                break;
            case R.id.wexin3:
                ClipboardManager cm3 = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm3.setText(weixin3.getText().toString().trim());
                Toast.makeText(ContactActivity.this, "微信号已复制", Toast.LENGTH_SHORT).show();
                break;
            case R.id.wexin4:
                ClipboardManager cm4 = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm4.setText(weixin4.getText().toString().trim());
                Toast.makeText(ContactActivity.this, "微信号已复制", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }


}
