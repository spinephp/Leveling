package com.leveling.new_chat;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lin on 2018/3/28.
 */

public class UserInfo {
    private String UserID;
    private String Number;
    private String Name;
    private String IDNumber;
    private int Gender;
    private String Phone;
    private String Password;
    private int Role;
    private String BigRankMax1;
    private String BigRankMax2;
    private String Wechat;
    private String NickName;
    private String Picture;
    private String InvitationCode;
    private String VIP;
    private String PayPW;
    private String CardType;
    private String CardNumber;
    private String Birthday;
    private int OrderCount;
    private int CreditSum;
    private String Credit;
    private String RegisterTime;
    private int IsEnabled;
    private String WeChatNickName;
    private String AlipayName;
    private String Alipay;
    private String CardUserName;
    private String AuthenticationTime;

    /**
     * 字段太多，暂时只挑有用的
     * */

    public String getUserID()
    {
        return UserID;
    }

    public String getPicture()
    {
        return Picture;
    }

    public void setUserID(String id)
    {
        this.UserID = id;
    }

    public void setPicture(String pic)
    {
        this.Picture = pic;
    }

    public UserInfo(JSONObject object)
    {
        try {
            setUserID(object.getString("UserID"));
            setPicture(object.getString("Picture"));
        }catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
