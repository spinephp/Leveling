package com.youyudj.leveling.new_chat;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import com.youyudj.leveling.R;

/**
 * Created by lin on 2018/3/29.
 */

public class MessageRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_SEND = 1;
    private static final int TYPE_RECEIVE = 2;
    private List<ChatMessage> dataList;
    MessageRecyclerHolder.QueryHeadInterface queryHeadInterface;

    public MessageRecyclerAdapter(List<ChatMessage> list) {
        this.dataList = list;
    }

    public final void setDataList(List<ChatMessage> list) {
        this.dataList = list;
        notifyDataSetChanged();
    }

    public void setQueryHeadInterface(MessageRecyclerHolder.QueryHeadInterface queryHeadInterface) {
        this.queryHeadInterface = queryHeadInterface;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        MessageRecyclerHolder ret = null;
        if (viewType == TYPE_SEND) {
            view = inflater.inflate(R.layout.item_message_sent, parent, false);
            ret = new MessageRecyclerHolder(view);
        } else {
            view = inflater.inflate(R.layout.item_message_received, parent, false);
            ret = new MessageRecyclerHolder(view);
        }
        if(ret != null){
            ret.setQueryHeadInterface(queryHeadInterface);
        }
        return ret;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MessageRecyclerHolder) holder).displayData(dataList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage message = dataList.get(position);
        if (message.getIsMine())

            return TYPE_SEND;
        else
            return TYPE_RECEIVE;
    }
}
