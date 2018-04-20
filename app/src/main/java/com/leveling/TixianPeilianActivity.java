package com.youyudj.leveling;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import com.youyudj.leveling.utils.HttpGetUtils;
import com.youyudj.leveling.utils.HttpPostUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class TixianPeilianActivity extends AppCompatActivity implements View.OnClickListener {
    private PopupWindow popupWindow;
    private View contentView;
    private Dialog dialog;
    private LinearLayout img_nima_back;
    private RelativeLayout zhifubao_update;
    private RelativeLayout weixin_update,card_update33;
    public static String str;
    public static String str1;
    private TextView updw,updzf,updca,zfb_account,wc_account,card_account;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tixian_peilian);
        img_nima_back = (LinearLayout) findViewById(R.id.img_nima_back);
        img_nima_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        zfb_account = (TextView) findViewById(R.id.zfb_account2);
        wc_account = (TextView) findViewById(R.id.wc_account2);
        card_account = (TextView) findViewById(R.id.card_account2);
        zhifubao_update = (RelativeLayout) findViewById(R.id.zhifubao_update33);
        weixin_update = (RelativeLayout) findViewById(R.id.weixin_update33);
        card_update33 = (RelativeLayout) findViewById(R.id.card_update33);

        updw = (TextView) findViewById(R.id.updw);
        updzf = (TextView) findViewById(R.id.updzf);
        updca = (TextView) findViewById(R.id.updca);

        zhifubao_update.setOnClickListener(this);
        weixin_update.setOnClickListener(this);
        card_update33.setOnClickListener(this);

        updw.setOnClickListener(this);
        updzf.setOnClickListener(this);
        updca.setOnClickListener(this);
        findViewById(R.id.ll_tixian_add33).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeAvatar();
            }
        });
        String url = "/api/Users/GetPayInfor";
        HttpGetUtils.httpGetFile(1,url,handler);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.zhifubao_update33:
                str = "1";
                Intent intet = new Intent(TixianPeilianActivity.this,TixianActivity.class);
                startActivity(intet);
                finish();
                break;
            case R.id.weixin_update33:
                str = "2";
                Intent intetn = new Intent(TixianPeilianActivity.this,TixianActivity.class);
                startActivity(intetn);
                finish();
                break;
            case R.id.card_update33:
                str = "2";
                Intent intetn1 = new Intent(TixianPeilianActivity.this,TixianActivity.class);
                startActivity(intetn1);
                finish();
                break;
            case R.id.updw:
                Intent intet2 = new Intent(TixianPeilianActivity.this,AddwcActivity.class);
                startActivity(intet2);
                finish();
                break;
            case R.id.updzf:
                Intent intet3 = new Intent(TixianPeilianActivity.this,AddZfbActivity.class);
                startActivity(intet3);
                finish();
                break;
            case R.id.updca:
                Intent intet4 = new Intent(TixianPeilianActivity.this,AddBankActivity.class);
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
                startActivity(new Intent(getApplicationContext(),AddBankActivity.class));
            }
        });
        dialog.findViewById(R.id.tx_add_zfb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AddZfbActivity.class));
            }
        });
//        dialog.findViewById(R.id.tx_add_wc).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(),AddwcActivity.class));
//            }
//        });
        dialog.show();
    }
    @Override
    protected void onResume() {
        super.onResume();
        String url = "/api/Users/GetPayInfor";
        HttpGetUtils.httpGetFile(1,url,handler);
    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    try {
                        String res = (String) msg.obj;
                        if (res==null){
                            return;
                        }
                        JSONObject result = new JSONObject(res);
                        String success = result.getString("Success");
                        String err = result.getString("ErrMsg");
                        String data = result.getString("Data");
                        if (success == "true") {
                            JSONObject js = new JSONObject(data);
                            String cardNumber = js.getString("CardNumber");
                            String wechat = js.getString("WeChat");
                            String alipay = js.getString("Alipay");
                            if (!cardNumber.equals("null")){
                                card_update33.setVisibility(View.VISIBLE);
                                card_account.setText(cardNumber);
                            }
                            if (!wechat.equals("null")){
                                weixin_update.setVisibility(View.VISIBLE);
                                wc_account.setText(wechat);
                            }
                            if (!alipay.equals("null")){
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

