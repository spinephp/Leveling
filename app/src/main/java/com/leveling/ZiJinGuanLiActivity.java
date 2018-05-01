package com.leveling;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.leveling.entity.AccountMoney;
import com.leveling.entity.Type;
import com.leveling.pay.AddMoney1Activity;
import com.leveling.pay.AddMoneyActivity;
import com.leveling.utils.HttpGetUtils;
import com.leveling.utils.HttpPostUtils;

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
public class ZiJinGuanLiActivity extends AppCompatActivity {
    private TextView account_left_money, useful, unuseful_money;
    private LinearLayout zijinguanli_img;
    private String money, money1;
    private LinearLayout money_sss;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == 1) {
                String money = data.getExtras().getString("money");
                Intent intent = new Intent(ZiJinGuanLiActivity.this, AddMoneyActivity.class);
                intent.putExtra("money", money);
                intent.putExtra("type", "Recharge");
                startActivityForResult(intent, 2);
            }
        }else if(requestCode == 2){
            String res = "";
            if(resultCode == 1) {
                res = "充值成功";
                money = data.getExtras().getString("balance");
                AccountMoney.money = money;
                useful.setText(money);
            }
            else
                res = "充值失败";
            Toast.makeText(ZiJinGuanLiActivity.this, res, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zijin_guanli_layout);
        money_sss = (LinearLayout) findViewById(R.id.money_sss);
        if (HttpPostUtils.getRole() == 0) {
            money_sss.setVisibility(View.VISIBLE);
        }

        zijinguanli_img = (LinearLayout) findViewById(R.id.zijinguanli_img);
        zijinguanli_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.add_money).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getApplicationContext(), AddMoney1Activity.class));
                Intent intent = new Intent(ZiJinGuanLiActivity.this, AddMoney1Activity.class);
                startActivityForResult(intent, 1);
            }
        });



        findViewById(R.id.rl_zijin_chongzhi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ChongzhiActivity.class));
            }
        });
        findViewById(R.id.btn_tixian).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), TixianActivity.class));
            }
        });
        findViewById(R.id.zijinguanli_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.rl_zijin_caiwu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CaiwuActivity.class));
            }
        });
        findViewById(R.id.rl_zijin_tixian).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), TixianHistoryActivity.class));
            }
        });

        account_left_money = (TextView) findViewById(R.id.account_left_money);
        useful = (TextView) findViewById(R.id.useful);
        unuseful_money = (TextView) findViewById(R.id.unuseful_money);
        String url = "/api/Pay/GetPayMoney";
        HttpGetUtils.httpGetFile(16, url, handler);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (HttpPostUtils.getRole() == 0) {
            money_sss.setVisibility(View.VISIBLE);
        }
        String url = "/api/Pay/GetPayMoney";
        HttpGetUtils.httpGetFile(16, url, handler);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 16:
                    try {
                        String res = (String) msg.obj;
                        JSONObject obj = new JSONObject(res);
                        String success = obj.getString("Success");
                        String err = obj.getString("ErrMsg");

                        if (success == "true") {
                            money = obj.getString("Data");
                            AccountMoney.money = money;
                            useful.setText(money);
                            String url = "/api/Pay/GetPayFreezeMoney";
                            HttpGetUtils.httpGetFile(17, url, handler);
                        } else if (success == "false") {
                            Toast.makeText(ZiJinGuanLiActivity.this, err, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                case 17:
                    try {
                        String res = (String) msg.obj;
                        JSONObject obj = new JSONObject(res);
                        String success = obj.getString("Success");
                        String err = obj.getString("ErrMsg");

                        if (success == "true") {
                            money1 = obj.getString("Data");
                            unuseful_money.setText(money1);
                            account_left_money.setText(Float.parseFloat(money1) + Float.parseFloat(money) + "");
                        } else if (success == "false") {
                            Toast.makeText(ZiJinGuanLiActivity.this, err, Toast.LENGTH_LONG).show();
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
