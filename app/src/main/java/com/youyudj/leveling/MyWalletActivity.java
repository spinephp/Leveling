package com.youyudj.leveling;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.youyudj.leveling.entity.AccountMoney;
import com.youyudj.leveling.pay.AddMoney1Activity;
import com.youyudj.leveling.pay.AddMoneyActivity;
import com.youyudj.leveling.utils.HttpGetUtils;
import com.youyudj.leveling.utils.HttpPostUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

/**
 * *******************************************************
 * Description:
 * Autour: 3W攻城狮
 * Date: 2017/11/7 21:47
 * Update:2017/11/7
 * Version:
 * *******************************************************
 */
public class MyWalletActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView accout_left_money, leveling_money_new, peilian_money_new;
    private Button tixian_my_money, add_leveling_money, add_peilian_money;
    private String mess;
    public static String message;
    private RelativeLayout packaa;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_my_wallet);

        packaa = (RelativeLayout) findViewById(R.id.lay_peilian_yajin);
        if (HttpPostUtils.getRole() == 3 || HttpPostUtils.getRole() == 4 || HttpPostUtils.getRole() == 7 || HttpPostUtils.getRole() == 8) {
            packaa.setVisibility(View.VISIBLE);
        }
        LinearLayout img_title_back = (LinearLayout) findViewById(R.id.img_title_back);
        add_leveling_money = (Button) findViewById(R.id.add_leveling_money);
        add_peilian_money = (Button) findViewById(R.id.add_peilian_money);
        add_leveling_money.setOnClickListener(this);
        add_peilian_money.setOnClickListener(this);
        img_title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        accout_left_money = (TextView) findViewById(R.id.select_left_money_new);
        leveling_money_new = (TextView) findViewById(R.id.leveling_money_new);
        peilian_money_new = (TextView) findViewById(R.id.peilian_money_new);
        tixian_my_money = (Button) findViewById(R.id.tixian_my_money);
        tixian_my_money.setOnClickListener(this);
        String url = "/api/Pay/GetPayMoney";
        HttpGetUtils.httpGetFile(1, url, handler);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (HttpPostUtils.getRole() == 3 || HttpPostUtils.getRole() == 4 || HttpPostUtils.getRole() == 7 || HttpPostUtils.getRole() == 8) {
            packaa.setVisibility(View.VISIBLE);
        }
        String url = "/api/Pay/GetPayMoney";
        HttpGetUtils.httpGetFile(1, url, handler);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    String res = (String) msg.obj;
                    if (res == null) {
                        return;
                    }
                    try {
                        JSONObject obj = new JSONObject(res);
                        String success = obj.getString("Success");
                        String err = obj.getString("ErrMsg");
                        String details = obj.getString("Data");
                        accout_left_money.setText(details);
                        String url = "/api/Pay/GetPayCashPledge1";
                        HttpGetUtils.httpGetFile(2, url, handler);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    String res1 = (String) msg.obj;
                    if (res1 == null) {
                        return;
                    }
                    try {
                        JSONObject obj = new JSONObject(res1);
                        String success = obj.getString("Success");
                        String err = obj.getString("ErrMsg");
                        String details = obj.getString("Data");
                        leveling_money_new.setText(details);
                        String url = "/api/Pay/GetPayCashPledge2";
                        HttpGetUtils.httpGetFile(3, url, handler);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    String res2 = (String) msg.obj;
                    if (res2 == null) {
                        return;
                    }
                    try {
                        JSONObject obj = new JSONObject(res2);
                        String success = obj.getString("Success");
                        String err = obj.getString("ErrMsg");
                        String details = obj.getString("Data");
                        peilian_money_new.setText(details);
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
            case R.id.tixian_my_money:
                showDialog();
                break;
            case R.id.add_leveling_money: {
                Intent intent = new Intent(MyWalletActivity.this, AddMoney1Activity.class);
                startActivityForResult(intent, 1);
                break;
            }
            case R.id.add_peilian_money: {
                Intent intent = new Intent(MyWalletActivity.this, AddMoney1Activity.class);
                startActivityForResult(intent, 3);
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == 1) {
                String money = data.getExtras().getString("money");
                Intent intent = new Intent(MyWalletActivity.this, AddMoneyActivity.class);
                intent.putExtra("money", money);
                intent.putExtra("type", "RechargeCashPledge");
                startActivityForResult(intent, 2);
            }
        }else if(requestCode == 2){
            String res = "";
            if(resultCode == 1) {
                res = "充值成功";
                String money = data.getExtras().getString("balance");
                AccountMoney.money = money;
                leveling_money_new.setText(money);
            }
            else
                res = "充值失败";
            Toast.makeText(MyWalletActivity.this, res, Toast.LENGTH_SHORT).show();
        }else if (requestCode == 3) {
            if (resultCode == 1) {
                String money = data.getExtras().getString("money");
                Intent intent = new Intent(MyWalletActivity.this, AddMoneyActivity.class);
                intent.putExtra("money", money);
                intent.putExtra("type", "RechargeCashPledge2");
                startActivityForResult(intent, 4);
            }
        }else if(requestCode == 4){
            String res = "";
            if(resultCode == 1) {
                res = "充值成功";
                String money = data.getExtras().getString("balance");
                AccountMoney.money = money;
                peilian_money_new.setText(money);
            }
            else
                res = "充值失败";
            Toast.makeText(MyWalletActivity.this, res, Toast.LENGTH_SHORT).show();
        }
    }

    private final void showDialog() {
        DataPickerDialog.Builder builder = new DataPickerDialog.Builder(this);
        List<String> data;
        if (HttpPostUtils.getRole() == 3 || HttpPostUtils.getRole() == 4 || HttpPostUtils.getRole() == 7 || HttpPostUtils.getRole() == 8) {
            //data = Arrays.asList(new String[]{"钱包提现", "代练押金提现", "陪练押金提现"});
            data = Arrays.asList(new String[]{"钱包提现", "陪练押金提现"});
        } else {
            data = Arrays.asList(new String[]{"钱包提现", "押金提现"});
        }
        DataPickerDialog dialog = builder.setUnit("").setData(data).setSelection(1).setTitle("")
                .setOnDataSelectedListener(new DataPickerDialog.OnDataSelectedListener() {
                    @Override
                    public void onDataSelected(String itemValue) {
                        mess = itemValue;
                        if (mess == "钱包提现") {
                            message = "1";
                            Intent intent = new Intent(MyWalletActivity.this, TixianPackageActivity.class);
                            startActivity(intent);
                        } else if (mess == "押金提现") {
                            message = "2";
                            Intent intent = new Intent(MyWalletActivity.this, TixianDailianActivity.class);
                            startActivity(intent);
                        } else if (mess == "陪练押金提现") {
                            message = "3";
                            Intent intent = new Intent(MyWalletActivity.this, TixianPeilianActivity.class);
                            startActivity(intent);
                        }
                    }
                }).create();
        dialog.show();
    }

}
