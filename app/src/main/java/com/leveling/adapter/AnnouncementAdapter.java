package com.leveling.adapter;

/**
 * Created by Administrator on 2017/12/8.
 */

import java.util.HashMap;
import java.util.List;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.leveling.R;

public class AnnouncementAdapter extends BaseAdapter {

    private Context context;
    private HashMap<String, Object> hashMap;
    private List<HashMap<String, Object>> list;
    public AnnouncementAdapter(Context context, List<HashMap<String, Object>> list) {
        super();
        this.context = context;
        this.list = list;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return parent;
        // TODO Auto-generated method stub

    }



    private View convertViewToItem(int position, View convertView,
                                   ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        if (convertView == null
                || !(convertView.getTag() instanceof ViewHolder)) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.system_info_layout, null);
            holder = new ViewHolder();
            holder.ann_title = (TextView) convertView
                    .findViewById(R.id.ann_title);
            holder.ann_title
                    .setText("title:" + hashMap.get("title").toString());
            holder.ann_url = (TextView) convertView.findViewById(R.id.ann_url);
            holder.ann_url.setText("title:" + hashMap.get("urlUploadFile").toString());
            holder.ann_time = (TextView) convertView
                    .findViewById(R.id.ann_time);
            holder.ann_time.setText("title:" + hashMap.get("time").toString());
        }
        return convertView;
    }

    private View convertViewToWeb(View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    class ViewHolder {
        public TextView ann_title;
        public TextView ann_url;
        public TextView ann_time;
    }
}
