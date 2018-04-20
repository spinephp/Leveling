package com.youyudj.leveling.pay;

/**
 * Created by myipp on 2018/3/18.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

/**
 * 支付宝支付
 *
 * @author super star boy
 */
public class Alipay {
    // 商户PID
    public static final String PARTNER = "123456789";
    // 商户收款账号
    public static final String SELLER = "qibin0506@gmail.com";
    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "rsa_private";
    // 支付宝公钥
    public static final String RSA_PUBLIC = "rsa_public";
    private static final int SDK_PAY_FLAG = 1;
    private WeakReference<Activity> mActivity;
    private OnAlipayListener mListener;
    public Alipay(Activity activity) {
        mActivity = new WeakReference<Activity>(activity);
    }
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == SDK_PAY_FLAG) {
                PayResult payResult = new PayResult((String) msg.obj);
                // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                String resultInfo = payResult.getResult();
                String resultStatus = payResult.getResultStatus();
                // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                if (TextUtils.equals(resultStatus, "9000")) {
                    if (mListener != null) mListener.onSuccess();
                } else {
                    // 判断resultStatus 为非“9000”则代表可能支付失败
                    // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，
                    // 最终交易是否成功以服务端异步通知为准（小概率状态）
                    if (TextUtils.equals(resultStatus, "8000")) {
                        if (mListener != null) mListener.onWait();
                    } else {
                        // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                        if (mListener != null) mListener.onCancel();
                    }
                }
            }
        }
    };
    /**
     * 支付
     *
     * @param title   标题 不能为空或者“”
     * @param desc  描述 不能为空或者“”
     * @param price 价格 不能为空或者“”
     * @param sn  商品唯一货号 不能为空或者“”
     * @param url 服务器回调url 不能为空或者“”
     */
    public void pay(String title, String desc, String price, String sn, String url) {
        // 订单
        String orderInfo = getOrderInfo(title, desc, price, sn, url);
        // 对订单做RSA 签名
        String sign = sign(orderInfo);
        try {
            // 仅需对sign 做URL编码
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 完整的符合支付宝参数规范的订单信息
        final String payInfo = orderInfo + "&sign=" + sign + "&"
                + getSignType();
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                Activity activity = mActivity.get();
                if(activity == null) return;
                // 构造PayTask 对象
                PayTask alipay = new PayTask(activity);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }
    /**
     * create the order info. 创建订单信息
     *
     */
    public String getOrderInfo(String subject, String body, String price,
                               String sn, String url) {
        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";
        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";
        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + sn + "\"";
        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";
        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";
        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";
        // 服务器异步通知页面路径
        orderInfo += "?ify_url=" + "\"" + url + "\"";
        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";
        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";
        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";
        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";
        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";
        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";
        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";
        return orderInfo;
    }
    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content
     * 待签名订单信息
     */
    public String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }
    /**
     * get the sign type we use. 获取签名方式
     *
     */
    public String getSignType() {
        return "sign_type=\"RSA\"";
    }
    public void setListener(OnAlipayListener l) {
        mListener = l;
    }
    /**
     * 支付回调接口
     *
     * @author lenovo
     *
     */
    public static class OnAlipayListener {
        /**
         * 支付成功
         */
        public void onSuccess() {}
        /**
         * 支付取消
         */
        public void onCancel() {}
        /**
         * 等待确认
         */
        public void onWait() {}
    }
}
final class Base64 {
    private static final int BASELENGTH = 128;
    private static final int LOOKUPLENGTH = 64;
    private static final int TWENTYFOURBITGROUP = 24;
    private static final int EIGHTBIT = 8;
    private static final int SIXTEENBIT = 16;
    private static final int FOURBYTE = 4;
    private static final int SIGN = -128;
    private static char PAD = '=';
    private static byte[] base64Alphabet = new byte[BASELENGTH];
    private static char[] lookUpBase64Alphabet = new char[LOOKUPLENGTH];
    static {
        for (int i = 0; i < BASELENGTH; ++i) {
            base64Alphabet[i] = -1;
        }
        for (int i = 'Z'; i >= 'A'; i--) {
            base64Alphabet[i] = (byte) (i - 'A');
        }
        for (int i = 'z'; i >= 'a'; i--) {
            base64Alphabet[i] = (byte) (i - 'a' + 26);
        }
        for (int i = '9'; i >= '0'; i--) {
            base64Alphabet[i] = (byte) (i - '0' + 52);
        }
        base64Alphabet['+'] = 62;
        base64Alphabet['/'] = 63;
        for (int i = 0; i <= 25; i++) {
            lookUpBase64Alphabet[i] = (char) ('A' + i);
        }
        for (int i = 26, j = 0; i <= 51; i++, j++) {
            lookUpBase64Alphabet[i] = (char) ('a' + j);
        }
        for (int i = 52, j = 0; i <= 61; i++, j++) {
            lookUpBase64Alphabet[i] = (char) ('0' + j);
        }
        lookUpBase64Alphabet[62] = (char) '+';
        lookUpBase64Alphabet[63] = (char) '/';
    }
    private static boolean isWhiteSpace(char octect) {
        return (octect == 0x20 || octect == 0xd || octect == 0xa || octect == 0x9);
    }
    private static boolean isPad(char octect) {
        return (octect == PAD);
    }
    private static boolean isData(char octect) {
        return (octect < BASELENGTH && base64Alphabet[octect] != -1);
    }
    /**
     * Encodes hex octects into Base64
     *
     * @param binaryData
     * Array containing binaryData
     * @return Encoded Base64 array
     */
    public static String encode(byte[] binaryData) {
        if (binaryData == null) {
            return null;
        }
        int lengthDataBits = binaryData.length * EIGHTBIT;
        if (lengthDataBits == 0) {
            return "";
        }
        int fewerThan24bits = lengthDataBits % TWENTYFOURBITGROUP;
        int numberTriplets = lengthDataBits / TWENTYFOURBITGROUP;
        int numberQuartet = fewerThan24bits != 0 ? numberTriplets + 1
                : numberTriplets;
        char encodedData[] = null;
        encodedData = new char[numberQuartet * 4];
        byte k = 0, l = 0, b1 = 0, b2 = 0, b3 = 0;
        int encodedIndex = 0;
        int dataIndex = 0;
        for (int i = 0; i < numberTriplets; i++) {
            b1 = binaryData[dataIndex++];
            b2 = binaryData[dataIndex++];
            b3 = binaryData[dataIndex++];
            l = (byte) (b2 & 0x0f);
            k = (byte) (b1 & 0x03);
            byte val1 = ((b1 & SIGN) == 0) ? (byte) (b1 >> 2)
                    : (byte) ((b1) >> 2 ^ 0xc0);
            byte val2 = ((b2 & SIGN) == 0) ? (byte) (b2 >> 4)
                    : (byte) ((b2) >> 4 ^ 0xf0);
            byte val3 = ((b3 & SIGN) == 0) ? (byte) (b3 >> 6)
                    : (byte) ((b3) >> 6 ^ 0xfc);
            encodedData[encodedIndex++] = lookUpBase64Alphabet[val1];
            encodedData[encodedIndex++] = lookUpBase64Alphabet[val2 | (k << 4)];
            encodedData[encodedIndex++] = lookUpBase64Alphabet[(l << 2) | val3];
            encodedData[encodedIndex++] = lookUpBase64Alphabet[b3 & 0x3f];
        }
        // form integral number of 6-bit groups
        if (fewerThan24bits == EIGHTBIT) {
            b1 = binaryData[dataIndex];
            k = (byte) (b1 & 0x03);
            byte val1 = ((b1 & SIGN) == 0) ? (byte) (b1 >> 2)
                    : (byte) ((b1) >> 2 ^ 0xc0);
            encodedData[encodedIndex++] = lookUpBase64Alphabet[val1];
            encodedData[encodedIndex++] = lookUpBase64Alphabet[k << 4];
            encodedData[encodedIndex++] = PAD;
            encodedData[encodedIndex++] = PAD;
        } else if (fewerThan24bits == SIXTEENBIT) {
            b1 = binaryData[dataIndex];
            b2 = binaryData[dataIndex + 1];
            l = (byte) (b2 & 0x0f);
            k = (byte) (b1 & 0x03);
            byte val1 = ((b1 & SIGN) == 0) ? (byte) (b1 >> 2)
                    : (byte) ((b1) >> 2 ^ 0xc0);
            byte val2 = ((b2 & SIGN) == 0) ? (byte) (b2 >> 4)
                    : (byte) ((b2) >> 4 ^ 0xf0);
            encodedData[encodedIndex++] = lookUpBase64Alphabet[val1];
            encodedData[encodedIndex++] = lookUpBase64Alphabet[val2 | (k << 4)];
            encodedData[encodedIndex++] = lookUpBase64Alphabet[l << 2];
            encodedData[encodedIndex++] = PAD;
        }
        return new String(encodedData);
    }
    /**
     * Decodes Base64 data into octects
     *
     * @param encoded
     * string containing Base64 data
     * @return Array containind decoded data.
     */
    public static byte[] decode(String encoded) {
        if (encoded == null) {
            return null;
        }
        char[] base64Data = encoded.toCharArray();
        // remove white spaces
        int len = removeWhiteSpace(base64Data);
        if (len % FOURBYTE != 0) {
            return null;// should be divisible by four
        }
        int numberQuadruple = (len / FOURBYTE);
        if (numberQuadruple == 0) {
            return new byte[0];
        }
        byte decodedData[] = null;
        byte b1 = 0, b2 = 0, b3 = 0, b4 = 0;
        char d1 = 0, d2 = 0, d3 = 0, d4 = 0;
        int i = 0;
        int encodedIndex = 0;
        int dataIndex = 0;
        decodedData = new byte[(numberQuadruple) * 3];
        for (; i < numberQuadruple - 1; i++) {
            if (!isData((d1 = base64Data[dataIndex++]))
                    || !isData((d2 = base64Data[dataIndex++]))
                    || !isData((d3 = base64Data[dataIndex++]))
                    || !isData((d4 = base64Data[dataIndex++]))) {
                return null;
            }// if found "no data" just return null
            b1 = base64Alphabet[d1];
            b2 = base64Alphabet[d2];
            b3 = base64Alphabet[d3];
            b4 = base64Alphabet[d4];
            decodedData[encodedIndex++] = (byte) (b1 << 2 | b2 >> 4);
            decodedData[encodedIndex++] = (byte) (((b2 & 0xf) << 4) | ((b3 >> 2) & 0xf));
            decodedData[encodedIndex++] = (byte) (b3 << 6 | b4);
        }
        if (!isData((d1 = base64Data[dataIndex++]))
                || !isData((d2 = base64Data[dataIndex++]))) {
            return null;// if found "no data" just return null
        }
        b1 = base64Alphabet[d1];
        b2 = base64Alphabet[d2];
        d3 = base64Data[dataIndex++];
        d4 = base64Data[dataIndex++];
        if (!isData((d3)) || !isData((d4))) {// Check if they are PAD characters
            if (isPad(d3) && isPad(d4)) {
                if ((b2 & 0xf) != 0)// last 4 bits should be zero
                {
                    return null;
                }
                byte[] tmp = new byte[i * 3 + 1];
                System.arraycopy(decodedData, 0, tmp, 0, i * 3);
                tmp[encodedIndex] = (byte) (b1 << 2 | b2 >> 4);
                return tmp;
            } else if (!isPad(d3) && isPad(d4)) {
                b3 = base64Alphabet[d3];
                if ((b3 & 0x3) != 0)// last 2 bits should be zero
                {
                    return null;
                }
                byte[] tmp = new byte[i * 3 + 2];
                System.arraycopy(decodedData, 0, tmp, 0, i * 3);
                tmp[encodedIndex++] = (byte) (b1 << 2 | b2 >> 4);
                tmp[encodedIndex] = (byte) (((b2 & 0xf) << 4) | ((b3 >> 2) & 0xf));
                return tmp;
            } else {
                return null;
            }
        } else { // No PAD e.g 3cQl
            b3 = base64Alphabet[d3];
            b4 = base64Alphabet[d4];
            decodedData[encodedIndex++] = (byte) (b1 << 2 | b2 >> 4);
            decodedData[encodedIndex++] = (byte) (((b2 & 0xf) << 4) | ((b3 >> 2) & 0xf));
            decodedData[encodedIndex++] = (byte) (b3 << 6 | b4);
        }
        return decodedData;
    }
    /**
     * remove WhiteSpace from MIME containing encoded Base64 data.
     *
     * @param data
     * the byte array of base64 data (with WS)
     * @return the new length
     */
    private static int removeWhiteSpace(char[] data) {
        if (data == null) {
            return 0;
        }
        // count characters that's not whitespace
        int newSize = 0;
        int len = data.length;
        for (int i = 0; i < len; i++) {
            if (!isWhiteSpace(data[i])) {
                data[newSize++] = data[i];
            }
        }
        return newSize;
    }
}
class PayResult {
    private String resultStatus;
    private String result;
    private String memo;
    public PayResult(String rawResult) {
        if (TextUtils.isEmpty(rawResult))
            return;
        String[] resultParams = rawResult.split(";");
        for (String resultParam : resultParams) {
            if (resultParam.startsWith("resultStatus")) {
                resultStatus = gatValue(resultParam, "resultStatus");
            }
            if (resultParam.startsWith("result")) {
                result = gatValue(resultParam, "result");
            }
            if (resultParam.startsWith("memo")) {
                memo = gatValue(resultParam, "memo");
            }
        }
    }
    @Override
    public String toString() {
        return "resultStatus={" + resultStatus + "};memo={" + memo
                + "};result={" + result + "}";
    }
    private String gatValue(String content, String key) {
        String prefix = key + "={";
        return content.substring(content.indexOf(prefix) + prefix.length(),
                content.lastIndexOf("}"));
    }
    /**
     * @return the resultStatus
     */
    public String getResultStatus() {
        return resultStatus;
    }
    /**
     * @return the memo
     */
    public String getMemo() {
        return memo;
    }
    /**
     * @return the result
     */
    public String getResult() {
        return result;
    }
}
class SignUtils {
    private static final String ALGORITHM = "RSA";
    private static final String SIGN_ALGORITHMS = "SHA1WithRSA";
    private static final String DEFAULT_CHARSET = "UTF-8";
    public static String sign(String content, String privateKey) {
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
                    Base64.decode(privateKey));
            KeyFactory keyf = KeyFactory.getInstance(ALGORITHM);
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);
            java.security.Signature signature = java.security.Signature
                    .getInstance(SIGN_ALGORITHMS);
            signature.initSign(priKey);
            signature.update(content.getBytes(DEFAULT_CHARSET));
            byte[] signed = signature.sign();
            return Base64.encode(signed);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}