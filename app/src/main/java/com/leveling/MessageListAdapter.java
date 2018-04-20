package com.youyudj.leveling;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.youyudj.leveling.entity.MessageItemBean;

import java.util.List;

/**
 * Created by lin on 2018/4/7.
 */

public class MessageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private OnitemClickListener listener;

    private List<MessageItemBean> dataList;


    public MessageListAdapter(List<MessageItemBean> list)
    {
        this.dataList = list;
    }

    public final void setDataList(List<MessageItemBean> list)
    {
        this.dataList = list;
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view;
        view = inflater.inflate(R.layout.message_item_layout, viewGroup, false);
        return new MessageListHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        final MessageItemBean info = dataList.get(i);
        ((MessageListHolder) viewHolder).displayData(info);
        ((MessageListHolder) viewHolder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null)
                    listener.onItemClick(v,info);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    public interface OnitemClickListener{
        void onItemClick(View v,MessageItemBean bean);
    }

    public void setListener(OnitemClickListener li)
    {
        this.listener = li;
    }


}
