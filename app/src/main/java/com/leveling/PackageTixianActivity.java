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

import com.youyudj.leveling.utils.HttpGetUtils;
import com.youyudj.leveling.utils.HttpPostUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class PackageTixianActivity extends AppCompatActivity implements PayPwdView.InputCallBack, View.OnClickListener {
    private LinearLayout img_back_from_tixian;
    private EditText input_money;
    private Button input_money_button;
    private TextView money_biggest_amount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_tixian);
        img_back_from_tixian = (LinearLayout) findViewById(R.id.img_back_from_peiliantixian1121);
        input_money = (EditText) findViewById(R.id.input_peilian_money1121);
        // money_biggest_amount = (TextView) findViewById(R.id.money_biggest_amount);
        input_money_button = (Button) findViewById(R.id.input_peilian_money_button1121);
        input_money_button.setOnClickListener(this);
        img_back_from_tixian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PackageTixianActivity.this,TixianPackageActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(PackageTixianActivity.this,TixianPackageActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.input_peilian_money_button1121:
                if (input_money.getText().toString().equals("")){
                    Toast.makeText(PackageTixianActivity.this,"请输入提现金额",Toast.LENGTH_SHORT).show();
                }else{
                    if (TixianPackageActivity.str=="1"){
                        Bundle bundle = new Bundle();
                        bundle.putString(PayFragment.EXTRA_CONTENT, "支付：¥ " + input_money.getText().toString().trim());
                        PayFragment fragment = new PayFragment();
                        fragment.setArguments(bundle);
                        fragment.setPaySuccessCallBack(PackageTixianActivity.this);
                        fragment.show(getSupportFragmentManager(), "Pay");
                    }else if (TixianPackageActivity.str=="2"){
                        Bundle bundle = new Bundle();
                        bundle.putString(PayFragment.EXTRA_CONTENT, "支付：¥ " + input_money.getText().toString().trim());
                        PayFragment fragment = new PayFragment();
                        fragment.setArguments(bundle);
                        fragment.setPaySuccessCallBack(PackageTixianActivity.this);
                        fragment.show(getSupportFragmentManager(), "Pay");
                    }else if (TixianPackageActivity.str=="3"){
                        Bundle bundle = new Bundle();
                        bundle.putString(PayFragment.EXTRA_CONTENT, "支付：¥ " + input_money.getText().toString().trim());
                        PayFragment fragment = new PayFragment();
                        fragment.setArguments(bundle);
                        fragment.setPaySuccessCallBack(PackageTixianActivity.this);
                        fragment.show(getSupportFragmentManager(), "Pay");
                    }
                }
                break;
        }
    }

    @Override
    public void onInputFinish(String result) {
        final String result1 = result;
        if(TixianPackageActivity.str=="1"){
            TixianPackageActivity.str="";
            String url = "/api/Pay/GetMoneyWithdrawal?money="+input_money.getText().toString().trim()+"&goal="+3+"&paypw="+result1;
            HttpGetUtils.httpGetFile(13,url,handler);
        }else if(TixianPackageActivity.str=="2"){
            TixianPackageActivity.str="";
            String url = "/api/Pay/GetMoneyWithdrawal?money="+input_money.getText().toString().trim()+"&goal="+2+"&paypw="+result1;
            HttpGetUtils.httpGetFile(12,url,handler);
        }else if(TixianPackageActivity.str=="3"){
            TixianPackageActivity.str="";
            String url = "/api/Pay/GetMoneyWithdrawal?money="+input_money.getText().toString().trim()+"&goal="+1+"&paypw="+result1;
            HttpGetUtils.httpGetFile(12,url,handler);
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
                            Intent intent  = new Intent(PackageTixianActivity.this,TixianSuccessActivity.class);
                            startActivity(intent);
                            finish();
                            //Toast.makeText(GetMoneyActivity.this, success, Toast.LENGTH_LONG).show();
                        } else if (success == "false") {
                            Toast.makeText(PackageTixianActivity.this, err, Toast.LENGTH_LONG).show();
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
                            Intent intent  = new Intent(PackageTixianActivity.this,TixianSuccessActivity.class);
                            startActivity(intent);
                            finish();
                            //Toast.makeText(GetMoneyActivity.this, success, Toast.LENGTH_LONG).show();
                        } else if (success == "false") {
                            Toast.makeText(PackageTixianActivity.this, err, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
}
