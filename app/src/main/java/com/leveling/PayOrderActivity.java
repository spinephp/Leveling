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
import com.youyudj.leveling.alipay.PayResult;
import com.youyudj.leveling.entity.AccountMoney;
import com.youyudj.leveling.entity.OrderInfo;
import com.youyudj.leveling.pay.AddMoneyActivity;
import com.youyudj.leveling.utils.HttpGetUtils;
import com.youyudj.leveling.utils.HttpPostUtils;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

public class PayOrderActivity extends AppCompatActivity implements PayPwdView.InputCallBack {
    private TextView orderid;
    private TextView ordermoney;
    private LinearLayout GetPayMoney;
    private String pay;
    private LinearLayout img_shouyintai_back;
    private TextView account_money;
    public static String test;
    private String orderInfo = null;
    private String payMethod = null;
    final String PM_YuE = "YuE";
    final String PM_Wechat = "Wechat";
    final String PM_AliPay = "AliPay";

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
        setContentView(R.layout.pay_order);
        img_shouyintai_back = (LinearLayout) findViewById(R.id.img_shouyintai_back);
        account_money = (TextView) findViewById(R.id.account_money);
        img_shouyintai_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        orderid = (TextView) findViewById(R.id.get_pay_ordermoney);
        ordermoney = (TextView) findViewById(R.id.pay_GetPayMoney);
        orderid.setText(OrderInfo.pay_money);
        ordermoney.setText("￥" + OrderInfo.pay_money);
        GetPayMoney = (LinearLayout) findViewById(R.id.GetPayMoney_btn);
        findViewById(R.id.bdr_yu_e).setOnClickListener(new BorderClick());
        findViewById(R.id.bdr_weixin).setOnClickListener(new BorderClick());
        findViewById(R.id.bdr_alipay).setOnClickListener(new BorderClick());
        GetPayMoney.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (payMethod == null) {
                    Toast.makeText(PayOrderActivity.this, "请选择支付方式", Toast.LENGTH_LONG).show();
                    return;
                }
                if (payMethod.equals(PM_YuE)) {
                    Bundle bundle = new Bundle();
                    bundle.putString(PayFragment.EXTRA_CONTENT, "支付：¥ " + OrderInfo.pay_money);
                    PayFragment fragment = new PayFragment();
                    fragment.setArguments(bundle);
                    fragment.setPaySuccessCallBack(PayOrderActivity.this);
                    fragment.show(getSupportFragmentManager(), "Pay");
                } else if (payMethod.equals(PM_AliPay)) {
                    if (TextUtils.isEmpty(orderInfo)) {
                        Toast.makeText(PayOrderActivity.this, "获取支付授权失败", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Runnable payRunnable = new Runnable() {
                        @Override
                        public void run() {
                            PayTask alipay = new PayTask(PayOrderActivity.this);
                            Map<String, String> result = alipay.payV2(orderInfo, true);
                            Log.i("msp", result.toString());
                            Message msg = new Message();
                            msg.what = 2;
                            msg.obj = result;
                            handler.sendMessage(msg);
                        }
                    };
                    new Thread(payRunnable).start();
                }else if(payMethod.equals(PM_Wechat)){
                    Toast.makeText(PayOrderActivity.this, "微信支付尚未开通，请选择其他支付方式", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        });
        //String urlUploadFile = "/api/Pay/Recharge?money=" + "10.00";
        //String urlUploadFile = "http://192.168.1.102:8082/api/Pay/PayForOrder?orderid=" + OrderInfo.order_id;
        String url = "/api/Pay/PayForOrder?orderid=" + OrderInfo.order_id;
        HttpGetUtils.httpGetFile(1, url, handler);
        url = "/api/Pay/GetPayMoney";
        HttpGetUtils.httpGetFile(18, url, handler);
    }

    public void onInputFinish(String result) {
        pay = result;
        //Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        String url = "/api/Order/GetPayMoney?orderid=" + OrderInfo.order_id + "&OrderMoney=" + OrderInfo.pay_money + "&payPW=" + pay;
        HttpGetUtils.httpGetFile(14, url, handler);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 1: {
                    String res = (String) msg.obj;
                    if (res != null) {
                        orderInfo = res;
                        if (res.startsWith("\""))
                            orderInfo = orderInfo.substring(1, orderInfo.length() - 1);
                        return;
                    } else {
                        Toast.makeText(PayOrderActivity.this, "获取支付宝支付信息失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                case 2: {
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
                        HttpPostUtils.httpPostFile(3, url, json, handler);
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        //Toast.makeText(PayOrderActivity.this, "充值失败", Toast.LENGTH_SHORT).show();
                        //PayOrderActivity.this.finish();
                    }
                    break;
                }
                case 3: {
                    String res = (String) msg.obj;
                    if (res == null || res.length() == 0) {
                        Toast.makeText(PayOrderActivity.this, "充值失败", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(PayOrderActivity.this, ReleaseOrderSuccessActivity.class);
                        startActivity(intent);
                    }
                    PayOrderActivity.this.finish();
                    break;
                }
                case 14:
                    try {
                        String res = (String) msg.obj;
                        if (res == null) {
                            return;
                        }
                        JSONObject result = new JSONObject(res);
                        String success = result.getString("Success");
                        String err = result.getString("ErrMsg");
                        if (success == "true") {
                            // Toast.makeText(PayOrderActivity.this, success, Toast.LENGTH_LONG).show();
                            test = "1";
                            Intent intent = new Intent(PayOrderActivity.this, ReleaseOrderSuccessActivity.class);
                            startActivity(intent);
                            finish();
                        } else if (success == "false") {
                            if (err.equals("支付密码错误")) {
                                Toast.makeText(PayOrderActivity.this, err, Toast.LENGTH_LONG).show();
                            } else {
                                test = "2";
                                Intent intent = new Intent(PayOrderActivity.this, ReleaseOrderFailedActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                case 18:
                    try {
                        String res = (String) msg.obj;
                        if (res == null) {
                            return;
                        }
                        JSONObject obj = new JSONObject(res);
                        String success = obj.getString("Success");
                        String err = obj.getString("ErrMsg");
                        if (success == "true") {
                            String bla = obj.getString("Data");
                            double dblBla = Double.parseDouble(bla);
                            double dblPay = Double.parseDouble(OrderInfo.pay_money);
                            if(dblBla < dblPay)
                                findViewById(R.id.tv_no_enough_balance).setVisibility(View.VISIBLE);
                            else
                                findViewById(R.id.tv_no_enough_balance).setVisibility(View.INVISIBLE);
                            account_money.setText(bla);
                        } else if (success == "false") {
                            Toast.makeText(PayOrderActivity.this, err, Toast.LENGTH_LONG).show();
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

