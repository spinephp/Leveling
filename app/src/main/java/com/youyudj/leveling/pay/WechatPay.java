package com.youyudj.leveling.pay;

import android.content.Context;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WechatPay {
    public static final String MECHANT_ID = "1500046952";
    public static final String APP_ID = "wx2b71a85ff6be04b2";
    public static final String SUCCESS = "SUCCESS";
    public static final String FAIL = "FAIL";
    private String return_code;
    private String return_message;
    private String appid;
    private String mch_id;
    private String device_info;
    private String nonce_str;
    private String sign;
    private String result_code;
    private String err_code;
    private String err_code_des;
    private String trade_type;
    private String prepay_id;
    private String time_stamp;
    private final String package_value = "Sign=WXPay";

    public void pay(Context context) {
        if (return_code.equals(SUCCESS) && result_code.equals(SUCCESS)) {
            IWXAPI msgApi = WXAPIFactory.createWXAPI(context, null);
            msgApi.registerApp(appid);

            PayReq req = new PayReq();
            req.appId = appid;
            req.partnerId = mch_id;
            req.prepayId = prepay_id;
            req.nonceStr = nonce_str;
            req.timeStamp = time_stamp;
            req.packageValue = package_value;
            req.sign = sign;
            msgApi.sendReq(req);
        }
    }

    public String getReturn_code() {
        return return_code;
    }

    public String getResult_code() {
        return result_code;
    }
}
