package com.youyudj.leveling;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.youyudj.leveling.entity.MessageItemBean;
import com.youyudj.leveling.entity.OrderBean;

import java.util.List;

/**
 * Created by lin on 2018/4/8.
 */

public class OrderListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private OnitemClickListener listener;

    private List<OrderBean> dataList;

    public OrderListAdapter(List<OrderBean> list)
    {
        this.dataList = list;
    }

    public final void setDataList(List<OrderBean> list)
    {
        this.dataList = list;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        view = inflater.inflate(R.layout.order_detail, parent, false);
        return new OrderListHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final OrderBean info = dataList.get(position);
        final int pos = position;
        ((OrderListHolder) holder).displayData(info);
        ((OrderListHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null)
                    listener.onItemClick(v,pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    public interface OnitemClickListener{
        void onItemClick(View v,int pos);
    }

    public void setListener(OnitemClickListener li)
    {
        this.listener = li;
    }

}
