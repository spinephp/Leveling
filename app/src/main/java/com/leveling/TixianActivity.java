package com.leveling;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.leveling.utils.HttpGetUtils;
import com.leveling.utils.HttpPostUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * *******************************************************
 * Description:
 * Autour: 3W攻城狮
 * Date: 2017/11/7 21:47
 * Update:2017/11/7
 * Version:
 * *******************************************************
 */
public class TixianActivity extends AppCompatActivity implements View.OnClickListener {

    private PopupWindow popupWindow;
    private View contentView;
    private Dialog dialog;
    private LinearLayout img_nima_back;
    private RelativeLayout zhifubao_update;
    private RelativeLayout weixin_update, card_update;
    public static String str;
    public static String str1;
    private TextView update_wc, update_zfb, update_card, zfb_account, wc_account, card_account;
    public static String name1, name2, name3;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.withdrawals_account_layout);
        img_nima_back = (LinearLayout) findViewById(R.id.img_nima_back);
        img_nima_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        zfb_account = (TextView) findViewById(R.id.zfb_account);
        wc_account = (TextView) findViewById(R.id.wc_account);
        card_account = (TextView) findViewById(R.id.card_account);
        zhifubao_update = (RelativeLayout) findViewById(R.id.zhifubao_update);
        weixin_update = (RelativeLayout) findViewById(R.id.weixin_update);
        card_update = (RelativeLayout) findViewById(R.id.card_update);
        update_wc = (TextView) findViewById(R.id.update_wc);
        update_zfb = (TextView) findViewById(R.id.update_zfb);
        update_card = (TextView) findViewById(R.id.update_card);
        zhifubao_update.setOnClickListener(this);
        weixin_update.setOnClickListener(this);
        card_update.setOnClickListener(this);
        update_wc.setOnClickListener(this);
        update_zfb.setOnClickListener(this);
        update_card.setOnClickListener(this);
        findViewById(R.id.ll_tixian_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeAvatar();
            }
        });
        String url = "/api/Users/GetPayInfor";
        HttpGetUtils.httpGetFile(1, url, handler);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.zhifubao_update:
                str = "1";
                Intent intetn = new Intent(TixianActivity.this, GetMoneyActivity.class);
                startActivity(intetn);
                finish();
                break;
            case R.id.weixin_update:
                str = "2";
                Intent intet = new Intent(TixianActivity.this, GetMoneyActivity.class);
                startActivity(intet);
                finish();
                break;
            case R.id.card_update:
                str = "3";
                Intent intet1 = new Intent(TixianActivity.this, GetMoneyActivity.class);
                startActivity(intet1);
                finish();
                break;
            case R.id.update_wc:
                Intent intet2 = new Intent(TixianActivity.this, AddwcActivity.class);
                startActivity(intet2);
                finish();
                break;
            case R.id.update_zfb:
                Intent intet3 = new Intent(TixianActivity.this, AddZfbActivity.class);
                startActivity(intet3);
                finish();
                break;
            case R.id.update_card:
                Intent intet4 = new Intent(TixianActivity.this, AddBankActivity.class);
                startActivity(intet4);
                finish();
                break;
        }
    }

    private void changeAvatar() {
        dialog = new Dialog(this, R.style.dialog);
        dialog.setContentView(R.layout.withdrawals_dialog_layout);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);
        window.setWindowAnimations(R.style.mypopwindow_anim_style);
        dialog.findViewById(R.id.tx_add_bank).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddBankActivity.class));
                finish();
            }
        });
        dialog.findViewById(R.id.tx_add_zfb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddZfbActivity.class));
                finish();
            }
        });
//        dialog.findViewById(R.id.tx_add_wc).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(),AddwcActivity.class));
//                finish();
//            }
//        });
        dialog.show();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    try {
                        String res = (String) msg.obj;
                        if (res == null) {
                            return;
                        }
                        JSONObject result = new JSONObject(res);
                        String success = result.getString("Success");
                        String err = result.getString("ErrMsg");
                        String data = result.getString("Data");
                        name1 = result.getString("Data");
                        if (success == "true") {
                            JSONObject js = new JSONObject(data);
                            String cardNumber = js.getString("CardNumber");
                            String wechat = js.getString("WeChat");
                            String alipay = js.getString("Alipay");
                            if (!cardNumber.equals("null")) {
                                card_update.setVisibility(View.VISIBLE);
                                card_account.setText(cardNumber);
                            }
                            if (!wechat.equals("null")) {
                                weixin_update.setVisibility(View.VISIBLE);
                                wc_account.setText(wechat);
                            }
                            if (!alipay.equals("null")) {
                                zhifubao_update.setVisibility(View.VISIBLE);
                                zfb_account.setText(alipay);
                            }
                        } else if (success == "false") {

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
}
