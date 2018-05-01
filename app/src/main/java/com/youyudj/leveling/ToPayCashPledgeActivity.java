package com.youyudj.leveling;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.youyudj.leveling.alipay.PayResult;
import com.youyudj.leveling.entity.AccountMoney;
import com.youyudj.leveling.entity.ApplyAuthentication;
import com.youyudj.leveling.entity.OrderInfo;
import com.youyudj.leveling.entity.UserInfo;
import com.youyudj.leveling.pay.WechatPay;
import com.youyudj.leveling.utils.HttpGetUtils;
import com.youyudj.leveling.utils.HttpPostUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class ToPayCashPledgeActivity extends AppCompatActivity implements OnClickListener ,PayPwdView.InputCallBack {
    private String auth;
    private String Id;
    private String cash;
    private TextView order_amount;
    private TextView account_money;
    private TextView pay_amount;
    private LinearLayout last_pay_conform;
    private LinearLayout img_shouyinPPP_back;
    private static final String AUTH = "auth";
    private UserInfo userInfo;
    private String payMethod = null;
    final String PM_YuE = "YuE";
    final String PM_Wechat = "Wechat";
    final String PM_AliPay = "AliPay";
    final int WECHAT_COMMON_ORDER_API = 20;
    private String aliPayOrderInfo = null;

    class BorderClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.bdr_yu_e)
                payMethod = PM_YuE;
            else if (v.getId() == R.id.bdr_weixin)
                payMethod = PM_Wechat;
            else
                payMethod = PM_AliPay;
            selectPayMethod();
        }
    }

    private void selectPayMethod() {
        findViewById(R.id.bdr_yu_e).setSelected(payMethod.equals(PM_YuE));
        findViewById(R.id.bdr_weixin).setSelected(payMethod.equals(PM_Wechat));
        findViewById(R.id.bdr_alipay).setSelected(payMethod.equals(PM_AliPay));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cashire_layout);
        img_shouyinPPP_back = (LinearLayout) findViewById(R.id.img_shouyinPPP_back);
        img_shouyinPPP_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.bdr_yu_e).setOnClickListener(new BorderClick());
        findViewById(R.id.bdr_weixin).setOnClickListener(new BorderClick());
        findViewById(R.id.bdr_alipay).setOnClickListener(new BorderClick());
        userInfo = new UserInfo(this);
        String url = "/api/Pay/GetPayMoney";
        HttpGetUtils.httpGetFile(1,url,handler);
        init();
        url = "/api/Pay/PayForAuth?authID=" + ApplyAuthentication.id;
        HttpGetUtils.httpGetFile(2, url, handler);
    }

    private void init() {
        // TODO Auto-generated method stub
        order_amount = (TextView) findViewById(R.id.order_amount);
        order_amount.setText("￥"+PayCashPledgeActivity.money);
        account_money = (TextView) findViewById(R.id.account_money);
        pay_amount = (TextView) findViewById(R.id.pay_amount);
        pay_amount.setText("￥"+ ApplyAuthentication.money);
        account_money.setText("￥"+PayCashPledgeActivity.money1);
        last_pay_conform = (LinearLayout) findViewById(R.id.last_pay_conform);
        last_pay_conform.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        int id = v.getId();
        switch (id) {
            case R.id.last_pay_conform:
                if (payMethod == null) {
                    Toast.makeText(ToPayCashPledgeActivity.this, "请选择支付方式", Toast.LENGTH_LONG).show();
                    return;
                }
                if (payMethod.equals(PM_YuE)) {
                    Bundle bundle = new Bundle();
                    bundle.putString(PayFragment.EXTRA_CONTENT, "支付：¥ " + ApplyAuthentication.money);
                    PayFragment fragment = new PayFragment();
                    fragment.setArguments(bundle);
                    fragment.setPaySuccessCallBack(ToPayCashPledgeActivity.this);
                    fragment.show(getSupportFragmentManager(), "Pay");
                } else if (payMethod.equals(PM_AliPay)) {
                    if (TextUtils.isEmpty(aliPayOrderInfo)) {
                        Toast.makeText(ToPayCashPledgeActivity.this, "获取支付授权失败", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Runnable payRunnable = new Runnable() {
                        @Override
                        public void run() {
                            PayTask alipay = new PayTask(ToPayCashPledgeActivity.this);
                            Map<String, String> result = alipay.payV2(aliPayOrderInfo, true);
                            Log.i("msp", result.toString());
                            Message msg = new Message();
                            msg.what = 3;
                            msg.obj = result;
                            handler.sendMessage(msg);
                        }
                    };
                    new Thread(payRunnable).start();
                }else if(payMethod.equals(PM_Wechat)){
                    String urlWX = "/api/Pay/WXRechargeCashPledge?money=" +ApplyAuthentication.money;
                    HttpGetUtils.httpGetFile(WECHAT_COMMON_ORDER_API, urlWX, handler);
                    //Toast.makeText(ToPayCashPledgeActivity.this, "微信支付尚未开通，请选择其他支付方式", Toast.LENGTH_LONG).show();
                    return;
                }
        }
    }

    public void onInputFinish(String result) {
      final String result1 = result;
                auth = ApplyAuthentication.id;
                Id = HttpPostUtils.getUserId();
                cash = ApplyAuthentication.money;
                String url10 ="/api/Authentication/GetPayCashPledge?AuthenticationID="
                       + auth + "&CashPledge=" + cash+"&payPW="+result1;
                HttpGetUtils.httpGetFile(1000,url10, handler);
    }
    private Handler  handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what){
                case 4: {
                    String res = (String) msg.obj;
                    if (res == null || res.length() == 0) {
                        Toast.makeText(ToPayCashPledgeActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    } else {
                        userInfo.setUserInfo(AUTH,true);
                        Intent intent = new Intent(ToPayCashPledgeActivity.this,BeaterAuthSuccessActivity.class);
                        startActivity(intent);
                        setResult(0x1001);
                        finish();
                    }
                    break;
                }
                case 3: {
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    if (TextUtils.equals(resultStatus, "9000")) {
                        String url = null;
                        url = "/api/Pay/AlipayCheckResult";//?responseText=" + URLEncoder.encode(resultInfo, "UTF-8");
                        JSONObject json = new JSONObject();
                        try {
                            json.put("ResponseText", resultInfo);
                        } catch (JSONException e) {
                        }
                        HttpPostUtils.httpPostFile(4, url, json, handler);
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        //Toast.makeText(PayOrderActivity.this, "充值失败", Toast.LENGTH_SHORT).show();
                        //PayOrderActivity.this.finish();
                    }
                    break;
                }
                case 2:{
                    String res = (String) msg.obj;
                    if (res != null) {
                        aliPayOrderInfo = res;
                        if (res.startsWith("\""))
                            aliPayOrderInfo = aliPayOrderInfo.substring(1, aliPayOrderInfo.length() - 1);
                        return;
                    } else {
                        Toast.makeText(ToPayCashPledgeActivity.this, "获取支付宝支付信息失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                case 1000:
                    try {
                        String res = (String) msg.obj;
                        if (res==null){
                            return;
                        }
                        JSONObject result = new JSONObject(res);
                        String success = result.getString("Success");
                        String err = result.getString("ErrMsg");
                        if(success == "true"){
                            userInfo.setUserInfo(AUTH,true);
                            Intent intent = new Intent(ToPayCashPledgeActivity.this,BeaterAuthSuccessActivity.class);
                            startActivity(intent);
                            finish();
                        }else if(success == "false"){
                            Toast.makeText(ToPayCashPledgeActivity.this, err,  Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    try {
                        String res = (String) msg.obj;
                        if (res==null){
                            return;
                        }
                        JSONObject obj = new JSONObject(res);
                        String success = obj.getString("Success");
                        String err = obj.getString("ErrMsg");
                        String  money = obj.getString("Data");
                        AccountMoney.money =money;
                        account_money.setText("￥"+money);
                        if(success == "true"){
                           // Toast.makeText(ToPayCashPledgeActivity.this,success,Toast.LENGTH_LONG).show();
                        }else if(success == "false"){
                            Toast.makeText(ToPayCashPledgeActivity.this,err,Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                case WECHAT_COMMON_ORDER_API: {
                    String res = (String) msg.obj;
                    if (res != null) {
                        String wxOrderInfo = res;
                        if (res.startsWith("\""))
                            wxOrderInfo = wxOrderInfo.substring(1, wxOrderInfo.length() - 1);
                        wxOrderInfo = wxOrderInfo.replace("\\", "");
                        WechatPay wechatPay = new Gson().fromJson(wxOrderInfo, WechatPay.class);
                        if (wechatPay.getPrepayid() != null) {
                            OrderInfo.WXPayType = 4;
                            wechatPay.pay(getApplicationContext());
                        } else {
                            Toast.makeText(ToPayCashPledgeActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

        }

    };
}
