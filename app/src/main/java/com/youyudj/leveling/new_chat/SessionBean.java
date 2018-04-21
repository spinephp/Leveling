package com.youyudj.leveling.new_chat;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lin on 2018/3/28.
 */

public class SessionBean {

    private String OrderID;
    private UserInfo mine;
    private UserInfo friend;

    public String getOrderID()
    {
        return OrderID;
    }

    public UserInfo getMine()
    {
        return mine;
    }

    public UserInfo getFriend()
    {
        return friend;
    }

    public void setOrderID(String id)
    {
        this.OrderID = id;
    }

    public void setMine(UserInfo info)
    {
        this.mine = info;
    }

    public void setFriend(UserInfo info)
    {
        this.friend = info;
    }

    public SessionBean(JSONObject object)
    {
        try {
            setOrderID(object.getString("OrderID"));
            UserInfo m = new UserInfo(object.getJSONObject("Mine"));
            setMine(m);
            UserInfo f = new UserInfo(object.getJSONObject("Friend"));
            setFriend(f);
        }catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
