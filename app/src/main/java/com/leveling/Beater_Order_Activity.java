package com.leveling;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
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

import com.leveling.entity.BeaterOrder;
import com.leveling.personcenter.AllOrdersActivity;
import com.leveling.utils.BitmapLoader;
import com.leveling.utils.HttpGetUtils;
import com.leveling.utils.HttpFileHelper;
import com.leveling.utils.HttpPostUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Beater_Order_Activity extends AppCompatActivity implements View.OnClickListener {

    private TextView in_tx_no_orders_yzc1, in_tx_no_orders_cancle, in_tx_no_orders_problem1, in_tx_no_orders_locking1, in_tx_no_orders_cancles1, in_tx_no_orders_yjs1;
    private ListView in_order_list_details1;
    private ImageView imageView100, imageView101;
    private LinearLayout img_sp_back;
    private List<HashMap<String, Object>> list;
    public static String test, od;
    private String tt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beater__order_);
        imageView100 = (ImageView) findViewById(R.id.imageView100);
        imageView101 = (ImageView) findViewById(R.id.imageView101);
        imageView100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView100.setSelected(true);
            }
        });
        img_sp_back = (LinearLayout) findViewById(R.id.img_ppsp_back);
        img_sp_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        in_order_list_details1 = (ListView) findViewById(R.id.in_order_list_details1);
        //代练中
        in_tx_no_orders_cancle = (TextView) findViewById(R.id.in_tx_no_orders_cancle);
        //撤单中
        in_tx_no_orders_problem1 = (TextView) findViewById(R.id.in_tx_no_orders_problem1);
        //待确认
        in_tx_no_orders_locking1 = (TextView) findViewById(R.id.in_tx_no_orders_locking1);
        //退款中
        in_tx_no_orders_cancles1 = (TextView) findViewById(R.id.in_tx_no_orders_cancles1);
        //待评价
        in_tx_no_orders_yjs1 = (TextView) findViewById(R.id.in_tx_no_orders_yjs1);
        //已完成
        in_tx_no_orders_yzc1 = (TextView) findViewById(R.id.in_tx_no_orders_yzc1);

        //代练中
        in_tx_no_orders_cancle.setOnClickListener(this);
        //撤单中
        in_tx_no_orders_problem1.setOnClickListener(this);
        //待确认
        in_tx_no_orders_locking1.setOnClickListener(this);
        //退款中
        in_tx_no_orders_cancles1.setOnClickListener(this);
        //待评价
        in_tx_no_orders_yjs1.setOnClickListener(this);
        //已完成
        in_tx_no_orders_yzc1.setOnClickListener(this);
        test = "2";
        if (BeaterOrder.orderType == "1") {
            BeaterOrder.orderType = "";
            test = "2";
            in_tx_no_orders_cancle.setBackgroundColor(Color.parseColor("#DDDDDD"));
            String url2 = "/api/Order/GetReceiverOrderList?state=2";
            HttpGetUtils.httpGetFile(2, url2, handler);
        } else if (BeaterOrder.orderType == "2") {
            BeaterOrder.orderType = "";
            test = "4";
            in_tx_no_orders_locking1.setBackgroundColor(Color.parseColor("#DDDDDD"));
            String url4 = "/api/Order/GetReceiverOrderList?state=4";
            HttpGetUtils.httpGetFile(4, url4, handler);
        } else if (BeaterOrder.orderType == "3") {
            BeaterOrder.orderType = "";
            test = "20";
            test = "7";
            in_tx_no_orders_yzc1.setBackgroundColor(Color.parseColor("#DDDDDD"));
            String url7 = "/api/Order/GetReceiverOrderList?state=7";
            HttpGetUtils.httpGetFile(7, url7, handler);
        } else if (BeaterOrder.orderType == "4") {
            BeaterOrder.orderType = "";
            test = "7";
            in_tx_no_orders_yzc1.setBackgroundColor(Color.parseColor("#DDDDDD"));
            String url7 = "/api/Order/GetReceiverOrderList?state=7";
            HttpGetUtils.httpGetFile(7, url7, handler);
        } else if (BeaterOrder.orderType == "5") {
            BeaterOrder.orderType = "";
            test = "2";
            in_tx_no_orders_cancle.setBackgroundColor(Color.parseColor("#DDDDDD"));
            String url2 = "/api/Order/GetReceiverOrderList?state=2";
            HttpGetUtils.httpGetFile(2, url2, handler);
        } else if (BeaterOrder.orderType == "6") {
            BeaterOrder.orderType = "";
            test = "2";
            in_tx_no_orders_cancle.setBackgroundColor(Color.parseColor("#DDDDDD"));
            String url2 = "/api/Order/GetReceiverOrderList?state=2";
            HttpGetUtils.httpGetFile(2, url2, handler);
        } else if (BeaterOrder.orderType == "10") {
            BeaterOrder.orderType = "";
            test = "2";
            in_tx_no_orders_cancle.setBackgroundColor(Color.parseColor("#DDDDDD"));
            String url2 = "/api/Order/GetReceiverOrderList?state=2";
            HttpGetUtils.httpGetFile(2, url2, handler);
        } else if (BeaterOrder.orderType == "123") {
            BeaterOrder.orderType = "";
            test = "3";
            in_tx_no_orders_problem1.setBackgroundColor(Color.parseColor("#DDDDDD"));
            String url3 = "/api/Order/GetReceiverOrderList?state=3";
            HttpGetUtils.httpGetFile(3, url3, handler);
        } else {
            test = "2";
            in_tx_no_orders_cancle.setBackgroundColor(Color.parseColor("#DDDDDD"));
            String url2 = "/api/Order/GetReceiverOrderList?state=2";
            HttpGetUtils.httpGetFile(2, url2, handler);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            //代练中
            case R.id.in_tx_no_orders_cancle:
                test = "2";
                in_tx_no_orders_cancle.setBackgroundColor(Color.parseColor("#DDDDDD"));
                in_tx_no_orders_problem1.setBackgroundColor(Color.parseColor("#ffffffff"));
                in_tx_no_orders_locking1.setBackgroundColor(Color.parseColor("#ffffffff"));
                in_tx_no_orders_cancles1.setBackgroundColor(Color.parseColor("#ffffffff"));
                in_tx_no_orders_yjs1.setBackgroundColor(Color.parseColor("#ffffffff"));
                in_tx_no_orders_yzc1.setBackgroundColor(Color.parseColor("#ffffffff"));
                String url2 = "/api/Order/GetReceiverOrderList?state=2";
                HttpGetUtils.httpGetFile(2, url2, handler);
                break;
            //撤单中
            case R.id.in_tx_no_orders_problem1:
                test = "3";
                in_tx_no_orders_cancle.setBackgroundColor(Color.parseColor("#ffffffff"));
                in_tx_no_orders_locking1.setBackgroundColor(Color.parseColor("#ffffffff"));
                in_tx_no_orders_cancles1.setBackgroundColor(Color.parseColor("#ffffffff"));
                in_tx_no_orders_yjs1.setBackgroundColor(Color.parseColor("#ffffffff"));
                in_tx_no_orders_yzc1.setBackgroundColor(Color.parseColor("#ffffffff"));
                in_tx_no_orders_problem1.setBackgroundColor(Color.parseColor("#DDDDDD"));
                String url3 = "/api/Order/GetReceiverOrderList?state=3";
                HttpGetUtils.httpGetFile(3, url3, handler);
                break;
            //待确认
            case R.id.in_tx_no_orders_locking1:
                test = "4";
                in_tx_no_orders_cancle.setBackgroundColor(Color.parseColor("#ffffffff"));
                in_tx_no_orders_problem1.setBackgroundColor(Color.parseColor("#ffffffff"));
                in_tx_no_orders_cancles1.setBackgroundColor(Color.parseColor("#ffffffff"));
                in_tx_no_orders_yjs1.setBackgroundColor(Color.parseColor("#ffffffff"));
                in_tx_no_orders_yzc1.setBackgroundColor(Color.parseColor("#ffffffff"));
                in_tx_no_orders_locking1.setBackgroundColor(Color.parseColor("#DDDDDD"));
                String url4 = "/api/Order/GetReceiverOrderList?state=4";
                HttpGetUtils.httpGetFile(4, url4, handler);
                break;
            //退款中
            case R.id.in_tx_no_orders_cancles1:
                test = "5";
                in_tx_no_orders_cancle.setBackgroundColor(Color.parseColor("#ffffffff"));
                in_tx_no_orders_problem1.setBackgroundColor(Color.parseColor("#ffffffff"));
                in_tx_no_orders_locking1.setBackgroundColor(Color.parseColor("#ffffffff"));
                in_tx_no_orders_cancles1.setBackgroundColor(Color.parseColor("#DDDDDD"));
                in_tx_no_orders_yjs1.setBackgroundColor(Color.parseColor("#ffffffff"));
                in_tx_no_orders_yzc1.setBackgroundColor(Color.parseColor("#ffffffff"));
                String url5 = "/api/Order/GetReceiverOrderList?state=5";
                HttpGetUtils.httpGetFile(5, url5, handler);
                break;
            //待评价
            case R.id.in_tx_no_orders_yjs1:
                test = "6";
                in_tx_no_orders_cancle.setBackgroundColor(Color.parseColor("#ffffffff"));
                in_tx_no_orders_problem1.setBackgroundColor(Color.parseColor("#ffffffff"));
                in_tx_no_orders_locking1.setBackgroundColor(Color.parseColor("#ffffffff"));
                in_tx_no_orders_cancles1.setBackgroundColor(Color.parseColor("#ffffffff"));
                in_tx_no_orders_yzc1.setBackgroundColor(Color.parseColor("#ffffffff"));
                in_tx_no_orders_yjs1.setBackgroundColor(Color.parseColor("#DDDDDD"));
                String url6 = "/api/Order/GetReceiverOrderList?state=6";
                HttpGetUtils.httpGetFile(6, url6, handler);
                break;
            //已完成
            case R.id.in_tx_no_orders_yzc1:
                test = "7";
                in_tx_no_orders_cancle.setBackgroundColor(Color.parseColor("#ffffffff"));
                in_tx_no_orders_problem1.setBackgroundColor(Color.parseColor("#ffffffff"));
                in_tx_no_orders_locking1.setBackgroundColor(Color.parseColor("#ffffffff"));
                in_tx_no_orders_cancles1.setBackgroundColor(Color.parseColor("#ffffffff"));
                in_tx_no_orders_yjs1.setBackgroundColor(Color.parseColor("#ffffffff"));
                in_tx_no_orders_yzc1.setBackgroundColor(Color.parseColor("#DDDDDD"));
                String url7 = "/api/Order/GetReceiverOrderList?state=7";
                HttpGetUtils.httpGetFile(7, url7, handler);
                break;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
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
                        list = new ArrayList<HashMap<String, Object>>();
                        list.clear();
                        //List<AnnouncementInfo> items = new ArrayList<AnnouncementInfo>();
                        if (data.equals("")) {
                            final MyAdapter adapter = new MyAdapter(Beater_Order_Activity.this);
                            in_order_list_details1.setAdapter(null);
                        } else {
                            JSONArray arr = new JSONArray(data);
                            HashMap<String, Object> hashMap;
                            for (int i = 0; i < arr.length(); i++) {
                                hashMap = new HashMap<String, Object>();
                                JSONObject inresult = (JSONObject) arr.get(i);
                                hashMap.put("OrderID", inresult.getString("OrderID"));
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
                                //hashMap.put("PiblisherPicture", inresult.getString("PiblisherPicture"));
                                //orderId = hashMap.get("OrderID").toString();
                                list.add(hashMap);
                            }
                        }

                        if (success == "true") {
                            // Toast.makeText(AllOrdersActivity.this, success, Toast.LENGTH_LONG).show();
                        } else if (success == "false") {
                            Toast.makeText(Beater_Order_Activity.this, err, Toast.LENGTH_LONG).show();
                        }
                        final MyAdapter adapter = new MyAdapter(Beater_Order_Activity.this);
                        in_order_list_details1.setAdapter(adapter);
                        in_order_list_details1.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

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
                        list = new ArrayList<HashMap<String, Object>>();
                        list.clear();
                        //List<AnnouncementInfo> items = new ArrayList<AnnouncementInfo>();
                        if (data.equals("")) {
                            final MyAdapter adapter = new MyAdapter(Beater_Order_Activity.this);
                            in_order_list_details1.setAdapter(null);
                        } else {
                            JSONArray arr = new JSONArray(data);
                            HashMap<String, Object> hashMap;
                            for (int i = 0; i < arr.length(); i++) {
                                hashMap = new HashMap<String, Object>();
                                JSONObject inresult = (JSONObject) arr.get(i);
                                hashMap.put("OrderID", inresult.getString("OrderID"));
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
                                //hashMap.put("PiblisherPicture", inresult.getString("PiblisherPicture"));
                                //orderId = hashMap.get("OrderID").toString();
                                list.add(hashMap);
                            }
                        }

                        if (success == "true") {
                            // Toast.makeText(AllOrdersActivity.this, success, Toast.LENGTH_LONG).show();
                        } else if (success == "false") {
                            Toast.makeText(Beater_Order_Activity.this, err, Toast.LENGTH_LONG).show();
                        }
                        final MyAdapter adapter = new MyAdapter(Beater_Order_Activity.this);
                        in_order_list_details1.setAdapter(adapter);
                        in_order_list_details1.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

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
                        list = new ArrayList<HashMap<String, Object>>();
                        list.clear();
                        //List<AnnouncementInfo> items = new ArrayList<AnnouncementInfo>();
                        if (data.equals("")) {
                            final MyAdapter adapter = new MyAdapter(Beater_Order_Activity.this);
                            in_order_list_details1.setAdapter(null);
                        } else {
                            JSONArray arr = new JSONArray(data);
                            HashMap<String, Object> hashMap;
                            for (int i = 0; i < arr.length(); i++) {
                                hashMap = new HashMap<String, Object>();
                                JSONObject inresult = (JSONObject) arr.get(i);
                                hashMap.put("OrderID", inresult.getString("OrderID"));
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
                                //hashMap.put("PiblisherPicture", inresult.getString("PiblisherPicture"));
                                //orderId = hashMap.get("OrderID").toString();
                                list.add(hashMap);
                            }
                        }
                        if (success == "true") {
                            // Toast.makeText(AllOrdersActivity.this, success, Toast.LENGTH_LONG).show();
                        } else if (success == "false") {
                            Toast.makeText(Beater_Order_Activity.this, err, Toast.LENGTH_LONG).show();
                        }
                        final MyAdapter adapter = new MyAdapter(Beater_Order_Activity.this);
                        in_order_list_details1.setAdapter(adapter);
                        in_order_list_details1.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

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
                        list = new ArrayList<HashMap<String, Object>>();
                        list.clear();
                        //List<AnnouncementInfo> items = new ArrayList<AnnouncementInfo>();
                        if (data.equals("")) {
                            final MyAdapter adapter = new MyAdapter(Beater_Order_Activity.this);
                            in_order_list_details1.setAdapter(null);
                        } else {
                            JSONArray arr = new JSONArray(data);
                            HashMap<String, Object> hashMap;
                            for (int i = 0; i < arr.length(); i++) {
                                hashMap = new HashMap<String, Object>();
                                JSONObject inresult = (JSONObject) arr.get(i);
                                hashMap.put("OrderID", inresult.getString("OrderID"));
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
                                //hashMap.put("PiblisherPicture", inresult.getString("PiblisherPicture"));
                                //orderId = hashMap.get("OrderID").toString();
                                list.add(hashMap);
                            }
                        }

                        if (success == "true") {
                            // Toast.makeText(AllOrdersActivity.this, success, Toast.LENGTH_LONG).show();
                        } else if (success == "false") {
                            Toast.makeText(Beater_Order_Activity.this, err, Toast.LENGTH_LONG).show();
                        }
                        final MyAdapter adapter = new MyAdapter(Beater_Order_Activity.this);
                        in_order_list_details1.setAdapter(adapter);
                        in_order_list_details1.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                case 6:
                    try {
                        String res = (String) msg.obj;
                        if (res == null) {
                            return;
                        } else {
                            JSONObject result = new JSONObject(res);
                            String success = result.getString("Success");
                            String err = result.getString("ErrMsg");
                            String data = result.getString("Data");
                            list = new ArrayList<HashMap<String, Object>>();
                            list.clear();
                            //List<AnnouncementInfo> items = new ArrayList<AnnouncementInfo>();
                            if (data.equals("")) {
                                final MyAdapter adapter = new MyAdapter(Beater_Order_Activity.this);
                                in_order_list_details1.setAdapter(null);
                            } else {
                                JSONArray arr = new JSONArray(data);
                                HashMap<String, Object> hashMap;
                                for (int i = 0; i < arr.length(); i++) {
                                    hashMap = new HashMap<String, Object>();
                                    JSONObject inresult = (JSONObject) arr.get(i);
                                    hashMap.put("OrderID", inresult.getString("OrderID"));
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
                                    //hashMap.put("PiblisherPicture", inresult.getString("PiblisherPicture"));
                                    //orderId = hashMap.get("OrderID").toString();
                                    list.add(hashMap);
                                }
                            }

                            if (success == "true") {
                                // Toast.makeText(AllOrdersActivity.this, success, Toast.LENGTH_LONG).show();
                            } else if (success == "false") {
                                Toast.makeText(Beater_Order_Activity.this, err, Toast.LENGTH_LONG).show();
                            }
                            final MyAdapter adapter = new MyAdapter(Beater_Order_Activity.this);
                            in_order_list_details1.setAdapter(adapter);
                            in_order_list_details1.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                        }
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
                        list = new ArrayList<HashMap<String, Object>>();
                        list.clear();
                        //List<AnnouncementInfo> items = new ArrayList<AnnouncementInfo>();
                        if (data.equals("")) {
                            final MyAdapter adapter = new MyAdapter(Beater_Order_Activity.this);
                            in_order_list_details1.setAdapter(null);
                        } else {
                            JSONArray arr = new JSONArray(data);
                            HashMap<String, Object> hashMap;
                            for (int i = 0; i < arr.length(); i++) {
                                hashMap = new HashMap<String, Object>();
                                JSONObject inresult = (JSONObject) arr.get(i);
                                hashMap.put("OrderID", inresult.getString("OrderID"));
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
                                //hashMap.put("PiblisherPicture", inresult.getString("PiblisherPicture"));
                                //orderId = hashMap.get("OrderID").toString();
                                list.add(hashMap);
                            }
                        }

                        if (success == "true") {
                            // Toast.makeText(AllOrdersActivity.this, success, Toast.LENGTH_LONG).show();
                        } else if (success == "false") {
                            Toast.makeText(Beater_Order_Activity.this, err, Toast.LENGTH_LONG).show();
                        }
                        final MyAdapter adapter = new MyAdapter(Beater_Order_Activity.this);
                        in_order_list_details1.setAdapter(adapter);
                        in_order_list_details1.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private class MyAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        HashMap<Integer, ViewHold> holders;

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
            convertView = inflater.inflate(R.layout.beater_order_item_layout, null, false);
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap = list.get(position);
            ViewHold viewHold = new ViewHold();
            holders.put(position, viewHold);
            viewHold.orderii = (LinearLayout) convertView.findViewById(R.id.orderii);
            viewHold.in_publisher_head = (ImageView) convertView.findViewById(R.id.in_publisher_head);
            viewHold.in_publisher_NickName = (TextView) convertView
                    .findViewById(R.id.in_publisher_NickName);
            viewHold.in_money_state = (TextView) convertView
                    .findViewById(R.id.in_money_state);
            viewHold.in_publisher_begin_rank = (TextView) convertView
                    .findViewById(R.id.in_publisher_begin_rank);
            viewHold.in_publisher_goal_rank = (TextView) convertView
                    .findViewById(R.id.in_publisher_goal_rank);
            viewHold.in_publisher_area = (TextView) convertView
                    .findViewById(R.id.in_publisher_area);
            viewHold.in_publisher_hero_name = (TextView) convertView
                    .findViewById(R.id.in_publisher_hero_name);
            viewHold.in_game_left_time = (TextView) convertView
                    .findViewById(R.id.in_game_left_time);
            viewHold.in_publisher_money = (TextView) convertView
                    .findViewById(R.id.in_publisher_money);
            viewHold.in_publisher_see_details = (Button) convertView
                    .findViewById(R.id.in_publisher_see_details);
            if (test == "2") {
                viewHold.in_money_state.setText("代练中");
            } else if (test == "3") {
                viewHold.in_money_state.setText("撤单中");
            } else if (test == "4") {
                viewHold.in_money_state.setText("待确认");
            } else if (test == "5") {
                viewHold.in_money_state.setText("退款中");
            } else if (test == "6") {
                viewHold.in_money_state.setText("待评价");
            } else if (test == "7") {
                viewHold.in_money_state.setText("已完成");
            }
            viewHold.in_publisher_NickName.setText(hashMap.get("NickName").toString());
            if (hashMap.get("GameType").toString().equals("1")) {
                viewHold.in_publisher_begin_rank.setText(hashMap.get("PiblisherBigRank").toString() + hashMap.get("PiblisherMediumRank").toString() + hashMap.get("PiblisherRank").toString());
                viewHold.in_publisher_goal_rank.setText(hashMap.get("PiblisherGoalBigRank").toString() + hashMap.get("PiblisherGoalMediumRank").toString() + hashMap.get("PiblisherGoalRank").toString());
            } else if (hashMap.get("GameType").toString().equals("2")) {
                viewHold.in_publisher_begin_rank.setText(hashMap.get("LOLRank").toString());
                viewHold.in_publisher_goal_rank.setText(hashMap.get("LOLGoalRank").toString());
            }
            if (Integer.parseInt(hashMap.get("GameOS").toString()) == 1) {
                viewHold.in_publisher_area.setText("手Q安卓" + "/" + hashMap.get("GameArea").toString());
            } else if (Integer.parseInt(hashMap.get("GameOS").toString()) == 2) {
                viewHold.in_publisher_area.setText("手Q苹果" + "/" + hashMap.get("GameArea").toString());
            } else if (Integer.parseInt(hashMap.get("GameOS").toString()) == 3) {
                viewHold.in_publisher_area.setText("微信安卓" + "/" + hashMap.get("GameArea").toString());
            } else if (Integer.parseInt(hashMap.get("GameOS").toString()) == 4) {
                viewHold.in_publisher_area.setText("微信苹果" + "/" + hashMap.get("GameArea").toString());
            } else if (Integer.parseInt(hashMap.get("GameOS").toString()) == 5) {
                viewHold.in_publisher_area.setText("网通" + "/" + hashMap.get("GameArea").toString());
            } else if (Integer.parseInt(hashMap.get("GameOS").toString()) == 6) {
                viewHold.in_publisher_area.setText("电信" + "/" + hashMap.get("GameArea").toString());
            } else if (Integer.parseInt(hashMap.get("GameOS").toString()) == 7) {
                viewHold.in_publisher_area.setText("其他" + "/" + hashMap.get("GameArea").toString());
            }

            viewHold.in_publisher_hero_name.setText("");
            String url = "/api/File/GetUserhead?filename=" + hashMap.get("Picture").toString();
            //String url = "/api/File/GetUserhead?filename=" + HttpPostUtils.getPicture();
            BitmapLoader.getImageFromLocalFirstNorDownload(url, "userhead", hashMap.get("Picture").toString(), handlerPic, position);
            //HttpFileHelper.httpGetFile(position, url, handlerPic);
            String time = hashMap.get("PiblishDateTime").toString().substring(0, 10);
            viewHold.in_game_left_time.setText(time);
            viewHold.in_publisher_money.setText(hashMap.get("OrderPrice").toString());
            od = hashMap.get("OrderID").toString();
            final HashMap<String, Object> finalHashMap = hashMap;
            viewHold.orderii.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(Beater_Order_Activity.this, Sure_Order_Activity.class);
                    intent.putExtra("orderid", finalHashMap.get("OrderID").toString());
                    startActivity(intent);
                    finish();
                }
            });
            viewHold.in_publisher_see_details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(Beater_Order_Activity.this, Sure_Order_Activity.class);
                    intent.putExtra("orderid", finalHashMap.get("OrderID").toString());
                    startActivity(intent);
                    finish();
                }
            });
            return convertView;
        }

        class ViewHold {
            private LinearLayout orderii;
            private ImageView in_publisher_head;
            private TextView in_publisher_NickName;
            private TextView in_money_state;
            private TextView in_publisher_begin_rank;
            private TextView in_publisher_goal_rank;
            private TextView in_publisher_area;
            private TextView in_publisher_hero_name;
            private TextView in_game_left_time;
            private TextView in_publisher_money;
            private Button in_publisher_see_details;
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
                        if(holder != null)
                            holder.in_publisher_head.setImageBitmap(bmp);
                    }
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
