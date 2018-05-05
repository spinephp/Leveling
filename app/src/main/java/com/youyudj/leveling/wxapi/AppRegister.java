package com.youyudj.leveling.wxapi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.youyudj.leveling.pay.WechatPay;

public class AppRegister extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        final IWXAPI msgApi = WXAPIFactory.createWXAPI(context, null);
        msgApi.registerApp(WechatPay.APP_ID);
    }
}
