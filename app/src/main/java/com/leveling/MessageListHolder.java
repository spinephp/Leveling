package com.leveling;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.leveling.entity.MessageItemBean;
import com.leveling.new_chat.BaseRecyclerHolder;
import com.leveling.new_chat.Const;
import com.leveling.utils.BitmapLoader;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lin on 2018/4/7.
 */

public class MessageListHolder extends BaseRecyclerHolder<MessageItemBean> {

    private CircleImageView user_avartar;
    private TextView username_txt,content_txt;

    public MessageListHolder(View view)
    {
        super(view);
        user_avartar = (CircleImageView)view.findViewById(R.id.avatar);
        username_txt = (TextView)view.findViewById(R.id.username_txt);
        content_txt = (TextView)view.findViewById(R.id.content_txt);
    }


    @Override
    public void displayData(MessageItemBean data) {
        username_txt.setText(data.getNickname());
        BitmapLoader.getImageFromLocalFirstNorDownloadWithStatusResponse(Const.getQueryStringWithToken(Const.GET_IMAGE, "at", "userhead", "fileName", data.getPicture()), "userhead", data.getPicture(), new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                BitmapLoader.StatusBitmap sb = (BitmapLoader.StatusBitmap) msg.obj;
                if(sb.getBitmap() != null) {
                    user_avartar.setImageBitmap(sb.getBitmap());
                }
            }
        }, 0);
        if(data.getType() == 0)
        {
            content_txt.setText(data.getContent());
        }
        else
            content_txt.setText("[图片]");
    }
}
