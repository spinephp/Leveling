package com.youyudj.leveling.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.youyudj.leveling.InGogShowActivity;
import com.youyudj.leveling.R;
import com.youyudj.leveling.utils.HttpFileHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Author       : yanbo
 * Date         : 2015-06-02
 * Time         : 09:47
 * Description  :
 */
public class RecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    public List<String> getData() {
        return data;
    }
    HashMap<Integer, ViewHolder> holders;
    public void setData(List<String> data) {
        this.data = data;
        holders = new HashMap<Integer, ViewHolder>();
    }

    View view;
    private List<String> data;
    private Context mContext;
    private MyItemClickListener myItemClickListener;
    private String video_user,video_bg,video_time,video_url,video_lable,video_title;
    private String videoId;
    private String v_url;
    public void setMyItemClickListener(MyItemClickListener myItemClickListener){
        this.myItemClickListener=myItemClickListener;
    }

    public RecyclerViewAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.god_show_rcy_layout, parent, false);
        return new ViewHolder(view, myItemClickListener);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        List<String> list = new ArrayList<>();
        holders.put(position, holder);
        String dd = data.get(position);
        try {
            JSONObject json = new JSONObject(dd);
            Log.d("STRRTDS",json.toString());
            holder.time.setText(json.getString("Time"));
            video_time = json.getString("Time");
            holder.name.setText(json.getString("Name"));
            video_user = json.getString("Name");
            videoId = json.getString("ID");
            String name1 = json.getString("Img");
            video_bg = json.getString("Img");
            String v_url = json.getString("URL");
            video_lable = json.getString("Label");
            video_title = json.getString("Title");
            video_url = v_url;
            String url = "/api/File/GetvideoIMG?filename="+name1;
            HttpFileHelper.httpGetFile(position, url, handler);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        holder.video_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, InGogShowActivity.class);
                intent.putExtra("video_lable",video_lable);
                intent.putExtra("video_title",video_title);
                intent.putExtra("video_bg",video_bg);
                intent.putExtra("video_user",video_user);
                intent.putExtra("video_time",video_time);
                intent.putExtra("video_url",video_url);

                mContext.startActivity(intent);
//                String urlUploadFile = "/api/Video/GetVideos?videoid="+videoId;
//                HttpGetUtils.httpGetFile(12,urlUploadFile,handlerVideo);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView name;
        public final TextView time;
        public final ImageView video_bg;

        public ViewHolder(final View view, final MyItemClickListener myItemClickListener) {
            super(view);
            name = (TextView) view.findViewById(R.id.tx_god_show_item_name);
            time = (TextView) view.findViewById(R.id.tx_god_show_item_time);
            video_bg = (ImageView) view.findViewById(R.id.video_bg);
            view.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   if(null!=myItemClickListener){
                       myItemClickListener.onItemClick(view,getLayoutPosition());
                   }
               }
           });
        }
    }

    public interface MyItemClickListener {
        public void onItemClick(View view, int postion);
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            if (msg.what < 0) {
                return;
            }
            int pos = msg.what;
            if(RecyclerViewAdapter.this.holders.containsKey(pos)){
                byte[] data = (byte[]) msg.obj;
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                ViewHolder holder = RecyclerViewAdapter.this.holders.get(pos);
                holder.video_bg.setImageBitmap(bitmap);
            }
        }
    };
//    private Handler handlerVideo = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            if (msg.what < 0) {
//                return;
//            }
//            switch (msg.what){
//                case 12:
//                    String res = (String)msg.obj;
//                    if (res == null){
//                        return;
//                    }
//                    try {
//                        JSONObject result = new JSONObject(res);
//                        String data = result.getString("URL");
//                        video_url = data;
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//            }
//        }
//    };
}
