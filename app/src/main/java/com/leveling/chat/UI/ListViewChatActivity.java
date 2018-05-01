package com.leveling.chat.UI;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.Toast;

import com.leveling.personcenter.LevelingActivity;
import com.leveling.chat.adapter.ChatListViewAdapter;
import com.leveling.chat.common.ChatConst;
import com.leveling.chat.db.ChatMessageBean;
import com.leveling.chat.utils.KeyBoardUtils;
import com.leveling.chat.widget.AudioRecordButton;
import com.leveling.chat.widget.pulltorefresh.PullToRefreshListView;
import com.leveling.chat.widget.pulltorefresh.base.PullToRefreshView;
import com.leveling.utils.HttpPostUtils;
import com.zsoft.SignalA.Hubs.HubInvokeCallback;
import com.zsoft.SignalA.Hubs.HubOnDataCallback;

import org.json.JSONArray;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mao Jiqing on 2016/10/10.
 */
public class ListViewChatActivity extends BaseActivity {
    public PullToRefreshListView myList;
    public ChatListViewAdapter tbAdapter;
    private SendMessageHandler sendMessageHandler;
    private String TAG = ListViewChatActivity.class.getName();
    String img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        tblist.clear();
        myList.setAdapter(null);
        sendMessageHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void findView() {
        super.findView();
        pullList.setSlideView(new PullToRefreshView(this).getSlideView(PullToRefreshView.LISTVIEW));
        myList = (PullToRefreshListView) pullList.returnMylist();
    }

    @Override
    protected void init() {
//        setTitle("ListView");
        sendMessageHandler = new SendMessageHandler(this);
        tbAdapter = new ChatListViewAdapter(this);
        tbAdapter.setUserList(tblist);
        myList.setAdapter(tbAdapter);
        tbAdapter.isPicRefresh = true;
        tbAdapter.notifyDataSetChanged();
        tbAdapter.setSendErrorListener(new ChatListViewAdapter.SendErrorListener() {
            @Override
            public void onClick(int position) {
                // TODO Auto-generated method stub
                ChatMessageBean tbub = tblist.get(position);
                if (tbub.getType() == ChatListViewAdapter.TO_USER_VOICE) {
                    sendVoice(tbub.getUserVoiceTime(), tbub.getUserVoicePath());
                    tblist.remove(position);
                } else if (tbub.getType() == ChatListViewAdapter.TO_USER_IMG) {
                    sendImage(tbub.getImageLocal());
                    tblist.remove(position);
                }
            }

        });
        tbAdapter.setVoiceIsReadListener(new ChatListViewAdapter.VoiceIsRead() {
            @Override
            public void voiceOnClick(int position) {
                // TODO Auto-generated method stub
                for (int i = 0; i < tbAdapter.unReadPosition.size(); i++) {
                    if (tbAdapter.unReadPosition.get(i).equals(position + "")) {
                        tbAdapter.unReadPosition.remove(i);
                        break;
                    }
                }
            }

        });
        myList.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub
                switch (scrollState) {
                    case SCROLL_STATE_IDLE:
                        tbAdapter.handler.removeCallbacksAndMessages(null);
                        tbAdapter.setIsGif(true);
                        tbAdapter.isPicRefresh = false;
                        tbAdapter.notifyDataSetChanged();
                        break;
                    case SCROLL_STATE_TOUCH_SCROLL:
                        tbAdapter.handler.removeCallbacksAndMessages(null);
                        tbAdapter.setIsGif(false);
                        tbAdapter.isPicRefresh = true;
                        reset();
                        KeyBoardUtils.hideKeyBoard(ListViewChatActivity.this,
                                mEditTextContent);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });

