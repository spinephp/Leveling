package com.youyudj.leveling.new_chat;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.youyudj.leveling.utils.BitmapLoader;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

import com.youyudj.leveling.R;

/**
 * Created by lin on 2018/3/26.
 */

public class ChatActivity extends ChatBaseActivity implements View.OnClickListener, IMSession.OnMessageArrivalListener, MessageRecyclerHolder.QueryHeadInterface {

    private final static String TAG = ChatActivity.class.getSimpleName();

    //调用系统相册-选择图片
    private static final int IMAGE = 1;

    private ImageView image_btn;

    private List<ChatMessage> msgList = new ArrayList<ChatMessage>();

    private Button btn_send;
    private EditText et_content;

    private static final int INDEX = 1;

    private static final int LOGIN = INDEX + 1;
    private static final int SEND = INDEX + 2;
    private static final int QUERY = INDEX + 3;
    private static final int LOGOUT = INDEX + 4;
    private static final int SENDIMAGE = INDEX + 5;
    private static final int GETIMAGE = INDEX + 6;
    private static final int QUERYLIST = INDEX + 7;

    private int takeSize = 10;

    private PtrFrameLayout listview_ptr;
    private RecyclerView listview_recy;

    //缓存bitmap
    private Bitmap bitmap_cache = null;

    //聊天列表图片存放路径
    private static final String FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Chat/Image/";

    //头像图片存放路径
    private static final String HEAD_ICON_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Chat/head/";

    //                  头部昵称显示   返回
    private TextView tv_toUsername, tv_goBack;

    //下载图片的文件名
    private String get_mine_name, get_friend_name;

    //                  我的头像  朋友头像
    private Bitmap my_head, friend_head;
    //               第一个头像给mine
    private boolean is_mine = true;

    private UIHandler myHandler = new UIHandler(this);

    // 创建会话保存的信息
    private SessionBean bean;

    //聊天信息列表图片是属于谁的
    private boolean is_my_pic = true;

    //图片下载的文件名
    private String temp_filename;

    private LinearLayoutManager layout;

    private MessageRecyclerAdapter adapters;

    @Override
    public void onMessageArrival(JSONArray msg) {
        if (msg != null) {
            getQueryResponse(msg);
            Log.d(TAG, msg.toString());
        }
    }

    @Override
    public Bitmap getMineHead() {
        return my_head;
    }

    @Override
    public Bitmap getFriendHead() {
        return friend_head;
    }

    private static class UIHandler extends Handler {
        WeakReference<ChatActivity> softReference;

        public UIHandler(ChatActivity activity) {
            softReference = new WeakReference<ChatActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }

    private String orderID = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        orderID = getIntent().getStringExtra("orderID");
        IMSession.getInstance().addOnMessageArrivalListener(this);
        init();
    }

