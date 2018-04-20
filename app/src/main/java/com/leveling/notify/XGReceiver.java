package com.youyudj.leveling.notify;

import android.content.Context;
import android.content.Intent;

import com.youyudj.leveling.BeaterReceiveOrderActivity;
import com.youyudj.leveling.Beater_Order_Activity;
import com.youyudj.leveling.ReleaseOrderSuccessActivity;
import com.youyudj.leveling.chat.app.ChatApplication;
import com.youyudj.leveling.entity.BeaterOrder;
import com.youyudj.leveling.entity.OrderType;
import com.youyudj.leveling.new_chat.ChatActivity;
import com.youyudj.leveling.personcenter.AllOrdersActivity;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by myipp on 2018/4/6.
 */

public class XGReceiver extends XGPushBaseReceiver {
    @Override
    public void onRegisterResult(Context context, int i, XGPushRegisterResult xgPushRegisterResult) {
        System.out.println("onRegisterResult");
    }

    @Override
    public void onUnregisterResult(Context context, int i) {
        System.out.println("onUnregisterResult");
    }

    @Override
    public void onSetTagResult(Context context, int i, String s) {
        System.out.println("onSetTagResult");
    }

    @Override
    public void onDeleteTagResult(Context context, int i, String s) {
        System.out.println("onDeleteTagResult");
    }

    @Override
    public void onTextMessage(Context context, XGPushTextMessage xgPushTextMessage) {
        System.out.println("onTextMessage");
    }

    @Override
    public void onNotifactionClickedResult(Context context, XGPushClickedResult xgPushClickedResult) {
        System.out.println("onNotifactionClickedResult");
        /* start of james edit */
        if(xgPushClickedResult == null)
            return;
        if(xgPushClickedResult.getActionType() != XGPushClickedResult.NOTIFACTION_CLICKED_TYPE)
            return;

        String actName = xgPushClickedResult.getActivityName();
        if(actName == null)
            return;

        try {
            if (actName.equals("ChatActivity")) {
                try {
                    JSONObject js = new JSONObject(xgPushClickedResult.getCustomContent());
                    String orderID = js.getString("OrderID");
                    Intent it = new Intent(ChatApplication.getInstance(), ChatActivity.class);
                    it.putExtra("orderID", orderID);
                    ChatApplication.getInstance().startActivity(it);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (actName.equals("AllOrders_General")) {
                OrderType.orderType = "5";
                Intent it = new Intent(ChatApplication.getInstance(), AllOrdersActivity.class);
                ChatApplication.getInstance().startActivity(it);
            } else if (actName.equals("AllOrders_DaShou")) {
                BeaterOrder.orderType = "1";
                Intent it = new Intent(ChatApplication.getInstance(), Beater_Order_Activity.class);
                ChatApplication.getInstance().startActivity(it);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        /* end of james edit */
    }

    @Override
    public void onNotifactionShowedResult(Context context, XGPushShowedResult xgPushShowedResult) {
        System.out.println("onNotifactionShowedResult");
    }
}
