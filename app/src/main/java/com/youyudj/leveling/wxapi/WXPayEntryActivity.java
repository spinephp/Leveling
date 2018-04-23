package com.youyudj.leveling.wxapi;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.youyudj.leveling.ReleaseOrderFailedActivity;
import com.youyudj.leveling.ReleaseOrderSuccessActivity;
import com.youyudj.leveling.pay.WechatPay;
import com.youyudj.leveling.utils.HttpPostUtils;

import org.json.JSONObject;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private final int VALIDATE_PAYMENT = 6;
    private IWXAPI api;
    private PayReq req;

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
        this.req = (PayReq)req;
    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.errCode == 0) {
            // 支付成功, 发送请求到服务器端验证
            String url = "";
            JSONObject json = new JSONObject();
            HttpPostUtils.httpPostFile(VALIDATE_PAYMENT, url, json, handler);
            return;
        } else if (resp.errCode == -1) {
            // 支付失败
        } else if (resp.errCode == -2) {
            // 用户取消支付
        } else {
            // 其他错误
        }
        Intent intent = new Intent(this, ReleaseOrderFailedActivity.class);
        startActivity(intent);
        finish();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case VALIDATE_PAYMENT: {
                    // 验证成功
                    Intent intent = new Intent(WXPayEntryActivity.this, ReleaseOrderSuccessActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                }
            }

        }
    };
}
