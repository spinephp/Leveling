package com.leveling;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.leveling.chat.app.ChatApplication;
import com.leveling.chat.utils.FileSaveUtil;
import com.leveling.entity.BeaterOrder;
import com.leveling.entity.OrderBean;
import com.leveling.entity.OrderType;
import com.leveling.new_chat.BasePtr;
import com.leveling.personcenter.AllOrdersActivity;
import com.leveling.utils.HttpGetUtils;
import com.leveling.utils.HttpPostUtils;
import com.leveling.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by lin on 2018/4/8.
 */

public class OrderNoticeListActivity extends AppCompatActivity {


    private static String TAG = OrderNoticeListActivity.class.getSimpleName();

    private PtrFrameLayout order_ptr;
    private RecyclerView order_recy;

    private List<OrderBean> list = new ArrayList<OrderBean>();

    private OrderListAdapter adapter;

    private LinearLayout img_orderpp_back;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_notice_list);

        img_orderpp_back = (LinearLayout) findViewById(R.id.img_orderpp_back);
        img_orderpp_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        order_ptr = (PtrFrameLayout) findViewById(R.id.order_ptr);
        order_recy = (RecyclerView) findViewById(R.id.order_recy);
        LinearLayoutManager layout = new LinearLayoutManager(this);
        order_recy.setLayoutManager(layout);
        BasePtr.setPagedPtrStyle(order_ptr);
        order_ptr.setMode(PtrFrameLayout.Mode.NONE);
        order_ptr.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                order_ptr.refreshComplete();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                order_ptr.refreshComplete();
            }
        });

        String url = "/api/Announcement/GetOrderNotice";
        HttpGetUtils.httpGetFile(GET_ORDER_NOTICE, url, myHandler);
    }

    private final static int GET_ORDER_NOTICE = 10;   //获取订单通知
    private final static int SET_ORDER_NOTICE_STATUS = 11;  //设置订单已读状态
    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GET_ORDER_NOTICE:
                    try {
                        String res = (String) msg.obj;
                        if (res == null) {
                            return;
                        }
                        Log.d(TAG, "订单列表：" + res);
                        JSONObject obj = new JSONObject(res);
                        String success = obj.getString("Success");
                        String err = obj.getString("ErrMsg");
                        if (success.equals("true")) {
                            JSONArray array = obj.getJSONArray("Data");
                            for (int i = 0; i < array.length(); i++) {
                                OrderBean bean = new OrderBean(array.getJSONObject(i));
                                list.add(bean);
                            }
                            if (adapter == null) {
                                adapter = new OrderListAdapter(list);
                                order_recy.setAdapter(adapter);
                                adapter.setListener(new OrderListAdapter.OnitemClickListener() {
                                    @Override
                                    public void onItemClick(View v, int pos) {
                                        try {
                                            OrderBean bean = list.get(pos);
                                            String url = "/api/Announcement/SetOrderNoticeReadState?noticeID=" + bean.getNoticeID();
                                            HttpGetUtils.httpGetFile(SET_ORDER_NOTICE_STATUS, url, myHandler);
                                            bean.setRead(true);
                                            RecyclerView.ViewHolder viewHolder = order_recy.findViewHolderForAdapterPosition(pos);
                                            if (viewHolder != null) {
                                                ((OrderListHolder) viewHolder).displayData(bean);
                                            }
                                            if (HttpPostUtils.getRole() == 0) {
                                                OrderType.orderType = "5";
                                                Intent it = new Intent(ChatApplication.getInstance(), AllOrdersActivity.class);
                                                OrderNoticeListActivity.this.startActivity(it);
                                            } else {
                                                BeaterOrder.orderType = "1";
                                                Intent it = new Intent(ChatApplication.getInstance(), Beater_Order_Activity.class);
                                                OrderNoticeListActivity.this.startActivity(it);
                                            }
                                        }
                                        catch (Exception ex){
                                            Toast.makeText(OrderNoticeListActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                                            FileSaveUtil.writeBytes(Environment.getExternalStorageDirectory().getAbsolutePath() + "/djlog.txt", ex.toString().getBytes(), true);
                                        }
                                    }
                                });
                            } else
                                adapter.setDataList(list);
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                case SET_ORDER_NOTICE_STATUS:
                    try {
                        String res = (String) msg.obj;
                        if (res == null) {
                            return;
                        }
                        Log.d(TAG, "订单列表：" + res);
                        JSONObject obj = new JSONObject(res);
                        String success = obj.getString("Success");
                        String err = obj.getString("ErrMsg");
                        if (success.equals("true")) {

                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };


}
