package com.leveling.common;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.leveling.R;
import com.leveling.ui.EasyLoading;
import com.leveling.ui.StatusControl;
import com.leveling.utils.HttpGetUtils;
import com.leveling.utils.HttpPostUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class CheckVerifyCodeActivity extends AppCompatActivity implements OnClickListener {
    private JSONObject json = new JSONObject();
    private LinearLayout btnBack;
    private Button btnSend;
    private TextView yourPhoneNumber;
    private Button btnConfirm;
    private EditText tvCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_verify_code);
        btnBack = (LinearLayout) findViewById(R.id.btn_back);
        yourPhoneNumber = (TextView) findViewById(R.id.your_phone_number);
        btnSend = (Button) findViewById(R.id.btn_send);
        btnConfirm = (Button) findViewById(R.id.btn_confirm);
        tvCode = (EditText) findViewById(R.id.tv_code);
        String ph = HttpPostUtils.getPhone();
        if (ph.length() == 11) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < ph.length(); i++) {
                char c = ph.charAt(i);
                if (i >= 3 && i <= 6) {
                    sb.append('*');
                } else {
                    sb.append(c);
                }
            }
            yourPhoneNumber.setText(sb.toString());
        }
        btnBack.setOnClickListener(this);
        btnSend.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
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

    private void beginTimer(){
        timerTicks = 60;
        btnSend.setClickable(false);
        handlerTimer.sendEmptyMessageDelayed(0, 1000);
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x1000: {
                    btnSend.setClickable(true);
                    try {
                        String res = (String) msg.obj;
                        if (res == null) {
                            return;
                        }
                        JSONObject obj = new JSONObject(res);
                        String success = obj.getString("Success");
                        String err = obj.getString("ErrMsg");
                        if (success == "true") {
                            Toast.makeText(CheckVerifyCodeActivity.this, "验证码已发送", Toast.LENGTH_LONG).show();
                            haveSent = true;
                            beginTimer();
                        } else if (success == "false") {
                            Toast.makeText(CheckVerifyCodeActivity.this, err, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case 0x1001:
                    try {
                        String res = (String) msg.obj;
                        if (res == null) {
                            return;
                        }
                        JSONObject obj = new JSONObject(res);
                        String success = obj.getString("Success");
                        String err = obj.getString("ErrMsg");
                        if (success == "true") {
                            setResult(0x1000);
                            finish();
                        } else if (success == "false") {
                            Toast.makeText(CheckVerifyCodeActivity.this, err, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_send: {
                String url = "/api/SMS/SendVerifyCode";
                HttpGetUtils.httpGetFile(0x1000, url, handler);
                btnSend.setClickable(false);
                break;
            }
            case R.id.btn_confirm: {
                final String code = tvCode.getText().toString();
                if (code.length() != 6) {
                    Toast.makeText(CheckVerifyCodeActivity.this, "请输入6位验证码", Toast.LENGTH_SHORT).show();
                    return;
                }
                EasyLoading.doWork(this, "正在校验", new EasyLoading.WorkerListener() {
                    @Override
                    public void run(StatusControl statusControl) {
                        String url = "/api/SMS/CheckVerifyCode?code=" + code;
                        HttpGetUtils.httpGetFile(0x1001, url, handler);
                    }
                });
                break;
            }
            case R.id.btn_back:
                finish();
                break;
        }
    }

}
