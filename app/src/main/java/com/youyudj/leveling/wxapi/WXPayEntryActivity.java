package com.youyudj.leveling.wxapi;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.youyudj.leveling.BeaterAuthSuccessActivity;
import com.youyudj.leveling.MainActivity;
import com.youyudj.leveling.ReleaseOrderFailedActivity;
import com.youyudj.leveling.ReleaseOrderSuccessActivity;
import com.youyudj.leveling.ZiJinGuanLiActivity;
import com.youyudj.leveling.entity.OrderInfo;
import com.youyudj.leveling.pay.AddMoneyActivity;
import com.youyudj.leveling.pay.WechatPay;
import com.youyudj.leveling.utils.HttpGetUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private final int VALIDATE_PAYMENT = 6;
    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        api = WXAPIFactory.createWXAPI(this, WechatPay.APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.errCode == 0) {
            // 支付成功, 发送请求到服务器端验证
            switch(OrderInfo.WXPayType) {
                case 1: {// AddMoney
                    //String url = "/api/Pay/GetPayMoney";
                    //HttpGetUtils.httpGetFile(3, url, handler);
                    //Intent intent = new Intent(WXPayEntryActivity.this, MainActivity.class);
                    //startActivity(intent);
                    //Intent intent = new Intent();
                    //this.setResult(1,intent);
                    break;
                }
                case 2: {//PayMon
                    Intent intent = new Intent(WXPayEntryActivity.this, BeaterAuthSuccessActivity.class);
                    startActivity(intent);
                    break;
                }
                case 3: {///PayOrder
                    Intent intent = new Intent(WXPayEntryActivity.this, ReleaseOrderSuccessActivity.class);
                    startActivity(intent);
                    break;
                }
                case 4: {//ToPayCashPledge
                    Intent intent = new Intent(WXPayEntryActivity.this, BeaterAuthSuccessActivity.class);
                    startActivity(intent);
                    break;
                }
                case 5: {//ToPayCashPledge2
                    Intent intent = new Intent(WXPayEntryActivity.this, BeaterAuthSuccessActivity.class);
                    startActivity(intent);
                    break;
                }
            }
            /*
            startActivity(intent);
            String url = "api/Pay/WXPaySuccess";
            JSONObject json = new JSONObject();
            try {
                json.put("TradeNo", OrderInfo.order_id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            HttpPostUtils.httpPostFile(VALIDATE_PAYMENT, url, json, handler);
            */
            this.finish();
            Toast.makeText(WXPayEntryActivity.this, "微信支付成功！", Toast.LENGTH_SHORT).show();
            return;
        } else if (resp.errCode == -1) {
            // 支付失败
            Toast.makeText(WXPayEntryActivity.this, "微信支付失败！", Toast.LENGTH_SHORT).show();
        } else if (resp.errCode == -2) {
            // 用户取消支付
            Toast.makeText(WXPayEntryActivity.this, "微信支付取消！", Toast.LENGTH_SHORT).show();
        } else {
            // 其他错误
            Toast.makeText(WXPayEntryActivity.this, "微信支付错误！", Toast.LENGTH_SHORT).show();
        }
        //Intent intent = new Intent(this, ReleaseOrderFailedActivity.class);
        //startActivity(intent);
        this.finish();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case VALIDATE_PAYMENT: {
                    String res = (String) msg.obj;
                    if (res == null) {
                        return;
                    }
                    String resultCode = "";
                    try {
                        JSONObject result = new JSONObject(res);
                        resultCode = result.getString("resultCode");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (resultCode.equals(WechatPay.SUCCESS)) {
                        // 验证成功
                        Intent intent = new Intent(WXPayEntryActivity.this, ReleaseOrderSuccessActivity.class);
                        startActivity(intent);
                    } else {
                        // 验证失败
                        Intent intent = new Intent(WXPayEntryActivity.this, ReleaseOrderFailedActivity.class);
                        startActivity(intent);
                    }
                    finish();
                    break;
                }
                case 3:{
                    String res = (String) msg.obj;
                    JSONObject obj = null;
                    try {
                        obj = new JSONObject(res);
                        String success = obj.getString("Success");
                        if (success == "true") {
                            String money = obj.getString("Data");
                            Intent intent = new Intent(WXPayEntryActivity.this, AddMoneyActivity.class);
                            intent.putExtra("balance", money);
                            startActivity(intent);
                            //setResult(1, intent);
                        }else{
                            Intent intent = new Intent(WXPayEntryActivity.this, AddMoneyActivity.class);
                            startActivity(intent);
                            //setResult(0);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    finish();
                    break;
                }
            }

        }
    };
}
