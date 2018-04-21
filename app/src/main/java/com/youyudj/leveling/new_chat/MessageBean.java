package com.youyudj.leveling.new_chat;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lin on 2018/3/28.
 */

public class MessageBean {
    private String ID;
    private String FromUserID;
    private String FromUserName;
    private String ToUserID;
    private String ToUserName;
    private String SendTime;
    private String ReceiveTime;
    private int Type;
    private String MessageContent;
    private String OrderID;

    public String getID() {
        return ID;
    }

    public String getFromUserID() {
        return FromUserID;
    }

    public String getFromUserName() {
        return FromUserName;
    }

    public String getToUserID() {
        return ToUserID;
    }

    public String getToUserName()
    {
        return ToUserName;
    }

    public String getSendTime()
    {
        return SendTime;
    }

    public String getReceiveTime()
    {
        return ReceiveTime;
    }

    public String getMessageContent()
    {
        return MessageContent;
    }

    public String getOrderID()
    {
        return OrderID;
    }

    public int getType()
    {
        return Type;
    }

    public void setID(String id)
    {
        this.ID = id;
    }

    public void setFromUserID(String id)
    {
        this.FromUserID = id;
    }

    public void setFromUserName(String name)
    {
        this.FromUserName = name;
    }

    public void setToUserID(String id)
    {
        this.ToUserID = id;
    }

    public void setToUserName(String name)
    {
        this.ToUserName = name;
    }

    public void setSendTime(String time)
    {
        this.SendTime = time;
    }

    public void setReceiveTime(String time)
    {
        this.ReceiveTime = time;
    }

    public void setType(int type)
    {
        this.Type = type;
    }

    public void setMessageContent(String content)
    {
        this.MessageContent = content;
    }

    public void setOrderID(String id)
    {
        this.OrderID = id;
    }


    public MessageBean(JSONObject object)
    {
        try{
            setID(object.getString("ID"));
            setFromUserID(object.getString("FromUserID"));
            setFromUserName(object.getString("FromUserName"));
            setToUserID(object.getString("ToUserID"));
            setToUserName(object.getString("ToUserName"));
            setSendTime(object.getString("SendTime"));
            setReceiveTime(object.getString("ReceiveTime"));
            setType(object.getInt("Type"));
            setMessageContent(object.getString("MessageContent"));
            setOrderID(object.getString("OrderID"));
        }catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