        voiceBtn.setAudioFinishRecorderListener(new AudioRecordButton.AudioFinishRecorderListener() {

            @Override
            public void onFinished(float seconds, String filePath) {
                // TODO Auto-generated method stub
                sendVoice(seconds, filePath);
            }

            @Override
            public void onStart() {
                // TODO Auto-generated method stub
                tbAdapter.stopPlayVoice();
            }
        });
        super.init();
    }

    static class SendMessageHandler extends Handler {
        WeakReference<ListViewChatActivity> mActivity;

        SendMessageHandler(ListViewChatActivity activity) {
            mActivity = new WeakReference<ListViewChatActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            ListViewChatActivity theActivity = mActivity.get();
            if (theActivity != null) {
                switch (msg.what) {
                    case REFRESH:
                        theActivity.tbAdapter.isPicRefresh = true;
                        theActivity.tbAdapter.notifyDataSetChanged();
                        theActivity.myList.setSelection(theActivity.tblist
                                .size() - 1);
                        break;
                    case SEND_OK:
                        theActivity.mEditTextContent.setText("");
                        theActivity.tbAdapter.isPicRefresh = true;
                        theActivity.tbAdapter.notifyDataSetChanged();
                        theActivity.myList.setSelection(theActivity.tblist
                                .size() - 1);
                        break;
                    case RECERIVE_OK:
                        theActivity.tbAdapter.isPicRefresh = true;
                        theActivity.tbAdapter.notifyDataSetChanged();
                        theActivity.myList.setSelection(theActivity.tblist
                                .size() - 1);
                        break;
                    case PULL_TO_REFRESH_DOWN:
                        theActivity.pullList.refreshComplete();
                        theActivity.tbAdapter.notifyDataSetChanged();
                        theActivity.myList.setSelection(theActivity.position - 1);
                        theActivity.isDown = false;
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    protected void loadRecords() {
        isDown = true;
        if (pagelist != null) {
            pagelist.clear();
        }
        pagelist = mChatDbManager.loadPages(page, number);
        position = pagelist.size();
        if (pagelist.size() != 0) {
            pagelist.addAll(tblist);
            tblist.clear();
            tblist.addAll(pagelist);
            if (imageList != null) {
                imageList.clear();
            }
            if (imagePosition != null) {
                imagePosition.clear();
            }
            int key = 0;
            int position = 0;
            for (ChatMessageBean cmb : tblist) {
                if (cmb.getType() == ChatListViewAdapter.FROM_USER_IMG || cmb.getType() == ChatListViewAdapter.TO_USER_IMG) {
                    imageList.add(cmb.getImageLocal());
                    imagePosition.put(key, position);
                    position++;
                }
                key++;
            }
            tbAdapter.setImageList(imageList);
            tbAdapter.setImagePosition(imagePosition);
            sendMessageHandler.sendEmptyMessage(PULL_TO_REFRESH_DOWN);
            if (page == 0) {
                pullList.refreshComplete();
                pullList.setPullGone();
            } else {
                page--;
            }
        } else {
            if (page == 0) {
                pullList.refreshComplete();
                pullList.setPullGone();
            }
        }
    }

    /**
     * 发送文字
     */
    @Override
    protected void sendMessage(final String orderId,final String receiverId, final String receiverName,final String message,final String operator) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HubInvokeCallback callback = new HubInvokeCallback() {
                    @Override
                    public void OnResult(boolean succeeded, String response) {
                        // TODO Auto-generated method stub
                        Log.d(TAG, response);
                    }
                    @Override
                    public void OnError(Exception ex) {
                        // TODO Auto-generated method stub
                        Log.d(TAG, ex.getMessage());
                    }
                };
                String content = mEditTextContent.getText().toString();
                List<String> args = new ArrayList<String>(3);
                args.add(orderId);
                args.add(receiverId);
                args.add(receiverName);
                args.add(message);
                tblist.add(getTbub(HttpPostUtils.getUserId(),orderId,receiverId,receiverName, ChatListViewAdapter.TO_USER_MSG, content, null, null,
                        null, null, null, 0f, ChatConst.COMPLETED));
                sendMessageHandler.sendEmptyMessage(SEND_OK);
                receriveHandler.sendEmptyMessageDelayed(0, 1000);
                hub.Invoke(operator, args, callback);
            }
        }).start();
    }
    /**
     * 接收文字
     */
    String content = "";
    JSONArray args = new JSONArray();
    private void receriveMsgText(JSONArray args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                hub.On("receivePrivateMessage", new HubOnDataCallback() {
                    // TODO Auto-generated method stub
                    public void OnReceived(JSONArray args) {
                        String message = args.opt(3).toString();
                        ChatMessageBean tbub = new ChatMessageBean();
                        String time = returnTime();
                        tbub.setUserContent(message);
                        tbub.setTime(time);
                        tbub.setType(ChatListViewAdapter.FROM_USER_MSG);
                        tblist.add(tbub);
                        sendMessageHandler.sendEmptyMessage(RECERIVE_OK);
                        mChatDbManager.insert(tbub);
                    }
                });
            }
        }).start();
    }
    /**
     * 发送图片
     */
    int i = 0;

    @Override
    protected void sendImage(final String filePath) {
         img = null;
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (i == 0) {
                    tblist.add(getTbub(HttpPostUtils.getUserId(),LevelingActivity.Orderid, LevelingActivity.receiverId,LevelingActivity.receivernickname, ChatListViewAdapter.TO_USER_IMG, null, null, null, filePath, null, null,
                            0f, ChatConst.SENDING));
                } else if (i == 1) {
                    tblist.add(getTbub(HttpPostUtils.getUserId(),LevelingActivity.Orderid, LevelingActivity.receiverId,LevelingActivity.receivernickname, ChatListViewAdapter.TO_USER_IMG, null, null, null, filePath, null, null,
                            0f, ChatConst.SENDERROR));
                } else if (i == 2) {
                    tblist.add(getTbub(HttpPostUtils.getUserId(),LevelingActivity.Orderid, LevelingActivity.receiverId,LevelingActivity.receivernickname, ChatListViewAdapter.TO_USER_IMG, null, null, null, filePath, null, null,
                            0f, ChatConst.COMPLETED));
                    i = -1;
                }
                imageList.add(tblist.get(tblist.size() - 1).getImageLocal());
                imagePosition.put(tblist.size() - 1, imageList.size() - 1);
                sendMessageHandler.sendEmptyMessage(SEND_OK);
                ListViewChatActivity.this.filePath = filePath;
                i++;
                File camFile = new File(filePath); // 图片文件路径
                FileInputStream fsCam = null;
                try {
                    fsCam = new FileInputStream(camFile);
                    int fileLen = (int)camFile.length();
                    byte[] fileData = new byte[fileLen];
                    if(fsCam.read(fileData, 0, fileLen) == fileLen){
                        img = Base64.encodeToString(fileData, Base64.NO_WRAP);
                        sendMyImage(LevelingActivity.Orderid, LevelingActivity.receiverId,LevelingActivity.receivernickname,
                                img, "SendImage");
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }catch (IOException e){
                    e.printStackTrace();
                }finally {
                    if(fsCam != null){
                        try {
                            fsCam.close();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }

    public void sendMyImage(final String orderId,final String receiverId,final String receiverName,final String message,final String operator) {
        new Thread(new Runnable(){
            @Override
            public void run() {
                HubInvokeCallback callback = new HubInvokeCallback() {
                    @Override
                    public void OnResult(boolean succeeded, String response) {
                        // TODO Auto-generated method stub
                        Toast.makeText(ListViewChatActivity.this, "发送成功"+response, Toast.LENGTH_LONG)
                                .show();
                        Log.d(TAG, response);
                    }
                    @Override
                    public void OnError(Exception ex) {
                        // TODO Auto-generated method stub
                        Toast.makeText(ListViewChatActivity.this, "Error:送发失败" + ex.getMessage(),
                                Toast.LENGTH_LONG).show();
                        Log.d(TAG, ex.getMessage());
                    }
                };
                List<String> args = new ArrayList<String>(3);
                args.add(orderId);
                args.add(receiverId);
                args.add(receiverName);
                args.add( message);
                hub.Invoke(operator, args, callback);
                receriveHandler.sendEmptyMessageDelayed(1, 3000);
            }
        }).start();
    }
    /**
     * 接收图片
     */
    String filePath = "";

    private void receriveImageText(final String filePath) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                ChatMessageBean tbub = new ChatMessageBean();
//                tbub.setUserName(userName);
//                String time = returnTime();
//                tbub.setTime(time);
//                tbub.setImageLocal(filePath);
//                tbub.setType(ChatListViewAdapter.FROM_USER_IMG);
//                tblist.add(tbub);
//                imageList.add(tblist.get(tblist.size() - 1).getImageLocal());
//                imagePosition.put(tblist.size() - 1, imageList.size() - 1);
//                sendMessageHandler.sendEmptyMessage(RECERIVE_OK);
//                mChatDbManager.insert(tbub);
//            }
//        }).start();
    }

    /**
     * 发送语音
     */
    @Override
    protected void sendVoice(final float seconds, final String filePath) {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                tblist.add(getTbub(userName, ChatListViewAdapter.TO_USER_VOICE, null, null, null, null, filePath,
//                        null, seconds, ChatConst.SENDING));
//                sendMessageHandler.sendEmptyMessage(SEND_OK);
//                ListViewChatActivity.this.seconds = seconds;
//                voiceFilePath = filePath;
//                receriveHandler.sendEmptyMessageDelayed(2, 3000);
            }
        }).start();
    }

    /**
     * 接收语音
     */
    float seconds = 0.0f;
    String voiceFilePath = "";

    private void receriveVoiceText(final float seconds, final String filePath) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                ChatMessageBean tbub = new ChatMessageBean();
//                tbub.setUserName(userName);
//                String time = returnTime();
//                tbub.setTime(time);
//                tbub.setUserVoiceTime(seconds);
//                tbub.setUserVoicePath(filePath);
//                tbAdapter.unReadPosition.add(tblist.size() + "");
//                tbub.setType(ChatListViewAdapter.FROM_USER_VOICE);
//                tblist.add(tbub);
//                sendMessageHandler.sendEmptyMessage(RECERIVE_OK);
//                mChatDbManager.insert(tbub);
//            }
//        }).start();
    }

    /**
     * 为了模拟接收延迟
     */
    private Handler receriveHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    receriveMsgText(args);
                    break;
                case 1:
                    receriveImageText(filePath);
                    break;
                case 2:
                    receriveVoiceText(seconds, voiceFilePath);
                    break;
                default:
                    break;
            }
        }
    };
}
