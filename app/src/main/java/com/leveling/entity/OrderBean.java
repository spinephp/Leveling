package com.youyudj.leveling.entity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lin on 2018/4/8.
 * 订单列表单个数据模型
 */

public class OrderBean {

    private String NoticeID;
    private String OrderID;
    private String UserID;
    private String Content;
    private String NoticeTime;
    private boolean isRead;


    public void setNoticeID(String id) {
        this.NoticeID = id;
    }

    public void setOrderID(String id) {
        this.OrderID = id;
    }

    public void setContent(String c) {
        this.Content = c;
    }

    public void setNoticeTime(String time) {
        this.NoticeTime = time;
    }

    public void setRead(boolean read) {
        this.isRead = read;
    }

    public void setUserID(String id){
        this.UserID = id;
    }

    public String getNoticeID() {
        return NoticeID;
    }

    public String getOrderID() {
        return OrderID;
    }

    public String getContent() {
        return Content;
    }

    public String getNoticeTime()
    {
        return NoticeTime;
    }

    public boolean getIs_read()
    {
        return isRead;
    }

    public OrderBean(JSONObject object)
    {
        try {
            setNoticeID(object.getString("NoticeID"));
            setContent(object.getString("Content"));
            String time = object.getString("NoticeTime");
            time = time.replace("T", " ");
            int pos = time.indexOf('.');
            if(pos != -1) {
                time = time.substring(0, pos);
            }
            setNoticeTime(time);
            setOrderID(object.getString("OrderID"));
            setRead(object.getBoolean("IsRead"));
            setUserID(object.getString("UsreID"));
        }catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
