package com.leveling;

import android.view.View;
import android.widget.TextView;

import com.leveling.entity.OrderBean;
import com.leveling.new_chat.BaseRecyclerHolder;

/**
 * Created by lin on 2018/4/8.
 */

public class OrderListHolder extends BaseRecyclerHolder<OrderBean> {

    private TextView noticeTime_txt,Content_txt,IsRead_txt;

    public OrderListHolder(View view)
    {
        super(view);
        noticeTime_txt =(TextView)view.findViewById(R.id.notice_time_txt);
        Content_txt = (TextView)view.findViewById(R.id.content_txt);
        IsRead_txt = (TextView)view.findViewById(R.id.is_read_txt);
    }

    @Override
    public void displayData(OrderBean data) {
        if(data.getIs_read())
            IsRead_txt.setText("已读");
        else
            IsRead_txt.setText("未读");
        noticeTime_txt.setText(data.getNoticeTime());
        Content_txt.setText(data.getContent());
    }
}
