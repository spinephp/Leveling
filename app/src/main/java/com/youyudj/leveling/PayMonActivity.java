package com.youyudj.leveling;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.youyudj.leveling.alipay.PayResult;
import com.google.gson.Gson;
import com.youyudj.leveling.entity.AccountMoney;
import com.youyudj.leveling.entity.ApplyAuthentication;
import com.youyudj.leveling.entity.OrderInfo;
import com.youyudj.leveling.entity.UserInfo;
import com.youyudj.leveling.pay.AddMoneyActivity;
import com.youyudj.leveling.pay.WechatPay;
import com.youyudj.leveling.utils.HttpGetUtils;
import com.youyudj.leveling.utils.HttpPostUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class PayMonActivity extends AppCompatActivity implements View.OnClickListener, PayPwdView.InputCallBack {

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
    private String aliPayOrderInfo = null;
    private String payMethod = null;
    final String PM_YuE = "YuE";
    final String PM_Wechat = "Wechat";
    final String PM_AliPay = "AliPay";
    final int WECHAT_COMMON_ORDER_API = 20;

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
        setContentView(R.layout.activity_pay_mon);
        findViewById(R.id.bdr_yu_e).setOnClickListener(new BorderClick());
        findViewById(R.id.bdr_weixin).setOnClickListener(new BorderClick());
        findViewById(R.id.bdr_alipay).setOnClickListener(new BorderClick());
        img_shouyinPPP_back = (LinearLayout) findViewById(R.id.img_shouyinPPP_back112);
        img_shouyinPPP_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        userInfo = new UserInfo(this);
        String url = "/api/Pay/GetPayMoney";
        HttpGetUtils.httpGetFile(1, url, handler);
        url = "/api/Pay/PayForAuth?authID=" + ApplyAuthentication.id;
        HttpGetUtils.httpGetFile(2, url, handler);
        init();
    }

    private void init() {
        // TODO Auto-generated method stub
        order_amount = (TextView) findViewById(R.id.order_amount112);
        order_amount.setText("￥" + ApplyAuthentication.money);
        account_money = (TextView) findViewById(R.id.account_money112);
        pay_amount = (TextView) findViewById(R.id.pay_amount112);
        pay_amount.setText("￥" + ApplyAuthentication.money);
        account_money.setText("￥" + PayCashPledgeActivity.money1);
        last_pay_conform = (LinearLayout) findViewById(R.id.last_pay_conform112);
        last_pay_conform.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        int id = v.getId();
        switch (id) {
            case R.id.last_pay_conform112:
                if (payMethod == null) {
                    Toast.makeText(PayMonActivity.this, "请选择支付方式", Toast.LENGTH_LONG).show();
                    return;
                }
                if (payMethod.equals(PM_YuE)) {
                    Bundle bundle = new Bundle();
                    bundle.putString(PayFragment.EXTRA_CONTENT, "支付：¥ " + ApplyAuthentication.money);
                    PayFragment fragment = new PayFragment();
                    fragment.setArguments(bundle);
                    fragment.setPaySuccessCallBack(PayMonActivity.this);
                    fragment.show(getSupportFragmentManager(), "Pay");
                } else if (payMethod.equals(PM_AliPay)) {
                    if (TextUtils.isEmpty(aliPayOrderInfo)) {
                        Toast.makeText(PayMonActivity.this, "获取支付授权失败", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Runnable payRunnable = new Runnable() {
                        @Override
                        public void run() {
                            PayTask alipay = new PayTask(PayMonActivity.this);
                            Map<String, String> result = alipay.payV2(aliPayOrderInfo, true);
                            Log.i("msp", result.toString());
                            Message msg = new Message();
                            msg.what = 3;
                            msg.obj = result;
                            handler.sendMessage(msg);
                        }
                    };
                    new Thread(payRunnable).start();
                } else if (payMethod.equals(PM_Wechat)) {
                    String urlWX = "/api/Pay/WXPayForAuth?authID=" + ApplyAuthentication.id;
                    HttpGetUtils.httpGetFile(WECHAT_COMMON_ORDER_API, urlWX, handler);
                    //Toast.makeText(PayMonActivity.this, "微信支付尚未开通，请选择其他支付方式", Toast.LENGTH_LONG).show();
                    //return;
                }
        }
    }

    public void onInputFinish(String result) {
        final String result1 = result;
        auth = ApplyAuthentication.id;
        Id = HttpPostUtils.getUserId();
        cash = ApplyAuthentication.money;
        String url10 = "/api/Authentication/GetPayCashPledge?AuthenticationID="
                + auth + "&CashPledge=" + ApplyAuthentication.money + "&payPW=" + result1;
        HttpGetUtils.httpGetFile(10, url10, handler);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 2: {
                    String res = (String) msg.obj;
                    if (res != null) {
                        aliPayOrderInfo = res;
                        if (res.startsWith("\""))
                            aliPayOrderInfo = aliPayOrderInfo.substring(1, aliPayOrderInfo.length() - 1);
                        return;
                    } else {
                        Toast.makeText(PayMonActivity.this, "获取支付宝支付信息失败", Toast.LENGTH_SHORT).show();
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
                case 4: {
                    String res = (String) msg.obj;
                    if (res == null || res.length() == 0) {
                        Toast.makeText(PayMonActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(PayMonActivity.this, BeaterAuthSuccessActivity.class);
                        startActivity(intent);
                    }
                    break;
                }
                case 10:
                    try {
                        String res = (String) msg.obj;
                        if (res == null) {
                            return;
                        } else {
                            JSONObject result = new JSONObject(res);
                            String success = result.getString("Success");
                            String err = result.getString("ErrMsg");
                            if (success == "true") {
                                userInfo.setUserInfo(AUTH, true);
                                Intent intent = new Intent(PayMonActivity.this, BeaterAuthSuccessActivity.class);
                                startActivity(intent);
                            } else if (success == "false") {
                                Toast.makeText(PayMonActivity.this, err, Toast.LENGTH_LONG).show();
                            }
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    try {
                        String res = (String) msg.obj;
                        JSONObject obj = new JSONObject(res);
                        String success = obj.getString("Success");
                        String err = obj.getString("ErrMsg");
                        String money = obj.getString("Data");
                        AccountMoney.money = money;
                        account_money.setText("￥" + money);
                        if (success == "true") {
                            // Toast.makeText(PayMonActivity.this,success,Toast.LENGTH_LONG).show();
                        } else if (success == "false") {
                            Toast.makeText(PayMonActivity.this, err, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                case WECHAT_COMMON_ORDER_API: {
                    String res = (String) msg.obj;
                    WechatPay.payByWechat(res,2,getApplicationContext(),PayMonActivity.this);
/*                    if (res != null) {
                        String wxOrderInfo = res;
                        if (res.startsWith("\""))
                            wxOrderInfo = wxOrderInfo.substring(1, wxOrderInfo.length() - 1);
                        wxOrderInfo = wxOrderInfo.replace("\\", "");
                        WechatPay wechatPay = new Gson().fromJson( wxOrderInfo, WechatPay.class);
                        if (wechatPay.getPrepayid() != null) {
                            OrderInfo.WXPayType = 2;
                            wechatPay.pay(getApplicationContext());
                         } else {
                            Toast.makeText(PayMonActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(PayMonActivity.this, "获取支付授权失败", Toast.LENGTH_SHORT).show();
                    }*/
                    break;
                }
            }

        }

    };
}
