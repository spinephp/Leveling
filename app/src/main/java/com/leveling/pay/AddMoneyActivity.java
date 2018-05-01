package com.leveling.pay;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.util.H5PayResultModel;
import com.leveling.R;
import com.leveling.alipay.AuthResult;
import com.leveling.alipay.PayResult;
import com.leveling.alipay.util.OrderInfoUtil2_0;
import com.leveling.chat.utils.FileSaveUtil;
import com.leveling.utils.HttpGetUtils;
import com.leveling.utils.HttpPostUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

public class AddMoneyActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int SDK_PAY_FLAG = 1;

    private TextView get_pay_ordermoney_new, pay_by_wechat, pay_by_alipay, pay_GetPayMoney_new;
    private LinearLayout GetPayMoney_btn_new, img_shouyintai_back;
    private String money, type;
    String res;
    String orderInfo;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        String url = "/api/Pay/AlipayCheckResult";
                        JSONObject json = new JSONObject();
                        try {
                            json.put("ResponseText", resultInfo);
                        } catch (JSONException e) {
                        }
                        HttpPostUtils.httpPostFile(2, url, json, handler);
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        setResult(0);
                        AddMoneyActivity.this.finish();
                    }
                    break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_money);
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        money = intent.getStringExtra("money");
        img_shouyintai_back = (LinearLayout) findViewById(R.id.img_shouyintai_back);
        get_pay_ordermoney_new = (TextView) findViewById(R.id.get_pay_ordermoney_new);
        get_pay_ordermoney_new.setText(Float.parseFloat(money) + "");
        pay_by_wechat = (TextView) findViewById(R.id.pay_by_wechat);
        pay_by_alipay = (TextView) findViewById(R.id.pay_by_alipay);
        pay_GetPayMoney_new = (TextView) findViewById(R.id.pay_GetPayMoney_new);
        pay_GetPayMoney_new.setText("￥" + Float.parseFloat(money));
        GetPayMoney_btn_new = (LinearLayout) findViewById(R.id.GetPayMoney_btn_new);
        img_shouyintai_back.setOnClickListener(this);
        pay_by_wechat.setOnClickListener(this);
        pay_by_alipay.setOnClickListener(this);
        GetPayMoney_btn_new.setOnClickListener(this);
        String url = null;
        if(type.equals("Recharge"))
            url = "/api/Pay/Recharge?money=" + money;
        else if(type.equals("RechargeCashPledge"))
            url = "/api/Pay/RechargeCashPledge?money=" + money;
        else if(type.equals("RechargeCashPledge2"))
            url = "/api/Pay/RechargeCashPledge2?money=" + money;
        HttpGetUtils.httpGetFile(1, url, handler);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    res = (String) msg.obj;
                    if (res != null) {
                        orderInfo = res;
                        if (res.startsWith("\""))
                            orderInfo = orderInfo.substring(1, orderInfo.length() - 1);
                        return;
                    } else {
                        Toast.makeText(AddMoneyActivity.this, "获取支付授权失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2: {
                    res = (String) msg.obj;
                    if(res == null || res.length() == 0)
                        setResult(0);
                    else {
                        Intent intent = new Intent();
                        intent.putExtra("balance", res);
                        setResult(1, intent);
                    }
                    AddMoneyActivity.this.finish();
                    break;
                }
            }
        }
    };

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.img_shouyintai_back:
                finish();
                break;
            case R.id.pay_by_alipay:
                if (TextUtils.isEmpty(orderInfo)) {
                    Toast.makeText(this, "获取支付授权失败", Toast.LENGTH_SHORT).show();
                    return;
                }
//                /**
//                 * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
//                 * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
//                 * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
//                 *
//                 * orderInfo的获取必须来自服务端；
//                 */
//                Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, true);
//                String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
//                System.out.println(orderParam);
//                String sign = OrderInfoUtil2_0.getSign(params, RSA2_PRIVATE, true);
//                System.out.println(sign);
//                final String orderInfo1 = orderParam + "&" + sign;
//                //final String orderInfo = "biz_content=%7B%22timeout_express%22%3A%2230m%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22total_amount%22%3A%2210.01%22%2C%22subject%22%3A%22%E5%85%85%E5%80%BC%22%2C%22body%22%3A%22%E5%85%85%E5%80%BC%22%2C%22out_trade_no%22%3A%22012345678901234%22%7D&method=alipay.trade.app.pay&charset=utf-8&version=1.0&app_id=2016091100488662&timestamp=2016-07-29+16%3A55%3A53&sign_type=RSA2&sign=ISe6ZxuOjHAupwICl0TmUukKmy5xB1S%2BShcX9ptvl2gGiHvPRtva3GH2Po4BX0%2FmxF3YCBhepTVGJPmyXYkj4US%2B25JjOHMi5Kd3pZ%2FT%2F%2Fj8Jx09EfsgKDQD3scYjYzhsfBIRh8jCvEEyggu60kLLZ8IRkMxG5coxh6MCBIcN9gRw1tKv7tUmmRdm3IW5b7dx6cdIC2HD3xxyzFn0cE9fUpYlExb8Fg3vDLL8nIMuJgCXpcptx05nTFCVcKwhcTlQerArF8EQx%2BGES02ml9xjOAQqz6HtMxa%2BnuGJMTfjf16drs7jaE3Rm8eeFfrW2yWpdV3VRQROupKCxX%2F1%2FKsmA";
//                //System.out.println(orderInfo);
//                System.out.println(orderInfo1);
                Runnable payRunnable = new Runnable() {
                    @Override
                    public void run() {
                        // 这里是支付的代码，就两行，orderInfo是后台传过来的
                        PayTask alipay = new PayTask(AddMoneyActivity.this);
                        Map<String, String> result = alipay.payV2(orderInfo, true);
                        Log.i("msp", result.toString());
                        Message msg = new Message();
                        msg.what = SDK_PAY_FLAG;
                        msg.obj = result;
                        mHandler.sendMessage(msg);
                    }
                };
                new Thread(payRunnable).start();
                break;
            case R.id.pay_by_wechat:
                break;
            case R.id.GetPayMoney_btn_new:
                break;
            default:
                break;
        }
    }
}
