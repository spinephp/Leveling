package com.leveling;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.leveling.entity.ApplyAuthentication;
import com.leveling.utils.HttpGetUtils;
import com.leveling.utils.HttpPostUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class PayMActivity extends AppCompatActivity implements View.OnClickListener {
    private Button pay_conform;
    private TextView cashPledge;
    private String auth;
    private String Id;
    private String cash;
    private LinearLayout img_paynm_back;
    public static String money;
    public static String money1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_m);
        img_paynm_back = (LinearLayout) findViewById(R.id.img_paynm_back12);
        img_paynm_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        init();
    }
    private void init() {
        // TODO Auto-generated method stub
        cashPledge = (TextView) findViewById(R.id.new_CashPledge12);
        auth = ApplyAuthentication.id;
        Id = HttpPostUtils.getUserId();
        cash = ApplyAuthentication.money;
        money = cash;
        pay_conform = (Button) findViewById(R.id.new_pay_conform12);
        cashPledge.setText("￥"+cash);
        pay_conform.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        int id = v.getId();
        switch (id) {
            case R.id.new_pay_conform12:
                Intent intent = new Intent(PayMActivity.this,PayMonActivity.class);
                startActivity(intent);
                break;
        }
    }
}
