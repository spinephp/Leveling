package com.youyudj.leveling.new_chat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import com.youyudj.leveling.R;
import com.youyudj.leveling.utils.BitmapLoader;

/**
 * Created by lin on 2018/3/27.
 */

public class MessageAdapter extends BaseAdapter {

    private List<ChatMessage> mData;
    private Context context;
    private LayoutInflater inflater;

    public MessageAdapter(List<ChatMessage> msgs, Context ctx) {
        this.mData = msgs;
        this.context = ctx;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return mData.size();
    }


    @Override
    public ChatMessage getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ChatMessage msg = getItem(i);
        boolean isMine = msg.getIsMine();

        if (isMine) {
            view = inflater.inflate(R.layout.item_message_sent, viewGroup, false);
        } else {
            view = inflater.inflate(R.layout.item_message_received, viewGroup, false);
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        if (holder == null) {
            holder = new ViewHolder();
            holder.tv = (TextView) view.findViewById(R.id.tv_chatcontent);
            holder.image = (ImageView) view.findViewById(R.id.image_chatcontent);
            holder.head = (ImageView) view.findViewById(R.id.iv_userhead);
            view.setTag(holder);
        }
        //holder.head.setImageBitmap(msg.getHead());
        if(msg.getMsgType() == 0){
            holder.tv.setVisibility(View.VISIBLE);
            holder.image.setVisibility(View.GONE);
            holder.tv.setText(msg.getMsgContent());
        }else{
            holder.image.setVisibility(View.VISIBLE);
            holder.tv.setVisibility(View.GONE);
            final ViewHolder finalHolder = holder;
            BitmapLoader.getImageFromLocalFirstNorDownload(Const.getQueryStringWithToken(Const.GET_IMAGE, "at", "chat", "fileName", msg.getMsgContent()), "chat", msg.getMsgContent(), new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    Bitmap bmp = (Bitmap)msg.obj;
                    if(bmp != null) {
                        finalHolder.image.setImageBitmap(bmp);
                    }
                }
            }, 0);
        }
        return view;
    }

    public Bitmap getBitmapFromByte(byte[] temp) {
        if (temp != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(temp, 0, temp.length);
            return bitmap;
        } else {
            return null;
        }
    }

    private static Bitmap zoomBitmap(Bitmap bt, int scale) {
        int w = bt.getWidth();
        int h = bt.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        Bitmap bmp = Bitmap.createBitmap(bt, 0, 0, w, h, matrix, true);
        return bmp;
    }

    private Bitmap getBitmap(String img_path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeFile(img_path, options);
    }

    public static class ViewHolder {
        TextView tv;
        ImageView image;
        ImageView head;
    }
}
