package com.youyudj.leveling.entity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lin on 2018/4/7.
 */

public class MessageItemBean {

    private String nickname;
    private String Picture;
    private String OrderID;
    private String UserID;
    private int type;
    private String content;


    public String getNickname() {
        return nickname;
    }

    public String getPicture() {
        return Picture;
    }

    public String getUserID() {
        return UserID;
    }

    public String getOrderID()
    {
        return OrderID;
    }

    public int getType()
    {
        return type;
    }

    public String getContent()
    {
        return content;
    }


    public void setNickname(String name)
    {
        this.nickname = name;
    }

    public void setPicture(String s)
    {
        this.Picture = s;
    }

    public void setUserID(String id)
    {
        this.UserID = id;
    }

    public void setOrderID(String m)
    {
        this.OrderID = m;
    }

    public void setType(int t)
    {
        this.type = t;
    }

    public void setContent(String c)
    {
        this.content = c;
    }


    public MessageItemBean(JSONObject object)
    {
        try {
            setNickname(object.getString("NickName"));
            setOrderID(object.getString("OrderID"));
            setPicture(object.getString("Picture"));
            setUserID(object.getString("UserID"));
            setType(object.getInt("Type"));
            setContent(object.getString("Content"));
        }catch (JSONException e)
        {
            e.printStackTrace();
        }
    }




}
