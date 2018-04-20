package com.youyudj.leveling;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.youyudj.leveling.entity.MessageItemBean;
import com.youyudj.leveling.new_chat.BasePtr;
import com.youyudj.leveling.new_chat.ChatActivity;
import com.youyudj.leveling.new_chat.Const;
import com.youyudj.leveling.personcenter.SystemInfoActivity;
import com.youyudj.leveling.utils.HttpGetUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * *******************************************************
 * Description:
 * Autour: 3W攻城狮
 * Date: 2017/11/8 11:26
 * Update:2017/11/8
 * Version:
 * *******************************************************
 */
public class MessageFragment extends Fragment {

    private static String TAG = MessageFragment.class.getSimpleName();

    private PtrFrameLayout ptr_chat;
    private RecyclerView recy_chat;

    private MessageListAdapter adapter;

    //系统通知数量显示
    private TextView system_info_num;
    //订单通知数量显示
    private TextView order_info_num;

    private List<MessageItemBean> list = new ArrayList<MessageItemBean>();

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.message_layout, null);
        system_info_num = (TextView)view.findViewById(R.id.system_info_num);
        order_info_num = (TextView)view.findViewById(R.id.order_info_num);
        view.findViewById(R.id.system_info_extra).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),SystemInfoActivity.class));
            }
        });
        view.findViewById(R.id.notification_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),OrderNoticeListActivity.class));
            }
        });
        getMessageList(view);
       return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getNonReadAnnounceCount();
        getNonReadOrderNoticeCount();
    }

    //初始化列表组件并执行获取信息请求
    private void getMessageList(View view)
    {
        ptr_chat = (PtrFrameLayout)view.findViewById(R.id.chat_ptr);
        recy_chat = (RecyclerView)view.findViewById(R.id.chat_recy);
        LinearLayoutManager layout = new LinearLayoutManager(getContext());
        recy_chat.setLayoutManager(layout);
        BasePtr.setPagedPtrStyle(ptr_chat);
        ptr_chat.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                /* start of james edit */
                ptr_chat.refreshComplete();
            }
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getFriendListMessage();
                /* end of james edit */
            }
        });
        getFriendListMessage();
    }

    //请求数据
    private void getFriendListMessage()
    {
        String url = Const.getQueryStringWithToken(Const.GET_FRIEND_LIST);
        HttpGetUtils.httpGetFile(100001,url,myHandler);
    }

    /**
     * 2018.04.09 add by lin
     * 两个请求数据的方法
     * 开始
     * */

    //获取未读系统信息
    private void getNonReadAnnounceCount()
    {
        String url = Const.getQueryString("/api/Announcement/GetNonReadAnnouncementCount");
        HttpGetUtils.httpGetFile(100002,url, myHandler);
    }

//   获取未读订单通知信息数量
    private void getNonReadOrderNoticeCount()
    {
        String url = Const.getQueryString("/api/Announcement/GetNonReadOrderNoticeCount");
        HttpGetUtils.httpGetFile(100003,url, myHandler);
    }

    /**
     * 两个请求数据的方法
     * 结束
     * */


    private Handler myHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 100001:
                    try {
                        ptr_chat.refreshComplete();
                        String res = (String) msg.obj;
                        if (res==null){
                            return;
                        }
                        Log.d(TAG, "消息列表:"+res);
                        JSONObject obj = new JSONObject(res);
                        String success = obj.getString("Succ");
                        String err = obj.getString("ErrMsg");
                        if(success.equals("true"))
                        {
                            JSONArray array = obj.getJSONArray("Data");
                            list.clear();
                            for(int i=0; i<array.length(); i++)
                            {
                                MessageItemBean bean = new MessageItemBean(array.getJSONObject(i));
                                list.add(bean);
                            }
                            if(adapter == null)
                            {
                                adapter = new MessageListAdapter(list);
                                recy_chat.setAdapter(adapter);
                                adapter.setListener(new MessageListAdapter.OnitemClickListener() {
                                    @Override
                                    public void onItemClick(View v, MessageItemBean bean) {
                                        Intent intent = new Intent(getContext(), ChatActivity.class);
                                        intent.putExtra("orderID",bean.getOrderID());
                                        startActivity(intent);
                                    }
                                });
                            }
//                            adapter = new MessageListAdapter(list);
                            adapter.setDataList(list);
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;

                /**
                 *2018.04.09 add by lin
                 *两个方法请求的回复处理
                 *开始
                 * */
                case 100002:
                    try {
                        String res = (String) msg.obj;
                        if (res==null){
                            return;
                        }
                        Log.d(TAG, "系统列表数量:"+res);
                        JSONObject obj = new JSONObject(res);
                        String success = obj.getString("Success");
                        String err = obj.getString("ErrMsg");
                        if(success.equals("true"))
                        {
                            system_info_num.setText(obj.getString("Data"));
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                case 100003:
                    try {
                        String res = (String) msg.obj;
                        if (res==null){
                            return;
                        }
                        Log.d(TAG, "订单列表数量:"+res);
                        JSONObject obj = new JSONObject(res);
                        String success = obj.getString("Success");
                        String err = obj.getString("ErrMsg");
                        if(success.equals("true"))
                        {
                            order_info_num.setText(obj.getString("Data"));
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                /**
                 *2018.04.09 add by lin
                 *两个方法请求的回复处理
                 *结束
                 * */
            }
        }
    };







}
