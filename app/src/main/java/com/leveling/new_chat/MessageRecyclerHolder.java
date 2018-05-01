package com.leveling.new_chat;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.leveling.R;
import com.leveling.utils.BitmapLoader;

/**
 * Created by lin on 2018/3/29.
 */

public class MessageRecyclerHolder extends BaseRecyclerHolder<ChatMessage> {

    private ImageView iv_userhead, image_chatcontent;
    private TextView tv_chatcontent;

    private QueryHeadInterface queryHeadInterface;

    public interface QueryHeadInterface{
        Bitmap getMineHead();
        Bitmap getFriendHead();
    }

    public void setQueryHeadInterface(QueryHeadInterface queryHeadInterface) {
        this.queryHeadInterface = queryHeadInterface;
    }

    public MessageRecyclerHolder(View view) {
        super(view);
        iv_userhead = (ImageView) view.findViewById(R.id.iv_userhead);
        image_chatcontent = (ImageView) view.findViewById(R.id.image_chatcontent);
        tv_chatcontent = (TextView) view.findViewById(R.id.tv_chatcontent);
    }

    @Override
    public void displayData(ChatMessage msg) {
        if(queryHeadInterface != null) {
            if (msg.getIsMine()) {
                iv_userhead.setImageBitmap(queryHeadInterface.getMineHead());
            }else{
                iv_userhead.setImageBitmap(queryHeadInterface.getFriendHead());
            }
        }
        if(msg.getMsgType() == 0){
            tv_chatcontent.setVisibility(View.VISIBLE);
            image_chatcontent.setVisibility(View.GONE);
            tv_chatcontent.setText(msg.getMsgContent());
        }else{
            image_chatcontent.setVisibility(View.VISIBLE);
            tv_chatcontent.setVisibility(View.GONE);
            BitmapLoader.getImageFromLocalFirstNorDownloadWithStatusResponse(Const.getQueryStringWithToken(Const.GET_IMAGE, "at", "chat", "fileName", msg.getMsgContent()), "chat", msg.getMsgContent(), new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    BitmapLoader.StatusBitmap sb = (BitmapLoader.StatusBitmap) msg.obj;
                    if(sb.getBitmap() != null) {
                        image_chatcontent.setImageBitmap(sb.getBitmap());
                    }
                }
            }, 0);
        }
    }
}
