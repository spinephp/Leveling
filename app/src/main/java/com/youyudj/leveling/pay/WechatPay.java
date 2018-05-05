package com.youyudj.leveling.pay;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.youyudj.leveling.entity.OrderInfo;

import java.util.HashMap;
import java.util.Map;

public class WechatPay {
    public static final String MECHANT_ID = "1500046952";
    public static final String APP_ID = "wx2b71a85ff6be04b2";
    public static final String SUCCESS = "SUCCESS";
    public static final String FAIL = "FAIL";
    private String appid;
    private String partnerid;
    private String noncestr;
    private String sign;
    private String trade_type;
    private String prepayid;
    private String timestamp;
    private final String package_value = "Sign=WXPay";

    public void pay(Context context) {
        final IWXAPI msgApi = WXAPIFactory.createWXAPI(context, null);
        msgApi.registerApp(appid);

        PayReq req = new PayReq();
        req.appId = appid;
        req.partnerId = partnerid;
        req.prepayId = prepayid;
        req.nonceStr = noncestr;
        req.timeStamp = timestamp;
        req.packageValue = package_value;
        req.sign = sign;
        msgApi.sendReq(req);
    }

    public static void payByWechat(String res,int type,Context context,Context activityContext) {
        try {
            if (res != null) {
                String wxOrderInfo = res;
                if (res.startsWith("\""))
                    wxOrderInfo = wxOrderInfo.substring(1, wxOrderInfo.length() - 1);
                wxOrderInfo = wxOrderInfo.replace("\\", "");
                WechatPay wechatPay = new Gson().fromJson(wxOrderInfo, WechatPay.class);
                if (wechatPay.getPrepayid() != null) {
                    OrderInfo.WXPayType = type;
                    wechatPay.pay(context);
                } else {
                    Toast.makeText(activityContext, "支付失败", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getAppid() {
        return appid;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public String getSign() {
        return sign;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getPackage_value() {
        return package_value;
    }
}
