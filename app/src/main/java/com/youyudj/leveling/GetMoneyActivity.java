package com.youyudj.leveling;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.youyudj.leveling.entity.ApplyAuthentication;
import com.youyudj.leveling.utils.HttpGetUtils;
import com.youyudj.leveling.utils.HttpPostUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static android.R.id.list;

public class GetMoneyActivity extends AppCompatActivity implements View.OnClickListener, PayPwdView.InputCallBack {

    private LinearLayout img_back_from_tixian;
    private EditText input_money;
    private Button input_money_button;
    private TextView money_biggest_amount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_money);
        img_back_from_tixian = (LinearLayout) findViewById(R.id.img_back_from_tixian);
        input_money = (EditText) findViewById(R.id.input_money);
       // money_biggest_amount = (TextView) findViewById(R.id.money_biggest_amount);
        input_money_button = (Button) findViewById(R.id.input_money_button);
        input_money_button.setOnClickListener(this);
        img_back_from_tixian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GetMoneyActivity.this,TixianActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(GetMoneyActivity.this,TixianActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.input_money_button:
                if (input_money.getText().toString().equals("")){
                    Toast.makeText(GetMoneyActivity.this,"请输入提现金额",Toast.LENGTH_SHORT).show();
                }else{
                    if (TixianActivity.str=="1"){
                        Bundle bundle = new Bundle();
                        bundle.putString(PayFragment.EXTRA_CONTENT, "支付：¥ " + input_money.getText().toString().trim());
                        PayFragment fragment = new PayFragment();
                        fragment.setArguments(bundle);
                        fragment.setPaySuccessCallBack(GetMoneyActivity.this);
                        fragment.show(getSupportFragmentManager(), "Pay");

                    }else if (TixianActivity.str=="2"){
                        Bundle bundle = new Bundle();
                        bundle.putString(PayFragment.EXTRA_CONTENT, "支付：¥ " + input_money.getText().toString().trim());
                        PayFragment fragment = new PayFragment();
                        fragment.setArguments(bundle);
                        fragment.setPaySuccessCallBack(GetMoneyActivity.this);
                        fragment.show(getSupportFragmentManager(), "Pay");
                    }else if (TixianActivity.str=="3"){
                        Bundle bundle = new Bundle();
                        bundle.putString(PayFragment.EXTRA_CONTENT, "支付：¥ " + input_money.getText().toString().trim());
                        PayFragment fragment = new PayFragment();
                        fragment.setArguments(bundle);
                        fragment.setPaySuccessCallBack(GetMoneyActivity.this);
                        fragment.show(getSupportFragmentManager(), "Pay");
                    }
                }
                break;
        }
    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 12:
                    try {
                        String res = (String) msg.obj;
                        JSONObject result = new JSONObject(res);
                        String success = result.getString("Success");
                        String err = result.getString("ErrMsg");
                        if (success == "true") {
                            Intent intent  = new Intent(GetMoneyActivity.this,TixianSuccessActivity.class);
                            startActivity(intent);
                            finish();
                            //Toast.makeText(GetMoneyActivity.this, success, Toast.LENGTH_LONG).show();
                        } else if (success == "false") {
                            Toast.makeText(GetMoneyActivity.this, err, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 13:
                    try {
                        String res = (String) msg.obj;
                        JSONObject result = new JSONObject(res);
                        String success = result.getString("Success");
                        String err = result.getString("ErrMsg");
                        if (success == "true") {
                            Intent intent  = new Intent(GetMoneyActivity.this,TixianSuccessActivity.class);
                            startActivity(intent);
                            finish();
                            //Toast.makeText(GetMoneyActivity.this, success, Toast.LENGTH_LONG).show();
                        } else if (success == "false") {
                            Toast.makeText(GetMoneyActivity.this, err, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    @Override
    public void onInputFinish(String result) {
        final String result1 = result;
        if(TixianActivity.str=="1"){
            String url = "/api/Pay/GetMoneyWithdrawal?money="+input_money.getText().toString().trim()+"&goal="+3+"&paypw="+result1;
            HttpGetUtils.httpGetFile(13,url,handler);
        }else if(TixianActivity.str=="2"){
            String url = "/api/Pay/GetMoneyWithdrawal?money="+input_money.getText().toString().trim()+"&goal="+2+"&paypw="+result1;
            HttpGetUtils.httpGetFile(12,url,handler);
        }else if(TixianActivity.str=="3"){
            String url = "/api/Pay/GetMoneyWithdrawal?money="+input_money.getText().toString().trim()+"&goal="+1+"&paypw="+result1;
            HttpGetUtils.httpGetFile(12,url,handler);
        }
    }
}
