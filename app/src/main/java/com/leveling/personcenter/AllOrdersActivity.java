package com.youyudj.leveling.personcenter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.youyudj.leveling.R;
import com.youyudj.leveling.entity.OrderType;
import com.youyudj.leveling.entity.UserInfo;
import com.youyudj.leveling.entity.gametype;
import com.youyudj.leveling.utils.BitmapLoader;
import com.youyudj.leveling.utils.HttpGetUtils;
import com.youyudj.leveling.utils.HttpPostUtils;
import com.youyudj.leveling.utils.HttpFileHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * *******************************************************
 * Description:
 * Autour: 3W攻城狮
 * Date: 2017/11/25 13:43
 * Update:2017/11/25
 * Version:
 * *******************************************************
 */
public class AllOrdersActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tx_no_orders_dys, tx_no_orders_beatering, tx_no_orders_all, tx_no_orders_cancle, tx_no_orders_problem, tx_no_orders_locking, tx_no_orders_cancles, tx_no_orders_yjs, tx_no_orders_yzc;
    private ListView order_list_details;
    public static int dd;
    private List<HashMap<String, Object>> list;
    private UserInfo userInfo;
    public static String selectedOrderType;
    public static String orderId;
    public static int POSTION;
    public static int game;
    LinearLayout king, hero;
    LinearLayout back;
    private int s;
    private MyAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_orders_layout);
        king = (LinearLayout) findViewById(R.id.ll_no_orders_king);
        hero = (LinearLayout) findViewById(R.id.ll_no_orders_hero);
        back = (LinearLayout) findViewById(R.id.img_all_order_back);
        userInfo = new UserInfo(this);
        //未接手
        tx_no_orders_dys = (TextView) findViewById(R.id.tx_no_orders_dys);
        //全部订单
        //tx_no_orders_all = (TextView) findViewById(R.id.tx_no_orders_all);
        //未付款
        tx_no_orders_beatering = (TextView) findViewById(R.id.tx_no_orders_beatering);
        //代练中
        tx_no_orders_cancle = (TextView) findViewById(R.id.tx_no_orders_cancle);
        //撤单中
        tx_no_orders_problem = (TextView) findViewById(R.id.tx_no_orders_problem);
        //退款中
        tx_no_orders_locking = (TextView) findViewById(R.id.tx_no_orders_locking);
        //已退款
        tx_no_orders_cancles = (TextView) findViewById(R.id.tx_no_orders_cancles);
        //待评价
        tx_no_orders_yjs = (TextView) findViewById(R.id.tx_no_orders_yjs);
        //已完成
        tx_no_orders_yzc = (TextView) findViewById(R.id.tx_no_orders_yzc);
        order_list_details = (ListView) findViewById(R.id.order_list_details);

        tx_no_orders_dys.setOnClickListener(this);
        tx_no_orders_beatering.setOnClickListener(this);
        tx_no_orders_cancle.setOnClickListener(this);
        tx_no_orders_problem.setOnClickListener(this);
        tx_no_orders_locking.setOnClickListener(this);
        tx_no_orders_cancles.setOnClickListener(this);
        tx_no_orders_yjs.setOnClickListener(this);
        tx_no_orders_yzc.setOnClickListener(this);
        king.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game = 1;
                hero.setSelected(false);
                king.setSelected(true);
                list.clear();
                selectedOrderType = "0";
                selectButton(tx_no_orders_beatering);
                String url1 = "/api/Order/GetPiblisherOrderList?gametype=" + game + "&state=" + 0;
                HttpGetUtils.httpGetFile(1, url1, handler);
            }
        });
        hero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game = 2;
                king.setSelected(false);
                hero.setSelected(true);
                list.clear();
                selectedOrderType = "0";
                selectButton(tx_no_orders_beatering);
                String url = "/api/Order/GetPiblisherOrderList?gametype=" + game + "&state=" + 0;
                HttpGetUtils.httpGetFile(10, url, handler);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (gametype.game == "1") {
            gametype.game = "";
            king.setSelected(true);
            game = 1;
            hero.setSelected(false);
        } else if (gametype.game == "2") {
            gametype.game = "";
            hero.setSelected(true);
            game = 2;
            king.setSelected(false);
        } else {
            king.setSelected(true);
            game = 1;
            hero.setSelected(false);
        }
        list = new ArrayList<HashMap<String, Object>>();
        adapter = new MyAdapter(AllOrdersActivity.this);
        order_list_details.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateOrderTypeToCategory();
        switchToOrderType();
    }

    String oldOrderType = null;
    void updateOrderTypeToCategory(){
        if(oldOrderType != OrderType.orderType) {
            if (OrderType.orderType == "1") {
                selectedOrderType = "1";
            } else if (OrderType.orderType == "2") {
                selectedOrderType = "0";
            } else if (OrderType.orderType == "1") {
                selectedOrderType = "10";
            } else if (OrderType.orderType == "3") {
                selectedOrderType = "7";
            } else if (OrderType.orderType == "4") {
                selectedOrderType = "4";
            } else if (OrderType.orderType == "5") {
                selectedOrderType = "2";
            } else if (OrderType.orderType == "6") {
                selectedOrderType = "7";
            } else if (OrderType.orderType == "7") {
                selectedOrderType = "5";
            } else if (OrderType.orderType == "8") {
                selectedOrderType = "0";
            } else if (OrderType.orderType == "101") {
                selectedOrderType = "6";
            } else if (OrderType.orderType == "103") {
                selectedOrderType = "10";
            } else if (OrderType.orderType == "123") {
                selectedOrderType = "10";
            } else {
                selectedOrderType = "";
            }
            oldOrderType = OrderType.orderType;
        }
    }

    void selectButton(TextView tv){
        tx_no_orders_beatering.setBackgroundColor(Color.parseColor("#ffffffff"));
        tx_no_orders_dys.setBackgroundColor(Color.parseColor("#ffffffff"));
        tx_no_orders_cancle.setBackgroundColor(Color.parseColor("#ffffffff"));
        tx_no_orders_problem.setBackgroundColor(Color.parseColor("#ffffffff"));
        tx_no_orders_locking.setBackgroundColor(Color.parseColor("#ffffffff"));
        tx_no_orders_cancles.setBackgroundColor(Color.parseColor("#ffffffff"));
        tx_no_orders_yjs.setBackgroundColor(Color.parseColor("#ffffffff"));
        tx_no_orders_yzc.setBackgroundColor(Color.parseColor("#ffffffff"));
        tv.setBackgroundColor(Color.parseColor("#DDDDDD"));
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    try {
                        String res = (String) msg.obj;
                        if (res == null) {
                            return;
                        }
                        JSONObject result = new JSONObject(res);
                        String success = result.getString("Success");
                        String err = result.getString("ErrMsg");
                        String data = result.getString("Data");
                        list.clear();
                        //List<AnnouncementInfo> items = new ArrayList<AnnouncementInfo>();
                        if (!data.equals("")){
                            JSONArray arr = new JSONArray(data);
                            HashMap<String, Object> hashMap;
                            for (int i = 0; i < arr.length(); i++) {
                                hashMap = new HashMap<String, Object>();
                                JSONObject inresult = (JSONObject) arr.get(i);
                                hashMap.put("OrderID", inresult.getString("OrderID"));
                                //orderId = inresult.getString("OrderID");
                                hashMap.put("Title", inresult.getString("Title"));
                                hashMap.put("PiblishDateTime", inresult.getString("PiblishDateTime"));
                                hashMap.put("PiblisherBigRank", inresult.getString("PiblisherBigRank"));
                                hashMap.put("PiblisherMediumRank", inresult.getString("PiblisherMediumRank"));
                                hashMap.put("PiblisherRank", inresult.getString("PiblisherRank"));
                                hashMap.put("PiblisherGoalBigRank", inresult.getString("PiblisherGoalBigRank"));
                                hashMap.put("PiblisherGoalMediumRank", inresult.getString("PiblisherGoalMediumRank"));
                                hashMap.put("PiblisherGoalRank", inresult.getString("PiblisherGoalRank"));
                                hashMap.put("OrderPrice", inresult.getString("OrderPrice"));
                                hashMap.put("GameType", inresult.getString("GameType"));
                                hashMap.put("GameOS", inresult.getString("GameOS"));
                                hashMap.put("GameArea", inresult.getString("GameArea"));
                                hashMap.put("Picture", inresult.getString("Picture"));
                                hashMap.put("NickName", inresult.getString("NickName"));
                                hashMap.put("RemainingTime", inresult.getString("RemainingTime"));
                                hashMap.put("LOLRank", inresult.getString("LOLRank"));
                                hashMap.put("LOLGoalRank", inresult.getString("LOLGoalRank"));
                                //orderId = hashMap.get("OrderID").toString();
                                list.add(hashMap);
                            }
                        }
                        if (success == "true") {
                            // Toast.makeText(AllOrdersActivity.this, success, Toast.LENGTH_LONG).show();
                        } else if (success == "false") {
                            Toast.makeText(AllOrdersActivity.this, err, Toast.LENGTH_LONG).show();
                        }
                        order_list_details.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        String res = (String) msg.obj;
                        if (res == null) {
                            return;
                        }
                        JSONObject result = new JSONObject(res);
                        String success = result.getString("Success");
                        String err = result.getString("ErrMsg");
                        String data = result.getString("Data");
                        list.clear();
                        //List<AnnouncementInfo> items = new ArrayList<AnnouncementInfo>();
                        if (!data.equals("")) {
                            JSONArray arr = new JSONArray(data);
                            HashMap<String, Object> hashMap;
                            for (int i = 0; i < arr.length(); i++) {
                                hashMap = new HashMap<String, Object>();
                                JSONObject inresult = (JSONObject) arr.get(i);
                                hashMap.put("OrderID", inresult.getString("OrderID"));
                                //orderId = inresult.getString("OrderID");
                                hashMap.put("Title", inresult.getString("Title"));
                                hashMap.put("PiblishDateTime", inresult.getString("PiblishDateTime"));
                                hashMap.put("PiblisherBigRank", inresult.getString("PiblisherBigRank"));
                                hashMap.put("PiblisherMediumRank", inresult.getString("PiblisherMediumRank"));
                                hashMap.put("PiblisherRank", inresult.getString("PiblisherRank"));
                                hashMap.put("PiblisherGoalBigRank", inresult.getString("PiblisherGoalBigRank"));
                                hashMap.put("PiblisherGoalMediumRank", inresult.getString("PiblisherGoalMediumRank"));
                                hashMap.put("PiblisherGoalRank", inresult.getString("PiblisherGoalRank"));
                                hashMap.put("OrderPrice", inresult.getString("OrderPrice"));
                                hashMap.put("GameType", inresult.getString("GameType"));
                                hashMap.put("GameOS", inresult.getString("GameOS"));
                                hashMap.put("GameArea", inresult.getString("GameArea"));
                                hashMap.put("Picture", inresult.getString("Picture"));
                                hashMap.put("NickName", inresult.getString("NickName"));
                                hashMap.put("RemainingTime", inresult.getString("RemainingTime"));
                                hashMap.put("LOLRank", inresult.getString("LOLRank"));
                                hashMap.put("LOLGoalRank", inresult.getString("LOLGoalRank"));
                                //orderId = hashMap.get("OrderID").toString();
                                list.add(hashMap);
                            }
                        }

                        if (success == "true") {
                            // Toast.makeText(AllOrdersActivity.this, success, Toast.LENGTH_LONG).show();
                        } else if (success == "false") {
                            Toast.makeText(AllOrdersActivity.this, err, Toast.LENGTH_LONG).show();
                        }
                        order_list_details.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    try {
                        String res = (String) msg.obj;
                        if (res == null) {
                            return;
                        }
                        JSONObject result = new JSONObject(res);
                        String success = result.getString("Success");
                        String err = result.getString("ErrMsg");
                        String data = result.getString("Data");
                        list.clear();
                        //List<AnnouncementInfo> items = new ArrayList<AnnouncementInfo>();
                        if (!data.equals(""))  {
                            JSONArray arr = new JSONArray(data);
                            HashMap<String, Object> hashMap;
                            for (int i = 0; i < arr.length(); i++) {
                                hashMap = new HashMap<String, Object>();
                                JSONObject inresult = (JSONObject) arr.get(i);
                                hashMap.put("OrderID", inresult.getString("OrderID"));
                                //orderId = inresult.getString("OrderID");
                                hashMap.put("Title", inresult.getString("Title"));
                                hashMap.put("PiblishDateTime", inresult.getString("PiblishDateTime"));
                                hashMap.put("PiblisherBigRank", inresult.getString("PiblisherBigRank"));
                                hashMap.put("PiblisherMediumRank", inresult.getString("PiblisherMediumRank"));
                                hashMap.put("PiblisherRank", inresult.getString("PiblisherRank"));
                                hashMap.put("PiblisherGoalBigRank", inresult.getString("PiblisherGoalBigRank"));
                                hashMap.put("PiblisherGoalMediumRank", inresult.getString("PiblisherGoalMediumRank"));
                                hashMap.put("PiblisherGoalRank", inresult.getString("PiblisherGoalRank"));
                                hashMap.put("OrderPrice", inresult.getString("OrderPrice"));
                                hashMap.put("GameType", inresult.getString("GameType"));
                                hashMap.put("GameOS", inresult.getString("GameOS"));
                                hashMap.put("GameArea", inresult.getString("GameArea"));
                                hashMap.put("Picture", inresult.getString("Picture"));
                                hashMap.put("NickName", inresult.getString("NickName"));
                                hashMap.put("RemainingTime", inresult.getString("RemainingTime"));
                                hashMap.put("LOLRank", inresult.getString("LOLRank"));
                                hashMap.put("LOLGoalRank", inresult.getString("LOLGoalRank"));
                                list.add(hashMap);
                            }
                        }

                        if (success == "true") {
                        } else if (success == "false") {
                            Toast.makeText(AllOrdersActivity.this, err, Toast.LENGTH_LONG).show();
                        }
                        order_list_details.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    try {
                        String res = (String) msg.obj;
                        if (res == null) {
                            return;
                        }
                        JSONObject result = new JSONObject(res);
                        String success = result.getString("Success");
                        String err = result.getString("ErrMsg");
                        String data = result.getString("Data");
                        list.clear();
                        if (!data.equals("")) {
                            JSONArray arr = new JSONArray(data);
                            HashMap<String, Object> hashMap;
                            for (int i = 0; i < arr.length(); i++) {
                                hashMap = new HashMap<String, Object>();
                                JSONObject inresult = (JSONObject) arr.get(i);
                                hashMap.put("OrderID", inresult.getString("OrderID"));
                                //orderId = inresult.getString("OrderID");
                                hashMap.put("Title", inresult.getString("Title"));
                                hashMap.put("PiblishDateTime", inresult.getString("PiblishDateTime"));
                                hashMap.put("PiblisherBigRank", inresult.getString("PiblisherBigRank"));
                                hashMap.put("PiblisherMediumRank", inresult.getString("PiblisherMediumRank"));
                                hashMap.put("PiblisherRank", inresult.getString("PiblisherRank"));
                                hashMap.put("PiblisherGoalBigRank", inresult.getString("PiblisherGoalBigRank"));
                                hashMap.put("PiblisherGoalMediumRank", inresult.getString("PiblisherGoalMediumRank"));
                                hashMap.put("PiblisherGoalRank", inresult.getString("PiblisherGoalRank"));
                                hashMap.put("OrderPrice", inresult.getString("OrderPrice"));
                                hashMap.put("GameType", inresult.getString("GameType"));
                                hashMap.put("GameOS", inresult.getString("GameOS"));
                                hashMap.put("GameArea", inresult.getString("GameArea"));
                                hashMap.put("Picture", inresult.getString("Picture"));
                                hashMap.put("NickName", inresult.getString("NickName"));
                                hashMap.put("RemainingTime", inresult.getString("RemainingTime"));
                                hashMap.put("LOLRank", inresult.getString("LOLRank"));
                                hashMap.put("LOLGoalRank", inresult.getString("LOLGoalRank"));
                                //orderId = hashMap.get("OrderID").toString();
                                list.add(hashMap);
                            }
                        }

                        if (success == "true") {
                            // Toast.makeText(AllOrdersActivity.this, success, Toast.LENGTH_LONG).show();
                        } else if (success == "false") {
                            Toast.makeText(AllOrdersActivity.this, err, Toast.LENGTH_LONG).show();
                        }
                        order_list_details.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                case 5:
                    try {
                        String res = (String) msg.obj;
                        if (res == null) {
                            return;
                        }
                        JSONObject result = new JSONObject(res);
                        String success = result.getString("Success");
                        String err = result.getString("ErrMsg");
                        String data = result.getString("Data");
                        list.clear();
                        //List<AnnouncementInfo> items = new ArrayList<AnnouncementInfo>();
                        if (!data.equals("")) {
                            JSONArray arr = new JSONArray(data);
                            HashMap<String, Object> hashMap;
                            for (int i = 0; i < arr.length(); i++) {
                                hashMap = new HashMap<String, Object>();
                                JSONObject inresult = (JSONObject) arr.get(i);
                                hashMap.put("OrderID", inresult.getString("OrderID"));
                                //orderId = inresult.getString("OrderID");
                                hashMap.put("Title", inresult.getString("Title"));
                                hashMap.put("PiblishDateTime", inresult.getString("PiblishDateTime"));
                                hashMap.put("PiblisherBigRank", inresult.getString("PiblisherBigRank"));
                                hashMap.put("PiblisherMediumRank", inresult.getString("PiblisherMediumRank"));
                                hashMap.put("PiblisherRank", inresult.getString("PiblisherRank"));
                                hashMap.put("PiblisherGoalBigRank", inresult.getString("PiblisherGoalBigRank"));
                                hashMap.put("PiblisherGoalMediumRank", inresult.getString("PiblisherGoalMediumRank"));
                                hashMap.put("PiblisherGoalRank", inresult.getString("PiblisherGoalRank"));
                                hashMap.put("OrderPrice", inresult.getString("OrderPrice"));
                                hashMap.put("GameType", inresult.getString("GameType"));
                                hashMap.put("GameOS", inresult.getString("GameOS"));
                                hashMap.put("GameArea", inresult.getString("GameArea"));
                                hashMap.put("Picture", inresult.getString("Picture"));
                                hashMap.put("NickName", inresult.getString("NickName"));
                                hashMap.put("RemainingTime", inresult.getString("RemainingTime"));
                                hashMap.put("LOLRank", inresult.getString("LOLRank"));
                                hashMap.put("LOLGoalRank", inresult.getString("LOLGoalRank"));
                                //orderId = hashMap.get("OrderID").toString();
                                list.add(hashMap);
                            }
                        }

                        if (success == "true") {
                            // Toast.makeText(AllOrdersActivity.this, success, Toast.LENGTH_LONG).show();
                        } else if (success == "false") {
                            Toast.makeText(AllOrdersActivity.this, err, Toast.LENGTH_LONG).show();
                        }
                        order_list_details.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                case 6:
                    try {
                        String res = (String) msg.obj;
                        if (res == null) {
                            return;
                        }
                        JSONObject result = new JSONObject(res);
                        String success = result.getString("Success");
                        String err = result.getString("ErrMsg");
                        String data = result.getString("Data");
                        list.clear();
                        if (!data.equals("")) {
                            JSONArray arr = new JSONArray(data);
                            HashMap<String, Object> hashMap;
                            for (int i = 0; i < arr.length(); i++) {
                                hashMap = new HashMap<String, Object>();
                                JSONObject inresult = (JSONObject) arr.get(i);
                                hashMap.put("OrderID", inresult.getString("OrderID"));
                                //orderId = inresult.getString("OrderID");
                                hashMap.put("Title", inresult.getString("Title"));
                                hashMap.put("PiblishDateTime", inresult.getString("PiblishDateTime"));
                                hashMap.put("PiblisherBigRank", inresult.getString("PiblisherBigRank"));
                                hashMap.put("PiblisherMediumRank", inresult.getString("PiblisherMediumRank"));
                                hashMap.put("PiblisherRank", inresult.getString("PiblisherRank"));
                                hashMap.put("PiblisherGoalBigRank", inresult.getString("PiblisherGoalBigRank"));
                                hashMap.put("PiblisherGoalMediumRank", inresult.getString("PiblisherGoalMediumRank"));
                                hashMap.put("PiblisherGoalRank", inresult.getString("PiblisherGoalRank"));
                                hashMap.put("OrderPrice", inresult.getString("OrderPrice"));
                                hashMap.put("GameType", inresult.getString("GameType"));
                                hashMap.put("GameOS", inresult.getString("GameOS"));
                                hashMap.put("GameArea", inresult.getString("GameArea"));
                                hashMap.put("Picture", inresult.getString("Picture"));
                                hashMap.put("NickName", inresult.getString("NickName"));
                                hashMap.put("RemainingTime", inresult.getString("RemainingTime"));
                                hashMap.put("LOLRank", inresult.getString("LOLRank"));
                                hashMap.put("LOLGoalRank", inresult.getString("LOLGoalRank"));
                                //orderId = hashMap.get("OrderID").toString();
                                list.add(hashMap);
                            }
                        }

                        if (success == "true") {
                            // Toast.makeText(AllOrdersActivity.this, success, Toast.LENGTH_LONG).show();
                        } else if (success == "false") {
                            Toast.makeText(AllOrdersActivity.this, err, Toast.LENGTH_LONG).show();
                        }
                        order_list_details.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                case 7:
                    try {
                        String res = (String) msg.obj;
                        if (res == null) {
                            return;
                        }
                        JSONObject result = new JSONObject(res);
                        String success = result.getString("Success");
                        String err = result.getString("ErrMsg");
                        String data = result.getString("Data");
                        list.clear();
                        //List<AnnouncementInfo> items = new ArrayList<AnnouncementInfo>();
                        if (!data.equals("")) {
                            JSONArray arr = new JSONArray(data);
                            HashMap<String, Object> hashMap;
                            for (int i = 0; i < arr.length(); i++) {
                                hashMap = new HashMap<String, Object>();
                                JSONObject inresult = (JSONObject) arr.get(i);
                                hashMap.put("OrderID", inresult.getString("OrderID"));
                                //orderId = inresult.getString("OrderID");
                                hashMap.put("Title", inresult.getString("Title"));
                                hashMap.put("PiblishDateTime", inresult.getString("PiblishDateTime"));
                                hashMap.put("PiblisherBigRank", inresult.getString("PiblisherBigRank"));
                                hashMap.put("PiblisherMediumRank", inresult.getString("PiblisherMediumRank"));
                                hashMap.put("PiblisherRank", inresult.getString("PiblisherRank"));
                                hashMap.put("PiblisherGoalBigRank", inresult.getString("PiblisherGoalBigRank"));
                                hashMap.put("PiblisherGoalMediumRank", inresult.getString("PiblisherGoalMediumRank"));
                                hashMap.put("PiblisherGoalRank", inresult.getString("PiblisherGoalRank"));
                                hashMap.put("OrderPrice", inresult.getString("OrderPrice"));
                                hashMap.put("GameType", inresult.getString("GameType"));
                                hashMap.put("GameOS", inresult.getString("GameOS"));
                                hashMap.put("GameArea", inresult.getString("GameArea"));
                                hashMap.put("Picture", inresult.getString("Picture"));
                                hashMap.put("NickName", inresult.getString("NickName"));
                                hashMap.put("RemainingTime", inresult.getString("RemainingTime"));
                                hashMap.put("LOLRank", inresult.getString("LOLRank"));
                                hashMap.put("LOLGoalRank", inresult.getString("LOLGoalRank"));
                                //orderId = hashMap.get("OrderID").toString();
                                list.add(hashMap);
                            }
                        }

                        if (success == "true") {
                            // Toast.makeText(AllOrdersActivity.this, success, Toast.LENGTH_LONG).show();
                        } else if (success == "false") {
                            Toast.makeText(AllOrdersActivity.this, err, Toast.LENGTH_LONG).show();
                        }
                        order_list_details.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                case 8:
                    try {
                        String res = (String) msg.obj;
                        if (res == null) {
                            return;
                        }
                        JSONObject result = new JSONObject(res);
                        String success = result.getString("Success");
                        String err = result.getString("ErrMsg");
                        String data = result.getString("Data");
                        list.clear();
                        //List<AnnouncementInfo> items = new ArrayList<AnnouncementInfo>();
                        if (!data.equals("")) {
                            JSONArray arr = new JSONArray(data);
                            HashMap<String, Object> hashMap;
                            for (int i = 0; i < arr.length(); i++) {
                                hashMap = new HashMap<String, Object>();
                                JSONObject inresult = (JSONObject) arr.get(i);
                                hashMap.put("OrderID", inresult.getString("OrderID"));
                                //orderId = inresult.getString("OrderID");
                                hashMap.put("Title", inresult.getString("Title"));
                                hashMap.put("PiblishDateTime", inresult.getString("PiblishDateTime"));
                                hashMap.put("PiblisherBigRank", inresult.getString("PiblisherBigRank"));
                                hashMap.put("PiblisherMediumRank", inresult.getString("PiblisherMediumRank"));
                                hashMap.put("PiblisherRank", inresult.getString("PiblisherRank"));
                                hashMap.put("PiblisherGoalBigRank", inresult.getString("PiblisherGoalBigRank"));
                                hashMap.put("PiblisherGoalMediumRank", inresult.getString("PiblisherGoalMediumRank"));
                                hashMap.put("PiblisherGoalRank", inresult.getString("PiblisherGoalRank"));
                                hashMap.put("OrderPrice", inresult.getString("OrderPrice"));
                                hashMap.put("GameType", inresult.getString("GameType"));
                                hashMap.put("GameOS", inresult.getString("GameOS"));
                                hashMap.put("GameArea", inresult.getString("GameArea"));
                                hashMap.put("Picture", inresult.getString("Picture"));
                                hashMap.put("NickName", inresult.getString("NickName"));
                                hashMap.put("RemainingTime", inresult.getString("RemainingTime"));
                                hashMap.put("LOLRank", inresult.getString("LOLRank"));
                                hashMap.put("LOLGoalRank", inresult.getString("LOLGoalRank"));
                                //orderId = hashMap.get("OrderID").toString();
                                list.add(hashMap);
                            }
                        }

                        if (success == "true") {
                            // Toast.makeText(AllOrdersActivity.this, success, Toast.LENGTH_LONG).show();
                        } else if (success == "false") {
                            Toast.makeText(AllOrdersActivity.this, err, Toast.LENGTH_LONG).show();
                        }
                        order_list_details.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                case 10:
                    try {
                        String res = (String) msg.obj;
                        if (res == null) {
                            return;
                        }
                        JSONObject result = new JSONObject(res);
                        String success = result.getString("Success");
                        String err = result.getString("ErrMsg");
                        String data = result.getString("Data");
                        list.clear();
                        if (!data.equals("")) {
                            JSONArray arr = new JSONArray(data);
                            HashMap<String, Object> hashMap;
                            for (int i = 0; i < arr.length(); i++) {
                                hashMap = new HashMap<String, Object>();
                                JSONObject inresult = (JSONObject) arr.get(i);
                                hashMap.put("OrderID", inresult.getString("OrderID"));
                                //orderId = inresult.getString("OrderID");
                                hashMap.put("Title", inresult.getString("Title"));
                                hashMap.put("PiblishDateTime", inresult.getString("PiblishDateTime"));
                                hashMap.put("PiblisherBigRank", inresult.getString("PiblisherBigRank"));
                                hashMap.put("PiblisherMediumRank", inresult.getString("PiblisherMediumRank"));
                                hashMap.put("PiblisherRank", inresult.getString("PiblisherRank"));
                                hashMap.put("PiblisherGoalBigRank", inresult.getString("PiblisherGoalBigRank"));
                                hashMap.put("PiblisherGoalMediumRank", inresult.getString("PiblisherGoalMediumRank"));
                                hashMap.put("PiblisherGoalRank", inresult.getString("PiblisherGoalRank"));
                                hashMap.put("OrderPrice", inresult.getString("OrderPrice"));
                                hashMap.put("GameType", inresult.getString("GameType"));
                                hashMap.put("GameOS", inresult.getString("GameOS"));
                                hashMap.put("GameArea", inresult.getString("GameArea"));
                                hashMap.put("Picture", inresult.getString("Picture"));
                                hashMap.put("NickName", inresult.getString("NickName"));
                                hashMap.put("RemainingTime", inresult.getString("RemainingTime"));
                                hashMap.put("LOLRank", inresult.getString("LOLRank"));
                                hashMap.put("LOLGoalRank", inresult.getString("LOLGoalRank"));
                                list.add(hashMap);
                            }
                        }
                        if (success == "true") {
                        } else if (success == "false") {
                            Toast.makeText(AllOrdersActivity.this, err, Toast.LENGTH_LONG).show();
                        }
                        order_list_details.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                case 11:
                    try {
                        String res = (String) msg.obj;
                        if (res == null) {
                            return;
                        }
                        JSONObject result = new JSONObject(res);
                        String success = result.getString("Success");
                        String err = result.getString("ErrMsg");
                        String data = result.getString("Data");
                        list.clear();
                        if (!data.equals("")) {
                            JSONArray arr = new JSONArray(data);
                            HashMap<String, Object> hashMap;
                            for (int i = 0; i < arr.length(); i++) {
                                hashMap = new HashMap<String, Object>();
                                JSONObject inresult = (JSONObject) arr.get(i);
                                hashMap.put("OrderID", inresult.getString("OrderID"));
                                //orderId = inresult.getString("OrderID");
                                hashMap.put("Title", inresult.getString("Title"));
                                hashMap.put("PiblishDateTime", inresult.getString("PiblishDateTime"));
                                hashMap.put("PiblisherBigRank", inresult.getString("PiblisherBigRank"));
                                hashMap.put("PiblisherMediumRank", inresult.getString("PiblisherMediumRank"));
                                hashMap.put("PiblisherRank", inresult.getString("PiblisherRank"));
                                hashMap.put("PiblisherGoalBigRank", inresult.getString("PiblisherGoalBigRank"));
                                hashMap.put("PiblisherGoalMediumRank", inresult.getString("PiblisherGoalMediumRank"));
                                hashMap.put("PiblisherGoalRank", inresult.getString("PiblisherGoalRank"));
                                hashMap.put("OrderPrice", inresult.getString("OrderPrice"));
                                hashMap.put("GameType", inresult.getString("GameType"));
                                hashMap.put("GameOS", inresult.getString("GameOS"));
                                hashMap.put("GameArea", inresult.getString("GameArea"));
                                hashMap.put("Picture", inresult.getString("Picture"));
                                hashMap.put("NickName", inresult.getString("NickName"));
                                hashMap.put("RemainingTime", inresult.getString("RemainingTime"));
                                hashMap.put("LOLRank", inresult.getString("LOLRank"));
                                hashMap.put("LOLGoalRank", inresult.getString("LOLGoalRank"));
                                list.add(hashMap);
                            }
                        }
                        if (success == "true") {
                        } else if (success == "false") {
                            Toast.makeText(AllOrdersActivity.this, err, Toast.LENGTH_LONG).show();
                        }
                        order_list_details.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                default:
                    return;
            }
            adapter.notifyDataSetChanged();
        }
    };

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            //未付款
            case R.id.tx_no_orders_beatering:
                selectedOrderType = "0";
                break;
            //未接手
            case R.id.tx_no_orders_dys:
                selectedOrderType = "1";
                break;
            //代练中
            case R.id.tx_no_orders_cancle:
                selectedOrderType = "2";
                break;
            //撤单中
            case R.id.tx_no_orders_problem:
                selectedOrderType = "3";
                break;
            //待确认
            case R.id.tx_no_orders_locking:
                selectedOrderType = "4";
                break;
            //退款中
            case R.id.tx_no_orders_cancles:
                selectedOrderType = "5";
                break;
            //待评价
            case R.id.tx_no_orders_yjs:
                selectedOrderType = "6";
                break;
            //已完成
            case R.id.tx_no_orders_yzc:
                selectedOrderType = "7";
                break;
            default:
                break;
        }
        switchToOrderType();
    }

    void switchToOrderType(){
        switch (selectedOrderType) {
            //未付款
            case "0":
                selectButton(tx_no_orders_beatering);
                String url111 = "/api/Order/GetPiblisherOrderList?gametype=" + game + "&state=" + 0;
                HttpGetUtils.httpGetFile(1, url111, handler);
                break;
            //未接手
            case "1":
                selectButton(tx_no_orders_dys);
                String url2 = "/api/Order/GetPiblisherOrderList?gametype=" + game + "&state=" + 1;
                HttpGetUtils.httpGetFile(2, url2, handler);
                break;
            //代练中
            case "2":
                selectButton(tx_no_orders_cancle);
                String url3 = "/api/Order/GetPiblisherOrderList?gametype=" + game + "&state=" + 2;
                HttpGetUtils.httpGetFile(3, url3, handler);
                break;
            //撤单中
            case "3":
                selectButton(tx_no_orders_problem);
                String url4 = "/api/Order/GetPiblisherOrderList?gametype=" + game + "&state=" + 3;
                HttpGetUtils.httpGetFile(4, url4, handler);
                break;
            //待确认
            case "4":
                selectButton(tx_no_orders_locking);
                String url5 = "/api/Order/GetPiblisherOrderList?gametype=" + game + "&state=" + 4;
                HttpGetUtils.httpGetFile(5, url5, handler);
                break;
            //退款中
            case "5":
                selectButton(tx_no_orders_cancles);
                String url6 = "/api/Order/GetPiblisherOrderList?gametype=" + game + "&state=" + 5;
                HttpGetUtils.httpGetFile(6, url6, handler);
                break;
            //待评价
            case "6":
                selectButton(tx_no_orders_yjs);
                String url7 = "/api/Order/GetPiblisherOrderList?gametype=" + game + "&state=" + 6;
                HttpGetUtils.httpGetFile(7, url7, handler);
                break;
            //已完成
            case "7":
                selectButton(tx_no_orders_yzc);
                String url8 = "/api/Order/GetPiblisherOrderList?gametype=" + game + "&state=" + 7;
                HttpGetUtils.httpGetFile(8, url8, handler);
                break;
            default:
                break;
        }
    }

    private class MyAdapter extends BaseAdapter {
        HashMap<Integer, ViewHold> holders;
        private LayoutInflater inflater;

        public MyAdapter(Context context) {
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            holders = new HashMap<Integer, ViewHold>();
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap = list.get(position);
            ViewHold viewHold = new ViewHold();
            holders.put(position, viewHold);
            Log.d("ssss", position + "");
            convertView = inflater.inflate(R.layout.all_orders_item, null);
            viewHold.orderi = (LinearLayout) convertView.findViewById(R.id.orderi);
            viewHold.publisher_head = (ImageView) convertView.findViewById(R.id.publisher_head);
            viewHold.publisher_NickName = (TextView) convertView
                    .findViewById(R.id.publisher_NickName);
            viewHold.money_state = (TextView) convertView
                    .findViewById(R.id.money_state);
            viewHold.publisher_begin_rank = (TextView) convertView
                    .findViewById(R.id.publisher_begin_rank);
            viewHold.publisher_goal_rank = (TextView) convertView
                    .findViewById(R.id.publisher_goal_rank);
            viewHold.publisher_area = (TextView) convertView
                    .findViewById(R.id.publisher_area);
            viewHold.publisher_hero_name = (TextView) convertView
                    .findViewById(R.id.publisher_hero_name);
            viewHold.game_left_time = (TextView) convertView
                    .findViewById(R.id.game_left_time);
            viewHold.publisher_money = (TextView) convertView
                    .findViewById(R.id.publisher_money);
            viewHold.publisher_see_details = (Button) convertView
                    .findViewById(R.id.publisher_see_details);
            if (selectedOrderType == "0") {
                viewHold.money_state.setText("未付款");
            } else if (selectedOrderType == "1") {
                viewHold.money_state.setText("未接手");
            } else if (selectedOrderType == "2") {
                viewHold.money_state.setText("代练中");
            } else if (selectedOrderType == "3") {
                viewHold.money_state.setText("撤单中");
            } else if (selectedOrderType == "4") {
                viewHold.money_state.setText("待确认");
            } else if (selectedOrderType == "5") {
                viewHold.money_state.setText("退款中");
            } else if (selectedOrderType == "6") {
                viewHold.money_state.setText("待评价");
            } else if (selectedOrderType == "7") {
                viewHold.money_state.setText("已完成");
            }
            viewHold.publisher_NickName.setText(hashMap.get("NickName").toString());
            if (hashMap.get("GameType").toString().equals("1")) {
                viewHold.publisher_begin_rank.setText(hashMap.get("PiblisherBigRank").toString() + hashMap.get("PiblisherMediumRank").toString() + hashMap.get("PiblisherRank").toString());
                viewHold.publisher_goal_rank.setText(hashMap.get("PiblisherGoalBigRank").toString() + hashMap.get("PiblisherGoalMediumRank").toString() + hashMap.get("PiblisherGoalRank").toString());
            } else if (hashMap.get("GameType").toString().equals("2")) {
                viewHold.publisher_begin_rank.setText(hashMap.get("LOLRank").toString());
                viewHold.publisher_goal_rank.setText(hashMap.get("LOLGoalRank").toString());
            }
            if (Integer.parseInt(hashMap.get("GameOS").toString()) == 1) {
                viewHold.publisher_area.setText("手Q安卓" + "/" + hashMap.get("GameArea").toString());
            } else if (Integer.parseInt(hashMap.get("GameOS").toString()) == 2) {
                viewHold.publisher_area.setText("手Q苹果" + "/" + hashMap.get("GameArea").toString());
            } else if (Integer.parseInt(hashMap.get("GameOS").toString()) == 3) {
                viewHold.publisher_area.setText("微信安卓" + "/" + hashMap.get("GameArea").toString());
            } else if (Integer.parseInt(hashMap.get("GameOS").toString()) == 4) {
                viewHold.publisher_area.setText("微信苹果" + "/" + hashMap.get("GameArea").toString());
            } else if (Integer.parseInt(hashMap.get("GameOS").toString()) == 5) {
                viewHold.publisher_area.setText("网通" + "/" + hashMap.get("GameArea").toString());
            } else if (Integer.parseInt(hashMap.get("GameOS").toString()) == 6) {
                viewHold.publisher_area.setText("电信" + "/" + hashMap.get("GameArea").toString());
            } else if (Integer.parseInt(hashMap.get("GameOS").toString()) == 7) {
                viewHold.publisher_area.setText("其他" + "/" + hashMap.get("GameArea").toString());
            }
            viewHold.publisher_hero_name.setText("");
            if (HttpPostUtils.getPicture().equals("null")) {
                viewHold.publisher_head.setImageResource(R.drawable.userhead);
            } else {
                String url = "/api/File/GetUserhead?filename=" + HttpPostUtils.getPicture();
                BitmapLoader.getImageFromLocalFirstNorDownload(url, "userhead", HttpPostUtils.getPicture(), handlerPic, position);
                //HttpFileHelper.httpGetFile(position, url, handlerPic);
            }
            String time = hashMap.get("PiblishDateTime").toString().substring(0, 10);
            viewHold.game_left_time.setText(time);
            viewHold.publisher_money.setText(hashMap.get("OrderPrice").toString());

            final HashMap<String, Object> finalHashMap = hashMap;
            viewHold.orderi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(AllOrdersActivity.this, LevelingActivity.class);
                    intent.putExtra("orderid", finalHashMap.get("OrderID").toString());
                    intent.putExtra("orderidType", selectedOrderType);
                    startActivity(intent);
                    //finish();
                }
            });
            viewHold.publisher_see_details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(AllOrdersActivity.this, LevelingActivity.class);
                    intent.putExtra("orderid", finalHashMap.get("OrderID").toString());
                    intent.putExtra("orderidType", selectedOrderType);
                    startActivity(intent);
                    //finish();
                }
            });
            return convertView;
        }

        class ViewHold {
            private LinearLayout orderi;
            private ImageView publisher_head;
            private TextView publisher_NickName;
            private TextView money_state;
            private TextView publisher_begin_rank;
            private TextView publisher_goal_rank;
            private TextView publisher_area;
            private TextView publisher_hero_name;
            private TextView game_left_time;
            private TextView publisher_money;
            private Button publisher_see_details;
        }

        private Handler handlerPic = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what < 0) {
                    return;
                }
                int pos = msg.what;
                if (MyAdapter.this.holders.containsKey(pos)) {
                    Bitmap bmp = (Bitmap)msg.obj;
                    if(bmp != null) {
                        ViewHold holder = MyAdapter.this.holders.get(pos);
                        if(holder != null) {
                            holder.publisher_head.setImageBitmap(bmp);
                        }
                    }
                }
            }
        };
    }

}
