package com.youyudj.leveling.new_chat;

import android.graphics.Bitmap;

/**
 * Created by lin on 2018/3/27.
 */

public class ChatMessage {

    private boolean isMine;
    private int msgType;           // 0-文字，1图片
    private String msgContent;

    public void setIsMine(boolean isMine) {
        this.isMine = isMine;
    }

    public boolean getIsMine() {
        return isMine;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public void setMsgContent(String msg) {
        this.msgContent = msg;
    }

    public String getMsgContent() {
        return msgContent;
    }

}