    private void init() {
        image_btn = (ImageView) findViewById(R.id.image_btn);
        image_btn.setOnClickListener(this);

        listview_recy = (RecyclerView) findViewById(R.id.listView_recycler);
        listview_ptr = (PtrFrameLayout) findViewById(R.id.listView_ptr);

        layout = new LinearLayoutManager(this);
        layout.setStackFromEnd(true); //反转显示
        layout.setReverseLayout(true);
        listview_recy.setLayoutManager(layout);
        BasePtr.setPagedPtrStyle(listview_ptr);
        listview_ptr.setMode(PtrFrameLayout.Mode.REFRESH);
        listview_ptr.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //if (msgList.size() == takeSize * (pageIndex - 1))
                sendRequest(Const.getQueryStringWithToken(Const.QUERY_LIST, "jumpSize", msgList.size(), "takeSize", takeSize), QUERYLIST);
                //else
                //    listview_ptr.refreshComplete();
            }
        });

        btn_send = (Button) findViewById(R.id.btn_send);
        et_content = (EditText) findViewById(R.id.et_content);

        adapters = new MessageRecyclerAdapter(msgList);
        adapters.setQueryHeadInterface(this);
        listview_recy.setAdapter(adapters);
        btn_send.setOnClickListener(this);

        tv_goBack = (TextView) findViewById(R.id.tv_goBack);
        tv_goBack.setOnClickListener(this);
        tv_toUsername = (TextView) findViewById(R.id.tv_toUsername);

        sendRequest(Const.getQueryStringWithToken(Const.CREATE_SESSION, "orderID", orderID), LOGIN);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        IMSession.getInstance().removeOnMessageArrivalListener(this);
    }

    public int getScollYDistance() {
        int position = layout.findFirstVisibleItemPosition();
        View firstVisiableChildView = layout.findViewByPosition(position);
        int firstVisiableChildViewTop = firstVisiableChildView.getTop();
        int itemHeight = firstVisiableChildView.getHeight();
        //可见的item的index*item高度-最顶端位置
        return (position) * itemHeight - firstVisiableChildViewTop;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_btn:
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, IMAGE);
                break;
            case R.id.btn_send:
                String content = et_content.getText().toString().trim();
                if (TextUtils.isEmpty(content))
                    return;
                //发送文本信息
                sendRequest(Const.getQueryStringWithToken(Const.SEND, "chatMessage", content), SEND);
                break;
            case R.id.tv_goBack:
                //退出聊天室
                sendRequest(Const.getQueryStringWithToken(Const.CLOSE_SESSION), LOGOUT);
                break;
        }
    }

    private void appendMessage(ChatMessage msg) {
        msgList.add(0, msg);
        adapters.setDataList(msgList);
        et_content.setText("");
        et_content.clearFocus();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取图片路径
        if (requestCode == IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            String imagePath = c.getString(columnIndex);
            sendImageRequest(Const.getQueryStringWithToken(Const.SEND_IMAGE), SENDIMAGE, imagePath);
            c.close();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        sendRequest(Const.getQueryStringWithToken(Const.CLOSE_SESSION), LOGOUT);
    }

    @Override
    public void getSuccessResponse(int what, Response<JSONObject> response) {

        super.getSuccessResponse(what, response);
        Log.d(TAG, "getSuccessResponse: " + response.get().toString());
        switch (what) {
            case LOGIN:
                getLoginResponse(response);
                break;
            case QUERY:
                //getQueryResponse(response.get());
                break;
            case SEND:
                getSendResponse(response);
                break;
            case SENDIMAGE:
                getSendImageResponse(response);
                break;
            case LOGOUT:
                getLogoutResponse(response);
                break;
            case QUERYLIST:
                getQueryListResponse(response);
                break;
        }
    }

    private ChatMessage msgBeanToChatMsg(MessageBean mb) {
        ChatMessage msg = new ChatMessage();
        if (bean.getMine().getUserID().equals(mb.getFromUserID())) {
            msg.setIsMine(true);
        } else {
            msg.setIsMine(false);
        }
        msg.setMsgType(mb.getType());
        msg.setMsgContent(mb.getMessageContent());
        return msg;
    }

    private void showMessageList(JSONArray arr) {
        for (int i = 0; i < arr.length(); i++) {
            ChatMessage msg = null;
            try {
                msg = msgBeanToChatMsg(new MessageBean(arr.getJSONObject(i)));
                msgList.add(msg);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        adapters.setDataList(msgList);
    }

    private void getQueryListResponse(Response<JSONObject> response) {
        try {
            listview_ptr.refreshComplete();
            Log.d(TAG, "getQueryListResponse: " + response.get());
            JSONArray array = response.get().getJSONArray("Data");
            showMessageList(array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getFailedResponse(int what, Response<JSONObject> response) {
        super.getFailedResponse(what, response);
        JSONObject json = response.get();
        if (json == null)
            Log.d(TAG, "getFailedResponse: null");
        else
            Log.d(TAG, "getFailedResponse: " + json.toString());
    }

    //退出聊天室
    private void getLogoutResponse(Response<JSONObject> response) {
        Log.d(TAG, "getLogoutResponse: " + response.get().toString());
        try {
            JSONObject object = response.get();
            if (object.getBoolean("Succ")) {
                Toast.makeText(this, "您已经退出聊天室", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "退出失败", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        finish();
    }

    @Override
    public void getSuccessImageResponse(int what, Response<byte[]> response) {
        super.getSuccessImageResponse(what, response);
        switch (what) {
            case GETIMAGE:
                downloadHeadImageResponse(response);
                break;
        }
    }

    private void downloadHeadImageResponse(Response<byte[]> response) {
        byte[] bsData = response.get();
        String statusText = null;
        int imageDataFrom = 0;
        for (int i = 0; i < bsData.length; i++) {
            if (bsData[i] == '\n') {
                statusText = new String(bsData, 0, i);
                imageDataFrom = i + 1;
                break;
            }
        }
        if (statusText == null)
            return;
        try {
            JSONObject status = new JSONObject(statusText);
            if (status.getString("Succ") != null || status.getBoolean("Succ")) {
                String fileName = status.getString("Data");
                Bitmap bmp = BitmapFactory.decodeByteArray(bsData, imageDataFrom, bsData.length - imageDataFrom);
                if (bean.getMine().getPicture().equals(fileName)) {
                    my_head = bmp;
                    adapters.notifyDataSetChanged();
                } else if (bean.getFriend().getPicture().equals(fileName)) {
                    friend_head = bmp;
                    adapters.notifyDataSetChanged();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //发送图片回复
    private void getSendImageResponse(Response<JSONObject> response) {
        try {
            JSONObject object = response.get();
            if (object.getBoolean("Succ")) {
                MessageBean bean = new MessageBean(object.getJSONObject("Data"));
                ChatMessage msg = msgBeanToChatMsg(bean);
                appendMessage(msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //发送文字回复
    private void getSendResponse(Response<JSONObject> res) {
        try {
            JSONObject object = res.get();
            if (object.getBoolean("Succ")) {
                MessageBean bean = new MessageBean(object.getJSONObject("Data"));
                ChatMessage msg = msgBeanToChatMsg(bean);
                appendMessage(msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /* start of james edit */
    Handler handlerHeadImage = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            BitmapLoader.StatusBitmap sb = (BitmapLoader.StatusBitmap)msg.obj;
            if(sb == null)
                return;
            if(msg.what == 1){
                my_head = sb.getBitmap();
                adapters.notifyDataSetChanged();
            }else if(msg.what == 2){
                friend_head = sb.getBitmap();
                adapters.notifyDataSetChanged();
            }
        }
    };
    /* end of james edit */

    //创建回话回复
    private void getLoginResponse(Response<JSONObject> response) {
        try {
            JSONObject object = response.get();
            if (object.getBoolean("Succ")) {
                //解析数据
                bean = new SessionBean(object.getJSONObject("Data"));
                get_mine_name = bean.getMine().getPicture();
                get_friend_name = bean.getFriend().getPicture();
                //执行头像下载请求
                /* start of james edit */
                String url = Const.getQueryStringWithToken(Const.GET_IMAGE, "fileName", bean.getMine().getPicture(), "at", "userhead");
                BitmapLoader.getImageFromLocalFirstNorDownloadWithStatusResponse(url, "userhead", bean.getMine().getPicture(), handlerHeadImage, 1);
                url = Const.getQueryStringWithToken(Const.GET_IMAGE, "fileName", bean.getFriend().getPicture(), "at", "userhead");
                BitmapLoader.getImageFromLocalFirstNorDownloadWithStatusResponse(url, "userhead", bean.getFriend().getPicture(), handlerHeadImage, 2);
                /* end of james edit */
                sendRequest(Const.getQueryStringWithToken(Const.QUERY_LIST, "jumpSize", msgList.size(), "takeSize", takeSize), QUERYLIST);
            } else {
                Toast.makeText(this, "创建会话失败", Toast.LENGTH_LONG).show();
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 请求列表回复
    private void getQueryResponse(JSONArray msgArr) {
        Log.d(TAG, "在getQueryResponse的成功情况下执行query操作");
        for (int i = 0; i < msgArr.length(); i++) {
            ChatMessage msg = null;
            try {
                msg = msgBeanToChatMsg(new MessageBean(msgArr.getJSONObject(i)));
                msgList.add(0, msg);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        adapters.setDataList(msgList);
    }
}
