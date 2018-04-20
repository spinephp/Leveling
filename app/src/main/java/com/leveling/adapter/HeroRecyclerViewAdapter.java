package com.youyudj.leveling.adapter;

import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youyudj.leveling.R;
import com.youyudj.leveling.entity.Hero;
import com.youyudj.leveling.utils.HttpFileHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/1/7.
 */

public class HeroRecyclerViewAdapter<T> extends RecyclerView.Adapter<HeroRecyclerViewAdapter.ViewHolder> {
    public List<String> getData() {
        return data;
    }

    public static HashMap<Integer, ViewHolder> holders;

    public void setData(List<String> data) {
        this.data = data;
        holders = new HashMap<Integer, ViewHolder>();
    }

    View view;
    public static int i;
    public static String hero1, hero2, ero3, hero4, name1;
    private List<String> data;
    private Context mContext;
    private MyItemClickListener myItemClickListener;
    public static String video_name;
    private String videoId;

    public void setMyItemClickListener(MyItemClickListener myItemClickListener) {
        this.myItemClickListener = myItemClickListener;
    }

    public HeroRecyclerViewAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.strong_hero_item_layout, parent, false);
        return new ViewHolder(view, myItemClickListener);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        List<String> list = new ArrayList<>();
        holders.put(position, holder);
        String dd = data.get(position);
        try {
            JSONObject json = new JSONObject(dd);
            Log.d("STRRTDS", json.toString());
            String name = json.getString("Name");
            holder.hero_name.setText(name);
            holder.pic_name.setText(json.getString("Image"));
            holder.itemView.setSelected(Hero.heros.containsKey(name));
            name1 = json.getString("Image");
            String url = "/api/File/GetHerohead?filename=" + name1;
            HttpFileHelper.httpGetFile(position, url, handler);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView hero_name;
        public final ImageView hero_picture;
        public final LinearLayout hero_details;
        public final TextView pic_name;

        public ViewHolder(final View view, final MyItemClickListener myItemClickListener) {
            super(view);
            hero_name = (TextView) view.findViewById(R.id.hero_name);
            hero_picture = (ImageView) view.findViewById(R.id.hero_picture);
            hero_details = (LinearLayout) view.findViewById(R.id.hero_details);
            pic_name = (TextView) view.findViewById(R.id.pic_name);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != myItemClickListener) {
                        myItemClickListener.onItemClick(view, getLayoutPosition());
                    }
                }
            });
        }
    }

    public interface MyItemClickListener {
        public void onItemClick(View view, int postion);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            if (msg.what < 0) {
                return;
            }
            int pos = msg.what;
            if (HeroRecyclerViewAdapter.this.holders.containsKey(pos)) {
                byte[] data = (byte[]) msg.obj;
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                HeroRecyclerViewAdapter.ViewHolder holder = HeroRecyclerViewAdapter.this.holders.get(pos);
                holder.hero_picture.setImageBitmap(bitmap);
            }
        }
    };
}
